plugins {
	java
	id("org.springframework.boot") version "4.0.0-M3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.inssider"
version = "0.0.1-SNAPSHOT"
description = "JWT 인가가 포함된 리소스 중심의 RESTful API 서버"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
