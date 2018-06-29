package com.rt.excel.checker;

import com.rt.util.ServiceResult;

public interface Checker {
    ServiceResult check(CheckerEvent event);
}
