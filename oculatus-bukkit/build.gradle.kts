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
    compileOnly(libs.papermc)
    implementation(project(":oculatus-core"))
    implementation(libs.commonsBukkit)
    implementation(libs.jetbrainsannotations)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}
