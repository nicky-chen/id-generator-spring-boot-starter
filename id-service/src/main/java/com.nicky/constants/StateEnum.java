package com.nicky.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author nicky_chin [shuilianpiying@163.com]
 * @since --created on 2018/7/25 at 15:35
 */
@Getter
@AllArgsConstructor
public enum StateEnum {

    //有
    HAVING(1, "HAVING"),

    //没有
    NON_HAVING(2, "NON_HAVING"),

    //数据库获取
    GENERATING(3, "GENERATING"),

    //异常
    EXCEPTION(4, "EXCEPTION");

    private int key;
    private String value;

}
