package com.nicky.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nicky.constants.ProcessorConstant;

import jodd.log.Logger;
import jodd.log.LoggerFactory;
import jodd.util.StringPool;

/**
 * @Description: 校验工具类
 * @date: 2019-03-25 11:55:41
 * @since JDK 1.8
 */
public class ProcessorUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorUtil.class);

    /**
     * 写入枚举名称的文件
     *
     * @param folderName
     * @param fileName
     * @param fileContent
     */
    public static void writeFile(String folderName, String fileName, String fileContent) {
        if (StringUtils.isAnyEmpty(folderName, fileName, fileContent)) {
            return;
        }
        File dir = new File(ProcessorConstant.ROOT_FOLDER_PATH + folderName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName + ProcessorConstant.FILE_EXT_TXT);
        if (file.exists()) {
            file.delete();
        }
        file = new File(dir, fileName + ProcessorConstant.FILE_EXT_TXT);
        try (FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(fileContent);
        } catch (Exception e) {
            LOGGER.error(() -> "write file error", e);
        }
    }

    /**
     * 读取文件内容，获取枚举名称
     *
     * @param folderName
     *
     * @return
     *
     * @author nicky_chin
     */
    public static Set<String> readFiles(String folderName) {
        if (StringUtils.isBlank(folderName)) {
            return null;
        }

        Set<String> enumNames = Sets.newHashSet();
        File dir = new File(ProcessorConstant.ROOT_FOLDER_PATH + folderName);
        File[] fileList = dir.listFiles();
        // 读取文件内容
        for (File file : fileList) {
            if (file.isFile()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String result = String.join("", br.lines().collect(Collectors.toList()));
                    enumNames.addAll(Splitter.on(StringPool.COMMA).trimResults().splitToList(result));
                } catch (Exception e) {
                    LOGGER.error(() -> "read file error", e);
                }
            }
        }

        return enumNames;
    }

    /**
     * 根据给定的全路径类名过滤出文件夹名称
     *
     * @author nicky_chin
     */
    public static String getFolderName(String qualifyName) {
        if (StringUtils.isBlank(qualifyName)) {
            return null;
        }
        List<String> folderNameList = Lists.newArrayList();
        String[] qualifyArr = qualifyName.split("\\.");
        for (String namePart : qualifyArr) {
            if (checkBaseName(namePart)) {
                folderNameList.add(namePart);
            } else {
                folderNameList.add(namePart);
                break;
            }
        }

        return Joiner.on(StringPool.DOT).join(folderNameList);
    }

    /**
     * 检查包名是否是基础名称
     *
     * @param name
     *
     * @return
     *
     * @author nicky_chin
     */
    private static boolean checkBaseName(String name) {
        return ProcessorConstant.COM_STR.equals(name);
    }
}