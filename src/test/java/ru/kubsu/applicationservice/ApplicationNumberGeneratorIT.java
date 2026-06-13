package ru.kubsu.applicationservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kubsu.applicationservice.service.ApplicationNumberGenerator;
import ru.kubsu.applicationservice.support.IntegrationTestBase;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет генерацию номеров заявлений по PostgreSQL sequence.
 */
class ApplicationNumberGeneratorIT extends IntegrationTestBase {

    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;

    /**
     * Номера должны генерироваться без ведущих нулей, начиная с 1.
     */
    @Test
    void shouldGenerateSequentialNumbersWithoutLeadingZerosWhenCalledForSameType() {
        assertThat(applicationNumberGenerator.nextNumber(ApplicationType.PRODUCT)).isEqualTo("BSPS-PRO-1");
        assertThat(applicationNumberGenerator.nextNumber(ApplicationType.PRODUCT)).isEqualTo("BSPS-PRO-2");
    }

    /**
     * Каждый тип заявления использует свой sequence и код в номере.
     */
    @Test
    void shouldUseIndependentSequencesWhenCalledForDifferentTypes() {
        assertThat(applicationNumberGenerator.nextNumber(ApplicationType.SERVICE)).isEqualTo("BSPS-SRV-1");
        assertThat(applicationNumberGenerator.nextNumber(ApplicationType.PROFILE_CHANGE)).isEqualTo("BSPS-PCH-1");
        assertThat(applicationNumberGenerator.nextNumber(ApplicationType.DOCUMENT)).isEqualTo("BSPS-DOC-1");
        assertThat(applicationNumberGenerator.nextNumber(ApplicationType.CLAIM)).isEqualTo("BSPS-CLM-1");
        assertThat(applicationNumberGenerator.nextNumber(ApplicationType.AUTHORITY)).isEqualTo("BSPS-AUT-1");
    }
}
