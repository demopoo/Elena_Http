package reactor.evenhandler;import reactor.EvenHandlerFactory;import reactor.evenhandler.handfactory.EvenHandlerAdapter;/** * Created by demopoo on 2017/8/16. */public class AcceptHandler extends EvenHandlerAdapter {    @Override    public void OnAccept() {        System.out.println("onaccept");    }    @Override    public void afterPropertiesSet() throws Exception {        EvenHandlerFactory.addEvenHandler(this.getClass().getSimpleName(),this);    }}