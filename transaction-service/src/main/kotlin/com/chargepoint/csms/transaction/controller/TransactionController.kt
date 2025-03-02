package com.chargepoint.csms.transaction.controller

import com.chargepoint.csms.dto.AuthorizationRequestDto
import com.chargepoint.csms.dto.AuthorizationResponseDto
import com.chargepoint.csms.mapper.toDto
import com.chargepoint.csms.models.AuthorizationRequest
import com.chargepoint.csms.models.enums.AuthStatus
import com.chargepoint.csms.transaction.service.TransactionService
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/csms")
class AuthorizationController(
    private val authService: TransactionService
) {

    @PostMapping("/authorize")
    fun authorize(@RequestBody request: AuthorizationRequestDto): ResponseEntity<AuthorizationResponseDto> {
        val responseDto = authService.processAuthorization(request).toDto()
        return when(responseDto.authorizationStatus){
            AuthStatus.ACCEPTED -> ResponseEntity.ok(responseDto)
            AuthStatus.UNKNOWN -> ResponseEntity.badRequest().body(null)
            AuthStatus.INVALID -> ResponseEntity.status(HttpStatus.FORBIDDEN).body(null)
            AuthStatus.REJECTED -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        }
    }
}