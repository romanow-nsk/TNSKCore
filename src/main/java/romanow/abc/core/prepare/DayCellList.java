package romanow.abc.core.prepare;

import lombok.Getter;
import romanow.abc.core.constants.Values;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DayCellList {
    //------ Диапазон статистики 6...24
    @Getter ArrayList<Cell> hourCells = new ArrayList<>();
    public DayCellList(){
        for (int i = Values.FirstStatisticHour; i<Values.LastStatisticHour;i++)
            hourCells.add(new Cell());
        }
    public void save(DataOutputStream out) throws IOException {
        for(Cell cc : hourCells)
            cc.save(out);
        }
    public void load(DataInputStream in) throws IOException{
        hourCells.clear();
        for (int i = Values.FirstStatisticHour; i<Values.LastStatisticHour;i++){
            Cell cc = new Cell();
            cc.load(in);
            hourCells.add(cc);
            }
        }
    public int getNotNullCells(){
        int cnt=0;
        for(Cell cc : hourCells)
            if (cc.getCount()!=0)
                cnt++;
        return cnt;
        }
    public int getTotalCounts(){
        int cnt=0;
        for(Cell cc : hourCells)
            cnt+=cc.getCount();
        return cnt;
    }
}
