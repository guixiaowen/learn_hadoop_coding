package rpc;

import org.apache.hadoop.ipc.ProtocolSignature;

import java.io.IOException;

public class ClientProtocolImpl implements ClientProtocol {
    @Override
    public String echo(String value) {
        return "hello : " + value;
    }

    @Override
    public long getProtocolVersion(String s,long l) throws IOException {
        return ClientProtocol.versionID;
    }

    @Override
    public ProtocolSignature getProtocolSignature(String s,long l,int i) throws IOException {
        return new ProtocolSignature(ClientProtocol.versionID, null);
    }
}
