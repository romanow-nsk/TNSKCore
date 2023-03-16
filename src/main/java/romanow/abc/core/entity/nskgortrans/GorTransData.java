/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.abc.core.entity.nskgortrans;

import lombok.Getter;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class GorTransData {
    @Getter private ArrayList<GorTransRouteList> routes=new ArrayList<>();
    public boolean valid(){ return routes!=null; }
    GorTransData(){ routes=null; }
    boolean init(){
        try {
            GorTransHttpClient xx=new GorTransHttpClient();
            routes=xx.getRouteList();
            } catch(Throwable ee){
                return false;
                }
        return true;
        }
    GorTransRoute getRoute(int routeType,String routeName){
        if (!valid()) return null;
        for(GorTransRouteList routeList : routes){
            if (routeList.type!=routeType) continue;
            for (int j=0;j<routeList.getWays().size();j++)
                if (routeName.equals(routeList.getWays().get(j).getName()))
                    return routeList.getWays().get(j);
            }
        return null;
        }
    GorTransCareList getCareList(int routeType,String routeName){
        GorTransCareList out=new  GorTransCareList();
        if (!valid()) return out;
        GorTransRoute route=getRoute(routeType,routeName);
        if (route==null) return out;
        try {
            GorTransHttpClient xx=new GorTransHttpClient();
            out=xx.getCareList(routeType,route.getMarsh());
            } catch(Throwable ee){
                return out;
                }
        return out;
        }
    ArrayList<GorTransPoint> getTrasse(int routeType, String routeName){
        ArrayList<GorTransPoint> out=new ArrayList<>();
        if (!valid()) return out;
        GorTransRoute route=getRoute(routeType,routeName);
        if (route==null) return out;
        try {
            GorTransHttpClient xx=new GorTransHttpClient();
            out=xx.getTrasse(routeType,route.getMarsh());
            } catch(Throwable ee){ routes=null; return out; }
        return out;
        }
   public static void main(String argv[]){
       GorTransData xx=new GorTransData();
       if (!xx.init()) return;
       int total=0;
       for(GorTransRouteList routeList : xx.routes){
            total+=routeList.getWays().size();
            for(int j=0;j<routeList.getWays().size();j++){
                //System.out.println(xx.routes[i].ways[j]);
                }
            }
       System.out.println("Всего маршрутов: "+total);
       //GorTransPoint[] zz=xx.getTrasse(0, "8");
       //for(int i=0;i<zz.length;i++)
       //     System.out.println(zz[i]);
       total=0;
       for(GorTransRouteList routeList : xx.routes){
           for(int j=0;j<routeList.getWays().size();j++){
                GorTransCareList cc=xx.getCareList(routeList.type,routeList.getWays().get(j).getMarsh());
                total += cc.getMarkers().size();
                }
          }
       System.out.println("Всего бортов: "+total);
       }

}
