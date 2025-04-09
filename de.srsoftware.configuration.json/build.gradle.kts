description = "SRSoftware Configuration | JSON Configuration"

dependencies {
    implementation(project(":de.srsoftware.configuration.api"))
    implementation("org.json:json:latest.release")
}


plugins {
    id("eu.kakde.gradle.sonatype-maven-central-publisher") version "1.0.6"
}

object Meta {
    val COMPONENT_TYPE = "java" // "java" or "versionCatalog"
    val GROUP = "de.srsoftware"
    val ARTIFACT_ID = "configuration.json"
    val VERSION = "1.0.2"
    val PUBLISHING_TYPE = "AUTOMATIC" // USER_MANAGED or AUTOMATIC
    val SHA_ALGORITHMS = listOf("SHA-256", "SHA-512") // sha256 and sha512 are supported but not mandatory. Only sha1 is mandatory but it is supported by default.
    val DESC = "SRSoftware Configuration: Json-based Implementation"
    val LICENSE = "MIT License"
    val LICENSE_URL = "http://www.opensource.org/licenses/mit-license.php"
    val GITHUB_REPO = "srsoftware-de/de.srsoftware.configuration"
    val DEVELOPER_ID = "srichter"
    val DEVELOPER_NAME = "Stephan Richter"
    val DEVELOPER_ORGANIZATION = "SRSoftware"
    val DEVELOPER_ORGANIZATION_URL = "https://srsoftware.de"
}

val sonatypeUsername: String? by project // this is defined in ~/.gradle/gradle.properties
val sonatypePassword: String? by project // this is defined in ~/.gradle/gradle.properties

sonatypeCentralPublishExtension {
    // Set group ID, artifact ID, version, and other publication details
    groupId.set(Meta.GROUP)
    artifactId.set(Meta.ARTIFACT_ID)
    version.set(Meta.VERSION)
    componentType.set(Meta.COMPONENT_TYPE) // "java" or "versionCatalog"
    publishingType.set(Meta.PUBLISHING_TYPE) // USER_MANAGED or AUTOMATIC

    // Set username and password for Sonatype repository
    username.set(sonatypeUsername)
    password.set(sonatypePassword)

    // Configure POM metadata
    pom {
        name.set(Meta.ARTIFACT_ID)
        description.set(Meta.DESC)
        url.set("https://github.com/${Meta.GITHUB_REPO}")
        licenses {
            license {
                name.set(Meta.LICENSE)
                url.set(Meta.LICENSE_URL)
            }
        }
        developers {
            developer {
                id.set(Meta.DEVELOPER_ID)
                name.set(Meta.DEVELOPER_NAME)
                organization.set(Meta.DEVELOPER_ORGANIZATION)
                organizationUrl.set(Meta.DEVELOPER_ORGANIZATION_URL)
            }
        }
        scm {
            url.set("https://github.com/${Meta.GITHUB_REPO}")
            connection.set("scm:git:https://github.com/${Meta.GITHUB_REPO}")
            developerConnection.set("scm:git:https://github.com/${Meta.GITHUB_REPO}")
        }
        issueManagement {
            system.set("GitHub")
            url.set("https://github.com/${Meta.GITHUB_REPO}/issues")
        }
    }
}
