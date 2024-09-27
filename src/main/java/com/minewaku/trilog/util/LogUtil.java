package com.minewaku.trilog.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class LogUtil {
    
    @Autowired
    public static final Logger LOGGER = LogManager.getLogger();
}
