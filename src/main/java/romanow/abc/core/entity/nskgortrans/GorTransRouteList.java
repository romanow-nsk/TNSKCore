/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.abc.core.entity.nskgortrans;

import lombok.Getter;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class GorTransRouteList {
    public int type=0;
    @Getter private ArrayList<GorTransRoute> ways=new ArrayList();
    public String toString(){
        String out = "Тип: "+type;
        for(GorTransRoute route : ways)
            out+="\n"+route;
        return out;
    }
}
