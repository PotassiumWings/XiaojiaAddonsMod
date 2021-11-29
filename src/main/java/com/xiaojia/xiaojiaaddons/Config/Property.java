package com.xiaojia.xiaojiaaddons.Config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface Property {
    Type type();

    String name();

    String parent() default "";

    String description() default "";

    boolean illegal() default false;

    int min() default 0;

    int max() default 2147483647;

    String[] options() default {};

    int step() default 1;

    String prefix() default "";

    String suffix() default "";

    enum Type {
        BOOLEAN, NUMBER, SELECT, CHECKBOX, FOLDER
    }
}
