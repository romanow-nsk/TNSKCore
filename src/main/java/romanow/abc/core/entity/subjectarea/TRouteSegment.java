package romanow.abc.core.entity.subjectarea;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;

public class TRouteSegment extends Entity {
    @Getter
    EntityLink<romanow.abc.core.entity.subjectarea.TRoute> TRoute = new EntityLink<>();                     // Обратная ссылка
    @Getter EntityLink<TSegment> segment = new EntityLink<>(TSegment.class);    //
    @Getter @Setter transient private TSegment near1=null;
    @Getter @Setter transient private TSegment near2=null;
    @Getter @Setter transient private int idNear1=0;
    @Getter @Setter transient private int idNear2=0;
    @Getter @Setter transient private int mode1=0;                // Вариант совпадения
    @Getter @Setter transient private int mode2=0;                // Вариант совпадения
    @Getter @Setter transient private int points1=0;              // Кол-во совпадаюших точек
    @Getter @Setter transient private int points2=0;
    public TRouteSegment(TSegment segment0){
        segment.setOidRef(segment0);
        }
    public TRouteSegment(){}

}
