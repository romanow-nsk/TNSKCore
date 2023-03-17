package romanow.abc.core.entity;

import lombok.Getter;
import romanow.abc.core.utils.GPSPoint;

public class TRouteStop extends TNamedEntity{
    @Getter EntityLink<TRoute> TRoute = new EntityLink<>();             // Обратная ссылка
    @Getter EntityLink<TStop> stop = new EntityLink<>(TStop.class);
    public TRouteStop(TStop stop0){
        stop.setOidRef(stop0);
        }
}
