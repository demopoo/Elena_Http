import biz.HttpServerExcuteRead;import threadpool.ThreadPoolCommen;import javax.naming.ldap.SortKey;import java.io.IOException;import java.net.InetSocketAddress;import java.nio.ByteBuffer;import java.nio.channels.SelectionKey;import java.nio.channels.Selector;import java.nio.channels.ServerSocketChannel;import java.nio.channels.SocketChannel;import java.util.Iterator;import java.util.SortedSet;import java.util.concurrent.CancellationException;import java.util.concurrent.ExecutorService;/** * Created by demopoo on 2017/8/2. */public class MainHttpServer {    public static ServerSocketChannel serverSocketChannel;    public static Selector selector;    public static void main(String[] arg){        try {            serverSocketChannel = ServerSocketChannel.open();            serverSocketChannel.bind(new InetSocketAddress(9898));            serverSocketChannel.configureBlocking(false);            selector = Selector.open();            int i = 0 ;            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);            while(selector.select() > 0){                Iterator<SelectionKey> iSK = selector.selectedKeys().iterator();                if (iSK.hasNext()){                    SelectionKey sk = iSK.next();                    try {                        if (sk.isAcceptable()){                            ServerSocketChannel sSocketChannel = (ServerSocketChannel) sk.channel();                            SocketChannel socketChannel = sSocketChannel.accept();                            socketChannel.configureBlocking(false);                            socketChannel.register(selector,SelectionKey.OP_READ);                        }                        if (sk.isReadable()){                            ExecutorService service = ThreadPoolCommen.achieveThread();                            HttpServerExcuteRead httpServerExcuteRead = new HttpServerExcuteRead(selector,sk,System.currentTimeMillis()+".png");                            service.submit(httpServerExcuteRead);                            sk.cancel();                        }//                        if (sk.isWritable()){//                            System.out.println("write ready");//                        }                    }catch (CancellationException ex){                        sk.cancel();                        ex.printStackTrace();                    }                }                iSK.remove();            }        }catch (IOException ex){            ex.printStackTrace();        }    }}