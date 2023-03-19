package romanow.abc.core.entity.server;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.entity.EntityRefList;
import romanow.abc.core.entity.subjectarea.TRoute;

import java.util.HashMap;

public class TServerData {
    @Getter @Setter private boolean careScanOn=false;
    @Getter TCareLocalStoryes careStoryes = new TCareLocalStoryes();  // Истории бортов маршрутов
    @Getter EntityRefList<TRoute> routes = new EntityRefList<>();
    public void clear(){
        careScanOn=false;
        careStoryes = new TCareLocalStoryes();
        }
    }
