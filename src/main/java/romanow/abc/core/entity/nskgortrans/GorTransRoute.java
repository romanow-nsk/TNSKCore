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
public class GorTransRoute  extends Entity {
    // {"marsh":"3","name":"3","stopb":"пос. Северный","stope":"Вокзал \"Новосибирск-Главный\""},    
    @Getter @Setter private String marsh="";
    @Getter @Setter private String name="";
    @Getter @Setter private String stopb="";
    @Getter @Setter private String stope="";
    public String toString(){ return marsh+" "+name+":"+stopb+"-"+stope; }
    }
