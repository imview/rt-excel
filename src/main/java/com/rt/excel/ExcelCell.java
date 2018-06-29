package com.rt.excel;

import com.rt.excel.checker.Checker;
import com.rt.excel.checker.CheckerEvent;
import com.rt.util.ServiceResult;

import java.lang.reflect.Field;
import java.util.*;

public class ExcelCell {
    private String title;
    private boolean nullable;
    private boolean repeatable;
    private String minValue;
    private String maxValue;
    private int minLength;
    private int maxLength;
    private String dateFormat;

    //对应class的field
    private Field field;
    //在worksheet中的第几列
    private int index;
    private String name;

    private List<Checker> checkers;
    private List<ServiceResult> checkResults;

    public void addChecker(Checker checker) {
        if (checkers == null) {
            checkers = new ArrayList<>();
        }
        checkers.add(checker);
    }

    public boolean existChecker(Checker checker) {
        boolean isExist = false;
        if (checkers != null) {
            for (Checker ck : checkers) {
                if (ck.getClass().isInstance(checker))
                    isExist = true;
            }
        }
        return isExist;
    }

    public void removeChecker(Checker checker) {
        if (checker == null)
            return;
        checkers.remove(checker);
    }

    public List<ServiceResult> check(CheckerEvent event) {
        checkResults = new ArrayList<>();
        if(checkers!=null) {
            Iterator iter = checkers.iterator();
            while (iter.hasNext()) {
                Checker listener = (Checker) iter.next();
                checkResults.add(listener.check(event));
            }
        }
        return checkResults;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Collection getCheckers() {
        return checkers;
    }

    public void setCheckers(List<Checker> checkers) {
        this.checkers = checkers;
    }

    public List<ServiceResult> getCheckResults() {
        return checkResults;
    }

    public void setCheckResults(List<ServiceResult> checkResults) {
        this.checkResults = checkResults;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
