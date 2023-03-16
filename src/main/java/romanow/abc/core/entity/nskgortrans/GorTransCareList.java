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
public class GorTransCareList {
    @Getter private ArrayList<GorTransCare> markers=new ArrayList<>();
    public String toString(){
        String ss="";
        for(GorTransCare care : markers)
            ss+=""+care+"\n";
        return ss;
        }
}
