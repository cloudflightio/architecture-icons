package io.cloudflight.architectureicons.aws;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AwsIconsTest {

    @Test
    void amazonAthena() {
        Assertions.assertNotNull(AwsArchitectureIcons.Analytics.AMAZON_ATHENA.getPngAsBase64ForHtml());
    }

    @Test
    void analytics() {
        Assertions.assertNotNull(AwsCategoryIcons.ANALYTICS.getPngAsBase64ForHtml());
    }

    @Test
    void resources() {
        Assertions.assertNotNull(AwsResourceIcons.Compute.AMAZON_EC2_AWS_MICROSERVICE_EXTRACTOR_FOR_DOTNET.getPngAsBase64ForHtml());
    }


}
