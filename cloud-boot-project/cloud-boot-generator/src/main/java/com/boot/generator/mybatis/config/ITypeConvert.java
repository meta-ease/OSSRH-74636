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
package com.boot.generator.mybatis.config;

import com.boot.generator.mybatis.config.po.TableField;
import com.boot.generator.mybatis.config.rules.IColumnType;

import javax.annotation.Nonnull;

/**
 * 数据库字段类型转换
 *
 * @author hubin
 * @author hanchunlin
 * @since 2017-01-20
 */
public interface ITypeConvert {

    /**
     * 执行类型转换
     *
     * @param globalConfig 全局配置
     * @param tableField   字段列信息
     * @return ignore
     */
    default IColumnType processTypeConvert(@Nonnull GlobalConfig globalConfig, @Nonnull TableField tableField) {
        return processTypeConvert(globalConfig, tableField.getType());
    }

    /**
     * 执行类型转换
     *
     * @param globalConfig 全局配置
     * @param fieldType    字段类型
     * @return ignore
     */
    IColumnType processTypeConvert(@Nonnull GlobalConfig globalConfig, @Nonnull String fieldType);

}
