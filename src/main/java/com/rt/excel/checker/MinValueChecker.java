package com.rt.excel.checker;

import com.rt.util.ServiceResult;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class MinValueChecker implements Checker {
    @Override
    public ServiceResult check(CheckerEvent event) {
        ServiceResult res = new ServiceResult();
        if(StringUtils.isNotBlank(event.getValue())){
            BigDecimal val=new BigDecimal(event.getValue());
            BigDecimal minVal=new BigDecimal(event.getCell().getMinValue());
            if(val.compareTo(minVal)==-1) {
                res.failed(String.format("[%s]小于最小值%s", event.getCell().getTitle(),event.getCell().getMinValue()));
            }
        }
        return res;
    }
}