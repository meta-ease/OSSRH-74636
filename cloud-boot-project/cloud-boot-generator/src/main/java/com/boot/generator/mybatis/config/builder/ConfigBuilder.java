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
package com.boot.generator.mybatis.config.builder;

import com.boot.generator.mybatis.IDatabaseQuery;
import com.boot.generator.mybatis.config.*;
import com.boot.generator.mybatis.config.po.TableInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 配置汇总 传递给文件生成工具
 *
 * @author YangHu, tangguo, hubin, Juzi
 * @since 2016-08-30
 */
public class ConfigBuilder {

    /**
     * 模板路径配置信息
     */
    private final TemplateConfig templateConfig;

    /**
     * 数据库表信息
     */
    private final List<TableInfo> tableInfoList = new ArrayList<>();

    /**
     * 路径配置信息
     */
    private final Map<OutputFile, String> pathInfo = new HashMap<>();

    /**
     * 策略配置
     */
    private StrategyConfig strategyConfig;

    /**
     * 全局配置信息
     */
    private GlobalConfig globalConfig;

    /**
     * 注入配置信息
     */
    private InjectionConfig injectionConfig;

    /**
     * 过滤正则
     */
    private static final Pattern REGX = Pattern
            .compile("[~!/@#$%^&*()+\\\\\\[\\]|{};:'\",<.>?]+");

    /**
     * 包配置信息
     */
    private final PackageConfig packageConfig;

    /**
     * 数据库配置信息
     */
    private final DataSourceConfig dataSourceConfig;

    /**
     * 在构造器中处理配置
     *
     * @param packageConfig    包配置
     * @param dataSourceConfig 数据源配置
     * @param strategyConfig   表配置
     * @param templateConfig   模板配置
     * @param globalConfig     全局配置
     */
    public ConfigBuilder(@Nullable PackageConfig packageConfig, @Nonnull DataSourceConfig dataSourceConfig,
                         @Nullable StrategyConfig strategyConfig, @Nullable TemplateConfig templateConfig,
                         @Nullable GlobalConfig globalConfig, @Nullable InjectionConfig injectionConfig) {
        this.dataSourceConfig = dataSourceConfig;
        this.strategyConfig = Optional.ofNullable(strategyConfig).orElseGet(() -> GeneratorBuilder.strategyConfig());
        this.globalConfig = Optional.ofNullable(globalConfig).orElseGet(() -> GeneratorBuilder.globalConfig());
        this.templateConfig = Optional.ofNullable(templateConfig).orElseGet(() -> GeneratorBuilder.templateConfig());
        this.packageConfig = Optional.ofNullable(packageConfig).orElseGet(() -> GeneratorBuilder.packageConfig());
        this.injectionConfig = Optional.ofNullable(injectionConfig).orElseGet(() -> GeneratorBuilder.injectionConfig());
        this.pathInfo.putAll(new PathInfoHandler(this.globalConfig, this.templateConfig, this.packageConfig).getPathInfo());
    }

    /**
     * 判断表名是否为正则表名(这表名规范比较随意,只能尽量匹配上特殊符号)
     *
     * @param tableName 表名
     * @return 是否正则
     * @since 3.5.0
     */
    public static boolean matcherRegTable(@Nonnull String tableName) {
        return REGX.matcher(tableName).find();
    }

    @Nonnull
    public ConfigBuilder setStrategyConfig(@Nonnull StrategyConfig strategyConfig) {
        this.strategyConfig = strategyConfig;
        return this;
    }

    @Nonnull
    public ConfigBuilder setGlobalConfig(@Nonnull GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
        return this;
    }

    @Nonnull
    public ConfigBuilder setInjectionConfig(@Nonnull InjectionConfig injectionConfig) {
        this.injectionConfig = injectionConfig;
        return this;
    }

    @Nonnull
    public TemplateConfig getTemplateConfig() {
        return templateConfig;
    }

    @Nonnull
    public List<TableInfo> getTableInfoList() {
        if (tableInfoList.isEmpty()) {
            // TODO 暂时不开放自定义
            List<TableInfo> tableInfos = new IDatabaseQuery.DefaultDatabaseQuery(this)
                    .queryTables();
            if (!tableInfos.isEmpty()) {
                this.tableInfoList.addAll(tableInfos);
            }
        }
        return tableInfoList;
    }

    @Nonnull
    public Map<OutputFile, String> getPathInfo() {
        return pathInfo;
    }

    @Nonnull
    public StrategyConfig getStrategyConfig() {
        return strategyConfig;
    }

    @Nonnull
    public GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    @Nullable
    public InjectionConfig getInjectionConfig() {
        return injectionConfig;
    }

    @Nonnull
    public PackageConfig getPackageConfig() {
        return packageConfig;
    }

    @Nonnull
    public DataSourceConfig getDataSourceConfig() {
        return dataSourceConfig;
    }
}
