/*
 * File Name:AnnatationFormatProcessor.java is created on Mar 22, 2019 10:24:52 AM by nicky_chin
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.config;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import org.apache.commons.lang3.StringUtils;

import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.nicky.constants.ProcessorConstant;
import com.nicky.util.ProcessorUtil;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import jodd.util.StringPool;

import static com.nicky.constants.ProcessorConstant.ENUM_NAME_PATTERN;
import static com.nicky.constants.ProcessorConstant.MESSAGE_BLANK;
import static com.nicky.constants.ProcessorConstant.MESSAGE_CONTENT_ILLEGALITY;

/**
 * @author nicky_chin
 * @Description: 注解校验器
 * @date: 2019-03-22 10:24:52
 * @since JDK 1.8
 */
//@AutoService(Processor.class)
public class AnnotationFormatProcessor extends AbstractProcessor {

    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationNameSet = Sets.newHashSet();
        ProcessorConstant.VLDT_ANNOTATION_SET.forEach(v -> annotationNameSet.add(v.getCanonicalName()));

        // 扫描错误枚举类
        annotationNameSet.add(ProcessorConstant.ANNO_VALIDATED.getCanonicalName());

        return annotationNameSet;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        // 校验所有的校验注解类
        return validate(getResultEnumNames(roundEnvironment), roundEnvironment);
    }

    /**
     * 检查校验注解是否合法
     *
     * @param resultEnumNames
     * @param roundEnvironment
     *
     * @return
     *
     * @author nicky_chin
     */
    private boolean validate(Set<String> resultEnumNames, RoundEnvironment roundEnvironment) {
        for (Class<? extends Annotation> annotationClass : ProcessorConstant.VLDT_ANNOTATION_SET) {
            // 遍历所有被注解了该注解的元素，元素包括类，方法，属性，参数，是一种静态结构
            for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(annotationClass)) {
                AnnotationMirror annotationMirror =
                    MoreElements.getAnnotationMirror(annotatedElement, annotationClass).get();
                // 检查被注解的元素是否是方法，注解，属性或者参数，只有这些元素才需要被参数校验
                Annotation validateAnnotation = annotatedElement.getAnnotation(annotationClass);
                try {
                    String message = (String)(annotationClass.getDeclaredMethod(ProcessorConstant.MESSAGE)
                        .invoke(validateAnnotation));
                    // 校验错误消息是否合法
                    if (!validateMessage(annotatedElement, annotationClass, message, annotationMirror,
                        resultEnumNames)) {
                        // 任何非法校验，将提示错误
                        printError(annotatedElement, ProcessorConstant.MESSAGE_ILLEGALITY, annotationMirror,
                            annotationClass.getSimpleName());
                    }
                } catch (Exception ex) {
                    // 任何非法校验，将提示错误
                    printError(annotatedElement, ProcessorConstant.MESSAGE_ILLEGALITY, annotationMirror,
                        annotationClass.getSimpleName());
                }
            }
        }

        return true;
    }

    /**
     * 获取所有被标记了{@code Validated}的枚举类，并获取类名
     *
     * @param roundEnvironment
     *
     * @return
     */
    private Set<String> getResultEnumNames(RoundEnvironment roundEnvironment) {
        Map<String, Set<String>> enumNameMap = Maps.newHashMap();
        for (Element annotatedResultErrElement : roundEnvironment
            .getElementsAnnotatedWith(ProcessorConstant.ANNO_VALIDATED)) {
            // 检查被注解的元素是否是枚举类，只有这些元素才需要被统计
            if (annotatedResultErrElement.getKind() == ElementKind.ENUM) {
                TypeElement typeElement = (TypeElement)annotatedResultErrElement;
                List<? extends Element> elements = typeElement.getEnclosedElements();
                if (CollectionUtil.isNotEmpty(elements)) {
                    Set<String> enumNames = Sets.newHashSet();
                    for (Element element : elements) {
                        if (element.getKind() == ElementKind.ENUM_CONSTANT) {
                            enumNames.add(element.getSimpleName().toString());
                        }
                    }
                    if (enumNames.size() > 0) {
                        enumNameMap.put(typeElement.getQualifiedName().toString(), enumNames);
                    }
                }
            }
        }
        if (enumNameMap.size() > 0) {
            List<String> enumNameList = new ArrayList<>(enumNameMap.keySet());
            String folderName = ProcessorUtil.getFolderName(enumNameList.get(0));
            for (Map.Entry<String, Set<String>> enumNameEntry : enumNameMap.entrySet()) {
                String fileName = enumNameEntry.getKey();
                String fileContent = Joiner.on(StringPool.COMMA).join(enumNameEntry.getValue());
                ProcessorUtil.writeFile(folderName, fileName, fileContent);
            }
        }
        for (Element annotatedValidateErrElement : roundEnvironment
            .getElementsAnnotatedWith(ProcessorConstant.ANNO_VALIDATED)) {
            String orgFolderName = null;
            // 检查被注解的元素是否是类和参数
            if (annotatedValidateErrElement.getKind() == ElementKind.CLASS) {
                orgFolderName = ((TypeElement)annotatedValidateErrElement).getQualifiedName().toString();
            } else if (annotatedValidateErrElement.getKind() == ElementKind.PARAMETER) {
                orgFolderName = annotatedValidateErrElement.asType().toString();
            }
            String folderName = ProcessorUtil.getFolderName(orgFolderName);
            return ProcessorUtil.readFiles(folderName);
        }

        return null;
    }

    /**
     * 校验出错信息的合法性，出错信息需要符合如下规则，否则报编译错误：
     *
     * <li>是已存在的实现{@code ResultError}接口的枚举实例名称
     * <li>提示错误消息的字符串(原生)
     *
     * @param annotationClass  校验注解类
     * @param annotatedElement 当前被注解的元素
     * @param message          要校验的字符串
     * @param annotationMirror 注解元素
     * @param resultEnumNames  要查找的错误枚举类名
     *
     * @return
     *
     * @throws Exception
     * @author nicky_chin
     */
    private boolean validateMessage(Element annotatedElement, Class<? extends Annotation> annotationClass,
        String message, AnnotationMirror annotationMirror, Set<String> resultEnumNames) throws Exception {
        if (StringUtils.isEmpty(message)) {
            // message不允许为空
            printError(annotatedElement, MESSAGE_BLANK, annotationMirror, annotationClass.getSimpleName());
            return false;
        }
        if (!(ENUM_NAME_PATTERN.matcher(message).matches())) {
            // 普通错误字符串，则忽略编译检查
            return true;
        }
        // 检查枚举名称是否已存在，且是否合法
        if (checkEnumNames(message, resultEnumNames)) {
            // 存在定义的错误枚举，则编译检查通过
            return true;
        } else {
            // 不存在/非法定义的错误枚举，则报编译错误
            printError(annotatedElement, MESSAGE_CONTENT_ILLEGALITY, annotationMirror, message);
            return false;
        }
    }

    /**
     * 检查是否存在错误的枚举
     *
     * @param message
     * @param resultEnumNames
     *
     * @return
     *
     * @author nicky_chin
     */
    private boolean checkEnumNames(String message, Set<String> resultEnumNames) {
        if (resultEnumNames == null || resultEnumNames.size() == 0) {
            return false;
        }

        for (String enumName : resultEnumNames) {
            if (message.equals(enumName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 打印编译出错提示
     *
     * @param element
     * @param errorMessage
     * @param args
     *
     * @author nicky_chin
     */
    private void printError(Element element, String errorMessage, AnnotationMirror annotationMirror, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(errorMessage, args), element, annotationMirror);
    }
}
