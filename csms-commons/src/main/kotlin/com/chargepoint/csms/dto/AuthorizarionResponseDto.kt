package com.chargepoint.csms.dto

import com.chargepoint.csms.models.enums.AuthStatus

data class AuthorizationResponseDto(
    val requestUUID: String,
    val authorizationStatus: AuthStatus
)