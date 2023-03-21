package romanow.abc.core.prepare;

import romanow.abc.core.utils.GPSPoint;

public class Distantion {
    public final boolean done;
    public final double distToLine;
    public final double distToPoint1;
    public final double distToPount2;
    public String toString(){
        return ""+done+" h="+distToLine+" to1="+distToPoint1+" to2="+distToPount2;
        }
        /*  Точка перпендикуляра на отрезок
    double L=(x1-x2)*(x1-x2)+(y1-y2)*(y1-y2);
    double PR=(x-x1)*(x2-x1)+(y-y1)*(y2-y1);
    bool res=true;
    double cf=PR/L;
    if(cf<0){ cf=0; res=false; }
    if(cf>1){ cf=1; res=false; }
    double xres=x1+cf*(x2-x1);
    double yres=y1+cf*(y2-y1);
    ------------------------------------------
    double L=(x1-x2)*(x1-x2)+(y1-y2)*(y1-y2);
    double PR=(x-x1)*(x2-x1)+(y-y1)*(y2-y1);
    bool res=(PR >= 0 && PR <= L);
    double LPerp=fabs((x-x1)*(y2-y1)-(y-y1)*(x2-x1))/sqrt(L);
    */
    public Distantion(GPSPoint point, GPSPoint p1, GPSPoint p2){
        double v1 = p1.diffX(p2);
        double v2 = p1.diffY(p2);
        double L=v1*v1+v2*v2;
        double PR = point.diffX(p1)*p2.diffX(p1)+point.diffY(p1)*p2.diffY(p1);
        double cf = PR/L;
        done =  PR>=0 && PR<=L;
        distToLine = Math.abs(point.diffX(p1)*p2.diffY(p1)-point.diffY(p1)*p2.diffX(p1))/Math.sqrt(L);
        v1 = cf*p2.diffX(p1);
        v2 = cf*p2.diffY(p1);
        distToPoint1 = Math.sqrt(v1*v1+v2*v2);
        distToPount2 = p1.diff(p2)-distToPoint1;
        }
    public static void main(String ss[]){
        GPSPoint p1=new GPSPoint(54,85,true);
        GPSPoint p2=new GPSPoint(54.01,85.01,true);
        GPSPoint p=new GPSPoint(54.0001,85.0001,true);
        System.out.println(new Distantion(p,p1,p2));
        }
}
