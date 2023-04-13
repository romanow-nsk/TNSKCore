package romanow.abc.core.entity.server;

import java.util.HashMap;

public class TCareLocalStoryes extends HashMap<String, TCare>{
    public int put(int hours,TCare care){
        TCare routeStory = get(care.getCareKey());
        if (routeStory==null){
            put(care.getCareKey(),care);
            return 0;
            }
        else{
            return routeStory.addCarePoint(hours,care.lastPoint());
            }
        }
    }
