package romanow.abc.core.entity.server;

import lombok.Getter;
import romanow.abc.core.prepare.DayCellList;

import java.util.ArrayList;

public class TSegmentStatistic {
    @Getter private ArrayList<DayCellList> weekCells = new ArrayList<>();
    @Getter private int notNullCellCount=0;
    @Getter private int totalValues=0;
    public TSegmentStatistic(ArrayList<DayCellList> weekCells, int notNullCellCount, int totalValues) {
        this.weekCells = weekCells;
        this.notNullCellCount = notNullCellCount;
        this.totalValues = totalValues;
        }
}
