package com.summersky.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.summersky.rabbitmq.tools.ConectionUtity;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-4-30
 * @Time:22:32
 * @Description:主题模式MQ生产者
 */

public class Send {
    /**
     * 声明交换机名称
     */
    private static final String EXCHANGE_NAME = "test_exchange_topic";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 获取连接
        Connection connection = ConectionUtity.getConnection();
        // 获取通道
        Channel channel = connection.createChannel();
        // 声明交换机，topic：主题模式
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
        // 路由键
        String rouyingKey = "ok.add";
        // 发消息
        for (int i = 0;i<50;i++){
            String msg = "How do you do!"+"   "+i;
            channel.basicPublish(EXCHANGE_NAME,rouyingKey,null,msg.getBytes());
            System.out.println("主题模式发消息来了："+msg);
            Thread.sleep(i*20);
        }
        // 关闭资源
        channel.close();
        connection.close();
    }

}
