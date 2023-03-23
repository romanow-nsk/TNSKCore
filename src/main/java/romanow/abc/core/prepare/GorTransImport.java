package romanow.abc.core.prepare;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.ErrorList;
import romanow.abc.core.constants.ConstValue;
import romanow.abc.core.constants.Values;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.nskgortrans.*;
import romanow.abc.core.entity.subjectarea.*;
import romanow.abc.core.utils.GPSPoint;
import romanow.abc.core.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author romanow
 */
public class GorTransImport {
    @Getter private TRouteList routes = new TRouteList();
    @Getter private TStopList stops = new TStopList();
    @Getter private TSegmentList segments = new TSegmentList();
    private int nears=0;
    private int pairs=0;
    private int cntMode[]=new int[6];
    private HashMap<Integer,ConstValue> nearMap;
    HashMap<Integer, ConstValue> map;
    @Setter private int traceLevel = ValuesBase.TraceModeNone;
    //public void importData(DBProfile profile, ProxyParams proxy,ServerLog log) throws Throwable{
    //    importData(profile,proxy,true,-1,log);
    //    }
    //------------- "Анонимные функции" --------------------------------------
    FunCmp<TStop> cmp1 = new FunCmp<TStop>(){               // Совпадение точек
        @Override
        public int cmp(TStop v1, TStop v2) {
            return v1.getGps().diff(v2.getGps());
            }
        };
    FunLike<TSegment> like1 = new FunLike<TSegment>(){      // Прямое совпадение сегментов
        @Override
        public boolean like(TSegment v1, TSegment v2){
            return v1.cmpExactFore(v2);
            }
        };
    FunLike<TSegment> like2 = new FunLike<TSegment>(){      // Обратное совпадение сегментов
        @Override
        public boolean like(TSegment v1, TSegment v2){
            return v1.cmpExactBack(v2);
            }
        };
    FunCmp<TSegment> cmp2 = new FunCmp<TSegment>(){         // Прямое совпадение по длине мин.сегмента от начала
        @Override
        public int cmp(TSegment v1, TSegment v2) {
            return v1.cmpNearFore1(v2);
            }
        };
    FunCmp<TSegment> cmp3 = new FunCmp<TSegment>(){         // Прямое совпадение по длине мин.сегмента  от конца
        @Override
        public int cmp(TSegment v1, TSegment v2) {
            return v1.cmpNearFore2(v2);
            }
        };
    FunCmp<TSegment> cmp4 = new FunCmp<TSegment>(){         // Инверсное совпадение по длине мин.сегмента  от начала
        @Override
        public int cmp(TSegment v1, TSegment v2) {
            return v1.cmpNearBack1(v2);
            }
        };
    FunCmp<TSegment> cmp5 = new FunCmp<TSegment>(){         // Инверсное совпадение по длине мин.сегмента  от конца
        @Override
        public int cmp(TSegment v1, TSegment v2) {
            return v1.cmpNearBack2(v2);
            }
        };
    //----------------- Вложенная функция поиска оригинального сегмента
    private void addOriginalSegment(ErrorList log, TRoute vv, TSegment cline){       // cline - новый сегмент
        TSegment like = segments.firstLike(cline,like1);
        if (like != null){                              // Прямое совпадение сегментов
            pairs++;
            TRouteSegment zz = new TRouteSegment(like);
            zz.setMode1(Values.OMRightFull);
            cntMode[zz.getMode1()]++;
            vv.getSegments().add(zz);
            }
        else{
            like = segments.firstLike(cline,like2);     // Обратное совпадение сегментов
            if (like != null){
            pairs++;
            TRouteSegment zz = new TRouteSegment(like);
            zz.setMode1(Values.OMInvertFull);
            cntMode[zz.getMode1()]++;
            vv.getSegments().add(zz);
            }
        else{
            cline.calcSegmentLength();
            segments.add(cline);                        // Сегмент создается уникальный при частичном совпадении
            //------------- Частично совпадающие сегменты !!!!!!!!!
            TSegment a1 = segments.vfar(cline,cmp2);    // Макс. прямое совпадение по длине мин.сегмента от начала
            TSegment a2 = segments.vfar(cline,cmp3);    // Макс. прямое совпадение по длине мин.сегмента  от конца
            TSegment a3 = segments.vfar(cline,cmp4);    // Макс. инверсное совпадение по длине мин.сегмента  от начала
            TSegment a4 = segments.vfar(cline,cmp5);    // Макс. инверсное совпадение по длине мин.сегмента  от конца
            int b1 = cline.cmpNearFore1(a1);            // Точек совпадения для a1
            int b2 = cline.cmpNearFore2(a2);            // Точек совпадения для a2
            int b3 = cline.cmpNearBack1(a3);            // Точек совпадения для a3
            int b4 = cline.cmpNearBack2(a4);            // Точек совпадения для a4
            TRouteSegment zz = new TRouteSegment(cline);
            vv.getSegments().add(zz);
            if (b1>1 || b2>1 || b3>1 || b4>1){
            nears++;
            if (b1>1){
                if (traceLevel==ValuesBase.TraceModeMid)
                    log.addInfo(vv.getTitle(map)+" "+nearMap.get(Values.OMRightPartBeg).title()+" общих "+b1+" из"+a1.size()+","+cline.size());
                if (zz.getNear1() == null){
                    zz.getNear1().setRef(a1);
                    zz.setMode1(Values.OMRightPartBeg);
                    cntMode[zz.getMode1()]++;
                    zz.setPoints1(b1);
                    }
                else{
                    zz.getNear2().setRef(a2);
                    zz.setMode2(Values.OMRightPartBeg);
                    cntMode[zz.getMode2()]++;
                    zz.setPoints2(b1);
                    }
                }
            if (b2>1){
                if (traceLevel==ValuesBase.TraceModeMid)
                    log.addInfo(vv.getTitle(map)+" "+nearMap.get(Values.OMRightPartEnd).title()+" общих "+b2+" из"+a2.size()+","+cline.size());
                if (zz.getNear1() == null){
                    zz.getNear1().setRef(a2);
                    zz.setMode1(Values.OMRightPartEnd);
                    cntMode[zz.getMode1()]++;
                    zz.setPoints1(b2);
                    }
                else{
                    zz.getNear2().setRef(a2);
                    zz.setMode2(Values.OMRightPartEnd);
                    cntMode[zz.getMode2()]++;
                    zz.setPoints2(b2);
                    }
                }
            if (b3>1){
                if (traceLevel==ValuesBase.TraceModeMid)
                    log.addInfo(vv.getTitle(map)+" "+nearMap.get(Values.OMInvertPartBeg).title()+" общих "+b3+" из"+a3.size()+","+cline.size());
                if (zz.getNear1() == null){
                    zz.getNear1().setRef(a3);
                    zz.setMode1(Values.OMInvertPartBeg);
                    cntMode[zz.getMode1()]++;
                    zz.setPoints1(b3);
                    }
                else{
                    zz.getNear2().setRef(a3);
                    zz.setMode1(Values.OMInvertPartBeg);
                    cntMode[zz.getMode1()]++;
                    zz.setPoints2(b3);
                    }
                }
            if (b4>1){
                if (traceLevel==ValuesBase.TraceModeMid)
                    log.addInfo(vv.getTitle(map)+" "+nearMap.get(Values.OMInvertPartEnd).title()+" общих "+b4+" из"+a4.size()+","+cline.size());
                if (zz.getNear1() == null){
                    zz.getNear1().setRef(a4);
                    zz.setMode1(Values.OMInvertPartEnd);
                    cntMode[zz.getMode1()]++;
                    zz.setPoints1(b4);
                    }
                else{
                    zz.getNear2().setRef(a4);
                    zz.setMode1(Values.OMInvertPartEnd);
                    cntMode[zz.getMode1()]++;
                    zz.setPoints1(b4);
                    }
                    }
                }
            }
        }
    }
    //----------------- Вложенная функция поиска оригинальной остановки
    private void addOriginalTStop(ErrorList log, TRoute vv, TStop stop){
        TStop stop2 = stops.vnear(stop,cmp1);
        if (stop2 == null){
            vv.getStops().add(new TRouteStop(stop));        // Добавить описатель остановки
            stops.add(stop);
        }
        else{
            double diff = stop2.getGps().diff(stop.getGps());
            if (!stop.getName().equals(stop2.getName())){   // Разные имена с ближайшей
                vv.getStops().add(new TRouteStop(stop));    // Добавить описатель остановки
                stops.add(stop);
            }
            else{
                vv.getStops().add(new TRouteStop(stop2,diff));      // Добавить описатель остановки
                if (diff!=0 && traceLevel==ValuesBase.TraceModeMax) // Расстояние до одноименной
                    log.addInfo("Расстояние до одноименной "+stop2.getName()+" "+diff);
            }
        }
    }
    /*
    Stop stop2 = stops.vnear(stop,cmp1);
        if (stop2 == null){
        Stop xx = new Stop(stop);
        vv.stops.add(xx);           // Добавить описатель остановки
        stops.add(new Stop(stop));
    }
        else{
        double diff = stop2.orm.diff(stop.orm);
        if (!stop.orm.name.equals(stop2.orm.name)){
            Stop xx = new Stop(stop);
            vv.stops.add(xx);       // Добавить описатель остановки
            stops.add(new Stop(stop));
        }
        else{
            Stop xx = new Stop(stop2);
            xx.diff = (int)diff;
            vv.stops.add(xx);     // Добавить описатель остановки
            if (diff!=0)        // Расстояние до одноименной
                log.onMessage(stop2.orm.name+" "+diff);
        }


    }
    */
    //--------------------------------------------------------------------------------
    public void  importData(ErrorList log){
        importData(true,-1,log);
        }
    public void  importData(boolean retrofit, int routeCount, ErrorList log){
        map = Values.constMap().getGroupMapByValue("RouteType");
        nearMap = Values.constMap().getGroupMapByValue("OverlapMode");
        for(int i=0;i<cntMode.length;i++)
            cntMode[i]=0;
        long tt = System.currentTimeMillis();
        GorTransHttpClient client1 = new GorTransHttpClient();
        GorTransAPIClient client2 = new GorTransAPIClient();
        try {
            Pair<String,ArrayList<GorTransRouteList>> data= retrofit ? client1.getRouteList() : client2.getRouteList();
            if (data.o1!=null){
                log.addError(data.o1);
                return;
                }
            int jj=0;
            for(GorTransRouteList list : data.o2){
                ArrayList<GorTransRoute> vv = list.getWays();
                int ttype = list.type;
                for(int j=0;j<vv.size();j++){
                    if (routeCount == -1 || jj < routeCount){
                        routes.add(new TRoute(ttype,vv.get(j)));
                        }
                    jj++;
                }
            }
        for(int i=0; i<routes.size();i++){
            TRoute vv = routes.get(i);
            Pair<String,ArrayList<GorTransPoint>> trasse = retrofit ? client1.getTrasse(vv.getTType(),vv.getRouteName()) :
                    client2.getTrasse(vv.getTType(),vv.getRouteName());
            if (trasse.o1!=null){
                log.addError(trasse.o1+vv.getTitle(map)+" "+vv.getStopName1()+" "+vv.getStopName2()+" ");
                vv.setDataValid(false);
                continue;
                }
            if (trasse.o2.size()==0){
                if (traceLevel!=ValuesBase.TraceModeNone)
                    log.addInfo("Нет данных: "+vv.getTitle(map)+" "+vv.getStopName1()+" "+vv.getStopName2()+" ");
                vv.setDataValid(false);
                continue;
                }
            //----------- Разрезание по остановкам -----------------------------
            TSegment cline = new TSegment();
            TStop prevTStop = null;
            double lastLnt = 0.0;
            for(GorTransPoint a:trasse.o2){
                cline.getPoints().add(new TSegPoint(new GPSPoint(a.getLat(),a.getLng(),true)));
                if (a.getN().length()!=0){       // остановка
                    TStop stop = new TStop(a.getN(),new GPSPoint(a.getLat(),a.getLng(),true));
                    boolean twoTStops=false;
                    if (prevTStop!=null && stop.getName().equals(prevTStop.getName())){
                        twoTStops = true;
                        lastLnt = cline.size();
                        if (traceLevel==ValuesBase.TraceModeMax)
                            log.addInfo("idx="+vv.getSegments().size()+" segment size="+cline.size()+" lnt="+lastLnt);
                        if (vv.getLastStopIdx()==0)            // Первый
                            vv.setLastStopIdx(vv.getStops().size() - 1);
                            }
                    if (!(twoTStops && lastLnt==0)){   // Между двумя одинаковыми нет проезда - не сохранять второй
                        addOriginalTStop(log,vv, stop);
                        if (cline.size()!=1)          // Для самого первого  =1
                            addOriginalSegment(log,vv, cline);
                        }
                    cline = new TSegment();
                    cline.getPoints().add(new TSegPoint(new GPSPoint(a.getLat(),a.getLng(),true)));
                    prevTStop = stop;
                    }
                }
            //pline.foreach((a)=> a.save(conn))
            //-------- Для отладки ---------------------------------------------
            //    }
            //if (vv.route.firstTStopIdx==0){      // копировать последнюю остановку
            //    val idx = vv.getStops().size-1
            //    val lastTStop = vv.getStops().get(idx)
            //    cline = new TSegment
            //    cline.add(new Coord(lastTStop.stop))
            //    cline.add(new Coord(lastTStop.stop))
            //    vv.segments.add(new RouteSegment(cline))
            //   }
            int lst = vv.getStops().size() - 1;
            if (vv.getSegments().size() !=  lst){
                if (traceLevel>=ValuesBase.TraceModeMin)
                    log.addInfo(vv.getTitle(map)+": не соответствует кол-во остановок и сегментов");
                vv.setDataValid(false);
                }
            String s1 = vv.getStops().get(0).getName();
            String s2 = vv.getStops().get(lst).getName();
            String s3 = vv.getStops().get(lst-1).getName();
            if (!s1.equals(s2)){
                if (traceLevel>=ValuesBase.TraceModeMin)
                    log.addInfo(vv.getTitle(map)+": не соовпадают остановки на концах маршрута: "+s1+" "+s2);
                vv.setDataValid(false);
                }
            if (s3.equals(s2)){
                vv.setLastStopDiff2(true);              // Проезд на втором конце
                int lnt2 = vv.getSegments().get(vv.getSegments().size()-1).getSegment().getRef().getPoints().size();
                if (traceLevel==ValuesBase.TraceModeMax)
                    log.addInfo("Проезд на втором конце: "+lnt2);
                }
            int idx = vv.getLastStopIdx();
            String s4 = vv.getStops().get(idx).getName();
            String s5 = vv.getStops().get(idx+1).getName();
            if (s4.equals(s5)){
                vv.setLastStopDiff1(true);      // Проезд на первом конце
                int lnt2 = vv.getSegments().get(idx).getSegment().getRef().getPoints().size();
                if (traceLevel==ValuesBase.TraceModeMax)
                    log.addInfo("Проезд на первом конце: "+lnt2);
                }
            if (traceLevel==ValuesBase.TraceModeMax){
                log.addInfo(vv.getTitle(map)+" "+ vv.getSegments().size()+" "+vv.getStops().size()+" "+s4+"["+idx+"] ");
                log.addInfo(vv.getStopName1()+" "+vv.getStopName2()+" ");
                }
            }
        for(int i=0;i<cntMode.length;i++)
            log.addInfo(""+(i+1)+". "+nearMap.get(i).title()+" "+cntMode[i]);
        log.addInfo("Сегментов="+segments.size()+" повторов="+pairs+" ближайших="+nears+" время="+(System.currentTimeMillis()-tt)/1000);
        } catch (Throwable e){ log.addError("Ошибка импорта: "+e.toString()); }
    }
    //------------------------------------------------------------------------------------------------------
    public static void main(String args[]) throws Throwable{
        GorTransImport xx = new GorTransImport();
        ErrorList errorList = new ErrorList();
        xx.importData(errorList);
        System.out.println(errorList);
        errorList.clear();
        xx.importData(false,-1,errorList);
        System.out.println(errorList);
        }
}
