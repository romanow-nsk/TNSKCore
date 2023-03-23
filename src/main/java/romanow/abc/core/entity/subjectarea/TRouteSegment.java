package romanow.abc.core.entity.subjectarea;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;

public class TRouteSegment extends Entity {
    @Getter
    EntityLink<TRoute> TRoute = new EntityLink<>();                             // Обратная ссылка
    @Getter EntityLink<TSegment> segment = new EntityLink<>(TSegment.class);    //
    @Getter @Setter EntityLink<TSegment> near1 = new EntityLink<>();            // Первое ближайшее совпадение
    @Getter @Setter EntityLink<TSegment> near2 = new EntityLink<>();            // Второе ближайшее совпадение
    @Getter @Setter private int mode1=0;                // Вариант совпадения
    @Getter @Setter private int mode2=0;                // Вариант совпадения
    @Getter @Setter private int points1=0;              // Кол-во совпадаюших точек в первом ближайшем
    @Getter @Setter private int points2=0;              // Кол-во совпадаюших точек во втором ближайшем
    public TRouteSegment(TSegment segment0){
        segment.setOidRef(segment0);
        }
    public TRouteSegment(){}

}
