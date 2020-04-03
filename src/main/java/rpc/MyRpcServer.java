package rpc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;

import java.io.IOException;

public class MyRpcServer {

    public static void main(String[] args) throws IOException {

        Server server = new RPC.Builder(new Configuration())
                .setProtocol(ClientProtocol.class)
                .setInstance(new ClientProtocolImpl())
                .setBindAddress("127.0.0.1")
                .setPort(8787)
                .setNumHandlers(5)
                .build();

        server.start();

    }
}
