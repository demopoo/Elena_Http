package session;import exceptions.NoTokenException;import util.Decode;import java.nio.ByteBuffer;import java.util.*;/** * Created by demopoo on 2017/8/2. */public class HttpSession {    private Map<String,String> map;    private Map<String,String> paramMap;    /**     * 计算header的长度     * @param bt     * @param rlen     * @return     */    public int excuteHttpHeaderLen(byte[] bt,int rlen){        System.out.println("解析数据:\r\n"+new String(bt));        int splitbyte = 1;        while (splitbyte < rlen){            if (bt[splitbyte] == '\r' && bt[splitbyte+1] == '\n' && bt[splitbyte+2] == '\r' && bt[splitbyte+3] == '\n'){                System.out.println("---头长:"+(splitbyte+4)+"全长:"+rlen);                return splitbyte + 4;            }            splitbyte++;        }        return 0;    }    /**     * 处理头部内容,按照行读取     * @param bt     * @param rlen     * @return     */    public Map<String,String> excuteHttpHeader(byte[] bt,int rlen){        List<String> list = new ArrayList<String>();        String line = "";        int splitbyte = 0;        for (int i = 0;i < rlen;i++){            if (bt[i] == '\r' && bt[i+1] == '\n'){                line = new String(bt,splitbyte,i-splitbyte);                list.add(line);                splitbyte = i+2;            }            if (bt[i] == '\r' && bt[i+1] == '\n' && bt[i+2] == '\r' && bt[i+3] == '\n'){                break;            }        }        Map<String,String> map = excuteHttpParams(list);        return map;    }    public Map<String,String> excuteHttpParams(List<String> list){        if (list == null || list.size() <= 0){            return null;        }        map = new HashMap<String, String>();        String fLine = list.get(0);        if (fLine == null){            return null;        }        StringTokenizer stringTokenizer = new StringTokenizer(fLine);        //判断是否还有分隔符        if (!stringTokenizer.hasMoreTokens()){            throw new NoTokenException("there is no token anymore.");        }        map.put("method",stringTokenizer.nextToken());        if (!stringTokenizer.hasMoreTokens()){            throw new NoTokenException("there is no token anymore.");        }        String requestURL = stringTokenizer.nextToken();        map.put("url",requestURL);        if (!stringTokenizer.hasMoreTokens()){            throw new NoTokenException("there is no token anymore.");        }        String protocolVersion = stringTokenizer.nextToken();        map.put("protocolVersion",protocolVersion);        int indexN = requestURL.indexOf('?');        if (indexN > 0){            paramMap = decodeParams(requestURL.substring(indexN));            return paramMap;        }        Iterator<String> iterator = list.iterator();        while (iterator.hasNext()){            String ik = iterator.next();            int colon = ik.indexOf(':');            String key = null;            String value = null;            if (colon > 0){                key = ik.substring(0,colon);                value = ik.substring(colon+1);                map.put(key,value);            }        }        return map;    }    public Map<String,String> decodeParams(String paramsURL){        if (paramsURL == null){            return null;        }        Map<String,String> map = new HashMap<String, String>();        StringTokenizer stringTokenizer = new StringTokenizer(paramsURL,"&");        while (stringTokenizer.hasMoreTokens()){            String param = stringTokenizer.nextToken();            String key = null;            String value = null;            int eIde = param.indexOf('=');            if (eIde > 0){                key = Decode.decodeParam(param.substring(0,eIde));                value = Decode.decodeParam(param.substring(eIde+1));                map.put(key,value);            }        }        return map;    }    public void responseHeader(StringBuffer sb,String key,String value){        sb.append(key).append(":").append(value).append("\r\n");    }}