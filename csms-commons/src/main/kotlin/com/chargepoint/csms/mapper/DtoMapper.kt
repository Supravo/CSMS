package com.chargepoint.csms.mapper

import com.chargepoint.csms.dto.AuthorizationRequestDto
import com.chargepoint.csms.dto.AuthorizationResponseDto
import com.chargepoint.csms.models.AuthorizationRequest
import com.chargepoint.csms.models.AuthorizationResponse

fun AuthorizationResponse.toDto(): AuthorizationResponseDto {
    return AuthorizationResponseDto(this. requestUUID, this.authenticationStatus)
}