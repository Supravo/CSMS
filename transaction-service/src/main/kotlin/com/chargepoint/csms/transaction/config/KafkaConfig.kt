package com.chargepoint.csms.transaction.config

import com.chargepoint.csms.models.AuthorizationRequest
import com.chargepoint.csms.models.AuthorizationResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate


@Configuration
open class KafkaConfig {

    @Value("\${kafka.reply.topic}")
    private lateinit var replyTopic: String

    @Bean
    open fun replyingTemplate(
        pf: ProducerFactory<Int, AuthorizationRequest>,
        repliesContainer: ConcurrentMessageListenerContainer<Int, AuthorizationResponse>
    ): ReplyingKafkaTemplate<Int, AuthorizationRequest, AuthorizationResponse> {
        val replyTemplate = ReplyingKafkaTemplate(pf, repliesContainer)
        replyTemplate.setSharedReplyTopic(true)
        return replyTemplate
    }

    @Bean
    open fun repliesContainer(
        containerFactory: ConcurrentKafkaListenerContainerFactory<Int, AuthorizationResponse>
    ): ConcurrentMessageListenerContainer<Int, AuthorizationResponse> {
        val repliesContainer: ConcurrentMessageListenerContainer<Int, AuthorizationResponse> =
            containerFactory.createContainer(replyTopic)
        repliesContainer.isAutoStartup = false
        return repliesContainer
    }
}