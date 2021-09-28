package com.open.cloud.mybatis.generator.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
@Mapper
public interface CodeGeneratorMapper {

    /**
     * 获取列数据
     */
    @Select(" SELECT DISTINCT\n" +
            "            t.comments\n" +
            "            FROM\n" +
            "            user_tab_comments t\n" +
            "            INNER JOIN USER_CONS_COLUMNS A ON A.TABLE_NAME = T.TABLE_NAME\n" +
            "            AND A.OWNER=#{dbName}\n" +
            "            WHERE\n" +
            "            t.TABLE_NAME=#{tableName}")
    public List<Map> getListMap(Map<String, Object> params);

    /**
     * 获取所有的表名称和注释
     */
    @Select(" SELECT DISTINCT\n" +
            "            t.TABLE_NAME,\n" +
            "            t.comments TABLE_COMMENT\n" +
            "            FROM\n" +
            "            user_tab_comments t\n" +
            "            INNER JOIN USER_CONS_COLUMNS A ON A.TABLE_NAME = T.TABLE_NAME\n" +
            "            AND A.OWNER = #{dbName}\n" +
            "            WHERE T.TABLE_NAME NOT LIKE 'ACT%'\n" +
            "            ORDER BY TABLE_NAME")
    public List<Map<String, Object>> getTablesList(Map<String, Object> params);

    /**
     * 获取指定表的注释
     */
    @Select("  SELECT DISTINCT\n" +
            "            t.column_name COLUMNNAME,\n" +
            "            t.data_type COLUMNTYPE,\n" +
            "            t.DATA_SCALE,\n" +
            "            a.COMMENTS COLUMNCOMMENT\n" +
            "            FROM\n" +
            "            user_tab_columns t\n" +
            "            INNER JOIN USER_COL_COMMENTS a ON t.table_name = a.table_NAME\n" +
            "            AND t.COLUMN_NAME = a.COLUMN_NAME\n" +
            "            INNER JOIN USER_CONS_COLUMNS C ON C.TABLE_NAME = T.TABLE_NAME\n" +
            "            WHERE\n" +
            "            C.OWNER = #{dbName}\n" +
            "            AND T.TABLE_NAME = #{tableName}")
    public String getTableComment(Map<String, Object> params);

    /**
     * 获取指定表的主键
     */
    @Select(" SELECT\n" +
            "            A.COLUMN_NAME\n" +
            "            FROM\n" +
            "            USER_CONS_COLUMNS A\n" +
            "            INNER JOIN USER_CONSTRAINTS B ON A.CONSTRAINT_NAME = B.CONSTRAINT_NAME\n" +
            "            AND B.CONSTRAINT_TYPE = 'P'\n" +
            "            WHERE\n" +
            "            A.TABLE_NAME = #{tableName}\n" +
            "            AND A.OWNER = #{dbName}")
    public List<String> getTableKeys(Map<String, Object> params);


}