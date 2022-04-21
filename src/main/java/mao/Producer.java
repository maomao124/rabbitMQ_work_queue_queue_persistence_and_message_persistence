package mao;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import mao.tools.RabbitMQ;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * Project name(项目名称)：rabbitMQ工作队列之队列持久化和消息持久化
 * Package(包名): mao
 * Class(类名): Producer
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/4/21
 * Time(创建时间)： 22:17
 * Version(版本): 1.0
 * Description(描述)：
 * 将消息标记为持久化并不能完全保证不会丢失消息。尽管它告诉 RabbitMQ 将消息保存到磁盘，但是
 * 这里依然存在当消息刚准备存储在磁盘的时候 但是还没有存储完，消息还在缓存的一个间隔点。此时并没
 * 有真正写入磁盘。持久性保证并不强，但是对于我们的简单任务队列而言，这已经绰绰有余了
 */

public class Producer
{
    private static final String QUEUE_NAME = "durable_work";

    public static void main(String[] args) throws IOException, TimeoutException
    {
        Channel channel = RabbitMQ.getChannel();
        //queue - 队列的名称
        //durable - 如果我们声明一个持久队列，则为真（该队列将在服务器重新启动后继续存在）
        //exclusive - 如果我们声明一个独占队列，则为真（仅限于此连接）
        //autoDelete - 如果我们声明一个自动删除队列，则为真（服务器将在不再使用时将其删除）
        //arguments - 队列的其他属性（构造参数）
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        for (int i = 0; i < 20; i++)
        {
            String message = "消息" + i;
            channel.basicPublish("", QUEUE_NAME,
                    MessageProperties.PERSISTENT_BASIC,
                    message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
