package romanow.abc.core.prepare;

import romanow.abc.core.ErrorList;
import romanow.abc.core.entity.nskgortrans.*;
import romanow.abc.core.entity.subjectarea.*;
import romanow.abc.core.utils.GPSPoint;

import java.util.ArrayList;

/**
 *
 * @author romanow
 */
public class GorTransImport {
    public static void main(String args[]) throws Throwable{
        //importData(new DBLocalProfile("217.71.129.186"));
        GorTransImport xx = new GorTransImport();
        ErrorList errorList = new ErrorList();
        xx.importData(errorList);
        System.out.println(errorList);
        }
    private TRouteList routes = new TRouteList();
    private TStopList stops = new TStopList();
    private TSegmentList segments = new TSegmentList();
    private int nears=0;
    private int pairs=0;
    //public void importData(DBProfile profile, ProxyParams proxy,ServerLog log) throws Throwable{
    //    importData(profile,proxy,true,-1,log);
    //    }
    //------------- "Анонимные функции" --------------------------------------
    FunCmp<TStop> cmp1 = new FunCmp<TStop>(){
        @Override
        public int cmp(TStop v1, TStop v2) {
            return v1.getGps().diff(v2.getGps());
            }
        };
    FunLike<TSegment> like1 = new FunLike<TSegment>(){
        @Override
        public boolean like(TSegment v1, TSegment v2){
            return v1.cmpExactFore(v2);
            }
        };
    FunLike<TSegment> like2 = new FunLike<TSegment>(){
        @Override
        public boolean like(TSegment v1, TSegment v2){
            return v1.cmpExactBack(v2);
            }
        };
    FunCmp<TSegment> cmp2 = new FunCmp<TSegment>(){
        @Override
        public int cmp(TSegment v1, TSegment v2) {
            return v1.cmpNearFore1(v2);
            }
        };
    FunCmp<TSegment> cmp3 = new FunCmp<TSegment>(){
        @Override
        public int cmp(TSegment v1, TSegment v2) {
            return v1.cmpNearFore2(v2);
            }
        };
    FunCmp<TSegment> cmp4 = new FunCmp<TSegment>(){
        @Override
        public int cmp(TSegment v1, TSegment v2) {
            return v1.cmpNearBack1(v2);
            }
        };
    FunCmp<TSegment> cmp5 = new FunCmp<TSegment>(){
        @Override
        public int cmp(TSegment v1, TSegment v2) {
            return v1.cmpNearBack2(v2);
            }
        };
    //----------------- Вложенная функция поиска оригинальной остановки
    private void addOriginalTStop(ErrorList log, TRoute vv, TStop stop){
        TStop stop2 = stops.vnear(stop,cmp1);
        if (stop2 == null){
            TStop xx = new TStop(stop);
            vv.getStops().add(new TRouteStop(xx));        // Добавить описатель остановки
            stops.add(new TStop(stop));
            }
        else{
            double diff = stop2.getGps().diff(stop.getGps());
            if (!stop.getName().equals(stop2.getName())){
            TStop xx = new TStop(stop);
            vv.getStops().add(new TRouteStop(xx));       // Добавить описатель остановки
            stops.add(new TStop(stop));
            }
        else{
            TStop xx = new TStop(stop2);
            xx.setDiff((int)diff);
            vv.getStops().add(new TRouteStop(xx));      // Добавить описатель остановки
            if (diff!=0)                                // Расстояние до одноименной
                log.addInfo("Расстояние до одноименной "+stop2.getName()+" "+diff);
                }
            }
        }
    //----------------- Вложенная функция поиска оригинальной остановки
    private void addOriginalSegment(ErrorList log, TRoute vv, TSegment cline){       // Поиск сегмента по совпадению
        TSegment like = segments.firstLike(cline,like1);
        if (like != null){
            pairs++;
            TRouteSegment zz = new TRouteSegment(like);
            vv.getSegments().add(zz);
            }
        else{
            like = segments.firstLike(cline,like2);
            if (like != null){
            pairs++;
            TRouteSegment zz = new TRouteSegment(like);
            vv.getSegments().add(zz);
            }
        else{
            segments.add(cline);
            //------------- Частично совпадающие сегменты !!!!!!!!!
            TSegment a1 = segments.vfar(cline,cmp2);
            TSegment a2 = segments.vfar(cline,cmp3);
            TSegment a3 = segments.vfar(cline,cmp4);
            TSegment a4 = segments.vfar(cline,cmp5);
            int b1 = cline.cmpNearFore1(a1);
            int b2 = cline.cmpNearFore2(a2);
            int b3 = cline.cmpNearBack1(a3);
            int b4 = cline.cmpNearBack2(a4);
            TRouteSegment zz = new TRouteSegment(cline);
            vv.getSegments().add(zz);
            if (b1>1 || b2>1 || b3>1 || b4>1){
            nears++;
            if (b1>1){
                log.addInfo("1. "+b1+"("+a1.size()+")="+cline.size());
                zz.setNear1(a1);
                zz.setMode1(1);
                }
            if (b2>1){
                log.addInfo("2. "+b2+"("+a2.size()+")="+cline.size());
                if (zz.getNear1() == null){
                    zz.setNear1(a2);
                    zz.setMode1(2);
                    }
            else{
                zz.setNear2(a2);
                zz.setMode2(2);
                }
            }
            if (b3>1){
                log.addInfo("3. "+b3+"("+a3.size()+")="+cline.size());
                if (zz.getNear1() == null){
                    zz.setNear1(a3);
                    zz.setMode1(3);
                    }
                else{
                    zz.setNear2(a3);
                    zz.setMode2(3);
                    }
                }
            if (b4>1){
                log.addInfo("4. "+b4+"("+a4.size()+")="+cline.size());
                if (zz.getNear1() == null){
                    zz.setNear1(a4);
                    zz.setMode1(4);
                    }
                else{
                    zz.setNear2(a4);
                    zz.setMode2(4);
                    }
                }
            }
        }
        }
    }
    //----------------- Вложенная функция сохранения в БД
    /*
    public void saveDB(ErrorList log) throws Throwable{
        //routes.saveTable(conn);
        log.addInfo("Маршруты сохранены");
        //stops.save(conn);
        log.addInfo("Остановки сохранены");
        //segments.save(conn,log);
        //DBStatistic xx = new DBStatistic("");
        for(int ii=0;ii<routes.size();ii++){
            Route a = routes.get(ii);
            if (ii%10==0)
                log.addInfo("Обработано "+ii+" маршрутов");
            DBItem yy[] = new DBItem[a.segments.size()];
            for(int jj=0;jj<a.segments.size();jj++){
                RouteSegment b = a.segments.get(jj);
                b.orm.idRoute = a.orm.getId();
                b.orm.idSegment = b.segment.orm.getId();    // Поставить id-ы основных таблиц
                if (b.near1!=null)
                    b.orm.idNear1 = b.near1.orm.getId();
                if (b.near2!=null)
                    b.orm.idNear2 = b.near2.orm.getId();
                conn.insert(xx);                            //Чтобы зарезервировать id-ы статистик
            b.orm.idStatistic = xx.getId();
            yy[jj] = b.orm;
            //conn.insert(b.orm);
            }
        conn.replace(yy);
        DBItem zz[] = new DBItem[a.getStops().size()];
        for(int jj=0;jj<a.getStops().size();jj++){
        TStop bb = a.getStops().get(jj);
        DBRouteTStop cc = new DBRouteTStop();
        cc.diff = (int)bb.diff;                     // Расстояние из этой остановки
        cc.idRoute = a.orm.getId();
        cc.idTStop = bb.getId();                     // Поставить id-ы основных таблиц
        zz[jj] = cc;
        //conn.insert(cc);
        }
        conn.replace(zz);
        }
        }
     */
    //--------------------------------------------------------------------------------
    public void  importData(ErrorList log){
        importData(-1,log);
        }
    public void  importData(int routeCount, ErrorList log){
        GorTransHttpClient xx=new GorTransHttpClient();
        try {
            ArrayList<GorTransRouteList> data=xx.getRouteList();
            int jj=0;
            for(GorTransRouteList list : data){
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
            ArrayList<GorTransPoint> trasse = xx.getTrasse(vv.getTType(),vv.getRouteName());
            if (trasse.size()==0){
                log.addInfo("Нет данных: "+vv.getTType()+":"+vv.getRouteName()+" "+vv.getStopName1()+" "+vv.getStopName2()+" ");
                vv.setDataValid(false);
                }
            else{
                //----------- Разрезание по остановкам -----------------------------
                TSegment cline = new TSegment();
                TStop prevTStop = null;
                double lastLnt = 0.0;
                for(GorTransPoint a:trasse){
                    cline.getPoints().add(new TSegPoint(new GPSPoint(a.getLat(),a.getLng(),true)));
                    if (a.getN().length()!=0){       // остановка
                        TStop stop = new TStop(a.getN(),new GPSPoint(a.getLat(),a.getLng(),true));
                        boolean twoTStops=false;
                        if (prevTStop!=null && stop.getName().equals(prevTStop.getName())){
                            twoTStops = true;
                            lastLnt = cline.size();
                            log.addInfo("idx="+vv.getSegments().size()+" segment size="+cline.size()+" lnt="+lastLnt);
                            if (vv.getLastStopIdx()==0)            // Первый
                                vv.setLastStopIdx(vv.getStops().size() - 1);
                                }
                        if (!(twoTStops && lastLnt==0)){   // Между двумя одинаковыми нет проезда - не сохранять второй
                            addOriginalTStop(log,vv, new TStop(stop));
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
                    log.addInfo("Не соответствует кол-во остановок и сегментов");
                    vv.setDataValid(false);
                    }
                String s1 = vv.getStops().get(0).getName();
                String s2 = vv.getStops().get(lst).getName();
                String s3 = vv.getStops().get(lst-1).getName();
                if (!s1.equals(s2)){
                    log.addInfo("Не соовпадают остановки на концах маршрута: "+s1+" "+s2);
                    vv.setDataValid(false);
                    }
                if (s3.equals(s2)){
                    vv.setLastStopDiff2(true);              // Проезд на втором конце
                    int lnt2 = vv.getSegments().get(vv.getSegments().size()-1).getSegment().getRef().getPoints().size();
                    log.addInfo("Проезд на втором конце: "+lnt2);
                    }
                int idx = vv.getLastStopIdx();
                String s4 = vv.getStops().get(idx).getName();
                String s5 = vv.getStops().get(idx+1).getName();
                if (s4.equals(s5)){
                    vv.setLastStopDiff1(true);      // Проезд на первом конце
                    int lnt2 = vv.getSegments().get(idx).getSegment().getRef().getPoints().size();
                    log.addInfo("Проезд на первом конце: "+lnt2);
                    }
                log.addInfo(vv.getTType()+":"+vv.getRouteName()+" "+
                    vv.getSegments().size()+" "+vv.getStops().size()+" "+s4+"["+idx+"] ");
                log.addInfo(vv.getStopName1()+" "+vv.getStopName2()+" ");
                }
            }
            log.addInfo("Сегментов="+segments.size()+" повторов="+pairs+" ближайших="+nears);
        } catch (Throwable e){ log.addError("Ошибка импорта: "+e.toString()); }
    }
}
