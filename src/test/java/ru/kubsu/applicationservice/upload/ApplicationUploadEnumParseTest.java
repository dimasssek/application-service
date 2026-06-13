package ru.kubsu.applicationservice.upload;

import org.junit.jupiter.api.Test;
import ru.kubsu.applicationservice.support.UploadXlsxTestWriter;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.ProductCategory;
import ru.kubsu.contracts.enums.service.client.Gender;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit-тесты разбора enum-меток из файла загрузки.
 */
class ApplicationUploadEnumParseTest {

    @Test
    void shouldParseApplicationTypeWhenFileTypeLabelValid() {
        assertThat(ApplicationType.parseFileTypeLabel("Банковский продукт")).isEqualTo(ApplicationType.PRODUCT);
    }

    @Test
    void shouldThrowWhenApplicationTypeUnknown() {
        assertThatThrownBy(() -> ApplicationType.parseFileTypeLabel("Продукты"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("неизвестный тип заявления");
    }

    @Test
    void shouldParseProductCategoryWhenFileLabelValid() {
        assertThat(ProductCategory.parseFileLabel("Кредит")).isEqualTo(ProductCategory.CREDIT);
    }

    @Test
    void shouldParseGenderWhenFileLabelValid() {
        assertThat(Gender.parseFileLabel("Мужской")).isEqualTo(Gender.MALE);
    }

    /**
     * Генерирует xlsx-пример в docs/upload-examples (для документации).
     */
    @Test
    void generateProductValidXlsxExample() throws Exception {
        byte[] content = UploadXlsxTestWriter.writeWorkbook("Банковский продукт", List.of(
                new String[][]{new String[]{"Иванов", "Иван", "Иванович", "15.01.1990", "Мужской", "4010", "123456",
                                "770123456789", "12345678901", "Кредит", "CR-001", "Потребительский кредит", "500000.00",
                                "36", "Ремонт", "T-01", "EXT-99"},
                        new String[]{"Петров", "Пётр", null, "20.06.1985", "Мужской", "4020", "654321",
                                null, null, "Депозит", null, "Накопительный вклад", "100000.00", "12",
                                null, null, null}}));
        Path target = Path.of("docs/upload-examples/product-valid.xlsx");
        Files.write(target, content);
        assertThat(Files.exists(target)).isTrue();
    }
}
