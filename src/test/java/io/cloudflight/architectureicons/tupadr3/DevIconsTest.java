package io.cloudflight.architectureicons.tupadr3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DevIconsTest {

    @Test
    void android() {
        Assertions.assertNotNull(DevIcons.ANDROID.getPngAsBase64ForHtml());
    }
}
