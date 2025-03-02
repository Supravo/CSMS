package com.chargepoint.csms.dto

data class DriverIdentifierDto(val id: String)

data class AuthorizationRequestDto(
    val stationUuid: String,
    val driverIdentifier: DriverIdentifierDto
)
