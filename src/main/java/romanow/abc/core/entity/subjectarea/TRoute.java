package romanow.abc.core.entity.subjectarea;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityRefList;
import romanow.abc.core.entity.nskgortrans.GorTransRoute;

public class TRoute extends Entity {
    @Getter private EntityRefList<TRouteSegment> segments = new EntityRefList<>(TRouteSegment.class);   // Сегменты маршрута
    @Getter private EntityRefList<TRouteStop> stops = new EntityRefList<>(TRouteStop.class);            // Остановки маршрута
    @Getter @Setter private boolean dataValid=true;
    @Getter @Setter private int tType=0;                 // Тип транспорта
    @Getter @Setter private String routeNumber="";       // Номер маршрута (напр. 13в)
    @Getter @Setter private String routeName="";         //
    @Getter @Setter private String stopName1="";
    @Getter @Setter private String stopName2="";
    @Getter @Setter private boolean lastStopDiff1=false; // пара конечных в середине (расстояние !=0 )
    @Getter @Setter private boolean lastStopDiff2=false; // пара конечных в конце (расстояние !=0 )
    @Getter @Setter private int lastStopIdx=0;           // Индекс пары конечных в маршруте (первый из пары)
    public TRoute(){}
    public TRoute(int type,GorTransRoute src){
        tType = type;
        routeNumber = src.getMarsh();
        routeName = src.getName();
        stopName1 = src.getStopb();
        stopName2 = src.getStope();
        }
    public String getRouteKey(){
        return tType+"_"+routeNumber;
        }
    public static void main(String ss[]){
        new TRoute();
        }
    }
