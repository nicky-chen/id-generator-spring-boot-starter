/*
 * File Name:ToStringBuilderProcessor is created on 2019/4/21下午9:25 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.config;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;

import com.google.auto.service.AutoService;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.nicky.annotation.ToStringBuilder;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

/**
 * @author nicky_chin
 * @description:toStringBuilder校验器 https://www.cnblogs.com/haoerlv/p/7562486.html
 * https://www.jianshu.com/p/899063e8452e
 * @date: 2019/4/21 下午9:25
 * @since JDK 1.8
 */
//@AutoService(Processor.class)
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
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(ToStringBuilder.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ToStringBuilder.class);
        for (Element element : elements) {
            ToStringBuilder annotation = element.getAnnotation(ToStringBuilder.class);
            messager.printMessage(Diagnostic.Kind.WARNING, "1111");
            JCTree jcTree = trees.getTree(element);
            jcTree.accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                    List<JCTree.JCVariableDecl> jcVariableDeclList = List.nil();

                    for (JCTree tree : jcClassDecl.defs) {
                        if (tree.getKind().equals(Tree.Kind.VARIABLE)) {
                            JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl)tree;
                            jcVariableDeclList = jcVariableDeclList.append(jcVariableDecl);
                        }
                    }
                    jcVariableDeclList.forEach(jcVariableDecl -> {
                        //messager.printMessage(Diagnostic.Kind.ERROR, jcVariableDecl.getName() + " has been processed");
                        jcClassDecl.defs = jcClassDecl.defs.prepend(makeGetterMethodDecl(jcVariableDecl));
                    });
                    super.visitClassDef(jcClassDecl);
                }

            });

            //类和接口
            if (element instanceof TypeElement) {
                TypeElement type = (TypeElement)element;
                try {
                    type.getQualifiedName().toString();

                } catch (Exception e) {
                    messager.printMessage(Diagnostic.Kind.ERROR, Throwables.getStackTraceAsString(e));
                }

            }

            //方法
            if (element instanceof ExecutableElement) {

            }
            //变量x
            if (element instanceof VariableElement) {

            }
        }

        return true;
    }

    private JCTree.JCMethodDecl makeGetterMethodDecl(JCTree.JCVariableDecl jcVariableDecl) {
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        statements.append(
            treeMaker.Return(treeMaker.Select(treeMaker.Ident(names.fromString("this")), jcVariableDecl.getName())));
        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());

        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC), getNewMethodName(jcVariableDecl.getName()),
            jcVariableDecl.vartype, List.nil(), List.nil(),
            List.nil(), body, null);
    }

    private Name getNewMethodName(Name name) {
        String s = name.toString();
        String getMethod = "get" + s.substring(0, 1).toUpperCase() + s.substring(1, name.length());
        messager.printMessage(Diagnostic.Kind.ERROR, getMethod + " has been processed");
        return names.fromString(getMethod);
    }

    //判断元素是否为public
    public boolean isPublic(Element e) {
        //获取元素的修饰符Modifier,注意此处的Modifier
        //非java.lang.reflect.Modifier
        Set<Modifier> modifiers = e.getModifiers();
        for (Modifier m : modifiers) {
            if (m.equals(Modifier.PUBLIC)) {
                return true;
            }
        }
        return false;
    }

}
