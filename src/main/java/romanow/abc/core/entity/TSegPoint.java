package romanow.abc.core.entity;

import lombok.Getter;
import romanow.abc.core.utils.GPSPoint;

public class TSegPoint extends Entity{
    @Getter EntityLink<TSegment> TSegment = new EntityLink<>();     // Обратная ссылка
    @Getter GPSPoint gps = new GPSPoint();
}
