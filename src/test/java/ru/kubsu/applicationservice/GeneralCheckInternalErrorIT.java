package ru.kubsu.applicationservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ClientLookupService;
import ru.kubsu.applicationservice.support.IntegrationTestBase;
import ru.kubsu.applicationservice.support.ProductApplicationRequestFixtures;
import ru.kubsu.contracts.dto.service.application.GeneralCheckRequest;
import ru.kubsu.contracts.dto.service.application.GeneralCheckResponse;
import ru.kubsu.contracts.dto.service.application.ProductApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusBusiness;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционные тесты general-check при внутренних ошибках обработки.
 */
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class GeneralCheckInternalErrorIT extends IntegrationTestBase {

    @Autowired
    private ApplicationRepository applicationRepository;

    @MockitoBean
    private ClientLookupService clientLookupService;

    /**
     * Исключение при обработке должно перевести заявление в REJECTED/INTERNAL_ERROR.
     */
    @Test
    void shouldMarkInternalErrorWhenProcessingFails() throws Exception {
        doThrow(new RuntimeException("lookup failed")).when(clientLookupService).findMatchingClient(any(ApplicationEntity.class));

        ProductApplicationTo created = createProduct();

        GeneralCheckRequest request = new GeneralCheckRequest()
                .setApplicationType(ApplicationType.PRODUCT)
                .setDateTo(OffsetDateTime.now().plusDays(1));

        String responseJson = mockMvc.perform(post("/general-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GeneralCheckResponse response = jsonMapper.readValue(responseJson, GeneralCheckResponse.class);
        assertThat(response.getProcessedCount()).isEqualTo(1);
        assertThat(response.getStatusCounts().get(ApplicationStatusBusiness.REJECTED)).isEqualTo(1);

        assertThat(applicationRepository.findById(created.getId())).isPresent()
                .get()
                .matches(entity -> entity.getStatusBusiness() == ApplicationStatusBusiness.REJECTED);
    }

    private ProductApplicationTo createProduct() throws Exception {
        String responseJson = mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(ProductApplicationRequestFixtures.validCreateRequest())))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return jsonMapper.readValue(responseJson, ProductApplicationTo.class);
    }
}
