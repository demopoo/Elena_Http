package reactormode;import reactormode.buffer.DirectByteBufferPools;import java.io.IOException;import java.nio.ByteBuffer;/** * Created by demopoo on 2017/9/9. */public class MainReactor {    public static void main(String[] args) throws IOException{        NIOAccepter nioAccepter = new NIOAccepter();        nioAccepter.start();    }}