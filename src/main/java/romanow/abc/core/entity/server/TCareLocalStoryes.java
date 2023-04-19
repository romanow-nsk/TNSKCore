package romanow.abc.core.entity.server;

import java.util.HashMap;

public class TCareLocalStoryes extends HashMap<String, TCare>{
    public int put(int hours,TCare care){
        TCare careStory = get(care.getCareKey());
        if (careStory==null){
            put(care.getCareKey(),care);
            return 0;
            }
        else{
            return careStory.addCarePoint(hours,care.lastPoint());
            }
        }
    }
