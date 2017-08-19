package reactor;import reactor.evenhandler.handfactory.ServerListener;import reactor.receive.HttpRequest;import threadpool.ThreadPoolCommen;import java.io.IOException;import java.net.InetSocketAddress;import java.nio.ByteBuffer;import java.nio.channels.SelectionKey;import java.nio.channels.Selector;import java.nio.channels.ServerSocketChannel;import java.nio.channels.SocketChannel;import java.util.Iterator;import java.util.concurrent.ExecutorService;/** * Created by demopoo on 2017/8/17. */public class MainHttpServer {    private ServerSocketChannel serverSocketChannel;    private SocketChannel socketChannel;    private ByteBuffer byteBuffer;    private Selector selector;    public MainHttpServer(int port){        try {            this.serverSocketChannel = ServerSocketChannel.open();            this.serverSocketChannel.bind(new InetSocketAddress(port));            this.serverSocketChannel.configureBlocking(false);            this.selector = Selector.open();            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);        }catch (IOException ex){            ex.printStackTrace();        }    }    public void run(){        while (selector != null){            try {                while (selector.select() > 0){                    Iterator<SelectionKey> sks = selector.selectedKeys().iterator();                    if (sks.hasNext()){                        SelectionKey sk = sks.next();                        if (sk.isAcceptable()){                            ServerListener serverListener = EvenHandlerFactory.getServerListener("AcceptHandler");                            serverListener.OnAccept();                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel)sk.channel();                            SocketChannel socketChannel = serverSocketChannel.accept();                            socketChannel.configureBlocking(false);                            socketChannel.register(selector,SelectionKey.OP_READ,socketChannel);                            HttpRequest httpRequest = new HttpRequest(socketChannel);                            serverListener = EvenHandlerFactory.getServerListener("AcceptedHandler");                            serverListener.OnAccepted(httpRequest);                        }                        if (sk.isReadable()){                            SocketChannel sc = (SocketChannel)sk.channel();                            HttpRequest httpRequest = new HttpRequest(sc);                            ServerListener serverListener = EvenHandlerFactory.getServerListener("ReadHandler");                            serverListener.OnRead(sk);                            ExecutorService service = ThreadPoolCommen.achieveThread();                            service.submit(serverListener);                            sk.cancel();                        }//                        if (sk.isWritable()){//                            System.out.println("write ready");//                        }                    }                    sks.remove();                }            }catch (IOException ex){                ex.printStackTrace();            }        }    }}