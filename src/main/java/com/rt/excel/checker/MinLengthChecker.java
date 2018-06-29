package com.rt.excel.checker;

import com.rt.util.ServiceResult;
import org.apache.commons.lang3.StringUtils;

public class MinLengthChecker implements Checker {
    @Override
    public ServiceResult check(CheckerEvent event) {
        ServiceResult res = new ServiceResult();
        if(StringUtils.isNotBlank(event.getValue())&&event.getValue().length()<event.getCell().getMinLength()){
            res.failed(String.format("[%s]小于最小长度%s",event.getCell().getTitle(),event.getCell().getMinLength()));
        }
        return res;
    }
}