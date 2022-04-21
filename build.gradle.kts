plugins {
    java
    `maven-publish`
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    signing
}

description = "Collection of icons for use in architecture diagrams, documentation and training"
group = "io.cloudflight.architectureicons"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

java {
    withJavadocJar()
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        val configuration = project.configurations.getByName(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME)
        val classpath = configuration.files.joinToString(" ") { it.name }
        val createdBy = "${System.getProperty("java.version")} (${System.getProperty("java.vendor")})"

        attributes(
            "Class-Path" to classpath,
            "Created-By" to createdBy,
            "Implementation-Vendor" to "Cloudflight",
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version
        )
    }
}

/**
 * This task needs to be called manually in order to recreate all icons and base64 versions.
 * We keep those files in VCS on purpose, as this operation takes quite long (we download all files)
 * and we also want to keep track of any changes from the corresponding sources.
 */
tasks.create("generateIcons", io.cloudflight.architectureicons.gradle.IconGeneratorTask::class.java)

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set(project.name)
                description.set(project.description)
                url.set("https://github.com/cloudflightio/architecture-icons")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://github.com/cloudflightio/architecture-icons/blob/master/LICENSE")
                    }
                }
                inceptionYear.set("2022")
                organization {
                    name.set("Cloudflight")
                    url.set("https://cloudflight.io")
                }
                developers {
                    developer {
                        id.set("klu2")
                        name.set("Klaus Lehner")
                        email.set("klaus.lehner@cloudflight.io")
                    }
                }
                scm {
                    connection.set("scm:ggit@github.com:cloudflightio/architecture-icons.git")
                    developerConnection.set("scm:git@github.com:cloudflightio/architecture-icons.git")
                    url.set("https://github.com/cloudflightio/architecture-icons")
                }
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {  //only for users registered in Sonatype after 24 Feb 2021
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(System.getenv("MAVEN_USERNAME"))
            password.set(System.getenv("MAVEN_PASSWORD"))
        }
    }
}

signing {
    setRequired {
        System.getenv("PGP_SECRET") != null
    }
    useInMemoryPgpKeys(System.getenv("PGP_SECRET"), System.getenv("PGP_PASSPHRASE"))
    sign(publishing.publications.getByName("maven"))
}
