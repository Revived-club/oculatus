plugins {
    application
    alias(libs.plugins.shadow)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") // Paper
}

dependencies {
    testImplementation(libs.junit)
    implementation(libs.guava)
    compileOnly(libs.papermc)
    implementation(project(":oculatus-core"))
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}
