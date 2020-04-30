package com.summersky.rabbitmq.work;

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
 * @Description:工作队列MQ生产者
 */

public class Send {
    private static final String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 获取连接
        Connection connection = ConectionUtity.getConnection();
        // 获取通道
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        // 发消息
        for (int i = 0;i<50;i++){
            String msg = "How do you do!"+"   "+i;
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
            System.out.println("工作模式发消息来了："+msg);
            Thread.sleep(i*20);
        }
        // 关闭资源
        channel.close();
        connection.close();
    }

}
