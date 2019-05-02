#!/usr/bin/env bash

#创建保存class文件的文件夹
if [ -d classes ]; then
    rm -rf classes;
fi
mkdir classes

#导入tools.jar，编译processor并输出
javac -cp $JAVA_HOME/lib/tools.jar com/nicky/lombok/*/ToString*  -d classes/

#编译App.java，并使用javac的-processor参数指定编译阶段的处理器ToStringBuilderProcessor
javac -cp classes -d classes -processor com.nicky.lombok.config.ToStringBuilderProcessor com/nicky/lombok/App.java

#用javap显示编译后的App.class文件(非必须，方便看结果)
javap -p classes classes/com/nicky/lombok/App.class

#执行测试类
java -cp classes com.nicky.lombok.App