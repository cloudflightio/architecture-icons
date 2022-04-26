plugins {
    kotlin("jvm") version "1.6.20"
    kotlin("plugin.serialization") version "1.6.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
}