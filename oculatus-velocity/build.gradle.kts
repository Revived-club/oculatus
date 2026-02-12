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
    implementation(project(":oculatus-core"))
    compileOnly(libs.velocity)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}
