package romanow.abc.core.entity.subjectarea;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;

public class TRouteStop extends Entity {
    @Getter @Setter private String name="";
    @Getter
    EntityLink<romanow.abc.core.entity.subjectarea.TRoute> TRoute = new EntityLink<>();             // Обратная ссылка
    @Getter EntityLink<TStop> stop = new EntityLink<>(TStop.class);
    public TRouteStop(TStop stop0){
        stop.setOidRef(stop0);
        }
    public TRouteStop(){}
}
