package com.summersky.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
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
        // 定义队列消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        // 监听哪一个队列
        channel.basicConsume(QUEUE_NAME,true,consumer);
        while (true){
            // 接收消息的包装体
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("消息收到了："+msg);
        }
    }
}
