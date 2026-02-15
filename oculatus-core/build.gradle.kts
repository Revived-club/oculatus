plugins {
    `java-library`
}

repositories {
    mavenCentral()
    maven("https://mvn.revived.club/releases")
}

dependencies {
    testImplementation(libs.junit)
    implementation(libs.guava)
    implementation(libs.jedis)
    api(libs.jetbrainsannotations)
    api(libs.commons)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}
