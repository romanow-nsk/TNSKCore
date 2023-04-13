package romanow.abc.core.entity.server;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.entity.users.User;
import romanow.abc.core.utils.OwnDateTime;

import java.util.ArrayList;

public class TPassenger {
    @Getter @Setter User user = new User();
    @Getter private ArrayList<TPassengerPoint> passengerStory = new ArrayList<>();
    public TPassenger(){}
    public TPassenger(User user0){
        user = user0;
        }
    public void squeezy(int hours){             // Сжать историю до нужного интервала в часах
        int sz = passengerStory.size();
        if (sz<=1)
            return;
        OwnDateTime t1 = passengerStory.get(sz-1).getGps().geoTime();
        if (!t1.dateTimeValid())
            passengerStory.get(sz-1).getGps().geoTime();
        while(sz!=1){
            OwnDateTime t2 = passengerStory.get(0).getGps().geoTime();
            if (!t2.dateTimeValid())
                passengerStory.get(0).getGps().geoTime();
            if ((t1.timeInMS()-t2.timeInMS())/1000/60/60 > hours){
                passengerStory.remove(0);
                sz--;
                }
            else
                return;
            }
        }
    public void addPassengerPoint(int hour, TPassengerPoint point){
        passengerStory.add(point);
        squeezy(hour);
        }
}
