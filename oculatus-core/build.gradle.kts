plugins {
    application
    alias(libs.plugins.shadow)
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit)
    implementation(libs.guava)
    implementation(libs.jedis)
    compileOnly(libs.jetbrainsannotations)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}
