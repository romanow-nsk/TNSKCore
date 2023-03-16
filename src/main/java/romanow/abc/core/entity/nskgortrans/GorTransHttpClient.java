/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.abc.core.entity.nskgortrans;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/**
http://maps.nskgortrans.ru/listmarsh.php?r&r=true - список марщрутов
http://maps.nskgortrans.ru/trasses.php?r=1-8-W - трасса маршрута
http://maps.nskgortrans.ru/markers.php?r=1-8-W - список бортов 1-группа (индекс+1) 8-маршрут
 */
public class GorTransHttpClient {
    final static String nskGorTransUrl="http://maps.nskgortrans.ru/";
    public String httpRequest(String req) throws Throwable {
        URLConnection connection = null;
        URL url = new URL(nskGorTransUrl+req);
        URLConnection connect=url.openConnection();
        InputStream in = connect.getInputStream();
        InputStreamReader isr = new InputStreamReader(in, "UTF-8");
        StringBuffer data = new StringBuffer();
        int c;
        while ((c = isr.read()) != -1)
            data.append((char) c);
        return data.toString();
        } 
    public GorTransCareList getCareList(int routeType,String route)throws Throwable{
        String ss=httpRequest("markers.php?r="+(routeType+1)+"-"+route+"-W");   // Можно без хвоста!!!! -"+route.getName()+"%257C");
        ObjectMapper mapper = new ObjectMapper(); 
        GorTransCareList zz=(GorTransCareList)mapper.readValue(ss, GorTransCareList.class);
        return zz;
        }
    public ArrayList<GorTransPoint> getTrasse(int routeType, String route)throws Throwable{
        String ss=httpRequest("trasses.php?r="+(routeType+1)+"-"+route+"-W");   // Можно без хвоста!!!! -"+route.getName()+"%257C");
        ObjectMapper mapper = new ObjectMapper(); 
        JsonNode node=mapper.readTree(ss);
        node=node.get("trasses").get(0).get("r").get(0).get("u");
        //node2=node.get("metaDataProperty").get( "GeocoderResponseMetaData").get("request");
        //node3=node.get("featureMember");
        int sz=node.size();
        ArrayList<GorTransPoint> data=new ArrayList<>();
        for(int i=0;i<sz;i++){
            data.add((GorTransPoint)mapper.readValue(node.get(i), GorTransPoint.class));
            }
        return data;
        }
    public ArrayList<GorTransRouteList> getRouteList() throws Throwable{
        String ss=httpRequest("listmarsh.php?r&r=true");
        ObjectMapper mapper = new ObjectMapper(); 
        // Получить главный узел - массив узлов
        // Из каждого элемента загрузить объект класса GorTransRouteList
        JsonNode node=mapper.readTree(ss.toString());
        int sz=node.size();
        ArrayList<GorTransRouteList> data=new ArrayList<>();
        String zz=null;
        for(int i=0;i<sz;i++){
            data.add((GorTransRouteList)mapper.readValue(node.get(i), GorTransRouteList.class));
            }
        return data;
        }
    public static void main(String argv[]){
        GorTransHttpClient xx=new GorTransHttpClient();
        try {
            int type=0;
            int idx=5;
            ArrayList<GorTransRouteList> data=xx.getRouteList();
            for(GorTransRouteList dd : data){
                System.out.println(dd);
                }
            System.out.println(data.get(type).getWays().get(idx));
            GorTransCareList cares=xx.getCareList(type,data.get(type).getWays().get(idx).getName());
            System.out.println(cares);
            ArrayList<GorTransPoint> pnt=xx.getTrasse(type,data.get(type).getWays().get(idx).getName());
            for(GorTransPoint point : pnt)
                System.out.println(point);
            } catch(Throwable ee){ System.out.println(ee.toString()); }
        }
}
