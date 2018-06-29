package com.rt.excel.checker;

import com.rt.util.ServiceResult;
import org.apache.commons.lang3.StringUtils;

public class NullableChecker implements Checker {
    @Override
    public ServiceResult check(CheckerEvent event) {
        ServiceResult res = new ServiceResult();
        if (StringUtils.isBlank((event.getValue())))
        {
            res.failed(String.format("[%s]不可为空", event.getCell().getTitle()));
        }
        return res;
    }
}
