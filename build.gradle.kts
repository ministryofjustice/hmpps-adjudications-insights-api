import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "10.5.4"
  kotlin("plugin.spring") version "2.3.21"
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
  all {
    resolutionStrategy.eachDependency {
      if (requested.group == "io.opentelemetry" && requested.name == "opentelemetry-api") {
        useVersion("1.62.0")
        because("CVE-2026-45292: resource allocation without limits, fixed in 1.62.0")
      }
    }
  }
}

repositories {
  maven { url = uri("https://repo.spring.io/milestone") }
  mavenCentral()
}

dependencies {
  constraints {
    implementation("org.springframework.retry:spring-retry:2.0.13") {
      because("CVE-2026-41710: resource allocation without limits, fixed in 2.0.13")
    }
    implementation("io.opentelemetry:opentelemetry-api:1.62.0") {
      because("CVE-2026-45292: resource allocation without limits, fixed in 1.62.0")
    }
  }

  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-webclient")
  implementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter:2.4.0")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
  implementation("org.springframework.boot:spring-boot-starter-cache")

  implementation("io.swagger:swagger-annotations:1.6.16")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.3")
  implementation("com.google.code.gson:gson:2.14.0")
  implementation("com.amazonaws:aws-java-sdk-s3:1.12.797")
  implementation("com.amazonaws:aws-java-sdk-sts:1.12.797")

  // AWS
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:7.3.2")

  testImplementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter-test:2.4.0")
  testImplementation("org.springframework.boot:spring-boot-starter-webflux-test")
  testImplementation("org.mockito.kotlin:mockito-kotlin:6.3.0")
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
