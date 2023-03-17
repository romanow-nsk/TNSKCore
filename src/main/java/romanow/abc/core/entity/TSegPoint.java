package romanow.abc.core.entity;

import lombok.Getter;
import romanow.abc.core.utils.GPSPoint;

public class TSegPoint extends Entity{
    @Getter EntityLink<TSegment> TSegment = new EntityLink<>();     // Обратная ссылка
    @Getter GPSPoint gps = new GPSPoint();
    public TSegPoint(GPSPoint gps0){
        gps = gps0;
        }
    public boolean cmpExact(TSegPoint two){
        if (!gps.isValid() || !two.gps.isValid())
            return false;
        return gps.geox() == two.gps.geox() && gps.geoy() == two.gps.geoy();
    }
}
