package rpc;


import org.apache.hadoop.ipc.VersionedProtocol;

public interface ClientProtocol extends VersionedProtocol {

    public static final long versionID = 11111l;
    String echo (String value);
}
