package romanow.abc.core.entity.server;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.nskgortrans.GorTransCare;
import romanow.abc.core.prepare.Distantion;
import romanow.abc.core.utils.GPSPoint;
import romanow.abc.core.utils.OwnDateTime;

import java.util.ArrayList;

public class TCare extends Entity {
    @Getter private int tType=0;                // Тип транспорта
    @Getter private String routeNumber="";      // Номер маршрута (напр. 13в)
    @Getter private int careRouteId=0;          // График - номер на маршруте
    @Getter private GPSPoint gps;               // Здесь же = серверное время
    @Getter private String direction;
    @Getter private OwnDateTime careTime;
    @Getter private double azimuth;
    @Getter private ArrayList<String> rasp;
    @Getter private String ramp="";
    @Getter private double speed;
    @Getter @Setter private Distantion routePoint=null;
    public String getRouteKey(){
        return tType+"_"+routeNumber;
        }
    public int getCareKey(){
        return careRouteId;
        }
    public TCare(boolean full,int tType0, String routeNumber0, GorTransCare src){
        tType = tType0;
        routeNumber = routeNumber0;
        careRouteId = src.getGraph();
        gps = new GPSPoint(src.getLat(),src.getLng(),true);
        speed = src.getSpeed();
        direction = src.getDirection();
        azimuth = src.getAzimuth();
        DateTime dateTime = DateTimeFormat.forPattern("dd.MM.yy HH:mm:ss").parseDateTime(src.getTime_nav());
        careTime = new OwnDateTime(dateTime.getMillis());
        rasp = new ArrayList<>();
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
        return "тип="+tType+" маршрут="+routeNumber+" скор="+speed+" место="+gps.toString()+" время="+careTime.toString()+" расписание:\n"+rasp.toString();
        }
}
