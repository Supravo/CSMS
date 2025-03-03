package com.chargepoint.csms.transaction.exception

import com.chargepoint.csms.models.exception.AuthorizationException
import com.chargepoint.csms.models.exception.ErrorResponse
import org.apache.kafka.common.errors.TimeoutException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.time.Instant
import java.util.concurrent.ExecutionException

@RestControllerAdvice
class RestExceptionHandler {

    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(TimeoutException::class)
    fun handleTimeoutException(ex: TimeoutException, request: WebRequest): ResponseEntity<ErrorResponse> {
        log.error("Request timed out: ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
            .body(createErrorResponse(HttpStatus.GATEWAY_TIMEOUT, "Request timeout", request))
    }

    @ExceptionHandler(ExecutionException::class)
    fun handleExecutionException(ex: ExecutionException, request: WebRequest): ResponseEntity<ErrorResponse> {
        log.error("Execution error: ${ex.cause?.message}", ex)
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(createErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable", request))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<ErrorResponse> {
        log.error("Invalid input: ${ex.message}", ex)
        return ResponseEntity.badRequest()
            .body(createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid request data", request))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        log.error("Unexpected error: ${ex.message}", ex)
        return ResponseEntity.internalServerError()
            .body(createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request))
    }

    @ExceptionHandler(AuthorizationException::class)
    fun handleAuthorizationException(ex: AuthorizationException, request: WebRequest): ResponseEntity<ErrorResponse> {
        log.warn("Authorization error: ${ex.message}")
        return ResponseEntity.status(ex.statusCode)
            .body(createErrorResponse(ex.statusCode, ex.message ?: "Authorization error", request))
    }

    private fun createErrorResponse(
        status: HttpStatus,
        message: String,
        request: WebRequest
    ): ErrorResponse {
        return ErrorResponse(
            timestamp = Instant.now(),
            status = status.value(),
            error = status.reasonPhrase,
            message = message,
            path = request.getDescription(false).replace("uri=", "")
        )
    }
}