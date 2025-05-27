package org.example.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false); // 如果是阻塞模式 只能使用ssc的accept()
        ssc.register(selector, SelectionKey.OP_ACCEPT); //
        ssc.bind(new InetSocketAddress("localhost",8080)); // ssc 绑定接口
        while(true)
        {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext())
            {
                SelectionKey key = iterator.next();
                // 这里是因为我不知道是否处理结束了
                // selector.select(); -- 作用就是把发生事件的 key 放入 selectionKeys
                // 但是并不会剔除这个集合
                iterator.remove(); // 剔除上一个 next 过的iter元素
                if(key.isAcceptable())
                {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }
                if(key.isReadable())
                {
                    SocketChannel channel = (SocketChannel)key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int length = channel.read(byteBuffer);
                    if(length == -1)
                    {
                        channel.close();
                    } else {
                        byteBuffer.flip();
                        byte[] buffer = new byte[byteBuffer.remaining()]; // 得到剩余
                        byteBuffer.get(buffer);
                        String message = new String(buffer);
                        System.out.println(message);
                    }
                }
            }
        }
    }
}
