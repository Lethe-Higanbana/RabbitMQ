package com.summersky.rabbitmq.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.summersky.rabbitmq.tools.ConectionUtity;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-4-30
 * @Time:16:52
 * @Description:消息生产者confirm批量模式
 */
public class Send02 {
    /**
     * 队列名称
     */
    private static final String QUEUE_NAME = "test_simple_queue";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 拿到连接
        Connection connection = ConectionUtity.getConnection();
        // 从连接中获取一个通道
        Channel channel = connection.createChannel();
        // 创建队列，相关参数后续解读
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        // 将channel设置为confirm模式,注意你的队列必须是未定义的，是一个新队列，或者说当前队列没有被设置过其他参数
        channel.confirmSelect();
        String msg = "How do you do!";
        // 批量发送
        for (int i = 0;i<10;i++){
            // 发消息
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
        }
        // 如果收到了回执消息，表示发送成功，反之失败
        if (channel.waitForConfirms()){
            System.out.println("发消息来了："+msg);
        }else {
            System.out.println("消息下发失败");
        }
        // 关连接、通道
        channel.close();
        connection.close();
    }
}
