package com.chargepoint.csms.authentication.service


import com.chargepoint.csms.models.AuthorizationRequest
import com.chargepoint.csms.models.AuthorizationResponse
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Service

@Service
class AuthenticationServiceListener(
    private val whitelistService: WhitelistService
) {

    @KafkaListener(topics = ["\${kafka.request.topic}"], groupId = "\${spring.kafka.consumer.group-id}")
    @SendTo
    fun listen (
        request: AuthorizationRequest,
        @Header(KafkaHeaders.REPLY_TOPIC) replyTopic: String,
        @Header(KafkaHeaders.CORRELATION_ID) correlationId: Int
    ): AuthorizationResponse {
        val status = whitelistService.checkAuthorization(request.driverIdentifier.id)
        val response = AuthorizationResponse(request.requestUuid, status)
        return response
    }
}