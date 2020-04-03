package nio;

import javax.naming.ldap.SortKey;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ServerSocketChannelTest {

    private  int size = 1024;
    private ServerSocketChannel socketChannel;
    private  ByteBuffer byteBuffer;
    private  Selector selector;
    private  final int port = 8998;
    private  int remoteChientNum = 0;

    public ServerSocketChannelTest() {
        try {
            initChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            new ServerSocketChannelTest().listener();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initChannel() throws IOException {
        socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.bind(new InetSocketAddress(port));

        System.out.println(" listener on port : " + port);

        // 创建selector
        selector = Selector.open();

        // 为了将Channel和Selector配合使用，必须将channel注册到selector上。
        socketChannel.register(selector,SelectionKey.OP_ACCEPT); // 接收就绪
        byteBuffer = ByteBuffer.allocateDirect(size);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
    }

    private void listener() throws IOException {
        while (true) {
            int n = selector.select();

            if (n == 0) {
                continue;
            }

            // 已选择的键的结合（selected key set）
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                if (key.isAcceptable()) {
                    // 获得 serverSocketChannel
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    SocketChannel channel = serverSocketChannel.accept();

                    registerChannel(selector, channel, SelectionKey.OP_READ);

                    remoteChientNum++;
                    System.out.println(" online client num = " + remoteChientNum);

                    // TODO : 回应客户端
                    replyClient(channel);
                }

                if (key.isReadable()) {
                    // TODO : 读取数据
                    readDataFromSocket(key);
                }

                iterator.remove();// must
            }
        }
    }

    private void replyClient(SocketChannel channel) throws IOException {

        byteBuffer.clear();
        byteBuffer.put(" hello client!".getBytes());
        byteBuffer.flip();
        channel.write(byteBuffer);
    }

    private void registerChannel(Selector selector,SocketChannel channel,int opRead) throws IOException {
        if (channel == null) {
            return;
        }

        channel.configureBlocking(false);
        channel.register(selector, opRead);
    }

    private void readDataFromSocket(SelectionKey key) throws IOException {

        SocketChannel socketChannel = (SocketChannel) key.channel();
        int count;
        byteBuffer.clear();
        while ((count = socketChannel.read(byteBuffer)) > 0) {
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                socketChannel.write(byteBuffer);
            }
            byteBuffer.clear();
        }
        if (count < 0) {
            socketChannel.close();
        }

    }


}
