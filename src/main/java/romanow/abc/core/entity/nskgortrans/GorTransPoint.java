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
public class GorTransPoint extends Entity {
    @Getter @Setter private long id=0;
    @Getter @Setter private String n="";
    @Getter @Setter private double len=0.0;
    @Getter @Setter private double lat=0.0;
    @Getter @Setter private double lng=0.0;
    public String toString(){ return n+" "+len+" "+lat+" "+lng;}
}
