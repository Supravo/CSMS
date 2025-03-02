package com.chargepoint.csms.mapper

import com.chargepoint.csms.dto.AuthorizationRequestDto
import com.chargepoint.csms.dto.DriverIdentifierDto
import com.chargepoint.csms.models.AuthorizationRequest
import com.chargepoint.csms.models.DriverIdentifier

fun DriverIdentifierDto.toDomain() : DriverIdentifier{
    return DriverIdentifier(this.id)
}

fun AuthorizationRequestDto.toDomain(requestUuid: String): AuthorizationRequest {
    return AuthorizationRequest(requestUuid, this.stationUuid, this.driverIdentifier.toDomain())
}