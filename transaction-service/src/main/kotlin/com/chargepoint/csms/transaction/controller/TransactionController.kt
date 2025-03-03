package com.chargepoint.csms.transaction.controller

import com.chargepoint.csms.dto.AuthorizationRequestDto
import com.chargepoint.csms.dto.AuthorizationResponseDto
import com.chargepoint.csms.mapper.toDto
import com.chargepoint.csms.models.enums.AuthStatus
import com.chargepoint.csms.models.exception.AuthorizationInvalidException
import com.chargepoint.csms.models.exception.AuthorizationRejectedException
import com.chargepoint.csms.models.exception.AuthorizationUnknownException
import com.chargepoint.csms.transaction.service.TransactionService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/csms")
class AuthorizationController(
    private val authService: TransactionService
) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/authorize")
    fun authorize(@RequestBody request: AuthorizationRequestDto): ResponseEntity<AuthorizationResponseDto> {
        val responseDto = authService.processAuthorization(request).toDto()
        return when(responseDto.authorizationStatus){
            AuthStatus.ACCEPTED -> ResponseEntity.ok(responseDto)
            AuthStatus.INVALID -> throw AuthorizationInvalidException("Invalid driverIdentifier! driverIdentifierId length should be within range of 20 - 80")
            AuthStatus.UNKNOWN -> throw AuthorizationUnknownException("Driver is unknown, not authorized for charging!")
            AuthStatus.REJECTED -> throw AuthorizationRejectedException("Driver is blacklisted, not authorized for charging!")
        }
    }
}