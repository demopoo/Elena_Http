package reactor.util;import java.net.URLDecoder;/** * Created by demopoo on 2017/8/4. */public class Decode {    public static String decodeParam(String str){        String decodeP = null;        try {            decodeP = URLDecoder.decode(str,"UTF-8");        }catch (Exception ex){            ex.printStackTrace();        }        return decodeP;    }}