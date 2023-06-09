package romanow.abc.core.entity.server;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.nskgortrans.GorTransCare;
import romanow.abc.core.prepare.Distantion;
import romanow.abc.core.utils.GPSPoint;
import romanow.abc.core.utils.OwnDateTime;

public class TPassengerPoint {
    @Getter private GPSPoint gps;                           // Здесь же = серверное время
    @Getter private Float azimuth=null;
    @Getter private Float speed=null;
    @Getter @Setter private int state = Values.PPStateNone; // Доп. состояние
    @Getter @Setter boolean onBoard=false;                  // Базовое состояние
    @Getter @Setter private Distantion distantion=null;     // Данные привязки к маршруту
    public TPassengerPoint(){}
    public boolean hasSpeed(){return speed!=null; }
    public boolean hasAzimuth(){return azimuth!=null; }
    public TPassengerPoint(GPSPoint gps0, Float speed0, Float azimuth0){
        gps = gps0;
        speed = speed0;
        azimuth = azimuth0;
        }
    public String toString(){
        return ""+(speed==null ? "" : (speed+" км/ч "))+gps.toTimeString();
        }
    }
