package romanow.abc.core.entity.subjectarea;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.ErrorList;
import romanow.abc.core.constants.ConstValue;
import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityRefList;
import romanow.abc.core.entity.nskgortrans.GorTransRoute;
import romanow.abc.core.entity.server.TCare;
import romanow.abc.core.prepare.Distantion;
import romanow.abc.core.utils.GPSPoint;

import java.util.HashMap;

public class TRoute extends Entity {
    @Getter private EntityRefList<TRouteSegment> segments = new EntityRefList<>(TRouteSegment.class);   // Сегменты маршрута
    @Getter private EntityRefList<TRouteStop> stops = new EntityRefList<>(TRouteStop.class);            // Остановки маршрута
    @Getter @Setter private boolean dataValid=true;
    @Getter @Setter private int tType=0;                 // Тип транспорта
    @Getter @Setter private String routeNumber="";       // Номер маршрута (напр. 13в)
    @Getter @Setter private String routeName="";         // ?????? из импорта
    @Getter @Setter private String stopName1="";
    @Getter @Setter private String stopName2="";
    @Getter @Setter private boolean lastStopDiff1=false; // пара конечных в середине (расстояние !=0 )
    @Getter @Setter private boolean lastStopDiff2=false; // пара конечных в конце (расстояние !=0 )
    @Getter @Setter private int lastStopIdx=0;           // Индекс пары конечных в маршруте (первый из пары)
    @Getter transient EntityRefList<TCare> actualCares= new EntityRefList<>();
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
    @Override
    public String getTitle(){
        return Values.constMap().getGroupMapByValue("RouteType").get(tType).title()+" "+routeNumber;
        }
    public String getTitle(HashMap<Integer, ConstValue> map){
        return map.get(tType).title()+" "+routeNumber;
        }
    public EntityRefList<TSegment> createSegmentList(){     // Прямые ссылки на сегменты маршрута
        EntityRefList<TSegment> out = new EntityRefList<>();
        for(TRouteSegment routeSegment : segments)
            out.add(routeSegment.getSegment().getRef());
        return out;
        }
    public String getName(){                // Для EntityRefList
        return tType+"_"+routeNumber;
        }
    public synchronized void setActualCares(EntityRefList<TCare> actualCares0){
        actualCares = actualCares0;
        }
    //--------------------------------------- Привязка к сегменту маршрута без учета направления (вся СД в памяти)
    public void createRoutePoint(TCare care, ErrorList errors, HashMap<Integer, ConstValue> map){
        Distantion nearest = new Distantion();
        if (!care.getGps().gpsValid()){
            errors.addError("Нет gps борта "+care.getCareRouteId()+" маршрута "+getTitle(map));
            care.setRoutePoint(nearest);
            }
        if (segments.size()<=1){
            errors.addError("Нет трассы маршрута "+getTitle(map));
            care.setRoutePoint(nearest);
            }
        for(int i=0;i<segments.size();i++){
            TSegment segment = segments.get(i).getSegment().getRef();
            Distantion two = segment.findRoutePoint(care.getGps());
            two.setSegIdx(i);
            if (!nearest.done)
                nearest = two;
            else{
                if (two.done && two.distToLine < nearest.distToLine)
                    nearest = two;
                }
            }
        if (nearest.done){
            double totalLength=0;
            for(int i=0;i<nearest.getSegIdx();i++)
                totalLength += segments.get(i).getSegment().getRef().getTotalLength();
            nearest.setTotalLength(totalLength+nearest.distToPoint1);
            errors.addInfo("Борт "+care.getCareRouteId()+" маршрута "+getTitle(map)+" скор.="+care.getSpeed()+" "+nearest);
            }
        else{
            errors.addError("Борт "+care.getCareRouteId()+" маршрута "+getTitle(map)+" не привязан к трассе");
            }
        care.setRoutePoint(nearest);
        }
    //------------------------------------------------------------------------------------------------------------------
    public static void main(String ss[]){
        new TRoute();
        }
    }
