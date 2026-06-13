package ru.kubsu.applicationservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.repository.OutboxMessageRepository;
import ru.kubsu.applicationservice.support.IntegrationTestBase;
import ru.kubsu.applicationservice.support.UploadXlsxTestWriter;
import ru.kubsu.contracts.dto.service.application.ApplicationUploadResponse;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusBusiness;
import ru.kubsu.contracts.enums.service.application.Channel;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционные тесты загрузки заявлений из файла.
 */
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class ApplicationUploadIT extends IntegrationTestBase {

    private static final Path UPLOAD_RESOURCES = Path.of("src/test/resources/upload");

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private OutboxMessageRepository outboxMessageRepository;

    /**
     * Валидный txt-файл должен создать заявления с каналом FILE.
     */
    @Test
    void shouldUploadProductApplicationsWhenTxtFileValid() throws Exception {
        ApplicationUploadResponse response = uploadFile("product-valid.txt");

        assertThat(response.isUploaded()).isTrue();
        assertThat(response.getDetailedError()).isNull();
        assertThat(applicationRepository.count()).isEqualTo(2);
        assertThat(applicationRepository.findAll())
                .allMatch(application -> application.getChannel() == Channel.FILE)
                .allMatch(application -> application.getStatusBusiness() == ApplicationStatusBusiness.IN_QUEUE);
        assertThat(outboxMessageRepository.count()).isPositive();
    }

    /**
     * Валидный xlsx-файл должен создать заявления.
     */
    @Test
    void shouldUploadProductApplicationsWhenXlsxFileValid() throws Exception {
        byte[] content = UploadXlsxTestWriter.writeWorkbook("Банковский продукт", java.util.Collections.singletonList(
                new String[]{"Иванов", "Иван", "Иванович", "15.01.1990", "Мужской", "4010", "123456",
                        "770123456789", "12345678901", "Кредит", "CR-001", "Потребительский кредит", "500000.00",
                        "36", "Ремонт", "T-01", "EXT-99"}));

        ApplicationUploadResponse response = uploadBytes("product-valid.xlsx", content);

        assertThat(response.isUploaded()).isTrue();
        assertThat(applicationRepository.count()).isEqualTo(1);
    }

    /**
     * Неподдерживаемый формат файла должен вернуть uploaded=false.
     */
    @Test
    void shouldRejectUploadWhenUnsupportedFileFormat() throws Exception {
        ApplicationUploadResponse response = uploadBytes("report.pdf", "test".getBytes());

        assertThat(response.isUploaded()).isFalse();
        assertThat(response.getDetailedError()).contains("Неподдерживаемый формат файла");
        assertThat(applicationRepository.count()).isZero();
    }

    /**
     * Ошибка парсинга строки должна отклонить весь файл.
     */
    @Test
    void shouldRejectUploadWhenParseErrorInRow() throws Exception {
        ApplicationUploadResponse response = uploadFile("product-invalid-enum-row3.txt");

        assertThat(response.isUploaded()).isFalse();
        assertThat(response.getDetailedError()).startsWith("Строка 3:");
        assertThat(applicationRepository.count()).isZero();
    }

    /**
     * Дубликаты внутри файла должны создаваться: первый IN_QUEUE, второй BAD.
     */
    @Test
    void shouldMarkDuplicateWhenSameRowsInUploadFile() throws Exception {
        String duplicateRow = "Иванов\tИван\tИванович\t15.01.1990\tМужской\t4010\t123456\t770123456789\t"
                + "12345678901\tКредит\tCR-001\tПотребительский кредит\t500000.00\t36\tРемонт\tT-01\tEXT-99";
        String content = "Банковский продукт\n" + duplicateRow + "\n" + duplicateRow;
        ApplicationUploadResponse response = uploadBytes("product-duplicate.txt", content.getBytes());

        assertThat(response.isUploaded()).isTrue();
        assertThat(applicationRepository.count()).isEqualTo(2);
        assertThat(applicationRepository.findAll().stream()
                .filter(application -> application.getStatusBusiness() == ApplicationStatusBusiness.IN_QUEUE)
                .count()).isEqualTo(1);
        assertThat(applicationRepository.findAll().stream()
                .filter(application -> application.getStatusBusiness() == ApplicationStatusBusiness.BAD)
                .count()).isEqualTo(1);
    }

    /**
     * Валидный файл услуг должен создать заявление.
     */
    @Test
    void shouldUploadServiceApplicationsWhenTxtFileValid() throws Exception {
        ApplicationUploadResponse response = uploadFile("service-valid.txt");

        assertThat(response.isUploaded()).isTrue();
        assertThat(applicationRepository.count()).isEqualTo(1);
    }

    private ApplicationUploadResponse uploadFile(String fileName) throws Exception {
        byte[] content = Files.readAllBytes(UPLOAD_RESOURCES.resolve(fileName));
        return uploadBytes(fileName, content);
    }

    private ApplicationUploadResponse uploadBytes(String fileName, byte[] content) throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", fileName, MediaType.MULTIPART_FORM_DATA_VALUE, content);

        String responseJson = mockMvc.perform(multipart("/applications/upload").file(file))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return jsonMapper.readValue(responseJson, ApplicationUploadResponse.class);
    }
}
