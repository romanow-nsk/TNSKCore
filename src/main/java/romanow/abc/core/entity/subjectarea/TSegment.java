package romanow.abc.core.entity.subjectarea;

import lombok.Getter;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityRefList;

public class TSegment extends Entity {
    @Getter private EntityRefList<TSegPoint> points = new EntityRefList<>(TSegPoint.class);     // Точки сегмента
    public int size() { return points.size(); }
    public TSegment(){}
    //----------- Манипуляции - сравнения и разрезания -------------------------
    public boolean cmpExactFore(TSegment two){
        if (size() != two.size())
            return false;
        for(int ii=0; ii<size();ii++){
            if (!points.get(ii).cmpExact(two.points.get(ii)))
                return false;
            }
        return true;
        }
    public boolean cmpExactBack(TSegment two){
        if (size() != two.size())
            return false;
        for(int ii=0; ii< points.size();ii++){
            if (!points.get(ii).cmpExact(two.points.get(points.size()-1-ii)));
            return false;
            }
        return true;
        }
    public int cmpNearFore1(TSegment two){
        int minsz = size();
        if (two.size() < size())
            minsz = two.size();
        int maxsz = size();
        if (two.size() > size())
            maxsz = two.size();
        for(int ii=0;ii<minsz;ii++){
            if (!points.get(ii).cmpExact(two.points.get(ii)))
                return ii;
            }
        return 0;
        }
    public int  cmpNearFore2(TSegment two){
        int minsz = size();
        if (two.size() < size())
            minsz = two.size();
        int maxsz = size();
        if (two.size() > size())
            maxsz = two.size();
        for(int ii=0;ii<minsz;ii++){
            if (!points.get(size()-1-ii).cmpExact(two.points.get(two.size()-1-ii)))
                return ii;
        }
        return 0;
    }
    public int cmpNearBack1(TSegment two){
        int minsz = size();
        if (two.size() < size())
            minsz = two.size();
        int maxsz = size();
        if (two.size() > size())
            maxsz = two.size();
        for(int ii=0;ii<minsz;ii++){
            if (!points.get(ii).cmpExact(two.points.get(two.size()-1-ii)))
                return ii;
            }
        return 0;
        }
    public int  cmpNearBack2(TSegment two){
        int minsz = size();
        if (two.size() < size())
            minsz = two.size();
        int maxsz = size();
        if (two.size() > size())
            maxsz = two.size();
        for(int ii=0;ii<minsz;ii++){
            if (!points.get(size()-1-ii).cmpExact(two.points.get(ii)))
                return ii;
            }
        return 0;
        }
    public double calcSegmentLength(){      // Сумма длин отрезков сегмента
        if (points.size()<=1)
            return 0;
        double lnt=0;
        for(int i=1;i<points.size();i++)
            lnt += points.get(i).getGps().diff(points.get(i-1).getGps());
        return lnt;
        }
    //--------------------------------------------------------------------------

}
