import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "10.0.4"
  kotlin("plugin.spring") version "2.3.10"
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
  all {
    resolutionStrategy.eachDependency {
      if (requested.group == "com.fasterxml.jackson.core" && requested.name == "jackson-core") {
        useVersion("2.21.1")
        because("Fix GHSA-72hv-8253-57qq: jackson-core async parser DoS")
      }
    }
  }
}

repositories {
  maven { url = uri("https://repo.spring.io/milestone") }
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-webclient")
  implementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter:2.0.2")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
  implementation("org.springframework.boot:spring-boot-starter-cache")

  implementation("io.swagger:swagger-annotations:1.6.16")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.2")
  implementation("com.google.code.gson:gson:2.13.1")
  implementation("com.amazonaws:aws-java-sdk-s3:1.12.787")
  implementation("com.amazonaws:aws-java-sdk-sts:1.12.787")

  // AWS
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:7.0.1")

  testImplementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter-test:2.0.0")
  testImplementation("org.springframework.boot:spring-boot-starter-webflux-test")
  testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

kotlin {
  jvmToolchain(25)
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions.jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25
  }
}

tasks.test {
  useJUnitPlatform()
}
