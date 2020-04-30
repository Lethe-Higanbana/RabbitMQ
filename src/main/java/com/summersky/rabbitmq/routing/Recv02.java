package com.summersky.rabbitmq.routing;

import com.rabbitmq.client.*;
import com.summersky.rabbitmq.tools.ConectionUtity;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-4-30
 * @Time:22:43
 * @Description:路由模式消费者02
 */
public class Recv02 {
    /**
     * 声明交换机名称
     */
    private static final String EXCHANGE_NAME = "test_exchange_direct";
    private static final String QUEUE_NAME = "test_queue_direct2";
    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConectionUtity.getConnection();
        // 获取通道
        final Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        // 保证一次只消费一个
        channel.basicQos(1);
        // 绑定队列到交换机,带路由key
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"error");
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"ok");
        // 定义消费者，接收消息，一旦有消息就会触发重写的方法，消息体就是body
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                String msg = new String(body,"utf-8");
                System.out.println("消费者02收到了消息："+ msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("消费者02出错。。。。");
                }finally {
                    // 手动返回确认消息给队列
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };
        // 自动应答，公平分发必须关闭
        boolean autoAck = false;
        // 监听队列
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);
    }
}
