package com.chargepoint.csms.models

import org.apache.kafka.common.Uuid

data class DriverIdentifier(val id: String = "")

data class AuthorizationRequest(
    val requestUuid: String = "",
    val stationUuid: String = "",
    val driverIdentifier: DriverIdentifier = DriverIdentifier()
)
