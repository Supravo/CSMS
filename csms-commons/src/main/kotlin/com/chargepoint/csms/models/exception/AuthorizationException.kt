package com.chargepoint.csms.models.exception

import org.springframework.http.HttpStatus

open class AuthorizationException(
    val statusCode: HttpStatus,
    message: String
) : RuntimeException(message)

class AuthorizationInvalidException(message: String = "Invalid authorization request") :
    AuthorizationException(HttpStatus.BAD_REQUEST, message)

class AuthorizationRejectedException(message: String = "Authorization rejected") :
    AuthorizationException(HttpStatus.UNAUTHORIZED, message)

class AuthorizationUnknownException(message: String = "Unknown authorization status") :
    AuthorizationException(HttpStatus.FORBIDDEN, message)