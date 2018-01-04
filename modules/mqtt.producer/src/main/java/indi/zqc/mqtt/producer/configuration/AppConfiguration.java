package indi.zqc.mqtt.producer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;

@Configuration
public class AppConfiguration {

    @Value("${mqtt.broker.serverUri}")
    private String serviceUri;

    @Value("${mqtt.broker.username:}")
    private String username;

    @Value("${mqtt.broker.password:}")
    private String password;

    @Value("${mqtt.clientId}")
    private String clientId;

    /**
     * 消息通道
     * @return
     */
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * mqtt服务器配置
     * @return
     */
    @Bean
    public MqttPahoClientFactory clientFactory() {
        DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
        clientFactory.setServerURIs(serviceUri);
        clientFactory.setUserName(username);
        clientFactory.setPassword(password);
        return clientFactory;
    }

    /**
     * 通道适配器
     * @param clientFactory
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MqttPahoMessageHandler mqttOutbound(MqttPahoClientFactory clientFactory) {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId, clientFactory);
        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(1);
        messageHandler.setDefaultRetained(false);
        messageHandler.setAsyncEvents(false);
        return messageHandler;
    }

}
