plugins {
    `maven-publish`
    java
}

group = "club.revived.oculatus"
version = "0.0.2"

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = project.group.toString()
            artifactId = "oculatus"
            version = project.version.toString()
        }
    }

    repositories {
        maven {
            name = "revived"
            url = uri("https://mvn.revived.club/releases")
            credentials {
                username = project.findProperty("repoUser")?.toString() ?: ""
                password = project.findProperty("repoPass")?.toString() ?: ""
            }
        }
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = "club.revived.oculatus"
    version = rootProject.version.toString()

    java {
        withSourcesJar()
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
                artifactId = project.name
            }
        }

        repositories {
            maven {
                name = "revived"
                url = uri("https://mvn.revived.club/releases")
                credentials {
                    username = rootProject.findProperty("repoUser")?.toString() ?: ""
                    password = rootProject.findProperty("repoPass")?.toString() ?: ""
                }
            }
        }
    }
}
