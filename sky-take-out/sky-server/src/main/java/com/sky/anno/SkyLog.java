package com.sky.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sky.enumeration.OperationType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface SkyLog {
    OperationType value();    
}
