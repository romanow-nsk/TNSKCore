package romanow.abc.core.entity.server;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class TServerData {
    @Getter @Setter private boolean careScanOn=false;
    @Getter TCareLocalStoryes careStoryes = new TCareLocalStoryes();  // Истории бортов маршрутов
    public void clear(){
        careScanOn=false;
        careStoryes = new TCareLocalStoryes();
        }
    }
