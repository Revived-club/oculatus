plugins {
    `java-library`
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") // Paper
    maven("https://mvn.revived.club/releases")
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
