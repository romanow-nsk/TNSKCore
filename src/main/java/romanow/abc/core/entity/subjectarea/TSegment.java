package romanow.abc.core.entity.subjectarea;

import lombok.Getter;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;
import romanow.abc.core.entity.EntityRefList;
import romanow.abc.core.entity.server.TCare;
import romanow.abc.core.entity.server.TSegmentStatistic;
import romanow.abc.core.prepare.Distantion;
import romanow.abc.core.prepare.WeekCellList;
import romanow.abc.core.utils.GPSPoint;
import romanow.abc.core.utils.OwnDateTime;

public class TSegment extends WeekCellList {        // Включая статистику
    @Getter private EntityRefList<TSegPoint> points = new EntityRefList<>(TSegPoint.class);     // Точки сегмента
    @Getter private double totalLength=0;
    public int size() { return points.size(); }
    public TSegment(){}
    //-------------------------------------------------------------------------
    public String getTitle(){
        return "Сегмент id="+getOid()+" точек "+points.size()+" длина "+String.format("%6.3f",totalLength);
        }
    public TSegmentStatistic getStatistic(){
        return new TSegmentStatistic(getWeekCells(),getNotNullCells(),getTotalCounts());
        }
    //----------- Манипуляции - сравнения и разрезания -------------------------
    public boolean cmpExactFore(TSegment two){      // Полное прямое совпадение
        if (size() != two.size())
            return false;
        for(int ii=0; ii<size();ii++){
            if (!points.get(ii).cmpExact(two.points.get(ii)))
                return false;
            }
        return true;
        }
    public boolean cmpExactBack(TSegment two){      // Полное инверсное совпадение
        if (size() != two.size())
            return false;
        for(int ii=0; ii< points.size();ii++){
            if (!points.get(ii).cmpExact(two.points.get(points.size()-1-ii)));
            return false;
            }
        return true;
        }
    public int cmpNearFore1(TSegment two){          // Прямое совпадение по длине минимального от начала
        int minsz = size();
        if (two.size() < size())
            minsz = two.size();
        for(int ii=0;ii<minsz;ii++){
            if (!points.get(ii).cmpExact(two.points.get(ii)))
                return ii;
            }
        return 0;
        }
    public int  cmpNearFore2(TSegment two){         // Прямое совпадение по длине минимального от конца
        int minsz = size();
        if (two.size() < size())
            minsz = two.size();
        for(int ii=0;ii<minsz;ii++){
            if (!points.get(size()-1-ii).cmpExact(two.points.get(two.size()-1-ii)))
                return ii;
        }
        return 0;
    }
    public int cmpNearBack1(TSegment two){         // Инверсное совпадение по длине минимального от начала
        int minsz = size();
        if (two.size() < size())
            minsz = two.size();
        for(int ii=0;ii<minsz;ii++){
            if (!points.get(ii).cmpExact(two.points.get(two.size()-1-ii)))
                return ii;
            }
        return 0;
        }
    public int  cmpNearBack2(TSegment two){         // Инверсное совпадение по длине минимального от конца
        int minsz = size();
        if (two.size() < size())
            minsz = two.size();
        for(int ii=0;ii<minsz;ii++){
            if (!points.get(size()-1-ii).cmpExact(two.points.get(ii)))
                return ii;
            }
        return 0;
        }
    public void calcSegmentLength(){      // Сумма длин отрезков сегмента
        totalLength=0;
        if (points.size()<=1)
            return;
        for(int i=1;i<points.size();i++)
            totalLength += points.get(i).getGps().diff(points.get(i-1).getGps());
        }
    //---------------------------------- Поиск ближайшего отрезка ------------------------------------------------------
    public Distantion findRoutePoint(GPSPoint point){
        Distantion nearest = new Distantion();
        if (points.size()<=1)
            return nearest;
        for(int i=1;i<points.size();i++){
            Distantion two = new Distantion(point,points.get(i-1).getGps(),points.get(i).getGps());
            if (!nearest.done)
                nearest = two;
            else{
                if (two.done && two.distToLine < nearest.distToLine)
                    nearest = two;
                }
            }
        return nearest;
        }
    public void addSpeedStatistic(TCare care){
        OwnDateTime ctime = new OwnDateTime();
        getCell(ctime.dayOfWeek(),ctime.hour()).addNotNull(care.lastPoint().getSpeed());
        }
}
