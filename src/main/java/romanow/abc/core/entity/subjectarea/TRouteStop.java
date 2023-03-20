package romanow.abc.core.entity.subjectarea;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;

public class TRouteStop extends Entity {
    @Getter @Setter private String name="";
    @Getter EntityLink<TRoute> TRoute = new EntityLink<>();             // Обратная ссылка
    @Getter EntityLink<TStop> stop = new EntityLink<>(TStop.class);
    @Getter @Setter double diff=0;
    public TRouteStop(TStop stop0){
        stop.setOidRef(stop0);
        }
    public TRouteStop(TStop stop0,double diff0){
        stop.setOidRef(stop0);
        diff = diff0;
        }
    public TRouteStop(){}
}
