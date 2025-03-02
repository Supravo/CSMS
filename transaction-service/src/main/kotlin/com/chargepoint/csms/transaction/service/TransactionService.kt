package com.chargepoint.csms.transaction.service

import com.chargepoint.csms.dto.AuthorizationRequestDto
import com.chargepoint.csms.mapper.toDomain
import com.chargepoint.csms.models.AuthorizationRequest
import com.chargepoint.csms.models.AuthorizationResponse
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit


@Service
class TransactionService(
    private val kafkaTemplate: ReplyingKafkaTemplate<String, AuthorizationRequest, AuthorizationResponse>
) {

    @Value("\${kafka.request.topic}")
    private lateinit var requestTopic: String

    fun processAuthorization(requestDto: AuthorizationRequestDto): AuthorizationResponse {
        val requestUuid = UUID.randomUUID().toString()
        val request = requestDto.toDomain(requestUuid)

        val producerRecord = ProducerRecord(requestTopic, requestUuid, request)
        val future = kafkaTemplate.sendAndReceive(producerRecord)
        val response = future.get(10, TimeUnit.SECONDS).value()
        return response
    }
}