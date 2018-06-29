package com.rt.excel.checker;

import com.rt.util.ServiceResult;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class MaxValueChecker implements Checker {
    @Override
    public ServiceResult check(CheckerEvent event) {
        ServiceResult res = new ServiceResult();
        if(StringUtils.isNotBlank(event.getValue())){
            BigDecimal val=new BigDecimal(event.getValue());
            BigDecimal maxVal=new BigDecimal(event.getCell().getMaxValue());
            if(val.compareTo(maxVal)==1) {
                res.failed(String.format("[%s]超过最大值%s", event.getCell().getTitle(),event.getCell().getMaxValue()));
            }
        }
        return res;
    }
}