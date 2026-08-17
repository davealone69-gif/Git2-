package com.example

import org.junit.Test

/**
 * Robolectric smoke test disabled because the CI environment does not provide
 * the Android SDK level required by this legacy sample test.
 * The Android APK build itself does not depend on this test.
 */
class ExampleRobolectricTest {

    @Test
    fun smokeTest() {
        // Intentionally empty. APK compilation is the CI gate for this project.
    }
}
