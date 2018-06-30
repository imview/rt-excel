package com.rt.excel;

import com.rt.excel.checker.*;
import com.rt.util.Reflections;
import com.rt.util.ServiceResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ExcelWorksheet<T extends BaseRowEntity> {
    private Workbook workbook;
    private Sheet sheet;
    private List<ExcelCell> cells;

    private int sheetIndex;
    private int titleRowIndex;

    private Class<T> clazz;

    public ExcelWorksheet(Class<T> clazz, String pFilePath) throws IOException {
        this(clazz, pFilePath, 0, 0);
    }

    public ExcelWorksheet(Class<T> clazz, String pFilePath, int pSheetIndex) throws IOException {
        this(clazz, pFilePath, pSheetIndex, 0);
    }

    public ExcelWorksheet(Class<T> clazz, String pFilePath, int pSheetIndex, int pTitleRowIndex) throws IOException {
        FileInputStream file = new FileInputStream(new File(pFilePath));
        if (StringUtils.isBlank(pFilePath)) {
            throw new RuntimeException("路径为空!");
        } else if (pFilePath.toLowerCase().endsWith("xls")) {
            this.workbook = new HSSFWorkbook(file);
        } else if (pFilePath.toLowerCase().endsWith("xlsx")) {
            this.workbook = new XSSFWorkbook(file);
        } else {
            throw new RuntimeException("文档格式不正确!");
        }
        if (this.workbook.getNumberOfSheets() < sheetIndex) {
            throw new RuntimeException("文档中没有工作表!");
        }
        this.sheet = this.workbook.getSheetAt(pSheetIndex);
        this.sheetIndex = pSheetIndex;
        this.titleRowIndex = pTitleRowIndex;
        this.clazz = clazz;
        this.initCells();
    }

    public List<T> transform() throws InstantiationException, IllegalAccessException {
        return this.transform(null);
    }

    public List<T> transform(Consumer<T> consumer) throws IllegalAccessException, InstantiationException {
        return this.initData(consumer, false);
    }

    public List<T> check() throws IllegalAccessException, InstantiationException {
        return this.check(null);
    }

    public List<T> check(Consumer<T> consumer) throws IllegalAccessException, InstantiationException {
        return this.initData(consumer, true);
    }

    public ExcelCell getCell(String pName) {
        return this.cells.stream().filter(m -> m.getName().equals(pName)).findFirst().get();
    }

    public void initCells() {
        cells = new ArrayList<>();
        List<Object[]> sheetColumns = new ArrayList<>();
        Row row = sheet.getRow(this.titleRowIndex);
        for (int i = 0; i < row.getLastCellNum(); i++) {
            if (StringUtils.isBlank(row.getCell(i).getStringCellValue())) {//有空单元格就中断
                break;
            } else {
                sheetColumns.add(new Object[]{i, row.getCell(i).getStringCellValue()});
            }
        }
        Field[] fields = this.clazz.getDeclaredFields();
        List<Object[]> annotationList = new ArrayList<>();
        for (Field field : fields) {
            ExcelCellAnnotation annotation = field.getAnnotation(ExcelCellAnnotation.class);
            if (annotation != null) {
                for (Object[] obj : annotationList) {
                    if (((ExcelCellAnnotation) obj[0]).title() == annotation.title()) {
                        throw new RuntimeException(String.format("field[%s],title[%s]重复", field, annotation.title()));
                    }
                }
                annotationList.add(new Object[]{annotation, field});

                ExcelCell cell = new ExcelCell();
                cell.setName(field.getName());
                cell.setField(field);
                cell.setTitle(annotation.title());
                for (Object[] obj : sheetColumns) {
                    if (((String) obj[1]).equals(annotation.title())) {
                        cell.setIndex((int) obj[0]);
                    }
                }
                cell.setNullable(annotation.nullable());
                if (!annotation.nullable()) {
                    cell.addChecker(new NullableChecker());
                }
                cell.setRepeatable(annotation.repeatable());
                if (!annotation.repeatable()) {
                    cell.addChecker(new RepeatableChecker());
                }
                Type fieldType = field.getType();
                if (fieldType == String.class) {
                    if (annotation.minLength() > 0) {
                        cell.setMinLength(annotation.minLength());
                        cell.addChecker(new MinLengthChecker());
                    }
                    if (annotation.maxLength() > 0) {
                        cell.setMaxLength(annotation.maxLength());
                        cell.addChecker(new MaxLengthChecker());
                    }
                    if (annotation.minLength() > 0
                            && annotation.maxLength() > 0
                            && annotation.maxLength() < annotation.minLength())
                        throw new RuntimeException(String.format("field[%s],minLength必须小于等于maxLength", field));
                } else if (fieldType == Integer.class
                        || fieldType == int.class
                        || fieldType == BigDecimal.class
                        || fieldType == Long.class
                        || fieldType == double.class
                        || fieldType == long.class) {
                    if (StringUtils.isNotBlank(annotation.minValue())) {
                        if (!tryParseBigDecimal(annotation.minValue())) {
                            throw new RuntimeException(String.format("field[%s],minValue赋值错误,只能为数字", field));
                        }
                        cell.setMinValue(annotation.minValue());
                        cell.addChecker(new MinValueChecker());
                    }
                    if (StringUtils.isNotBlank(annotation.maxValue())) {
                        if (!tryParseBigDecimal(annotation.maxValue())) {
                            throw new RuntimeException(String.format("field[%s],maxValue赋值错误,只能为数字", field));
                        }
                        cell.setMaxValue(annotation.maxValue());
                        cell.addChecker(new MaxValueChecker());
                    }
                    if (StringUtils.isNotBlank(annotation.minValue())
                            && StringUtils.isNotBlank(annotation.maxValue()))
                        if (new BigDecimal(annotation.maxValue()).compareTo(new BigDecimal(annotation.minValue())) == -1) {
                            throw new RuntimeException(String.format("field[%s],minValue必须小于等于maxValue", field));
                        }
                } else if (fieldType == Date.class) {
                    if (!StringUtils.isBlank(annotation.dateFormat()))
                        cell.setDateFormat(annotation.dateFormat());
                }
                if ((fieldType == int.class
                        || fieldType == double.class
                        || fieldType == long.class
                        || fieldType == float.class)
                        && !cell.existChecker(new NullableChecker())) {
                    cell.addChecker(new NullableChecker());
                }
                cells.add(cell);
            }
        }

        List<String> requiredCell = new ArrayList<>();
        List<String> columns = new ArrayList<>();
        sheetColumns.stream().forEach(m -> columns.add((String) m[1]));
        for (ExcelCell cell : cells) {
            if (!columns.contains(cell.getTitle())) {
                requiredCell.add(cell.getTitle());
            }
        }
        if (requiredCell.size() > 0) {
            throw new RuntimeException(String.format("列名[%s]在excel中不存在", String.join(",", requiredCell)));
        }
    }

    private List<T> initData(Consumer<T> consumer, boolean check) throws IllegalAccessException, InstantiationException {
        List<T> list = new ArrayList<T>();
        for (int i = titleRowIndex + 1; i <= sheet.getLastRowNum(); i++) {
            Row row = this.sheet.getRow(i);
            T entity = (T) this.clazz.newInstance();
            int blankCount = 0;
            List<String> failedMessage = new ArrayList<>();
            for (ExcelCell cell : cells) {
                row.getCell(cell.getIndex()).setCellType(CellType.STRING);
                String cellValue = row.getCell(cell.getIndex()).getStringCellValue();
                Boolean transformSuccess = true;
                if (StringUtils.isBlank(cellValue)) {
                    blankCount++;
                } else {
                    Class<?> valType = Class.class;
                    if (cell.getField() instanceof Field) {
                        valType = cell.getField().getType();
                    }
                    try {
                        Object objCellValue = new Object();
                        if (valType == String.class) {
                            objCellValue = String.valueOf(cellValue);
                        } else if (valType == Integer.class
                                || valType == int.class) {
                            if (StringUtils.isNotBlank(cellValue.toString())) {
                                objCellValue = new BigDecimal(cellValue).intValue();
                            }
                        } else if (valType == Long.class
                                || valType == long.class) {
                            objCellValue = Double.valueOf(cellValue).longValue();
                        } else if (valType == Double.class
                                || valType == double.class) {
                            objCellValue = Double.valueOf(cellValue);
                        } else if (valType == Float.class
                                || valType == float.class) {
                            if (StringUtils.isNotBlank(cellValue.toString())) {
                                objCellValue = Float.valueOf(cellValue);
                            }
                        } else if (valType == Date.class) {
                            if (StringUtils.isNotBlank(cell.getDateFormat()) && StringUtils.isNotBlank(cellValue.toString())) {
                                SimpleDateFormat dateForm = new SimpleDateFormat(cell.getDateFormat());
                                objCellValue = dateForm.parse(cellValue);
                            } else if (StringUtils.isBlank(cell.getDateFormat()) && StringUtils.isNotBlank(cellValue.toString())) {
                                objCellValue = DateUtil.getJavaDate(Double.valueOf(cellValue).longValue());
                            }
                        } else if (valType == BigDecimal.class) {
                            objCellValue = new BigDecimal(cellValue);
                        } else {
                            objCellValue = String.valueOf(cellValue);
                        }
                        Reflections.invokeSetter(entity, cell.getField().getName(), objCellValue);
                        entity.setIsSuccess(true);
                        entity.setMessage("");
                    } catch (Exception ex) {
                        failedMessage.add(String.format("[%s]格式不正确", cell.getTitle()));
                        transformSuccess = false;
                    }
                }
                if (transformSuccess && check) {
                    CheckerEvent event = new CheckerEvent();
                    event.setCell(cell);
                    event.setValue(cellValue);
                    event.setRow(row);
                    event.setSheet(this.sheet);
                    event.setCells(this.cells);
                    cell.check(event);
                    List<ServiceResult> failedResult = cell.getCheckResults().stream().filter(m -> m.getIsSuccess() == false).collect(Collectors.toList());
                    if (failedResult.size() > 0) {
                        failedMessage.addAll(failedResult.stream().map(m -> m.getMessage()).collect(Collectors.toList()));
                    }
                }
            }
            if (failedMessage.size() > 0) {
                entity.setIsSuccess(false);
                entity.setMessage(String.join(";", failedMessage));
            }
            list.add(entity);
            if (consumer != null) {
                consumer.accept(entity);
            }
            if (blankCount == cells.size()) {//碰到没有数据的行就中断
                break;
            }
        }
        return list;
    }

    private boolean tryParseBigDecimal(String value) {
        try {
            new BigDecimal(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
