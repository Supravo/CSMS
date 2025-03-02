package com.chargepoint.csms.authentication.service


import com.chargepoint.csms.models.AuthorizationRequest
import com.chargepoint.csms.models.AuthorizationResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service

@Service
class AuthenticationServiceListener(
    private val whitelistService: WhitelistService,
    private val kafkaTemplate: KafkaTemplate<String, AuthorizationResponse>
) {

    @Value("\${kafka.reply.topic}")
    private lateinit var replyTopic: String

    @KafkaListener(topics = ["\${kafka.request.topic}"])
    fun listen(
        request: AuthorizationRequest
    ) {
        val status = whitelistService.checkAuthorization(request.driverIdentifier.id)
        val response = AuthorizationResponse(request.requestUuid, status)
        kafkaTemplate.send(replyTopic, request.requestUuid, response)
    }
}