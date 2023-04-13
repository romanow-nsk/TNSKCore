package romanow.abc.core.entity.server;

import java.util.HashMap;

public class TCareLocalStoryes extends HashMap<String, TCare>{
    public void put(int hours,TCare care){
        TCare routeStory = get(care.getRouteKey());
        if (routeStory==null){
            put(care.getRouteKey(),care);
            }
        else{
            routeStory.addCarePoint(hours,care.lastPoint());
            }
        }
    }
