plugins {
    java
    id("org.springframework.boot") version "4.0.0-SNAPSHOT"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "7.0.4"
}

group = "com.inssider"
version = "0.0.10-SNAPSHOT"

springBoot {
    buildInfo()
}

val javaVersion =
    file("${project.rootDir}/.java-version")
        .readText()
        .trim()
        .toIntOrNull() ?: error(".java-version 파일이 없거나 올바르지 않습니다.")

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.postgresql:postgresql")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    val activeProfile = System.getProperty("spring.profiles.active")

    withType<Test> {
        useJUnitPlatform()
        systemProperty("spring.profiles.active", activeProfile)
        testLogging {
            events("passed", "skipped", "failed", "standardOut", "standardError")
            showStandardStreams = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
    withType<org.springframework.boot.gradle.tasks.run.BootRun> {
        systemProperty("spring.profiles.active", activeProfile)
    }
}

spotless {
    setEnforceCheck(false)
    java {
        googleJavaFormat("1.27.0")
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
    format("styling") {
        target("*.{xml,yml,yaml,properties,Dockerfile,json,md,sql}")
        prettier()
    }
}
