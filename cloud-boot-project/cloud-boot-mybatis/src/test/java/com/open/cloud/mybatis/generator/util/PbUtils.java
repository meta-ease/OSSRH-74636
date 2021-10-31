/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.open.cloud.mybatis.generator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PbUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PbUtils.class);

    /*
     * 表名转换为驼峰命名
     */
    public static String convertToCamelCase(String str) {
        String result = "";
        if (str == null) {
            return "";
        }
        String[] strArr = str.trim().split("_");
        for (String s : strArr) {
            if (s.length() > 1) {
                result += s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
            } else {
                result += s.toUpperCase();
            }
        }

        return result;
    }

    /*
     * 表名转换为首字母小写的驼峰命名
     */
    public static String convertToFirstLetterLowerCaseCamelCase(String str) {
        String resultCamelCase = convertToCamelCase(str);

        String result = "";
        if (resultCamelCase.length() > 1) {
            result = resultCamelCase.substring(0, 1).toLowerCase() + resultCamelCase.substring(1);
        } else {
            result = resultCamelCase.toLowerCase();
        }

        return result;
    }

    /**
     * 日期转换成字符串
     *
     * @return str
     */
    public static String DateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转换成日期
     *
     * @return date
     */
    public static Date StrToDate(String str) {
        // SimpleDateFormat format = new
        // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            LOGGER.error("StrToDate Failure;[{}]", e);
        }
        return date;
    }

    /**
     * 字符串转换成日期 yyyy-MM-dd HH:mm:ss
     *
     * @return date
     */
    public static Date StrToDateTime(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            LOGGER.error("StrToDateTime Failure;[{}]", e);
        }
        return date;
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(Object str) {

        if (null == str) {
            return true;
        } else if (str instanceof String) {
            return str == null || "".equals(((String) str).trim());
        } else {
            return false;
        }
    }

    /**
     * 获取当前时间
     *
     * @return yyyyMMddHHmmss
     */
    public static synchronized String getCurrentTime() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return yyyyMMddHHmmss
     */
    public static synchronized String getCurrenForMilli() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static synchronized String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return yyyy-MM-dd
     */
    public static synchronized String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return yyyyMMddhhmmssSSSSSS
     */
    public static synchronized String getMillisecondTime() {
        return new SimpleDateFormat("yyyyMMddhhmmssSSSSSS").format(new Date());
    }

    /**
     * 根据指定格式返回当前时间
     */
    public static String getCurrentTime(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * 替换掉下划线并让紧跟它后面的字母大写,例如 ad_code 转成 adCode
     */
    public static String strRelplacetoLowerCase(String str) {
        StringBuffer sb = new StringBuffer();
        sb.append(str.toLowerCase());

        int count = sb.indexOf("_");
        while (count != 0) {
            int num = sb.indexOf("_", count);
            count = num + 1;
            if (num != -1) {
                char ia;
                if (Character.isDigit(sb.charAt(count))) {
                    ia = sb.charAt(count);
                } else {
                    char ss = sb.charAt(count);
                    ia = (char) (ss - 32);
                }
                sb.replace(count, count + 1, ia + "");
            }
        }
        return sb.toString().replaceAll("_", "");
    }

    /**
     * 返回首字母大写的字符串
     */
    public static String fristStrToUpperCase(String str) {
        String resultStr = str.substring(0, 1).toUpperCase() + str.substring(1);
        return resultStr;
    }

    /**
     * 返回首字母和第二个字母小写的字符串
     */
    public static String fristAndSecondStrToLowerCase(String str) {
        String resultStr = str.substring(0, 1).toLowerCase() + str.substring(1, 2).toLowerCase()
                + str.substring(2);
        return resultStr;
    }

    /**
     * 返回首字母小写的字符串
     */
    public static String fristStrToLowerCase(String str) {
        String resultStr = str.substring(0, 1).toLowerCase() + str.substring(1);
        return resultStr;
    }

    /**
     * String 转化 Date
     *
     * @param dateString YYYY-MM-DD HH:MI:SS
     */
    public static Date getDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            LOGGER.error("getDate Failure;[{}]", e);
        }
        return date;
    }

    /**
     * po中的列类型 JDBC Type           Java Type CHAR                String VARCHAR             String LONGVARCHAR         String NUMERIC             java.math.BigDecimal DECIMAL
     * java.math.BigDecimal BIT                 boolean BOOLEAN             boolean TINYINT             byte SMALLINT            short INTEGER             INTEGER BIGINT
     * long REAL                float FLOAT               double DOUBLE              double BINARY              byte[] VARBINARY           byte[] LONGVARBINARY       byte[] DATE
     * java.sql.Date TIME                java.sql.Time TIMESTAMP           java.sql.Timestamp CLOB                Clob BLOB                Blob ARRAY
     * Array DISTINCT            mapping of underlying type STRUCT              Struct REF                 Ref DATALINK            java.net.URL
     */

    public static String convertJavaType(String type, String dataScale) {
        String javaType = "String";
        type = type.toUpperCase();
        if (type.contains("CHAR") || type.contains("VARCHAR") || type.contains("LONGVARCHAR")) {
            javaType = "String";
        } else if (type.contains("DECIMAL") || type.contains("NUMERIC")) {
            if (dataScale.equals("0")) {
                javaType = "Long";
            } else {
                javaType = "java.math.BigDecimal";
            }
        } else if (type.contains("NUMBER")) {
            if (dataScale.equals("0")) {
                javaType = "Long";
            } else {
                javaType = "java.math.BigDecimal";
            }
        } else if (type.contains("BIT") || type.contains("BOOLEAN")) {
            javaType = "boolean";
        } else if (type.contains("TINYINT")) {
            javaType = "byte";
        } else if (type.contains("SMALLINT")) {
            javaType = "short";
        } else if (type.contains("INTEGER")) {
            javaType = "Integer";
        } else if (type.contains("BIGINT")) {
            javaType = "Long";
        } else if (type.contains("REAL")) {
            javaType = "float";
        } else if (type.contains("DOUBLE") || type.contains("FLOAT")) {
            javaType = "double";
        } else if (type.contains("BINARY") || type.contains("VARBINARY")
                || type.contains("LONGVARBINARY")) {
            javaType = "byte[]";
        } else if (type.contains("DATE")) {
            javaType = "java.util.Date";
        } else if (type.equals("TIMESTAMP")) {
            javaType = "java.sql.Timestamp";
        } else if (type.equals("TIME")) {
            javaType = "java.sql.Time";
        } else if (type.contains("INT")) {
            javaType = "Integer";
        }
        return javaType;
    }

    public static String convertJdbcType(String type) {
        type = type.toUpperCase();
        if (type.contains("CHAR") || type.contains("VARCHAR") || type.contains("LONGVARCHAR")) {
            type = "VARCHAR";
        } else if (type.contains("DECIMAL") || type.contains("NUMERIC")) {
            type = "DECIMAL";
        } else if (type.contains("TINYINT")) {
            type = "TINYINT";
        } else if (type.contains("SMALLINT")) {
            type = "SMALLINT";
        } else if (type.contains("INTEGER") || type.contains("INT")) {
            type = "INTEGER";
        } else if (type.contains("BIGINT")) {
            type = "BIGINT";
        } else if (type.contains("REAL")) {
            type = "REAL";
        } else if (type.contains("DOUBLE") || type.contains("FLOAT")) {
            type = "NUMERIC";
        } else if (type.contains("BINARY")) {
            type = "BINARY";
        } else if (type.contains("DATE")) {
            type = "DATE";
        } else if (type.equals("TIME")) {
            type = "TIME";
        } else if (type.equals("TIMESTAMP")) {
            type = "TIMESTAMP";
        } else if (type.contains("LONG")) {
            type = "BIGINT";
        } else if (type.contains("NUMBER")) {
            type = "NUMERIC";
        } else {
            type = "OTHER";
        }
        return type.toUpperCase();
    }

    /**
     * public static String convertJdbcType(String type) { String jdbcType = type; if (type.contains("interval") || type.contains("blob") || type.contains("varchar") ||
     * type.contains("char")) { jdbcType = "VARCHAR"; } else if (type.contains("long")) { jdbcType = "BIGINT"; }else if (type.contains("decimal") || type.contains("float") ||
     * type.contains("double") || type.contains("integer") || type.contains("longtext")) { jdbcType = "NUMERIC"; }else if (type.contains("date") || type.contains("datetime")) {
     * jdbcType = "TIMESTAMP"; } else if (type.contains("int")) { jdbcType = "INTEGER"; } return jdbcType; }
     */

    /*
     * 将数据库的数据类型转换为java的数据类型
     */
    public static String convertType(String databaseType) {
        String javaType = "";
        String databaseTypeStr = databaseType.trim().toLowerCase();
        if (databaseTypeStr.startsWith("int") || "smallint".equals(databaseTypeStr)
                || "tinyint".equals(databaseTypeStr)) {
            javaType = "Integer";
        } else if ("char".equals(databaseTypeStr)) {
            javaType = "String";
        } else if ("number".equals(databaseTypeStr) || "numeric".equals(databaseTypeStr)) {
            javaType = "Integer";
        } else if (databaseTypeStr.indexOf("varchar") != -1) {
            javaType = "String";
        } else if ("blob".equals(databaseTypeStr)) {
            javaType = "Byte[]";
        } else if ("float".equals(databaseTypeStr)) {
            javaType = "Float";
        } else if ("double".equals(databaseTypeStr)) {
            javaType = "Double";
        } else if ("decimal".equals(databaseTypeStr)) {
            javaType = "java.math.BigDecimal";
        } else if (databaseTypeStr.startsWith("bigint")) {
            javaType = "Long";
        } else if ("date".equals(databaseTypeStr)) {
            javaType = "java.util.Date";
        } else if ("time".equals(databaseTypeStr)) {
            javaType = "java.util.Date";
        } else if ("datetime".equals(databaseTypeStr)) {
            javaType = "java.util.Date";
        } else if (databaseTypeStr.startsWith("timestamp")) {
            javaType = "java.util.Date";
        } else if ("year".equals(databaseTypeStr)) {
            javaType = "java.util.Date";
        } else {
            javaType = "String";
        }
        return javaType;
    }

}
