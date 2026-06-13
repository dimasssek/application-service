package ru.kubsu.applicationservice;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.repository.OutboxMessageRepository;
import ru.kubsu.applicationservice.support.IntegrationTestBase;
import ru.kubsu.applicationservice.support.ProductApplicationRequestFixtures;
import ru.kubsu.contracts.dto.common.PageData;
import ru.kubsu.contracts.dto.service.application.ProductApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ProductApplicationQueryParams;
import ru.kubsu.contracts.dto.service.application.ProductApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusBusiness;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;
import ru.kubsu.contracts.enums.service.application.OutboxStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционные тесты REST API продуктовых заявлений.
 */
class ProductApplicationControllerIT extends IntegrationTestBase {

    @Autowired
    private OutboxMessageRepository outboxMessageRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    /**
     * Создание заявления должно вернуть IN_QUEUE и записать outbox.
     */
    @Test
    void shouldCreateProductApplicationWhenRequestValid() throws Exception {
        ProductApplicationCreateRequest request = ProductApplicationRequestFixtures.validCreateRequest();

        String responseJson = mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ProductApplicationTo created = jsonMapper.readValue(responseJson, ProductApplicationTo.class);
        assertThat(created.getNumber()).startsWith("BSPS-PRO-");
        assertThat(created.getStatusBusiness()).isEqualTo(ApplicationStatusBusiness.IN_QUEUE);
        assertThat(created.getStatusInternal()).isEqualTo(ApplicationStatusInternal.IN_QUEUE);
        assertThat(created.getReason()).isEqualTo("Ожидает обработки");
        assertThat(responseJson).doesNotContain("\"deleted\"");

        assertThat(outboxMessageRepository.findAll().stream()
                .filter(message -> created.getId().equals(message.getAggregateId()))
                .map(message -> message.getEventType()))
                .contains(OutboxEventType.APPLICATION_CREATED, OutboxEventType.PRODUCT_APPLICATION_CREATED);
    }

    /**
     * Повторное создание с теми же ключевыми полями должно вернуть BAD/DUPLICATE.
     */
    @Test
    void shouldMarkDuplicateWhenSameApplicationCreatedTwice() throws Exception {
        ProductApplicationCreateRequest request = ProductApplicationRequestFixtures.validCreateRequest();

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        String responseJson = mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ProductApplicationTo duplicate = jsonMapper.readValue(responseJson, ProductApplicationTo.class);
        assertThat(duplicate.getStatusBusiness()).isEqualTo(ApplicationStatusBusiness.BAD);
        assertThat(duplicate.getStatusInternal()).isEqualTo(ApplicationStatusInternal.DUPLICATE);
    }

    /**
     * Получение по id и поиск не должны возвращать удалённые заявления.
     */
    @Test
    void shouldHideDeletedApplicationsFromFetchAndSearch() throws Exception {
        ProductApplicationTo created = createProduct();

        mockMvc.perform(delete("/product/{id}", created.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/product/{id}", created.getId()))
                .andExpect(status().isNotFound());

        String searchJson = mockMvc.perform(post("/product/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new ProductApplicationQueryParams())))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PageData<?> page = jsonMapper.readValue(searchJson, PageData.class);
        assertThat(page.getMetaData().getTotalElements()).isZero();
    }

    /**
     * Обновление заявления в конечном статусе APPLIED должно быть запрещено.
     */
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldRejectUpdateWhenApplicationInFinalStatus() throws Exception {
        ProductApplicationTo created = createProduct();

        jdbcTemplate.update("""
                update application
                set status_internal = 'APPLIED',
                    status_business = 'APPLIED'
                where id = ?
                """, created.getId());

        entityManager.clear();

        mockMvc.perform(put("/product/{id}", created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(
                                ProductApplicationRequestFixtures.validUpdateRequest())))
                .andExpect(status().isBadRequest());
    }

    /**
     * Удаление должно пометить заявление deleted и отправить outbox UPDATED.
     */
    @Test
    void shouldSoftDeleteAndPublishOutboxUpdated() throws Exception {
        ProductApplicationTo created = createProduct();

        mockMvc.perform(delete("/product/{id}", created.getId()))
                .andExpect(status().isNoContent());

        assertThat(applicationRepository.findById(created.getId())).isPresent()
                .get()
                .matches(entity -> entity.isDeleted());

        assertThat(outboxMessageRepository.findAll().stream()
                .filter(message -> message.getAggregateId().equals(created.getId()))
                .filter(message -> message.getEventType() == OutboxEventType.APPLICATION_UPDATED)
                .anyMatch(message -> message.getStatus() == OutboxStatus.NEW
                        || message.getStatus() == OutboxStatus.SENT)).isTrue();
    }

    private ProductApplicationTo createProduct() throws Exception {
        String responseJson = mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(
                                ProductApplicationRequestFixtures.validCreateRequest())))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return jsonMapper.readValue(responseJson, ProductApplicationTo.class);
    }
}
