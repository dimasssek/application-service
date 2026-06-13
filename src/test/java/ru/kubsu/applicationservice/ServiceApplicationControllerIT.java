package ru.kubsu.applicationservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.kubsu.applicationservice.repository.OutboxMessageRepository;
import ru.kubsu.applicationservice.support.IntegrationTestBase;
import ru.kubsu.applicationservice.support.ServiceApplicationRequestFixtures;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusBusiness;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционные тесты REST API заявлений на банковские услуги.
 */
class ServiceApplicationControllerIT extends IntegrationTestBase {

    @Autowired
    private OutboxMessageRepository outboxMessageRepository;

    /**
     * Создание заявления должно вернуть IN_QUEUE и записать два outbox-события.
     */
    @Test
    void shouldCreateServiceApplicationWhenRequestValid() throws Exception {
        ServiceApplicationCreateRequest request = ServiceApplicationRequestFixtures.validCreateRequest();

        String responseJson = mockMvc.perform(post("/service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ServiceApplicationTo created = jsonMapper.readValue(responseJson, ServiceApplicationTo.class);
        assertThat(created.getNumber()).startsWith("BSPS-SRV-");
        assertThat(created.getStatusBusiness()).isEqualTo(ApplicationStatusBusiness.IN_QUEUE);
        assertThat(created.getStatusInternal()).isEqualTo(ApplicationStatusInternal.IN_QUEUE);
        assertThat(created.getReason()).isEqualTo("Ожидает обработки");

        assertThat(outboxMessageRepository.findAll().stream()
                .filter(message -> created.getId().equals(message.getAggregateId()))
                .map(message -> message.getEventType()))
                .contains(OutboxEventType.APPLICATION_CREATED, OutboxEventType.SERVICE_APPLICATION_CREATED);
    }
}
