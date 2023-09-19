package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.config

import com.amazonaws.services.s3.model.AmazonS3Exception
import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {
  @ExceptionHandler(ValidationException::class)
  fun handleValidationException(e: Exception): ResponseEntity<ErrorResponse> {
    log.info("Validation exception: {}", e.message)
    return ResponseEntity
      .status(BAD_REQUEST)
      .body(
        ErrorResponse(
          status = BAD_REQUEST,
          userMessage = "Validation failure: ${e.message}",
          developerMessage = e.message,
        ),
      )
  }

  @ExceptionHandler(java.lang.Exception::class)
  fun handleException(e: java.lang.Exception): ResponseEntity<ErrorResponse?>? {
    log.error("Unexpected exception", e)
    return ResponseEntity
      .status(INTERNAL_SERVER_ERROR)
      .body(
        ErrorResponse(
          status = INTERNAL_SERVER_ERROR,
          userMessage = "Unexpected error: ${e.message}",
          developerMessage = e.message,
        ),
      )
  }

  @ExceptionHandler(AmazonS3Exception::class)
  fun handleValidationAmazonS3Exception(e: AmazonS3Exception): ResponseEntity<ErrorResponse> {
    log.info("Amazon S3 Bucket exception: {}", e.message)
    return ResponseEntity
      .status(INTERNAL_SERVER_ERROR)
      .body(
        ErrorResponse(
          status = INTERNAL_SERVER_ERROR,
          userMessage = "Amazon S3 Bucket failure: ${e.message}",
          developerMessage = e.message,
        ),
      )
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Error Response")
data class ErrorResponse(
  @Schema(description = "Status of Error", example = "500", required = true)
  val status: Int,
  @Schema(description = "Error Code", example = "500", required = false)
  val errorCode: Int? = null,
  @Schema(description = "User Message of error", example = "Bad Data", required = false, maxLength = 200, pattern = "^[a-zA-Z\\d. _-]{1,200}\$")
  val userMessage: String? = null,
  @Schema(description = "More detailed error message", example = "This is a stack trace", required = false, maxLength = 4000, pattern = "^[a-zA-Z\\d. _-]*\$")
  val developerMessage: String? = null,
  @Schema(description = "More information about the error", example = "More info", required = false, maxLength = 4000, pattern = "^[a-zA-Z\\d. _-]*\$")
  val moreInfo: String? = null,
) {
  constructor(
    status: HttpStatus,
    errorCode: Int? = null,
    userMessage: String? = null,
    developerMessage: String? = null,
    moreInfo: String? = null,
  ) :
    this(status.value(), errorCode, userMessage, developerMessage, moreInfo)
}
