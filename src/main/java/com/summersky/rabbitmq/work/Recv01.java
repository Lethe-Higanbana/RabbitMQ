package com.summersky.rabbitmq.work;

import com.rabbitmq.client.*;
import com.summersky.rabbitmq.tools.ConectionUtity;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-4-30
 * @Time:22:43
 * @Description:工作队列消费者01
 */
public class Recv01 {
    private static final String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConectionUtity.getConnection();
        // 获取通道
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        // 定义消费者，接收消息，一旦有消息就会触发重写的方法，消息体就是body
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                String msg = new String(body,"utf-8");
                System.out.println("消费者01收到了消息："+ msg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("消费者01出错。。。。");
                }
            }
        };
        // 监听队列
        channel.basicConsume(QUEUE_NAME,true,consumer);
    }
}
