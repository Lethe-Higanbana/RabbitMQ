package com.summersky.rabbitmq.simple;

import com.rabbitmq.client.*;
import com.summersky.rabbitmq.tools.ConectionUtity;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-4-30
 * @Time:17:28
 * @Description:MQ消费者
 */
public class Recv {
    /**
     * 队列名称
     */
    private static final String QUEUE_NAME = "test_simple_queue";

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 获取连接
        Connection connection = ConectionUtity.getConnection();
        // 创建通道
        Channel channel = connection.createChannel();
        // 定义队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 定义消费者，事件监听，消息发送者发送消息会被监听到，消息体就是body
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                String msg = new String(body,"utf-8");
                System.out.println("新API收到了消息："+ msg);
            }
        };
        // 监听队列
        channel.basicConsume(QUEUE_NAME,true,consumer);
    }


    /**
     * 已过时的API
     *
     * @throws IOException
     * @throws TimeoutException
     * @throws InterruptedException
     */
    public static void oldApi() throws IOException, TimeoutException, InterruptedException {
        // 获取连接
        Connection connection = ConectionUtity.getConnection();
        // 创建通道
        Channel channel = connection.createChannel();
        // 定义队列消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        // 监听哪一个队列
        channel.basicConsume(QUEUE_NAME, true, consumer);
        while (true) {
            // 接收消息的包装体
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("旧API收到了消息：" + msg);
        }
    }
}
