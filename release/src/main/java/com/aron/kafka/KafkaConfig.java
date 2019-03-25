package com.aron.kafka;

import com.aron.kafka.dto.RequestReply;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * description:
 * <p>KafkaConfig .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/3/22        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/3/22 16:34
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@PropertySource("classpath:kafka.properties")
@ConfigurationProperties(prefix = "kafka")
@Configuration
public class KafkaConfig {

    @Setter
    @Getter
    private String bootstrapServers;

    @Setter
    @Getter
    private String replyTopic;

    @Setter
    @Getter
    private String group;

    @Setter
    @Getter
    private Integer replyTimeout;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>(16);
        // list of host:port pairs used for establishing the initial connections to the Kakfa cluster
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>(16);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        return props;
    }

    @Bean
    public ProducerFactory<String, RequestReply> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, RequestReply> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ReplyingKafkaTemplate<String, RequestReply, RequestReply> replyKafkaTemplate(ProducerFactory<String, RequestReply> pf,
                                                                                        KafkaMessageListenerContainer<String,
                                                                                                RequestReply> container) {
        ReplyingKafkaTemplate<String, RequestReply, RequestReply> replyingKafkaTemplate = new ReplyingKafkaTemplate<>(pf, container);
        replyingKafkaTemplate.setReplyTimeout(getReplyTimeout());
        return replyingKafkaTemplate;
    }

    @Bean
    public KafkaMessageListenerContainer<String, RequestReply> replyContainer(ConsumerFactory<String, RequestReply> cf) {
        ContainerProperties containerProperties = new ContainerProperties(getReplyTopic());
        return new KafkaMessageListenerContainer<>(cf, containerProperties);
    }

    @Bean
    public ConsumerFactory<String, RequestReply> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(),
                new JsonDeserializer<>(RequestReply.class));
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, RequestReply>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RequestReply> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        //set reply template
        factory.setReplyTemplate(kafkaTemplate());
        return factory;
    }
}
