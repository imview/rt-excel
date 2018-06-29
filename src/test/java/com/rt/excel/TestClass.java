package com.rt.excel;

import java.math.BigDecimal;
import java.util.Date;

public class TestClass extends BaseRowEntity {
    @ExcelCellAnnotation(title="title1",nullable = false,minLength = 1,maxLength = 10)
    String column1;
    @ExcelCellAnnotation(title="title2",nullable = false,dateFormat = "yyyy-MM-dd HH:mm:ss")
    Date column2;
    @ExcelCellAnnotation(title="title3",nullable = false,minValue = "1",maxValue = "40")
    int column3;
    @ExcelCellAnnotation(title="title4",repeatable = false)
    Integer column4;
    @ExcelCellAnnotation(title="title5")
    BigDecimal column5;
    @ExcelCellAnnotation(title="title6")
    Long column6;
    @ExcelCellAnnotation(title="title7")
    long column7;
    @ExcelCellAnnotation(title="title8")
    double column8;

    public TestClass(){}

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public Date getColumn2() {
        return column2;
    }

    public void setColumn2(Date column2) {
        this.column2 = column2;
    }

    public int getColumn3() {
        return column3;
    }

    public void setColumn3(int column3) {
        this.column3 = column3;
    }

    public Integer getColumn4() {
        return column4;
    }

    public void setColumn4(Integer column4) {
        this.column4 = column4;
    }

    public BigDecimal getColumn5() {
        return column5;
    }

    public void setColumn5(BigDecimal column5) {
        this.column5 = column5;
    }

    public Long getColumn6() {
        return column6;
    }

    public void setColumn6(Long column6) {
        this.column6 = column6;
    }

    public long getColumn7() {
        return column7;
    }

    public void setColumn7(long column7) {
        this.column7 = column7;
    }

    public double getColumn8() {
        return column8;
    }

    public void setColumn8(double column8) {
        this.column8 = column8;
    }

    @Override
    public String toString() {
        return "TestClass{" +
                "column1='" + column1 + '\'' +
                ", column2=" + column2 +
                ", column3=" + column3 +
                ", column4=" + column4 +
                ", column5=" + column5 +
                ", column6=" + column6 +
                ", column7=" + column7 +
                ", column8=" + column8 +
                ", isSuccess=" + getIsSuccess() +
                ", message=" + getMessage() +
                '}';
    }
}

