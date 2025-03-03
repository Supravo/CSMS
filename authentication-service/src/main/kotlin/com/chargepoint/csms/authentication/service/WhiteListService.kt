package com.chargepoint.csms.authentication.service

import com.chargepoint.csms.models.enums.AuthStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WhitelistService {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    private val whitelist = mapOf(
        "validId12345678901234567890" to true,
        "knownButNotAllowedId1234567890" to false
    )

    fun checkAuthorization(identifier: String): AuthStatus {
        if (identifier.length < 20 || identifier.length > 80) {
            return AuthStatus.INVALID
        }

        return when (val allowed = whitelist[identifier]) {
            true -> AuthStatus.ACCEPTED
            false -> AuthStatus.REJECTED
            null -> AuthStatus.UNKNOWN
        }
    }
}