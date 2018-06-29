package com.rt.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelCellAnnotation {
    String title();
    boolean nullable() default true;//int、long、double are not nullable no matter how you defined
    boolean repeatable() default true;
    String minValue() default "";//>=,limit to type of [int Integer BigDecimal] field
    String maxValue() default "";//<=,limit to type of [int Integer BigDecimal] field
    int minLength() default Integer.MIN_VALUE;//>=,limit to type of String field
    int maxLength() default Integer.MIN_VALUE;//<=,limit to type of String field
    String dateFormat() default "yyyy-MM-dd";//limit to type of Date field
}
