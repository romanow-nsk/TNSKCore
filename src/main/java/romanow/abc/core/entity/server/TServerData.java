package romanow.abc.core.entity.server;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.entity.EntityRefList;
import romanow.abc.core.entity.subjectarea.TRoute;
import romanow.abc.core.entity.subjectarea.TSegment;
import romanow.abc.core.entity.subjectarea.TStop;
import romanow.abc.core.utils.GPSPoint;

import java.util.HashMap;

public class TServerData {
    @Getter @Setter private boolean careScanOn=false;
    @Getter TCareLocalStoryes careStoryes = new TCareLocalStoryes();    // Истории бортов маршрутов
    @Getter EntityRefList<TRoute> routes = new EntityRefList<>();       // Маршруты, привязаннные к сегментам
    @Getter EntityRefList<TSegment> segments = new EntityRefList<>();   // Сегменты, привязанные к точкам
    @Getter EntityRefList<TStop> stops = new EntityRefList<>();         // Остановки
    @Getter EntityRefList<TCare> actualCares = new EntityRefList<>();   // Последние данные о бортах
    public void clear(){
        careScanOn=false;
        careStoryes = new TCareLocalStoryes();
        }
    public synchronized void setActualCares(EntityRefList<TCare> actualCares0){
        actualCares = actualCares0;
        }
    public EntityRefList<TCare> getNearestCares(GPSPoint point,double diff){
        EntityRefList<TCare> cares = new EntityRefList<>();
        if (!point.gpsValid())
            return cares;
        for(TCare care : actualCares){
            if (!care.lastPoint().getGps().gpsValid())
                continue;
            if (care.lastPoint().getGps().diff(point)<diff)
                cares.add(care);
            }
        return cares;
        }
    }
