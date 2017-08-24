package reactor;import reactor.evenhandler.ReadHandler;import reactor.evenhandler.handfactory.ServerListener;import reactor.receive.HttpRequest;import threadpool.ThreadPoolCommen;import java.io.File;import java.io.IOException;import java.net.InetSocketAddress;import java.nio.ByteBuffer;import java.nio.channels.*;import java.nio.file.Paths;import java.nio.file.StandardOpenOption;import java.util.HashMap;import java.util.Iterator;import java.util.Map;import java.util.Random;import java.util.concurrent.ExecutorService;/** * Created by demopoo on 2017/8/17. */public class MainHttpServer {    private ServerSocketChannel serverSocketChannel;    private SocketChannel socketChannel;    private ByteBuffer byteBuffer;    private Selector selector;    private Map<SelectionKey,FileChannel> map = new HashMap<SelectionKey, FileChannel>();    public MainHttpServer(int port){        try {            this.serverSocketChannel = ServerSocketChannel.open();            this.serverSocketChannel.bind(new InetSocketAddress(port));            this.serverSocketChannel.configureBlocking(false);            this.selector = Selector.open();            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);        }catch (IOException ex){            ex.printStackTrace();        }    }    public void run(){        while (selector != null){            try {                while (true){                    if (selector.select() == 0)                        continue;                    try {                        Iterator<SelectionKey> sks = selector.selectedKeys().iterator();                        if (sks.hasNext()){                            SelectionKey sk = sks.next();                            if (!sk.isValid())                                continue;                            if (sk.isAcceptable()){                                ServerListener serverListener = EvenHandlerFactory.getServerListener("AcceptHandler");                                serverListener.OnAccept();                                ServerSocketChannel serverSocketChannel = (ServerSocketChannel)sk.channel();                                SocketChannel socketChannel = serverSocketChannel.accept();                                socketChannel.configureBlocking(false);                                SelectionKey selectionKeys = socketChannel.register(selector,SelectionKey.OP_READ);                                Random random = new Random();                                FileChannel fileChannel = FileChannel.open(Paths.get("/Users/demopoo/Desktop/"+System.currentTimeMillis()+""+random.nextInt(1000)+".png"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);                                map.put(selectionKeys,fileChannel);                                HttpRequest httpRequest = new HttpRequest(socketChannel);                                serverListener = EvenHandlerFactory.getServerListener("AcceptedHandler");                                serverListener.OnAccepted(httpRequest);                                sks.remove();                            }else if (sk.isReadable()){                                System.out.println("*************************准备进入读事件***********************");                                SocketChannel sc = (SocketChannel)sk.channel();                                HttpRequest httpRequest = new HttpRequest(sc);                                ServerListener serverListener = EvenHandlerFactory.getServerListener("ReadHandler");                                serverListener.OnRead(sks,selector,sk,map);                                ExecutorService service = ThreadPoolCommen.achieveThread();                                service.submit(serverListener);                                sk.interestOps(sk.interestOps() &~ sk.readyOps());//                                sk.cancel();                            } else if (sk.isWritable()){                                ByteBuffer bf = (ByteBuffer) sk.attachment();                                SocketChannel socketChannel = (SocketChannel)sk.channel();                                if (bf != null){                                    socketChannel.write(bf);                                    if (bf.hasRemaining()){                                        bf = bf.compact();                                        sk.attach(bf);                                        sk.interestOps(sk.interestOps() | SelectionKey.OP_WRITE);                                    }else {                                        sk.attach(null);                                        sk.interestOps(sk.interestOps() &~ SelectionKey.OP_WRITE);                                    }                                }                                sks.remove();                            }                        }                    }catch (CancelledKeyException ex){                        ex.printStackTrace();                    }                }            }catch (IOException ex){                ex.printStackTrace();            }        }    }}