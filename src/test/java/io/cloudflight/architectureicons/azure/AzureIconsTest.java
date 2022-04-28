package io.cloudflight.architectureicons.azure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AzureIconsTest {

    @Test
    void android() {
        Assertions.assertNotNull(AzureIcons.Analytics.AZURE_ANALYSIS_SERVICES.getPngAsBase64ForHtml());
    }

    @Test
    void androidMono() {
        Assertions.assertNotNull(AzureMonoIcons.Analytics.AZURE_ANALYSIS_SERVICES.getPngAsBase64ForHtml());
    }

}
