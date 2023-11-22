plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "5.9.0"
  kotlin("plugin.spring") version "1.9.20"
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

repositories {
  maven { url = uri("https://repo.spring.io/milestone") }
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

  implementation("io.swagger:swagger-annotations:1.6.12")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
  implementation("com.google.code.gson:gson:2.10.1")
  implementation("com.amazonaws:aws-java-sdk-s3:1.12.571")
  implementation("com.amazonaws:aws-java-sdk-sts:1.12.571")

  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  testImplementation("io.jsonwebtoken:jjwt-impl:0.12.3")
  testImplementation("io.jsonwebtoken:jjwt-jackson:0.12.3")
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(19))
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "19"
    }
  }
}
