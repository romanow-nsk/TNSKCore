/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.abc.core.entity.nskgortrans;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.server.TCare;
import romanow.abc.core.utils.Pair;

/**
http://maps.nskgortrans.ru/listmarsh.php?r&r=true - список марщрутов
http://maps.nskgortrans.ru/trasses.php?r=1-8-W - трасса маршрута
http://maps.nskgortrans.ru/markers.php?r=1-8-W - список бортов 1-группа (индекс+1) 8-маршрут
 */
public class GorTransHttpClient {
    public String httpRequest(String req) throws IOException {
        URLConnection connection = null;
        URL url = new URL("http://"+Values.NskGorTransURL+"/"+req);
        URLConnection connect=url.openConnection();
        InputStream in = connect.getInputStream();
        InputStreamReader isr = new InputStreamReader(in, "UTF-8");
        StringBuffer data = new StringBuffer();
        int c;
        while ((c = isr.read()) != -1)
            data.append((char) c);
        return data.toString();
        } 
    public Pair<String,GorTransCareList> getCareList(int routeType, String route){
        try {
            String ss = httpRequest("markers.php?r=" + (routeType + 1) + "-" + route + "-W");   // Можно без хвоста!!!! -"+route.getName()+"%257C");
            ObjectMapper mapper = new ObjectMapper();
            GorTransCareList zz = (GorTransCareList) mapper.readValue(ss, GorTransCareList.class);
                return new Pair<>(null,zz);
            }catch (IOException ee){
                return new Pair<>("Ошибка NskGorTrans: "+ee.toString(),null);
                }
        }
    public Pair<String,ArrayList<GorTransPoint>> getTrasse(int routeType, String route){
        try {
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
            return new Pair<>(null,data);
            }catch (IOException ee){
                return new Pair<>("Ошибка NskGorTrans: "+ee.toString(),null);
                }
        }
    public Pair<String,ArrayList<GorTransRouteList>> getRouteList(){
        try {
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
            return new Pair<>(null,data);
            }catch (IOException ee){
                return new Pair<>("Ошибка NskGorTrans: "+ee.toString(),null);
                }
        }
    public static void main(String argv[]){
        GorTransHttpClient xx=new GorTransHttpClient();
        int type=0;
        int idx=5;
        Pair<String,ArrayList<GorTransRouteList>> data=xx.getRouteList();
        if (data.o1!=null)
            System.out.println(data.o1);
        else{
            for(GorTransRouteList dd : data.o2){
                System.out.println(dd);
                }
            System.out.println(data.o2.get(type).getWays().get(idx));
            }
        String routeName = data.o2.get(type).getWays().get(idx).getName();
        Pair<String,ArrayList<GorTransPoint>> pnt=xx.getTrasse(type,routeName);
        if (pnt.o1!=null)
            System.out.println(pnt.o1);
        else{
            for(GorTransPoint point : pnt.o2)
                System.out.println(point);
            }
        long tt = System.currentTimeMillis();
        if (data.o1!=null)
            return;
        int cnt=0;
        for(GorTransRouteList dd : data.o2){
            type = dd.type;
            for (GorTransRoute route : dd.getWays()){
                routeName = route.getName();
                Pair<String,GorTransCareList> cares=xx.getCareList(type,routeName);
                if (cares.o1!=null)
                    System.out.println(cares.o1);
                else{
                    //System.out.println(cares.o2);
                    cnt+=cares.o2.getMarkers().size();
                    for (GorTransCare care : cares.o2.getMarkers()){
                        TCare tCare = new TCare(true,type,routeName,care,null);
                        System.out.println(tCare);
                        }
                    }
                }
            }
        System.out.println("Бортов на маршрутах: "+cnt+" опрос="+(System.currentTimeMillis()-tt)/1000+" сек");
        }
}
