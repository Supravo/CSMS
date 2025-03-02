package com.chargepoint.csms.transaction.config

import com.chargepoint.csms.models.AuthorizationRequest
import com.chargepoint.csms.models.AuthorizationResponse
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import java.time.Duration


@Configuration
open class KafkaConfig {

    @Value("\${kafka.bootstrap-servers}")
    private lateinit var bootstrapServers: String

    @Value("\${kafka.groupid}")
    private lateinit var groupId: String

    @Value("\${kafka.request.topic}")
    private lateinit var requestTopic: String

    @Value("\${kafka.reply.topic}")
    private lateinit var replyTopic: String

    @Value("\${kafka.reply.timeout}")
    private var replyTimeout: Long = 5000

    @Bean
    open fun producerFactory(): ProducerFactory<String, AuthorizationRequest> {
        val configs = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
        )
        return DefaultKafkaProducerFactory(configs)
    }

    @Bean
    open fun replyConsumerFactory(): ConsumerFactory<String, AuthorizationResponse> {
        val configs = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ConsumerConfig.GROUP_ID_CONFIG to groupId,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
            JsonDeserializer.TRUSTED_PACKAGES to "com.chargepoint.csms.*"
        )
        return DefaultKafkaConsumerFactory(configs)
    }

    @Bean
    open fun repliesContainer(consumerFactory: ConsumerFactory<String, AuthorizationResponse>): ConcurrentMessageListenerContainer<String, AuthorizationResponse> {
        val containerProperties = org.springframework.kafka.listener.ContainerProperties(replyTopic)
        return ConcurrentMessageListenerContainer(consumerFactory, containerProperties)
    }

    @Bean
    open fun replyingKafkaTemplate(
        producerFactory: ProducerFactory<String, AuthorizationRequest>,
        repliesContainer: ConcurrentMessageListenerContainer<String, AuthorizationResponse>
    ): ReplyingKafkaTemplate<String, AuthorizationRequest, AuthorizationResponse> {
        val template = ReplyingKafkaTemplate(producerFactory, repliesContainer)
        template.defaultTopic = requestTopic
        template.setDefaultReplyTimeout(Duration.ofMillis(replyTimeout))
        return template
    }
}