package romanow.abc.core.entity.server;

import lombok.Getter;

import lombok.Setter;
import romanow.abc.core.ErrorList;
import romanow.abc.core.constants.ConstValue;
import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.nskgortrans.GorTransCare;
import romanow.abc.core.entity.subjectarea.TRoute;
import romanow.abc.core.prepare.Distantion;
import romanow.abc.core.utils.GPSPoint;
import romanow.abc.core.utils.OwnDateTime;

import java.util.ArrayList;
import java.util.HashMap;

public class TCare extends Entity {
    @Getter private int tType=0;                                        // Тип транспорта
    @Getter private String routeNumber="";                              // Номер маршрута (напр. 13в)
    @Getter private int careRouteId=0;                                  // График - номер на маршруте
    @Getter private ArrayList<String> rasp = new ArrayList<>();         //
    @Getter private ArrayList<TCarePoint> careStory = new ArrayList<>();// История (здесь одна последняя точка)
    @Getter @Setter private TRoute route=null;
    //------------------------------------------------------------------------------
    public String getCareKey(){
        return tType+"_"+routeNumber+"_"+careRouteId;
        }
    public String getTitle(HashMap<Integer, ConstValue> typeMap){
        return typeMap.get(tType).title()+" "+routeNumber+" ("+careRouteId+") ";
        }
    public int getCareId(){
        return careRouteId;
        }
    public boolean noCareData(){ return careStory.size()==0; }
    public TCarePoint lastPoint(){
        return careStory.size()==0 ? new TCarePoint() : careStory.get(careStory.size()-1);
        }

    public TCare(boolean full,int tType0, String routeNumber0, GorTransCare src, TRoute route0){
        route = route0;
        tType = tType0;
        routeNumber = routeNumber0;
        careRouteId = src.getGraph();
        careStory.add(new TCarePoint(src));
        if (full){
            String ss = src.getRasp();
            while(true){
                int idx=ss.indexOf("|");
                if (idx==-1){
                    if (ss.length()!=0)
                        rasp.add(ss);
                    break;
                    }
                rasp.add(ss.substring(0,idx));
                ss = ss.substring(idx+1);
                }
            }
        }
    public String toString(){
        TCarePoint point = lastPoint();
        return "тип="+tType+" маршрут="+routeNumber+" "+lastPoint().toString()+" расписание:\n"+rasp.toString();
        }
    public String toString(HashMap<Integer, ConstValue> typeMap){
        return getTitle(typeMap)+" "+lastPoint().toString();
        }
    public String toStringFull(HashMap<Integer, ConstValue> typeMap){
        String ss = getTitle(typeMap);
        for(TCarePoint point : careStory)
            ss+="\n"+point.toString();
        return ss;
        }
    public void squeezy(int hours){             // Сжать историю до нужного интервала в часах
        int sz = careStory.size();
        if (sz<=1)
            return;
        OwnDateTime t1 = careStory.get(sz-1).getGps().geoTime();
        if (!t1.dateTimeValid())
            careStory.get(sz-1).getGps().geoTime();
        while(sz!=1){
            OwnDateTime t2 = careStory.get(0).getGps().geoTime();
            if (!t2.dateTimeValid())
                careStory.get(0).getGps().geoTime();
            if ((t1.timeInMS()-t2.timeInMS())/1000/60/60 > hours){
                careStory.remove(0);
                sz--;
                }
            else
                return;
            }
        }
    public int addCarePoint(int hour, TCarePoint point){
        if (careStory.size()==0){
            careStory.add(point);
            return 0;
            }
        else{
            TCarePoint last = careStory.get(careStory.size()-1);
            if (point.getCareTime().timeInMS()==last.getCareTime().timeInMS()){
                return 1;       // Это одна и та же точка от NSKGorTrans
                }
            careStory.add(point);
            }
        squeezy(hour);
        return 0;
        }
    public void setDistantion(Distantion distantion){
        lastPoint().setRoutePoint(distantion);
        }
    public GPSPoint getGps(){
        return lastPoint().getGps();
        }
    //-------------------------- Поиск в истории маршрута для точки пассажира ---------------------------
    public ErrorList searchInRoute(TPassengerPoint point, int MaxDistance, double maxDiff){
        point.setState(Values.PPStateOffBoard);
        ErrorList errors = new ErrorList();
        if (point.getGps().state()!=GPSPoint.GeoGPS){
            errors.addError("Нет точных GPS-координат");
            return errors;
            }
        Distantion distantion = route.createRoutePoint(getGps());   // Привязать пассажира к трассе маршрута
        if (!distantion.done){
            errors.addError("Точка не привязана к маршруту");
            return errors;
            }
        if (distantion.distToLine > MaxDistance){
            point.setState(Values.PPStateOver);
            errors.addError("Расстояние до маршрута "+(int)distantion.distToLine);
            return errors;
            }
        if (careStory.size()==0){
            errors.addError("Нет истории борта");
            return errors;
            }
        int idx;
        TCarePoint before=null,after=null,carePoint;
        for(idx=careStory.size()-1;idx>=0;idx--){              // Справа налево, пока время больше
            if (!careStory.get(idx).getRoutePoint().done || careStory.get(idx).getGps().geoTime().timeInMS() >= point.getGps().geoTime().timeInMS())
                continue;
            before = careStory.get(idx);
            break;
            }
        if (before==null){
            errors.addError("Нет истории борта с отметкой \'до\'");
            return errors;
            }
        for(idx=0;idx<careStory.size();idx++){              // Слева направо, пока время меньше
            if (!careStory.get(idx).getRoutePoint().done || careStory.get(idx).getGps().geoTime().timeInMS() <= point.getGps().geoTime().timeInMS())
                continue;
            after = careStory.get(idx);
            break;
            }
        if (after==null){
            point.setState(Values.PPStateContinue);
            errors.addError("Нет истории борта с отметкой \'после\'");
            return errors;
            }
        //----------------- Движение в обратном направлении по маршруту
        double wayBefore = before.getRoutePoint().getTotalLength();
        double wayAfter = after.getRoutePoint().getTotalLength();
        long tBefore = before.getGps().geoTime().timeInMS();
        long tAfter = after.getGps().geoTime().timeInMS();
        long tt = point.getGps().geoTime().timeInMS();
        if (wayBefore > wayAfter){
            errors.addInfo("Движение в обратном направлении ");
            double vv = wayAfter; wayAfter=wayBefore; wayBefore=vv;
            }
        double way = wayBefore + (wayAfter-wayAfter)*(tt-tBefore)/(tAfter-tBefore);     // Из пропорции
        if (way>maxDiff){
            point.setState(Values.PPStateOver);
            errors.addError("Расстояние до борта "+(int)way);
            return errors;
            }
        point.setState(Values.PPStateOnBoard);
        point.setCare(this);
        errors.addInfo("до маршрута "+(int)distantion.distToLine+" до борта "+(int)way);
        return errors;
        }
}
