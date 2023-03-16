/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.abc.core.entity.nskgortrans;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.entity.Entity;

/**
 *
 * @author user
 */
public class GorTransCare extends Entity {
    @Getter @Setter private String title;
    @Getter @Setter private String id_typetr;
    @Getter @Setter private String marsh;
    @Getter @Setter private String graph;
    @Getter @Setter private String direction;
    @Getter @Setter private String lat;
    @Getter @Setter private String lng;
    @Getter @Setter private String time_nav;
    @Getter @Setter private String azimuth;
    @Getter @Setter private String rasp;
    @Getter @Setter private String ramp;
    @Getter @Setter private String speed;
    @Getter @Setter private String segment_order;
    public String toString(){ return "график="+getGraph()+" шир.="+getLat()+" долг.="+getLng()+" скор="+speed; }
//"title":"23","id_typetr":"8","marsh":"5023","graph":"1","direction":"B","lat":"54.889652","lng":"83.079155","time_nav":"2013-05-15 17:45:51","azimuth":"185","rasp":"-","speed":"31"    
}
