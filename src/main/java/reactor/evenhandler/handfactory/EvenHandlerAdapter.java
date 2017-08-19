package reactor.evenhandler.handfactory;import org.springframework.beans.factory.InitializingBean;import reactor.receive.HttpRequest;import reactor.send.HttpResponse;import java.nio.channels.SelectionKey;/** * Created by demopoo on 2017/8/17. */public abstract class EvenHandlerAdapter implements ServerListener,InitializingBean {    public void OnError(String errorMsg) {    }    public void OnRead(SelectionKey selectionKey) {    }    public void OnWriter(HttpRequest httpRequest, HttpResponse httpResponse) {    }    public void OnAccept() {    }    public void OnAccepted(HttpRequest httpRequest) {    }    public void OnClosed(HttpRequest httpRequest) {    }    public void run() {    }}