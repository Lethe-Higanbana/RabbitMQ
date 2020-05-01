package com.summersky.rabbitmq.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.summersky.rabbitmq.tools.ConectionUtity;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-4-30
 * @Time:16:52
 * @Description:消息生产者confirm普通模式
 */
public class Send03 {
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
        // 存放未确认的消息标识
        final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
        // 通道添加监听
        channel.addConfirmListener(new ConfirmListener() {
            // 成功的
            public void handleAck(long l, boolean b) throws IOException {
                if (b){
                    confirmSet.headSet(l+1).clear();
                }else {
                    confirmSet.remove(l);
                }
            }
            // 失败的
            public void handleNack(long l, boolean b) throws IOException {
                if (b){
                    confirmSet.headSet(l+1).clear();
                }else {
                    confirmSet.remove(l);
                }
            }
        });
        String msg = "How do you do!";
        while (true){
            long sendNo = channel.getNextPublishSeqNo();
            // 发消息
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
            // 将标识存入容器中
            confirmSet.add(sendNo);
        }
    }
}
