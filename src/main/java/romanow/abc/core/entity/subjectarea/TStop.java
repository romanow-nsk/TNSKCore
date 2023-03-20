package romanow.abc.core.entity.subjectarea;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.utils.GPSPoint;

public class TStop extends Entity {
    @Getter @Setter private String name="";
    @Getter GPSPoint gps = new GPSPoint();
    public TStop(){}
    public TStop(TStop two){
        setName(two.getName());
        gps = two.getGps().copy();
        }
    public TStop(String name,GPSPoint two){
        setName(name);
        gps = two.copy();
        }
}
