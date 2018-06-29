package com.rt.excel;

import com.rt.util.ServiceResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.util.List;

public class AppTest {
    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException {
        ExcelWorksheet worksheet = new ExcelWorksheet(TestClass.class, "E:\\git\\rt-excel\\template.xls");

        List<TestClass> list = worksheet.transform();
        list.forEach(m -> System.out.println(m.toString()));

        list = worksheet.transform(m -> {
            TestClass entity = ((TestClass) m);
            entity.column1 = entity.column1==null?"":entity.column1 + "__postfix";
        });
        list.forEach(m -> System.out.println(m.toString()));

        List<TestClass> list2 = worksheet.check();
        list2.forEach(m -> System.out.println(m.toString()));

        worksheet.getCell("column3").addChecker(event -> {
            ServiceResult res = new ServiceResult();
            if (StringUtils.isNotBlank(event.getValue())) {
                Row row = event.getRow();
                ExcelCell c4 = event.getCell("column4");
                row.getCell(c4.getIndex()).setCellType(CellType.STRING);
                if (row.getCell(c4.getIndex()).getStringCellValue().equals(event.getValue())) {
                    res.failed(String.format("[%s]不能等于[%s]", event.getCell().getTitle(),c4.getTitle()));
                }
            }
            return res;
        });
        list2 = worksheet.check();
        list2.forEach(m -> System.out.println(m.toString()));

        list2 = worksheet.check(m -> {
            TestClass entity = ((TestClass) m);
            entity.column1 = entity.column1==null?"":entity.column1 + "__postfix";
        });
        list2.forEach(m -> System.out.println(m.toString()));
    }
}
