/*
 * File Name:ToStringBuilderProcessor is created on 2019/4/21下午9:25 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.lombok.config;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.google.auto.service.AutoService;
import com.google.common.base.CaseFormat;
import com.nicky.lombok.annotation.ToStringBuilder;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

/**
 * @author nicky_chin
 * @description:toStringBuilder校验器 AST语法参考 <href = "https://blog.csdn.net/a_zhenzhen/article/details/86065063#JCTree%E7%9A%84%E4%BB%8B%E7%BB%8D"/>
 * 用法参考<href = "https://blog.mythsman.com/2017/12/19/1/"></href>
 * @date: 2019/4/21 下午9:25
 * @since JDK 1.8
 */
@SupportedAnnotationTypes({"com.nicky.lombok.annotation.ToStringBuilder"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class ToStringBuilderProcessor extends AbstractProcessor {

    /**
     * 抽象语法树
     */
    private JavacTrees trees;

    /**
     * AST
     */
    private TreeMaker treeMaker;

    /**
     * 标识符
     */
    private Names names;

    /**
     * 日志处理
     */
    private Messager messager;

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment)processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> annotation = roundEnv.getElementsAnnotatedWith(ToStringBuilder.class);
        annotation.stream().map(element -> trees.getTree(element)).forEach(tree -> tree.accept(new TreeTranslator() {
            @Override
            public void visitClassDef(JCClassDecl jcClass) {
                //过滤属性
                Map<Name, JCVariableDecl> treeMap =
                    jcClass.defs.stream().filter(k -> k.getKind().equals(Tree.Kind.VARIABLE))
                        .map(tree -> (JCVariableDecl)tree)
                        .collect(Collectors.toMap(JCVariableDecl::getName, Function.identity()));
                //处理变量
                treeMap.forEach((k, jcVariable) -> {
                    messager.printMessage(Diagnostic.Kind.NOTE, String.format("fields:%s", k));
                    try {
                        //增加get方法
                        jcClass.defs = jcClass.defs.prepend(generateGetterMethod(jcVariable));
                        //增加set方法
                        jcClass.defs = jcClass.defs.prepend(generateSetterMethod(jcVariable));
                    } catch (Exception e) {
                        messager.printMessage(Diagnostic.Kind.ERROR, e.getLocalizedMessage());
                    }
                });
                //增加toString方法
                jcClass.defs = jcClass.defs.prepend(generateToStringBuilderMethod());
                super.visitClassDef(jcClass);
            }

            @Override
            public void visitMethodDef(JCMethodDecl jcMethod) {
                //打印所有方法
                messager.printMessage(Diagnostic.Kind.NOTE, jcMethod.toString());
                //修改方法
                if ("getTest".equals(jcMethod.getName().toString())) {
                    result = treeMaker
                        .MethodDef(jcMethod.getModifiers(), getNameFromString("testMethod"), jcMethod.restype,
                            jcMethod.getTypeParameters(), jcMethod.getParameters(), jcMethod.getThrows(),
                            jcMethod.getBody(), jcMethod.defaultValue);
                }
                super.visitMethodDef(jcMethod);
            }
        }));
        return true;
    }

    /**
     * 生成get方法
     *
     * @param jcVariable
     *
     * @return
     */
    private JCMethodDecl generateGetterMethod(JCVariableDecl jcVariable) {

        //修改方法级别
        JCModifiers jcModifiers = treeMaker.Modifiers(Flags.PUBLIC);

        //添加方法名称
        Name methodName = handleMethodSignature(jcVariable.getName(), "get");

        //添加方法内容
        ListBuffer<JCStatement> jcStatements = new ListBuffer<>();
        jcStatements.append(
            treeMaker.Return(treeMaker.Select(treeMaker.Ident(getNameFromString("this")), jcVariable.getName())));
        JCBlock jcBlock = treeMaker.Block(0, jcStatements.toList());

        //添加返回值类型
        JCExpression returnType = jcVariable.vartype;

        //参数类型
        List<JCTypeParameter> typeParameters = List.nil();

        //参数变量
        List<JCVariableDecl> parameters = List.nil();

        //声明异常
        List<JCExpression> throwsClauses = List.nil();
        //构建方法
        return treeMaker
            .MethodDef(jcModifiers, methodName, returnType, typeParameters, parameters, throwsClauses, jcBlock, null);
    }

    /**
     * 生成toString方法
     *
     * @return
     */
    private JCMethodDecl generateToStringBuilderMethod() {

        //修改方法级别
        JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);

        //添加方法名称
        Name methodName = getNameFromString("toString");

        //设置调用方法函数类型和调用函数
        JCExpressionStatement statement = treeMaker.Exec(treeMaker.Apply(List.of(memberAccess("java.lang.Object")),
            memberAccess("com.nicky.lombok.adapter.AdapterFactory.builderStyleAdapter"),
            List.of(treeMaker.Ident(getNameFromString("this")))));
        ListBuffer<JCStatement> jcStatements = new ListBuffer<>();
        jcStatements.append(treeMaker.Return(statement.getExpression()));
        //设置方法体
        JCBlock jcBlock = treeMaker.Block(0, jcStatements.toList());

        //添加返回值类型
        JCExpression returnType = memberAccess("java.lang.String");

        //参数类型
        List<JCTypeParameter> typeParameters = List.nil();

        //参数变量
        List<JCVariableDecl> parameters = List.nil();

        //声明异常
        List<JCExpression> throwsClauses = List.nil();

        return treeMaker
            .MethodDef(modifiers, methodName, returnType, typeParameters, parameters, throwsClauses, jcBlock, null);
    }

    /**
     * 创建set方法
     *
     * @param jcVariable
     *
     * @return
     */
    private JCMethodDecl generateSetterMethod(JCVariableDecl jcVariable) throws ReflectiveOperationException {

        //修改方法级别
        JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);

        //添加方法名称
        Name variableName = jcVariable.getName();
        Name methodName = handleMethodSignature(variableName, "set");

        //设置方法体
        ListBuffer<JCStatement> jcStatements = new ListBuffer<>();
        jcStatements.append(treeMaker.Exec(treeMaker
            .Assign(treeMaker.Select(treeMaker.Ident(getNameFromString("this")), variableName),
                treeMaker.Ident(variableName))));
        //定义方法体
        JCBlock jcBlock = treeMaker.Block(0, jcStatements.toList());

        //添加返回值类型
        JCExpression returnType =
            treeMaker.Type((Type)(Class.forName("com.sun.tools.javac.code.Type$JCVoidType").newInstance()));

        List<JCTypeParameter> typeParameters = List.nil();

        //定义参数
        JCVariableDecl variableDecl = treeMaker
            .VarDef(treeMaker.Modifiers(Flags.PARAMETER, List.nil()), jcVariable.name, jcVariable.vartype, null);
        List<JCVariableDecl> parameters = List.of(variableDecl);

        //声明异常
        List<JCExpression> throwsClauses = List.nil();
        return treeMaker
            .MethodDef(modifiers, methodName, returnType, typeParameters, parameters, throwsClauses, jcBlock, null);

    }

    private JCExpression memberAccess(String components) {
        String[] componentArray = components.split("\\.");
        JCExpression expr = treeMaker.Ident(getNameFromString(componentArray[0]));
        for (int i = 1; i < componentArray.length; i++) {
            expr = treeMaker.Select(expr, getNameFromString(componentArray[i]));
        }
        return expr;
    }

    private Name handleMethodSignature(Name name, String prefix) {
        return names.fromString(prefix + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name.toString()));
    }

    private Name getNameFromString(String s) {
        return names.fromString(s);
    }

}
