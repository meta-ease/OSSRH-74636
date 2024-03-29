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
package io.github.meta.ease.generator.mybatis;

import io.github.meta.ease.generator.mybatis.annotation.DbType;
import io.github.meta.ease.generator.mybatis.config.DataSourceConfig;
import io.github.meta.ease.generator.mybatis.config.GlobalConfig;
import io.github.meta.ease.generator.mybatis.config.StrategyConfig;
import io.github.meta.ease.generator.mybatis.config.builder.ConfigBuilder;
import io.github.meta.ease.generator.mybatis.config.builder.Entity;
import io.github.meta.ease.generator.mybatis.config.po.TableField;
import io.github.meta.ease.generator.mybatis.config.po.TableInfo;
import io.github.meta.ease.generator.mybatis.config.querys.DecoratorDbQuery;
import io.github.meta.ease.generator.mybatis.config.querys.H2Query;
import io.github.meta.ease.generator.mybatis.config.rules.IColumnType;
import io.github.meta.ease.generator.mybatis.jdbc.DatabaseMetaDataWrapper;
import io.github.meta.ease.generator.mybatis.util.StringPool;
import io.github.meta.ease.generator.mybatis.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author nieqiurong 2021/1/6.
 * @since 3.5.0
 */
public abstract class IDatabaseQuery {

    protected final ConfigBuilder configBuilder;

    protected final DataSourceConfig dataSourceConfig;

    public IDatabaseQuery(@Nonnull ConfigBuilder configBuilder) {
        this.configBuilder = configBuilder;
        this.dataSourceConfig = configBuilder.getDataSourceConfig();
    }

    @Nonnull
    public ConfigBuilder getConfigBuilder() {
        return configBuilder;
    }

    @Nonnull
    public DataSourceConfig getDataSourceConfig() {
        return dataSourceConfig;
    }

    /**
     * 获取表信息
     *
     * @return 表信息
     */
    @Nonnull
    public abstract List<TableInfo> queryTables();

    /**
     * 后面切换到元数据获取表与字段会移除这里
     *
     * @author nieqiurong 2021/1/6.
     * @since 3.5.0
     */
    public static class DefaultDatabaseQuery extends IDatabaseQuery {

        private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDatabaseQuery.class);

        private final StrategyConfig strategyConfig;

        private final GlobalConfig globalConfig;

        private final DecoratorDbQuery dbQuery;

        public DefaultDatabaseQuery(@Nonnull ConfigBuilder configBuilder) {
            super(configBuilder);
            this.strategyConfig = configBuilder.getStrategyConfig();
            this.dbQuery = new DecoratorDbQuery(dataSourceConfig, strategyConfig);
            this.globalConfig = configBuilder.getGlobalConfig();
        }

        @Nonnull
        @Override
        public List<TableInfo> queryTables() {
            boolean isInclude = strategyConfig.getInclude().size() > 0;
            boolean isExclude = strategyConfig.getExclude().size() > 0;
            //所有的表信息
            List<TableInfo> tableList = new ArrayList<>();

            //需要反向生成或排除的表信息
            List<TableInfo> includeTableList = new ArrayList<>();
            List<TableInfo> excludeTableList = new ArrayList<>();
            try {
                dbQuery.query(dbQuery.tablesSql(), result -> {
                    String tableName = result.getStringResult(dbQuery.tableName());
                    if (StringUtils.isNotBlank(tableName)) {
                        TableInfo tableInfo = new TableInfo(this.configBuilder, tableName);
                        String tableComment = result.getTableComment();
                        // 跳过视图
                        if (!(strategyConfig.isSkipView() && "VIEW".equals(tableComment))) {
                            tableInfo.setComment(tableComment);
                            if (isInclude && strategyConfig.matchIncludeTable(tableName)) {
                                includeTableList.add(tableInfo);
                            } else if (isExclude && strategyConfig.matchExcludeTable(tableName)) {
                                excludeTableList.add(tableInfo);
                            }
                            tableList.add(tableInfo);
                        }
                    }
                });
                if (isExclude || isInclude) {
                    Map<String, String> notExistTables = new HashSet<>(isExclude ? strategyConfig.getExclude() : strategyConfig.getInclude())
                            .stream()
                            .filter(s -> !ConfigBuilder.matcherRegTable(s))
                            .collect(Collectors.toMap(String::toLowerCase, s -> s, (o, n) -> n));
                    // 将已经存在的表移除，获取配置中数据库不存在的表
                    for (TableInfo tabInfo : tableList) {
                        if (notExistTables.isEmpty()) {
                            break;
                        }
                        //解决可能大小写不敏感的情况导致无法移除掉
                        notExistTables.remove(tabInfo.getName().toLowerCase());
                    }
                    if (notExistTables.size() > 0) {
                        LOGGER.warn("表[{}]在数据库中不存在！！！", String.join(StringPool.COMMA, notExistTables.values()));
                    }
                    // 需要反向生成的表信息
                    if (isExclude) {
                        tableList.removeAll(excludeTableList);
                    } else {
                        tableList.clear();
                        tableList.addAll(includeTableList);
                    }
                }
                // 性能优化，只处理需执行表字段 github issues/219
                tableList.forEach(this::convertTableFields);
                return tableList;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                // 数据库操作完成,释放连接对象
                dbQuery.closeConnection();
            }
        }

        private void convertTableFields(@Nonnull TableInfo tableInfo) {
            DbType dbType = this.dataSourceConfig.getDbType();
            String tableName = tableInfo.getName();
            try {
                final Map<String, DatabaseMetaDataWrapper.ColumnsInfo> columnsMetaInfoMap = new HashMap<>();
                //TODO 增加元数据信息获取,后面查询表字段要改成这个.
                Map<String, DatabaseMetaDataWrapper.ColumnsInfo> columnsInfo =
                        new DatabaseMetaDataWrapper(dbQuery.getConnection()).getColumnsInfo(null, dataSourceConfig.getSchemaName(), tableName);
                if (columnsInfo != null && !columnsInfo.isEmpty()) {
                    columnsMetaInfoMap.putAll(columnsInfo);
                }
                String tableFieldsSql = dbQuery.tableFieldsSql(tableName);
                Set<String> h2PkColumns = new HashSet<>();
                if (DbType.H2 == dbType) {
                    dbQuery.query(String.format(H2Query.PK_QUERY_SQL, tableName), result -> {
                        String primaryKey = result.getStringResult(dbQuery.fieldKey());
                        if (Boolean.parseBoolean(primaryKey)) {
                            h2PkColumns.add(result.getStringResult(dbQuery.fieldName()));
                        }
                    });
                }
                Entity entity = strategyConfig.entity();
                dbQuery.query(tableFieldsSql, result -> {
                    String columnName = result.getStringResult(dbQuery.fieldName());
                    TableField field = new TableField(this.configBuilder, columnName);
                    // 避免多重主键设置，目前只取第一个找到ID，并放到list中的索引为0的位置
                    boolean isId = DbType.H2 == dbType ? h2PkColumns.contains(columnName) : result.isPrimaryKey();
                    // 处理ID
                    if (isId) {
                        field.primaryKey(dbQuery.isKeyIdentity(result.getResultSet()));
                        tableInfo.setHavePrimaryKey(true);
                        if (field.isKeyIdentityFlag() && entity.getIdType() != null) {
                            LOGGER.warn("当前表[{}]的主键为自增主键，会导致全局主键的ID类型设置失效!", tableName);
                        }
                    }
                    field.setColumnName(columnName)
                            .setType(result.getStringResult(dbQuery.fieldType()))
                            .setComment(result.getFiledComment())
                            .setCustomMap(dbQuery.getCustomFields(result.getResultSet()));
                    String propertyName = entity.getNameConvert().propertyNameConvert(field);
                    IColumnType columnType = dataSourceConfig.getTypeConvert().processTypeConvert(globalConfig, field);
                    field.setPropertyName(propertyName, columnType);
                    field.setMetaInfo(new TableField.MetaInfo(columnsMetaInfoMap.get(columnName.toLowerCase())));
                    tableInfo.addField(field);
                });
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            tableInfo.processTable();
        }
    }

}
