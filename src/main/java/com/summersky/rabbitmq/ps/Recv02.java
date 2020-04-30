package com.summersky.rabbitmq.ps;

import com.rabbitmq.client.*;
import com.summersky.rabbitmq.tools.ConectionUtity;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-4-30
 * @Time:22:43
 * @Description:工作队列消费者02
 */
public class Recv02 {
    /**
     * 声明队列名称
     */
    private static final String QUEUE_NAME = "test_queue_fanout_sms";
    /**
     * 声明交换机名称
     */
    private static final String EXCHANGE_NAME = "test_exchange_fanout";
    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConectionUtity.getConnection();
        // 获取通道
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        // 绑定队列到交换机
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");
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
                }
            }
        };
        // 自动应答，公平分发必须关闭
        boolean autoAck = false;
        // 监听队列
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);
    }
}
