package romanow.abc.core.entity.subjectarea;

import lombok.Getter;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;
import romanow.abc.core.utils.GPSPoint;

public class TSegPoint extends Entity {
    @Getter private EntityLink<romanow.abc.core.entity.subjectarea.TSegment> TSegment = new EntityLink<>();     // Обратная ссылка
    @Getter private GPSPoint gps = new GPSPoint();
    public TSegPoint(GPSPoint gps0){
        gps = gps0;
        }
    public TSegPoint(){
        super(0);
        }
    public boolean cmpExact(TSegPoint two){
        if (!gps.isValid() || !two.gps.isValid())
            return false;
        return gps.geox() == two.gps.geox() && gps.geoy() == two.gps.geoy();
    }
}
