package romanow.abc.core.entity.server;

import romanow.abc.core.utils.OwnDateTime;

import java.util.ArrayList;

public class TCareStory extends ArrayList<TCare>{
    public void squeezy(int hours){             // Сжать историю до нужного интервала в часах
        if (size()<=1)
            return;
        OwnDateTime t1 = get(size()-1).getCareTime();
        if (!t1.dateTimeValid())
            get(size()-1).getGps().geoTime();
        while(size()!=1){
            OwnDateTime t2 = get(0).getCareTime();
            if (!t2.dateTimeValid())
                get(0).getGps().geoTime();
            if ((t1.timeInMS()-t2.timeInMS())/1000/60/60 > hours)
                remove(0);
            else
                return;
            }
    }
}
