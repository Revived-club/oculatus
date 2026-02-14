plugins {
    application
    alias(libs.plugins.shadow)
}

repositories {
    mavenCentral()
    maven("https://mvn.revived.club/releases")
}

dependencies {
    testImplementation(libs.junit)
    implementation(libs.guava)
    implementation(libs.jedis)
    compileOnly(libs.jetbrainsannotations)
    compileOnly(libs.commons)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}
