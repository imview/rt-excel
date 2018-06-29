package com.rt.excel.checker;


import com.rt.util.ServiceResult;
import org.apache.commons.lang3.StringUtils;

public class MaxLengthChecker implements Checker {
    @Override
    public ServiceResult check(CheckerEvent event) {
        ServiceResult res = new ServiceResult();
        if(StringUtils.isNotBlank(event.getValue())&&event.getValue().length()>event.getCell().getMaxLength()){
            res.failed(String.format("[%s]超过最大长度%s",event.getCell().getTitle(),event.getCell().getMaxLength()));
        }
        return res;
    }
}
