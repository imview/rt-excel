package com.rt.excel.checker;

import com.rt.util.ServiceResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

public class RepeatableChecker implements Checker {
    @Override
    public ServiceResult check(CheckerEvent event) {
        ServiceResult res = new ServiceResult();
        if(StringUtils.isNotBlank(event.getValue())) {
            int count = 0;
            for (int i = event.getTitleRowIndex() + 1; i <= event.getSheet().getLastRowNum(); i++) {
                Row row = event.getSheet().getRow(i);
                row.getCell(event.getCell().getIndex()).setCellType(CellType.STRING);
                if(StringUtils.isNotBlank(row.getCell(event.getCell().getIndex()).getStringCellValue())) {
                    if (row.getCell(event.getCell().getIndex()).getStringCellValue().equals(event.getValue())) {
                        count++;
                    }
                }
            }
            if (count >= 2)
                res.failed(String.format("[%s]有重复值", event.getCell().getTitle()));
        }
        return res;
    }
}
