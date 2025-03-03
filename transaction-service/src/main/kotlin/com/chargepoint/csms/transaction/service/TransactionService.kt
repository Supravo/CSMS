package com.chargepoint.csms.transaction.service

import com.chargepoint.csms.dto.AuthorizationRequestDto
import com.chargepoint.csms.mapper.toDomain
import com.chargepoint.csms.models.AuthorizationRequest
import com.chargepoint.csms.models.AuthorizationResponse
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit


@Service
class TransactionService(
    private val kafkaTemplate: ReplyingKafkaTemplate<Int, AuthorizationRequest, AuthorizationResponse>
) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${kafka.request.topic}")
    private lateinit var requestTopic: String

    fun processAuthorization(requestDto: AuthorizationRequestDto): AuthorizationResponse {
        val requestUuid = UUID.randomUUID().toString()
        val request = requestDto.toDomain(requestUuid)

        val producerRecord: ProducerRecord<Int, AuthorizationRequest>  = ProducerRecord(requestTopic, request)
        val future = kafkaTemplate.sendAndReceive(producerRecord)
        val sendResult = future.getSendFuture().get(10, TimeUnit.SECONDS)
        log.debug("Auth Request Sent with " +
                "metadata: ${sendResult.recordMetadata}, " +
                "headers: ${sendResult.producerRecord.headers()}, " +
                "key: ${sendResult.producerRecord.key()}, " +
                "body: ${sendResult.producerRecord.value()}")
        val response = future.get(10, TimeUnit.SECONDS)
        log.debug("Received Auth Response with message: ${response.value()}, headers: ${response.headers()}")
        return response.value()
    }
}