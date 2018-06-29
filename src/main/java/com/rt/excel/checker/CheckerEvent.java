package com.rt.excel.checker;

import com.rt.excel.ExcelCell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public class CheckerEvent {
    private ExcelCell cell;
    private List<ExcelCell> cells;
    private String value;
    private Row row;
    private Sheet sheet;
    private int titleRowIndex;

    public ExcelCell getCell() {
        return cell;
    }

    public ExcelCell getCell(String pName) {
        return this.cells.stream().filter(m -> m.getName().equals(pName)).findFirst().get();
    }

    public void setCell(ExcelCell cell) {
        this.cell = cell;
    }

    public List<ExcelCell> getCells() {
        return cells;
    }

    public void setCells(List<ExcelCell> cells) {
        this.cells = cells;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public int getTitleRowIndex() {
        return titleRowIndex;
    }

    public void setTitleRowIndex(int titleRowIndex) {
        this.titleRowIndex = titleRowIndex;
    }
}
