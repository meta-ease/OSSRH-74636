package com.open.cloud.test.web.mybatis;

import com.github.pagehelper.Dialect;
import com.github.pagehelper.page.PageAutoDialect;
import com.github.pagehelper.page.PageBoundSqlInterceptors;
import com.github.pagehelper.page.PageMethod;
import com.github.pagehelper.page.PageParams;
import com.github.pagehelper.parser.CountSqlParser;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Properties;

/**
 * @author leijian
 * @version 1.0
 * @date 2021/10/12 23:24
 */
public class MybatisPageHelper extends PageMethod implements Dialect {
    private PageParams pageParams;
    private PageAutoDialect autoDialect;
    private PageBoundSqlInterceptors pageBoundSqlInterceptors;


    @Override
    public boolean skip(MappedStatement ms, Object parameterObject, RowBounds rowBounds) {
        autoDialect.initDelegateDialect(ms, null);
        return false;
    }

    @Override
    public boolean beforeCount(MappedStatement ms, Object parameterObject, RowBounds rowBounds) {
        return autoDialect.getDelegate().beforeCount(ms, parameterObject, rowBounds);
    }

    @Override
    public String getCountSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds, CacheKey countKey) {
        return autoDialect.getDelegate().getCountSql(ms, boundSql, parameterObject, rowBounds, countKey);
    }

    @Override
    public boolean afterCount(long count, Object parameterObject, RowBounds rowBounds) {
        return false;
    }

    @Override
    public Object processParameterObject(MappedStatement ms, Object parameterObject, BoundSql boundSql, CacheKey pageKey) {
        return null;
    }

    @Override
    public boolean beforePage(MappedStatement ms, Object parameterObject, RowBounds rowBounds) {
        return false;
    }

    @Override
    public String getPageSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds, CacheKey pageKey) {
        return null;
    }

    @Override
    public Object afterPage(List pageList, Object parameterObject, RowBounds rowBounds) {
        return null;
    }

    @Override
    public void afterAll() {

    }

    @Override
    public void setProperties(Properties properties) {
        setStaticProperties(properties);
        pageParams = new PageParams();
        autoDialect = new PageAutoDialect();
        pageBoundSqlInterceptors = new PageBoundSqlInterceptors();
        pageParams.setProperties(properties);
        autoDialect.setProperties(properties);
        pageBoundSqlInterceptors.setProperties(properties);
        //20180902新增 aggregateFunctions, 允许手动添加聚合函数（影响行数）
        CountSqlParser.addAggregateFunctions(properties.getProperty("aggregateFunctions"));
    }
}