package romanow.abc.core.entity.server;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import romanow.abc.core.entity.nskgortrans.GorTransCare;
import romanow.abc.core.prepare.Distantion;
import romanow.abc.core.utils.GPSPoint;
import romanow.abc.core.utils.OwnDateTime;

public class TCarePoint {
    @Getter
    private GPSPoint gps;                   // Здесь же = серверное время полцчения GPS
    @Getter private String direction;
    @Getter private double azimuth;
    @Getter private String ramp="";
    @Getter private double speed;
    @Getter @Setter private Distantion routePoint=new Distantion();
    public TCarePoint(){}
    public TCarePoint(GorTransCare src){
        gps = new GPSPoint(src.getLat(),src.getLng(),true);
        speed = src.getSpeed();
        direction = src.getDirection();
        azimuth = src.getAzimuth();
        ramp = src.getRamp();
        DateTime dateTime = DateTimeFormat.forPattern("dd.MM.yy HH:mm:ss").parseDateTime(src.getTime_nav());
        gps.setGpsTime(dateTime.getMillis());
        }
    public OwnDateTime getCareTime(){
        return gps.geoTime();
        }
    public String toString(){
        return ""+speed+" км/ч "+gps.toTimeString()+" "+gps.geoTime().timeFullToString()+" "+routePoint.toString();
        }
    }
