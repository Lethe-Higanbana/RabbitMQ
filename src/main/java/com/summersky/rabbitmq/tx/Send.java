package com.summersky.rabbitmq.tx;

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
 * @Description:消息生产者
 */
public class Send {
    /**
     * 队列名称
     */
    private static final String QUEUE_NAME = "test_simple_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        // 拿到连接
        Connection connection = ConectionUtity.getConnection();
        // 从连接中获取一个通道
        Channel channel = connection.createChannel();
        // 创建队列，相关参数后续解读
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        String msg = "How do you do!";
        try {
            // 开启事务
            channel.txSelect();
            // 发消息
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
            // 提交事务
            channel.txCommit();
            System.out.println("发消息来了："+msg);
        } catch (IOException e) {
            // 失败回滚
            channel.txRollback();
            e.printStackTrace();
        }
        // 关连接、通道
        channel.close();
        connection.close();
    }
}
