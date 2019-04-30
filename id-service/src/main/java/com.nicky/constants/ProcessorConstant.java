package com.nicky.constants;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.collect.Sets;
import com.nicky.annotation.AnnotationFormat;
import com.nicky.annotation.IdStrategy;

/**
 * @Description: 校验注解常量类
 * @date: 2019-03-22 20:09:39
 * @since JDK 1.8
 */
public class ProcessorConstant {
    public static final Set<Class<? extends Annotation>> VLDT_ANNOTATION_SET = Sets.newHashSet();

    static {
        VLDT_ANNOTATION_SET.add(IdStrategy.class);

    }

    public static final Class<? extends Annotation> ANNO_VALIDATED = AnnotationFormat.class;

    public static final String MESSAGE_ILLEGALITY = "@%s enumName非法";

    public static final String MESSAGE_BLANK = "@%s enumName不能为空";

    public static final String MESSAGE_CONTENT_ILLEGALITY = "enumName指定为非法枚举%s";

    public static final String MESSAGE = "enumName";

    public static final String ROOT_FOLDER_PATH = System.getProperty("user.home") + "/.local/EnumRecord/";

    public static final String FILE_EXT_TXT = ".txt";

    /**
     * 包路径
     */
    public static final String COM_STR = "com";


    public static final Pattern ENUM_NAME_PATTERN = Pattern.compile("^[A-Z0-9_]+$");
}
