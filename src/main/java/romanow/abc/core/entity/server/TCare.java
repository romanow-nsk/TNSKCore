package romanow.abc.core.entity.server;

import com.sun.scenario.effect.impl.prism.ps.PPSBlend_ADDPeer;
import lombok.Getter;

import lombok.Setter;
import romanow.abc.core.ErrorList;
import romanow.abc.core.constants.ConstValue;
import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.nskgortrans.GorTransCare;
import romanow.abc.core.entity.subjectarea.TRoute;
import romanow.abc.core.prepare.Distantion;
import romanow.abc.core.utils.GPSPoint;
import romanow.abc.core.utils.OwnDateTime;

import java.util.ArrayList;
import java.util.HashMap;

public class TCare extends Entity {
    @Getter private int tType=0;                                        // Тип транспорта
    @Getter private String routeNumber="";                              // Номер маршрута (напр. 13в)
    @Getter private int careRouteId=0;                                  // График - номер на маршруте
    @Getter private ArrayList<String> rasp = new ArrayList<>();         //
    @Getter private ArrayList<TCarePoint> careStory = new ArrayList<>();// История (здесь одна последняя точка)
    @Getter @Setter private TRoute route=null;
    //------------------------------------------------------------------------------
    public String getCareKey(){
        return tType+"_"+routeNumber+"_"+careRouteId;
        }
    public String getTitle(HashMap<Integer, ConstValue> typeMap){
        return typeMap.get(tType).title()+" "+routeNumber+" ("+careRouteId+") ";
        }
    public int getCareId(){
        return careRouteId;
        }
    public boolean noCareData(){ return careStory.size()==0; }
    public TCarePoint lastPoint(){
        return careStory.size()==0 ? new TCarePoint() : careStory.get(careStory.size()-1);
        }

    public TCare(){}
    public TCare(boolean full,int tType0, String routeNumber0, GorTransCare src, TRoute route0){
        route = route0;
        tType = tType0;
        routeNumber = routeNumber0;
        careRouteId = src.getGraph();
        careStory.add(new TCarePoint(src));
        if (full){
            String ss = src.getRasp();
            while(true){
                int idx=ss.indexOf("|");
                if (idx==-1){
                    if (ss.length()!=0)
                        rasp.add(ss);
                    break;
                    }
                rasp.add(ss.substring(0,idx));
                ss = ss.substring(idx+1);
                }
            }
        }
    public String toString(){
        TCarePoint point = lastPoint();
        return "тип="+tType+" маршрут="+routeNumber+" "+lastPoint().toString()+" расписание:\n"+rasp.toString();
        }
    public String toString(HashMap<Integer, ConstValue> typeMap){
        return getTitle(typeMap)+" "+lastPoint().toString();
        }
    public String toStringFull(HashMap<Integer, ConstValue> typeMap){
        String ss = getTitle(typeMap);
        for(TCarePoint point : careStory)
            ss+="\n"+point.toString();
        return ss;
        }
    public void squeezy(int hours){             // Сжать историю до нужного интервала в часах
        int sz = careStory.size();
        if (sz<=1)
            return;
        OwnDateTime t1 = careStory.get(sz-1).getGps().geoTime();
        if (!t1.dateTimeValid())
            careStory.get(sz-1).getGps().geoTime();
        while(sz!=1){
            OwnDateTime t2 = careStory.get(0).getGps().geoTime();
            if (!t2.dateTimeValid())
                careStory.get(0).getGps().geoTime();
            if ((t1.timeInMS()-t2.timeInMS())/1000/60/60 > hours){
                careStory.remove(0);
                sz--;
                }
            else
                return;
            }
        }
    public int addCarePoint(int hour, TCarePoint point){
        if (careStory.size()==0){
            careStory.add(point);
            return 0;
            }
        else{
            TCarePoint last = careStory.get(careStory.size()-1);
            if (point.getCareTime().timeInMS()==last.getCareTime().timeInMS()){
                return 1;       // Это одна и та же точка от NSKGorTrans
                }
            careStory.add(point);
            }
        squeezy(hour);
        return 0;
        }
    public void setDistantion(Distantion distantion){
        lastPoint().setDistantion(distantion);
        }
    public GPSPoint getGps(){
        return lastPoint().getGps();
        }
    //-------------------------- Поиск в истории маршрута для точки пассажира ---------------------------
    // 1. Каждую точку пассажира привязываем к маршруту - distantion
    // 2. Находим две точки борта на маршруте с ближайшим временем меньше и больше - before, after
    // 3. TotalLength - расстояние от начала маршрута
    // 4. Строим пропорцию расстояний и времен, определяем расстояние борта от начала на момент времени пассажира (way)
    // 5. Определяем разницу расстояний way и точки пассажира
    public ErrorList searchInRoute(TPassengerPoint point, int MaxDistance, double maxDiff){
        point.setState(Values.PPStateOffBoard);
        ErrorList errors = new ErrorList();
        if (point.getGps().state()!=GPSPoint.GeoGPS){
            errors.addError("Нет точных GPS-координат");
            return errors;
            }
        Distantion distantion = route.createRoutePoint(getGps());   // Привязать пассажира к трассе маршрута
        if (!distantion.done){
            errors.addError("Точка не привязана к маршруту");
            return errors;
            }
        if (distantion.distToLine > MaxDistance){
            point.setState(Values.PPStateOver);
            errors.addError("Расстояние до маршрута "+(int)distantion.distToLine);
            return errors;
            }
        if (careStory.size()==0){
            errors.addError("Нет истории борта");
            return errors;
            }
        int idx;
        TCarePoint before=null,after=null,carePoint;
        for(idx=careStory.size()-1;idx>=0;idx--){              // Справа налево, пока время больше
            if (!careStory.get(idx).getDistantion().done || careStory.get(idx).getGps().geoTime().timeInMS() >= point.getGps().geoTime().timeInMS())
                continue;
            before = careStory.get(idx);
            break;
            }
        if (before==null){
            errors.addError("Нет истории борта с отметкой \'до\'");
            return errors;
            }
        for(idx=0;idx<careStory.size();idx++){              // Слева направо, пока время меньше
            if (!careStory.get(idx).getDistantion().done || careStory.get(idx).getGps().geoTime().timeInMS() <= point.getGps().geoTime().timeInMS())
                continue;
            after = careStory.get(idx);
            break;
            }
        if (after==null){
            point.setState(Values.PPStateContinue);
            errors.addError("Нет истории борта с отметкой \'после\'");
            return errors;
            }
        //----------------- Движение в обратном направлении по маршруту
        double wayBefore = before.getDistantion().getTotalLength();
        double wayAfter = after.getDistantion().getTotalLength();
        double wayPoint = distantion.getTotalLength();
        long tBefore = before.getGps().geoTime().timeInMS();
        long tAfter = after.getGps().geoTime().timeInMS();
        long tt = point.getGps().geoTime().timeInMS();
        if (wayBefore > wayAfter){
            errors.addInfo("Движение в обратном направлении ");
            double vv = wayAfter; wayAfter=wayBefore; wayBefore=vv;
            }
        double way = wayBefore + (wayAfter-wayAfter)*(tt-tBefore)/(tAfter-tBefore);     // Из пропорции
        double df = Math.abs(way - wayPoint);
        if (df>maxDiff){
            point.setState(Values.PPStateOver);
            errors.addError("Расстояние до борта "+(int)way);
            return errors;
            }
        point.setState(Values.PPStateOnBoard);
        errors.addInfo("до маршрута "+(int)distantion.distToLine+" до борта "+(int)way);
        return errors;
        }
    //-------------------------- Поиск в истории маршрута для точки пассажира ---------------------------
    // 1. Для каждой точки борта на маршруте ищем ближайшую по времени точку пассажира (точки пассажира - 10 сек, частые)
    // 2. Привязываем к маршруту, определяем разницу путем и времен, корректируем путь
    // 3. Отмечаем найденные точки по критерию расстояния, времени (привязана, не привязана)
    // 4. Между привязанными точками выполняется вторичная привязка
    // int routeDistance - отклонение от маршрута (длина перпендикуляра)
    // double carePassDistance - расстояние между бротом и пассажиром ПО ДЛИНЕ МАРШРУТА
    // double speedDiff - разность скоростей борта и пассажира
    // double speedMax - максимальная скорость пешком
    // Фиксируется только ДВИЖЕНИЕ СОВМЕСТНО С БОРТОМ
    public ErrorList searchInRoute2(TPassenger passenger, int routeDistance, double carePassDistance, double speedDiff, double speedMax) {
        ErrorList errors = new ErrorList();
        if (careStory.size()==0){
            errors.addError("Нет истории борта");
            return errors;
            }
        if (passenger.getPassengerStory().size()==0){
            errors.addError("Нет истории пассажира");
            return errors;
            }
        for (TPassengerPoint point : passenger.getPassengerStory()){
            point.setOnBoard(false);
            }
        ArrayList<Integer> idxList = new ArrayList<>();
        for (TCarePoint carePoint : careStory){
            boolean careOnRoute = !carePoint.getDistantion().done && carePoint.getDistantion().distToLine < routeDistance;
            carePoint.setOnRoute(careOnRoute);
            if (!careOnRoute)                                                           // Борт не привязан -  не искать пассажира
                continue;
            int idx = passenger.nearestPointIdx(carePoint.getCareTime());               // Ближайшая точка пассажира
            idxList.add(idx);                                                           // Запомнить индексы
            TPassengerPoint point = passenger.getPassengerStory().get(idx);
            Distantion distantion = route.createRoutePoint(point.getGps());             // Привязка к маршруту
            point.setDistantion(distantion);                                            // Данные привязки
            boolean passOnRoute = !point.getDistantion().done && point.getDistantion().distToLine < routeDistance;
            double gpsDiff = carePoint.getGps().diff(point.getGps());
            double routeDiff = carePoint.getDistantion().getTotalLength()-point.getDistantion().getTotalLength();
            double timeDiff = (carePoint.getGps().geoTime().timeInMS()-point.getGps().geoTime().timeInMS())/1000.;      // Разница в секундах
            double speed = carePoint.getSpeed()/3.6;                                    // в м/сек
            routeDiff -= speed * timeDiff;                                              // Коррекция по разнице времени и скорости
            passOnRoute = passOnRoute && Math.abs(routeDiff) < carePassDistance;        // Критерий близости по пути маршрута
                                                                                        // Критерий близости по разности скоростей
            passOnRoute = passOnRoute && Math.abs(carePoint.getSpeed()-point.getSpeed()) < speedDiff;
            passOnRoute = passOnRoute && point.getSpeed() < speedMax;                   // Критерий по скорости пешехода
                    point.setOnBoard(passOnRoute);
            }
        if (idxList.size()==0){
            errors.addError("Нет пересечений с бортом");
            return errors;
            }
        int lim = idxList.get(0);
        TPassengerPoint point = passenger.getPassengerStory().get(lim);
        for(int i=0;i<lim;i++){             // Промежуточные до первого совпадения
            TPassengerPoint point2 = passenger.getPassengerStory().get(i);
            point2.setOnBoard(false);
            if (point.isOnBoard()){
                Distantion distantion = route.createRoutePoint(point2.getGps());
                if (distantion.done && distantion.distToLine < routeDistance && point2.getSpeed() < speedMax)
                    point2.setOnBoard(true);
                }
            }
        lim = idxList.get(idxList.size()-1);
        point = passenger.getPassengerStory().get(lim);
        for(int i=lim+1;i<passenger.getPassengerStory().size();i++){      // Промежуточные после последнего совпадения
            TPassengerPoint point2 = passenger.getPassengerStory().get(i);
            point2.setOnBoard(false);
            if (point.isOnBoard()){
                Distantion distantion = route.createRoutePoint(point2.getGps());
                if (distantion.done && distantion.distToLine < routeDistance && point2.getSpeed() < speedMax)
                    point2.setOnBoard(true);
                }
            }
        for(int idx=0; idx<idxList.size()-1;idx++){
            point = passenger.getPassengerStory().get(idxList.get(idx));
            TPassengerPoint point1 = passenger.getPassengerStory().get(idxList.get(idx+1));
            for(int jj=idxList.get(idx)+1; jj<idxList.get(idx+1);jj++){ // Промежуточные между парой совпадений
                TPassengerPoint point2 = passenger.getPassengerStory().get(jj);
                point2.setOnBoard(false);
                if (point.isOnBoard() || point1.isOnBoard()){               // Хотя бы один на маршруте
                    Distantion distantion = route.createRoutePoint(point2.getGps());
                    if (distantion.done && distantion.distToLine < routeDistance && point2.getSpeed() < speedMax)
                        point2.setOnBoard(true);
                    }
                }
            }
        return  errors;
        }
    }

