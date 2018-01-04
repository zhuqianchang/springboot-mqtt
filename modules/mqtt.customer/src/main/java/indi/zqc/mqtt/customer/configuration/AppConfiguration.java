package indi.zqc.mqtt.customer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Configuration
public class AppConfiguration {

    @Value("${mqtt.broker.serverUri}")
    private String serviceUri;

    @Value("${mqtt.broker.username:}")
    private String username;

    @Value("${mqtt.broker.password:}")
    private String password;

    @Value("${mqtt.topic}")
    private String topic;

    @Value("${mqtt.clientId}")
    private String clientId;

    @Value("${mqtt.qos}")
    private Integer qos;

    @Value("${mqtt.completionTimeout}")
    private Integer completionTimeout;

    /**
     * 消息通道
     * @return
     */
    @Bean
    public MessageChannel mqttInputChannel() {
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
     * @param mqttInputChannel
     * @return
     */
    @Bean
    public MessageProducer inbound(MqttPahoClientFactory clientFactory, MessageChannel mqttInputChannel) {
        //clientId 客户端ID，生产端和消费端的客户端ID需不同，不然服务器会认为是同一个客户端，会接收不到信息
        //topic 订阅的主题
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId, clientFactory, topic);
        //超时时间
        adapter.setCompletionTimeout(completionTimeout);
        //转换器
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(qos);
        adapter.setOutputChannel(mqttInputChannel);
        return adapter;
    }

    /**
     * 消息处理
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {

            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println(message.getPayload());
            }

        };
    }

}
