package romanow.abc.core.entity.server;

import lombok.Getter;

import romanow.abc.core.constants.ConstValue;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.nskgortrans.GorTransCare;
import romanow.abc.core.prepare.Distantion;
import romanow.abc.core.utils.GPSPoint;
import romanow.abc.core.utils.OwnDateTime;

import java.util.ArrayList;
import java.util.HashMap;

public class TCare extends Entity {
    @Getter private int tType=0;                // Тип транспорта
    @Getter private String routeNumber="";      // Номер маршрута (напр. 13в)
    @Getter private int careRouteId=0;          // График - номер на маршруте
    @Getter private ArrayList<String> rasp = new ArrayList<>();             //
    @Getter private ArrayList<TCarePoint> careStory = new ArrayList<>();    // История
    //------------------------------------------------------------------------------
    public String getCareKey(){
        return tType+"_"+routeNumber+"_"+careRouteId;
        }
    public String getTitle(HashMap<Integer, ConstValue> typeMap){
        return typeMap.get(tType).title()+" "+routeNumber+", борт "+careRouteId;
        }
    public int getCareId(){
        return careRouteId;
        }
    public boolean noCareData(){ return careStory.size()==0; }
    public TCarePoint lastPoint(){
        return careStory.size()==0 ? new TCarePoint() : careStory.get(careStory.size()-1);
        }

    public TCare(boolean full,int tType0, String routeNumber0, GorTransCare src){
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
                return 1;
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
}
