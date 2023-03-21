package romanow.abc.core.prepare;

import romanow.abc.core.utils.Pair;

public class TUtils {
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
}
