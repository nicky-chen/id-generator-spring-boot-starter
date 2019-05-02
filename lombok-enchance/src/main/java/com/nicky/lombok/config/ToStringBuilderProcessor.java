/*
 * File Name:ToStringBuilderProcessor is created on 2019/4/21下午9:25 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.lombok.config;

import java.util.Optional;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.nicky.lombok.annotation.ToStringBuilder;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
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
 * @description:toStringBuilder校验器
 * https://blog.csdn.net/a_zhenzhen/article/details/86065063#JCTree%E7%9A%84%E4%BB%8B%E7%BB%8D
 * @date: 2019/4/21 下午9:25
 * @since JDK 1.8
 */
@SupportedAnnotationTypes({"com.nicky.lombok.annotation.ToStringBuilder"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
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
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> annotation = roundEnv.getElementsAnnotatedWith(ToStringBuilder.class);
        annotation.stream().map(element -> trees.getTree(element))
            .forEach(t -> t.accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {

                    Optional<JCTree.JCVariableDecl> first =
                        jcClassDecl.defs.stream().filter(k -> k.getKind().equals(Tree.Kind.VARIABLE))
                            .map(tree -> (JCTree.JCVariableDecl)tree).findFirst();

                    jcClassDecl.defs.stream().filter(k -> k.getKind().equals(Tree.Kind.VARIABLE))
                        .map(tree -> (JCTree.JCVariableDecl)tree).forEach(jcVariableDecl -> {
                        //添加get方法
                        messager.printMessage(Diagnostic.Kind.NOTE, jcVariableDecl.getName() + " has been processed");
                        jcClassDecl.defs = jcClassDecl.defs.prepend(makeGetterMethodDecl(jcVariableDecl));
                        //添加set方法
                        jcClassDecl.defs = jcClassDecl.defs.prepend(makeSetterMethodDecl(jcVariableDecl));
                    });
                    //tostring
                    first.ifPresent(x -> jcClassDecl.defs = jcClassDecl.defs.prepend(makeToStringBuilderMethod(x)));
                    super.visitClassDef(jcClassDecl);
                }

                @Override
                public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
                    messager.printMessage(Diagnostic.Kind.NOTE, jcMethodDecl.toString());

                    if (jcMethodDecl.getName().toString().equals("getUserName")) {
                        JCTree.JCMethodDecl methodDecl = treeMaker
                            .MethodDef(jcMethodDecl.getModifiers(), names.fromString("testMethod"),
                                jcMethodDecl.restype, jcMethodDecl.getTypeParameters(), jcMethodDecl.getParameters(),
                                jcMethodDecl.getThrows(), jcMethodDecl.getBody(), jcMethodDecl.defaultValue);
                        result = methodDecl;
                    }

                    super.visitMethodDef(jcMethodDecl);
                }
            }));
        return true;
    }

    /**
     * 创建get方法
     *
     * @param jcVariableDecl
     *
     * @return
     */
    private JCTree.JCMethodDecl makeGetterMethodDecl(JCTree.JCVariableDecl jcVariableDecl) {
        //方法的访问级别
        JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);
        //方法名称
        Name methodName = getMethodName(jcVariableDecl.getName());
        //设置返回值类型
        JCTree.JCExpression returnMethodType = jcVariableDecl.vartype;
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        statements.append(
            treeMaker.Return(treeMaker.Select(treeMaker.Ident(names.fromString("this")), jcVariableDecl.getName())));
        //设置方法体
        JCTree.JCBlock methodBody = treeMaker.Block(0, statements.toList());
        List<JCTree.JCTypeParameter> methodGenericParams = List.nil();
        List<JCTree.JCVariableDecl> parameters = List.nil();
        List<JCTree.JCExpression> throwsClauses = List.nil();
        //构建方法
        return treeMaker
            .MethodDef(modifiers, methodName, returnMethodType, methodGenericParams, parameters, throwsClauses,
                methodBody, null);
    }

    private JCTree.JCMethodDecl makeToStringBuilderMethod(JCTree.JCVariableDecl jcVariableDecl) {

        //方法的访问级别
        JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);
        //方法名称
        Name methodName = getString(jcVariableDecl.getName());

        //设置返回值类型
        JCTree.JCExpression returnMethodType = memberAccess("java.lang.String");

        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();


        JCTree.JCExpressionStatement printVar = treeMaker.Exec(treeMaker.Apply(
            List.of(memberAccess("java.lang.String")),//参数类型
            memberAccess("java.util.Objects.toString"),
            List.of(treeMaker.Ident(getNameFromString("this")))
            )
        );
        JCTree.JCExpression expression = treeMaker.Exec(treeMaker
            .Binary(JCTree.Tag.PLUS, treeMaker.Literal("-Binary operator one"),
                treeMaker.Literal("-Binary operator two"))).getExpression();

        statements.append(
            treeMaker.Return(printVar.getExpression()));
        //设置方法体
        JCTree.JCBlock methodBody = treeMaker.Block(0, statements.toList());


        List<JCTree.JCTypeParameter> methodGenericParams = List.nil();
        List<JCTree.JCVariableDecl> parameters = List.nil();
        List<JCTree.JCExpression> throwsClauses = List.nil();
        return treeMaker
            .MethodDef(modifiers, methodName, returnMethodType, methodGenericParams, parameters, throwsClauses,
                methodBody, null);
    }

    private JCTree.JCExpression memberAccess(String components) {
        String[] componentArray = components.split("\\.");
        JCTree.JCExpression expr = treeMaker.Ident(getNameFromString(componentArray[0]));
        for (int i = 1; i < componentArray.length; i++) {
            expr = treeMaker.Select(expr, getNameFromString(componentArray[i]));
        }
        return expr;
    }

    private Name getNameFromString(String s) {
        return names.fromString(s);
    }

    /**
     * 创建set方法
     *
     * @param jcVariableDecl
     *
     * @return
     */
    private JCTree.JCMethodDecl makeSetterMethodDecl(JCTree.JCVariableDecl jcVariableDecl) {
        try {
            //方法的访问级别
            JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);
            //定义方法名
            Name methodName = setMethodName(jcVariableDecl.getName());
            //定义返回值类型
            JCTree.JCExpression returnMethodType =
                treeMaker.Type((Type)(Class.forName("com.sun.tools.javac.code.Type$JCVoidType").newInstance()));
            ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
            statements.append(treeMaker.Exec(treeMaker
                .Assign(treeMaker.Select(treeMaker.Ident(names.fromString("this")), jcVariableDecl.getName()),
                    treeMaker.Ident(jcVariableDecl.getName()))));
            //定义方法体
            JCTree.JCBlock methodBody = treeMaker.Block(0, statements.toList());
            List<JCTree.JCTypeParameter> methodGenericParams = List.nil();
            //定义入参
            JCTree.JCVariableDecl param = treeMaker
                .VarDef(treeMaker.Modifiers(Flags.PARAMETER, List.nil()), jcVariableDecl.name, jcVariableDecl.vartype,
                    null);
            //设置入参
            List<JCTree.JCVariableDecl> parameters = List.of(param);
            List<JCTree.JCExpression> throwsClauses = List.nil();
            //构建新方法
            return treeMaker
                .MethodDef(modifiers, methodName, returnMethodType, methodGenericParams, parameters, throwsClauses,
                    methodBody, null);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;

    }

    private Name getMethodName(Name name) {
        String s = name.toString();
        return names.fromString("get" + s.substring(0, 1).toUpperCase() + s.substring(1, name.length()));
    }

    private Name getString(Name name) {
        return names.fromString("toString");
    }

    private Name setMethodName(Name name) {
        String s = name.toString();
        return names.fromString("set" + s.substring(0, 1).toUpperCase() + s.substring(1, name.length()));
    }

}
