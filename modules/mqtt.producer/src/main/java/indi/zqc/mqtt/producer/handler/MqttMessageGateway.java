package indi.zqc.mqtt.producer.handler;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

/**
 * Title : MqttMessageGateway.java
 * Package : indi.zqc.mqtt.producer.handler
 * Description : 消息发送接口，不需要实现，spring会通过代理的方式实现
 * Create on : 2018/1/4 10:58
 *
 * @author Zhu.Qianchang
 * @version v1.0.0
 */
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttMessageGateway {

    void sendMessage(Message<?> message);
}
