package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.DateTimeSchema
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.media.StringSchema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType

@Configuration
class OpenApiConfiguration(
  buildProperties: BuildProperties,
) {
  private val version: String = buildProperties.version

  @Bean
  fun customOpenAPI(): OpenAPI = OpenAPI()
    .servers(
      listOf(
        Server().url("https://adjudications-insights-api-dev.hmpps.service.justice.gov.uk").description("Development"),
        Server().url("https://adjudications-insights-api-preprod.hmpps.service.justice.gov.uk").description("PreProd"),
        Server().url("https://adjudications-insights-api.hmpps.service.justice.gov.uk").description("Prod"),
        Server().url("http://localhost:8080").description("Local"),
      ),
    )
    .info(
      Info().title("HMPPS Adjudications Insights API")
        .version(version)
        .license(License().name("MIT").url("https://opensource.org/license/mit-0"))
        .description("API for adjudications insights")
        .contact(
          Contact()
            .name("Adjudications Support Team")
            .email("feedback@digital.justice.gov.uk")
            .url("https://github.com/ministryofjustice/hmpps-adjudications-insights-api"),
        ),
    )
    .components(
      Components().addSecuritySchemes(
        "bearer-jwt",
        SecurityScheme()
          .type(SecurityScheme.Type.HTTP)
          .scheme("bearer")
          .bearerFormat("JWT")
          .`in`(SecurityScheme.In.HEADER)
          .name("Authorization"),
      ),
    )
    .addSecurityItem(SecurityRequirement().addList("bearer-jwt", listOf("read", "write")))

  @Bean
  fun openAPICustomiser(): OpenApiCustomizer = OpenApiCustomizer {
    it.paths.forEach { (_, path: PathItem) ->
      path.readOperations().forEach { operation ->
        operation.responses.default = createErrorApiResponse("Unexpected error")
        operation.responses.addApiResponse("401", createErrorApiResponse("Unauthorized"))
        operation.responses.addApiResponse("403", createErrorApiResponse("Forbidden"))
        operation.responses.addApiResponse("406", createErrorApiResponse("Not able to process the request because the header “Accept” does not match with any of the content types this endpoint can handle"))
        operation.responses.addApiResponse("429", createErrorApiResponse("Too many requests"))
      }
    }
    it.components.schemas.forEach { (_, schema: Schema<*>) ->
      schema.additionalProperties = false
      val properties = schema.properties ?: mutableMapOf()
      for (propertyName in properties.keys) {
        val propertySchema = properties[propertyName]!!
        if (propertySchema is DateTimeSchema) {
          properties.replace(
            propertyName,
            StringSchema()
              .example("2021-07-05T10:35:17")
              .pattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$")
              .description(propertySchema.description)
              .required(propertySchema.required),
          )
        }
      }
    }
  }

  private fun createErrorApiResponse(message: String): ApiResponse {
    val errorResponseSchema = Schema<Any>()
    errorResponseSchema.name = "ErrorResponse"
    errorResponseSchema.`$ref` = "#/components/schemas/ErrorResponse"
    return ApiResponse()
      .description(message)
      .content(
        Content().addMediaType(MediaType.APPLICATION_JSON_VALUE, io.swagger.v3.oas.models.media.MediaType().schema(errorResponseSchema)),
      )
  }
}
