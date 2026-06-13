package ru.kubsu.applicationservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.repository.GeneralCheckRepository;
import ru.kubsu.applicationservice.service.ClientProjectionService;
import ru.kubsu.applicationservice.support.ApplicationCreateRequestFixtures;
import ru.kubsu.applicationservice.support.IntegrationTestBase;
import ru.kubsu.applicationservice.support.ProductApplicationRequestFixtures;
import ru.kubsu.contracts.dto.service.application.ApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.GeneralCheckRequest;
import ru.kubsu.contracts.dto.service.application.GeneralCheckResponse;
import ru.kubsu.contracts.dto.service.application.ProductApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ProductApplicationTo;
import ru.kubsu.contracts.dto.service.client.ClientTo;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusBusiness;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.client.ClientStatus;
import tools.jackson.databind.JsonNode;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционные тесты general-check.
 */
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class GeneralCheckIT extends IntegrationTestBase {

    @Autowired
    private ClientProjectionService clientProjectionService;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private GeneralCheckRepository generalCheckRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * При найденном клиенте и совпадении ИНН/СНИЛС заявление любого типа должно перейти в APPLIED.
     */
    @ParameterizedTest
    @EnumSource(ApplicationType.class)
    void shouldSetAppliedWhenClientFoundForEachApplicationType(ApplicationType applicationType) throws Exception {
        ApplicationCreateRequest createRequest = ApplicationCreateRequestFixtures.validCreateRequest(applicationType);
        saveMatchingClient(createRequest);

        UUID applicationId = createApplication(applicationType, createRequest);
        GeneralCheckResponse response = executeGeneralCheck(applicationType, OffsetDateTime.now().plusDays(1));

        assertThat(response.getProcessedCount()).isEqualTo(1);
        assertThat(response.getStatusCounts().get(ApplicationStatusBusiness.APPLIED)).isEqualTo(1);

        assertThat(applicationRepository.findById(applicationId)).isPresent()
                .get()
                .matches(entity -> entity.getClientId() != null)
                .matches(entity -> entity.getStatusBusiness() == ApplicationStatusBusiness.APPLIED)
                .matches(entity -> entity.getStatusInternal() == ApplicationStatusInternal.APPLIED);
    }

    /**
     * При отсутствии ИНН в заявлении должно быть отклонение ITN_NOT_REQUIRED.
     */
    @Test
    void shouldRejectApplicationWhenItnNotRequired() throws Exception {
        ProductApplicationCreateRequest createRequest = ProductApplicationRequestFixtures.validCreateRequest();
        createRequest.setItn(null);
        saveMatchingClient(createRequest);

        ProductApplicationTo created = createProduct(createRequest);
        GeneralCheckResponse response = executeGeneralCheck(ApplicationType.PRODUCT, OffsetDateTime.now().plusDays(1));

        assertThat(response.getStatusCounts().get(ApplicationStatusBusiness.REJECTED)).isEqualTo(1);
        assertThat(applicationRepository.findById(created.getId())).isPresent()
                .get()
                .matches(entity -> entity.getStatusInternal() == ApplicationStatusInternal.ITN_NOT_REQUIRED);
    }

    /**
     * При расхождении ИНН business-статус APPLIED, internal ITN_NOT_ACTUAL.
     */
    @Test
    void shouldSetItnNotActualWhenItnMismatch() throws Exception {
        ProductApplicationCreateRequest createRequest = ProductApplicationRequestFixtures.validCreateRequest();
        saveMatchingClient(createRequest);
        createRequest.setItn("999999999999");

        ProductApplicationTo created = createProduct(createRequest);
        GeneralCheckResponse response = executeGeneralCheck(ApplicationType.PRODUCT, OffsetDateTime.now().plusDays(1));

        assertThat(response.getStatusCounts().get(ApplicationStatusBusiness.APPLIED)).isEqualTo(1);
        assertThat(applicationRepository.findById(created.getId())).isPresent()
                .get()
                .matches(entity -> entity.getStatusInternal() == ApplicationStatusInternal.ITN_NOT_ACTUAL);
    }

    /**
     * При отсутствии СНИЛС в заявлении должно быть отклонение INSURANCE_NUMBER_NOT_REQUIRED.
     */
    @Test
    void shouldRejectApplicationWhenInsuranceNumberNotRequired() throws Exception {
        ProductApplicationCreateRequest createRequest = ProductApplicationRequestFixtures.validCreateRequest();
        createRequest.setInsuranceNumber(null);
        saveMatchingClient(createRequest);

        ProductApplicationTo created = createProduct(createRequest);
        GeneralCheckResponse response = executeGeneralCheck(ApplicationType.PRODUCT, OffsetDateTime.now().plusDays(1));

        assertThat(response.getStatusCounts().get(ApplicationStatusBusiness.REJECTED)).isEqualTo(1);
        assertThat(applicationRepository.findById(created.getId())).isPresent()
                .get()
                .matches(entity -> entity.getStatusInternal() == ApplicationStatusInternal.INSURANCE_NUMBER_NOT_REQUIRED);
    }

    /**
     * При отсутствии клиента заявление должно перейти в REJECTED/CLIENT_NOT_FOUND.
     */
    @Test
    void shouldRejectApplicationWhenClientNotFound() throws Exception {
        ProductApplicationTo created = createProduct(ProductApplicationRequestFixtures.validCreateRequest());

        GeneralCheckResponse response = executeGeneralCheck(ApplicationType.PRODUCT, OffsetDateTime.now().plusDays(1));

        assertThat(response.getProcessedCount()).isEqualTo(1);
        assertThat(response.getStatusCounts().get(ApplicationStatusBusiness.REJECTED)).isEqualTo(1);

        assertThat(applicationRepository.findById(created.getId())).isPresent()
                .get()
                .matches(entity -> entity.getClientId() == null)
                .matches(entity -> entity.getStatusBusiness() == ApplicationStatusBusiness.REJECTED);
    }

    /**
     * В пакет должны попадать только заявления с created_date <= dateTo.
     */
    @Test
    void shouldFilterByDateToWhenApplicationsCreatedAtDifferentTimes() throws Exception {
        ProductApplicationCreateRequest includedRequest = ProductApplicationRequestFixtures.validCreateRequest();
        saveMatchingClient(includedRequest);
        ProductApplicationTo included = createProduct(includedRequest);

        ProductApplicationCreateRequest excludedRequest = ProductApplicationRequestFixtures.validCreateRequest();
        saveMatchingClient(excludedRequest);
        ProductApplicationTo excluded = createProduct(excludedRequest);

        OffsetDateTime cutoff = OffsetDateTime.parse("2026-06-01T12:00:00Z");
        jdbcTemplate.update("""
                update application
                set created_date = ?
                where id = ?
                """, cutoff.minusHours(1), included.getId());
        jdbcTemplate.update("""
                update application
                set created_date = ?
                where id = ?
                """, cutoff.plusHours(1), excluded.getId());

        GeneralCheckResponse response = executeGeneralCheck(ApplicationType.PRODUCT, cutoff);

        assertThat(response.getProcessedCount()).isEqualTo(1);
        assertThat(applicationRepository.findById(included.getId())).isPresent()
                .get()
                .matches(entity -> entity.getClientId() != null);
        assertThat(applicationRepository.findById(excluded.getId())).isPresent()
                .get()
                .matches(entity -> entity.getClientId() == null);
    }

    /**
     * Несколько заявлений должны обрабатываться в одном запуске general-check.
     */
    @Test
    void shouldProcessBatchWhenMultipleApplicationsInQueue() throws Exception {
        for (int index = 0; index < 5; index++) {
            ProductApplicationCreateRequest createRequest = ProductApplicationRequestFixtures.validCreateRequest();
            saveMatchingClient(createRequest);
            createProduct(createRequest);
        }

        GeneralCheckResponse response = executeGeneralCheck(ApplicationType.PRODUCT, OffsetDateTime.now().plusDays(1));

        assertThat(response.getProcessedCount()).isEqualTo(5);
        assertThat(response.getStatusCounts().get(ApplicationStatusBusiness.APPLIED)).isEqualTo(5);
        assertThat(generalCheckRepository.count()).isEqualTo(1);
    }

    private GeneralCheckResponse executeGeneralCheck(ApplicationType applicationType, OffsetDateTime dateTo)
            throws Exception {
        GeneralCheckRequest request = new GeneralCheckRequest()
                .setApplicationType(applicationType)
                .setDateTo(dateTo);

        String responseJson = mockMvc.perform(post("/general-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return jsonMapper.readValue(responseJson, GeneralCheckResponse.class);
    }

    private UUID createApplication(ApplicationType applicationType, ApplicationCreateRequest request)
            throws Exception {
        String responseJson = mockMvc.perform(post(ApplicationCreateRequestFixtures.createPath(applicationType))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode response = jsonMapper.readTree(responseJson);
        return UUID.fromString(response.get("id").asString());
    }

    private ProductApplicationTo createProduct(ProductApplicationCreateRequest request) throws Exception {
        String responseJson = mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return jsonMapper.readValue(responseJson, ProductApplicationTo.class);
    }

    private void saveMatchingClient(ApplicationCreateRequest request) {
        ClientTo client = new ClientTo()
                .setId(UUID.randomUUID())
                .setLastName(request.getLastName())
                .setFirstName(request.getFirstName())
                .setPatronymic(request.getPatronymic())
                .setBirthDate(request.getBirthDate())
                .setGender(request.getGender())
                .setIdentityDocumentSeries(request.getIdentityDocumentSeries())
                .setIdentityDocumentNumber(request.getIdentityDocumentNumber())
                .setItn(request.getItn())
                .setInsuranceNumber(request.getInsuranceNumber())
                .setActualDate(OffsetDateTime.now())
                .setAddressDefined(true)
                .setStatus(ClientStatus.ACTUAL);

        clientProjectionService.applyClientEvent(client);
    }
}
