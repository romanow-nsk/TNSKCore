package romanow.abc.core.prepare;

import java.util.ArrayList;

public class ArrayListX<T>  extends ArrayList<T> {
    public ArrayListX(){
        clear();
        }
    public ArrayList<T> copy(){
        ArrayList<T> xx = new java.util.ArrayList();
        for(int ii=0;ii<size();ii++)
            xx.add(get(ii));
        return xx;
        }
    public boolean isIndex(int ii){
        return ii >=0 && ii<size();
        }
    public T remove(int ii){
        if (isIndex(ii))
            return super.remove(ii);
        return null;
        }
    public T get(int ii){
        return isIndex(ii) ? super.get(ii) : null;
        }
    public void foreach(FunDo ff){
        for(int ii=0;ii<size();ii++)
            ff.doIt(get(ii));
        }
    public T firstThat(FunTest ff){
        for(int ii=0; ii<size();ii++){
            T vv = get(ii);
            if (ff.test(vv))
                return vv;
            }
        return null;
        }
    public T firstLike(T in,FunLike ff){
        for(int ii=0; ii<size();ii++){
            T vv = get(ii);
            if (ff.like(in,vv))
                return vv;
            }
        return null;
        }
    int inear(T obj, FunCmp diff){
        if (size()==0)
            return -1;
        int vmin = 0;
        int zz = diff.cmp(obj,get(0));
        for(int ii=1;ii<size();ii++){
            int rr = diff.cmp(obj,get(ii));
            if (rr < zz){
                vmin = ii;
                zz = rr;
                }
            }
        return vmin;
        }
    public T vnear(T obj, FunCmp diff){
        int ii=inear(obj,diff);
        return ii==-1 ? null: get(ii);
        }
    int ifar(T obj, FunCmp diff){
        if (size()==0)
            return -1;
        int vmin = 0;
        int zz = diff.cmp(obj,get(0));
        for(int ii=1;ii<size();ii++){
            int rr = diff.cmp(obj,get(ii));
            if (rr > zz){
                vmin = ii;
                zz = rr;
                }
            }
        return vmin;
        }
    public T vfar(T obj, FunCmp diff){
        int ii=ifar(obj,diff);
        return ii==-1 ? null: get(ii);
        }
    int imin(FunCmp cmp){
        if (size()==0)
            return -1;
        int vmin= 0;
        for(int ii=1; ii< size();ii++){
            if (cmp.cmp(get(ii), get(vmin)) < 0)
                vmin = ii;
            }
        return vmin;
        }
    T vmin(FunCmp cmp){
        int ii=imin(cmp);
        return ii == -1 ?  null : get(ii);
        }
    int imax(FunCmp cmp){
        if (size()==0)
            return -1;
        int vmin= 0;
        for(int ii=1; ii< size();ii++){
            if (cmp.cmp(get(ii), get(vmin)) > 0)
                vmin = ii;
            }
        return vmin;
        }
    T vmax(FunCmp cmp){
        int ii=imax(cmp);
        return ii == -1 ?  null : get(ii);
        }
    public void remove(FunTest ff){
        int sz = size();
        int ii = 0;
        while (ii < sz){
            T vv = get(ii);
            if (ff.test(vv)){
                super.remove(ii);
                sz--;
            }
            else ii++;
        }
    }
}
