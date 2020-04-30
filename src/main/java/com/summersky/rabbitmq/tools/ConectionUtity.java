package com.summersky.rabbitmq.tools;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-4-30
 * @Time:16:31
 * @Description:MQ连接工具类
 */
public class ConectionUtity {

    /**
     * 返回一个MQ连接
     * @return
     */
    public static Connection getConnection() throws IOException, TimeoutException {
        // 定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 连接地址
        factory.setHost("127.0.0.1");
        // 协议端口
        factory.setPort(5672);
        // vhosts,相当于数据库
        factory.setVirtualHost("/MQ01");
        // 用户名
        factory.setUsername("jc");
        // 密码
        factory.setPassword("root");
        return factory.newConnection();
    }

}
