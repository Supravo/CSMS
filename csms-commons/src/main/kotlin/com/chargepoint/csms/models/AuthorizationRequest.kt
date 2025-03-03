package com.chargepoint.csms.models

data class DriverIdentifier(val id: String = "")

data class AuthorizationRequest(
    val requestUuid: String = "",
    val stationUuid: String = "",
    val driverIdentifier: DriverIdentifier = DriverIdentifier()
)
