package reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class SocketReadHandler implements Runnable {

    private SocketChannel socketChannel;

    public SocketReadHandler(Selector selector,SocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);

        SelectionKey selectionKey = socketChannel.register(selector, 0);

        //将该socketChannel注册的SelectionKey绑定为本SocketReadHandler
        //下一步有事件触发时，将调用本类的run方法。
        //参看dispatch(SelectionKey key)
        selectionKey.attach(this);

        // 同时将selectionKey 标记为可读，以便读取
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();

    }

    /**
     *
     */
    @Override
    public void run() {
        ByteBuffer input = ByteBuffer.allocate(1024);
        input.clear();

        try {
            socketChannel.read(input);
            //激活线程池 处理这些request
            //requestHandle(new Request(socket,btt));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
