package romanow.abc.core.entity;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.utils.GPSPoint;

public class TStop extends TNamedEntity{
    @Getter GPSPoint gps = new GPSPoint();
    @Getter @Setter transient double diff=0;
    public TStop(TStop two){
        setName(two.getName());
        gps = two.getGps().copy();
        }
    public TStop(String name,GPSPoint two){
        setName(name);
        gps = two.copy();
        }
}
