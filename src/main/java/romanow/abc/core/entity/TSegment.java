package romanow.abc.core.entity;

import lombok.Getter;
import romanow.abc.core.entity.Entity;

public class TSegment extends Entity {
    @Getter EntityRefList<TSegPoint> points = new EntityRefList<>(TSegPoint.class);     // Точки сегмента
}
