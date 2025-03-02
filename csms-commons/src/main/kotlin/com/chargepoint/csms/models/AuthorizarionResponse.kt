package com.chargepoint.csms.models

import com.chargepoint.csms.models.enums.AuthStatus

data class AuthorizationResponse(
    val requestUUID: String = "",
    val authenticationStatus: AuthStatus = AuthStatus.INVALID
)