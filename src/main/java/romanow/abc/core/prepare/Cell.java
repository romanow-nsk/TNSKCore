package romanow.abc.core.prepare;

import lombok.Getter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Cell {
    private double sum=0;
    private double sum2=0;
    @Getter private int count=0;
    public void add(double vv){
        sum+=vv;
        sum2+=vv*vv;
        count++;
        }
    public void addNotNull(double vv){
        if (vv==0)
            return;
        sum+=vv;
        sum2+=vv*vv;
        count++;
        }
    public double middle(){
        return count==0 ? 0 : sum/count;
    }
    public double stdOtkl(){
        return count==0 ? 0 : Math.sqrt(sum2/count - middle()*middle());
    }
    public String toString(){
        return String.format("num=%d mid=%5.2f  diff2=%5.2f",count,middle(),stdOtkl());
        }
    public void save(DataOutputStream out) throws IOException{
        out.writeInt(count);
        out.writeDouble(sum);
        out.writeDouble(sum2);
        }
    public void load(DataInputStream in) throws IOException{
        count = in.readInt();
        sum=in.readDouble();
        sum2=in.readDouble();
        }
    }