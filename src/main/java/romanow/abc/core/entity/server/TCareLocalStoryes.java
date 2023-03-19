package romanow.abc.core.entity.server;

import java.util.HashMap;

public class TCareLocalStoryes extends HashMap<String, HashMap<Integer,TCareStory>>{
    public void put(int hours,TCare care){
        HashMap<Integer,TCareStory> routeStory = get(care.getRouteKey());
        if (routeStory==null){
            routeStory = new HashMap<>();
            put(care.getRouteKey(),routeStory);
            }
        TCareStory careStory = routeStory.get(care.getCareKey());
        if (careStory==null){
            careStory = new TCareStory();
            routeStory.put(care.getCareKey(),careStory);
            }
        careStory.add(care);
        careStory.squeezy(hours);
    }
}
