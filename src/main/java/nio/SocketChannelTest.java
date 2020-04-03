package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

public class SocketChannelTest {

    private int size = 1024;
    private ByteBuffer byteBuffer;
    private SocketChannel socketChannel;

    public void connectServer() throws IOException, InterruptedException {
        socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8998));

        byteBuffer = ByteBuffer.allocateDirect(size);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);

        receive();

    }

    private void receive() throws IOException, InterruptedException {

        while (true) {
            int count;
            byteBuffer.clear();
            while ((count = socketChannel.read(byteBuffer)) > 0) {
                byteBuffer.flip();

                StringBuffer stringBuffer = new StringBuffer();
                while (byteBuffer.hasRemaining()) {
                    stringBuffer.append((char)byteBuffer.get());
//                    System.out.print((char)byteBuffer.get());
                }
                System.out.println(" ==== : " + stringBuffer.toString());
                send("send data to server".getBytes());

                byteBuffer.clear();
                Thread.sleep(6000);
            }

        }

    }

    private void send(byte[] data) throws IOException {
        byteBuffer.clear();
        byteBuffer.put(data);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new SocketChannelTest().connectServer();
    }

}
