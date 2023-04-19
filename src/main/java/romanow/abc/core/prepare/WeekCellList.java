package romanow.abc.core.prepare;

import lombok.Getter;
import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.utils.Base64Coder;

import java.io.*;
import java.util.ArrayList;

public class WeekCellList extends Entity {
    //------ Диапазон статистики 0...7
    @Getter private String zipData="";
    @Getter transient private ArrayList<DayCellList> weekCells = new ArrayList<>();
    public Cell getCell(int dayOfWeek,int hour) {
        if (hour< Values.FirstStatisticHour || hour>=Values.LastStatisticHour || dayOfWeek<0 || dayOfWeek>=7)
            return new Cell();
        return weekCells.get(dayOfWeek).hourCells.get(hour-Values.FirstStatisticHour);
        }
    public void zip() throws IOException{
        ByteArrayOutputStream dd = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(dd);
        save(out);
        out.flush();
        char[] bb = Base64Coder.encode(dd.toByteArray());
        zipData = new String(bb);
        out.close();
        }
    public void unzip() throws IOException{
        if(zipData.length()==0)
            return;
        byte bb[] = Base64Coder.decode(zipData);
        ByteArrayInputStream dd = new ByteArrayInputStream(bb);
        DataInputStream in = new DataInputStream(dd);
        load(in);
        in.close();
        }
    public WeekCellList() {
        for (int i = 0; i < 7; i++)
            weekCells.add(new DayCellList());
        }
    public void save(DataOutputStream out) throws IOException {
        for (DayCellList cc : weekCells)
            cc.save(out);
        }
    public void load(DataInputStream in) throws IOException {
        weekCells.clear();
        for (int i = 0; i < 7; i++) {
            DayCellList cc = new DayCellList();
            cc.load(in);
            weekCells.add(cc);
            }
        }
    public int getNotNullCells(){
        int cnt=0;
        for(DayCellList cc : weekCells)
            cnt += cc.getNotNullCells();
        return cnt;
        }
    public int getTotalCounts(){
        int cnt=0;
        for(DayCellList cc : weekCells)
            cnt += cc.getTotalCounts();
        return cnt;
        }
    public static void main(String ss[]) throws IOException {
        WeekCellList dd = new WeekCellList();
        dd.getCell(0,8).add(44);
        dd.getCell(3,12).add(55);
        dd.getCell(4,11).add(66);
        dd.zip();
        dd.unzip();
        System.out.println(dd.getCell(0,8));
        System.out.println(dd.getCell(3,12));
        System.out.println(dd.getCell(4,11));
        }
}

