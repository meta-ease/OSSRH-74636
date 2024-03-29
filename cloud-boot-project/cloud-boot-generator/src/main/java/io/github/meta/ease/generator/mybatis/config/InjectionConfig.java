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
package io.github.meta.ease.generator.mybatis.config;

import io.github.meta.ease.generator.mybatis.config.po.TableInfo;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 注入配置
 *
 * @author hubin
 * @since 2016-12-07
 */
public class InjectionConfig {

    /**
     * 输出文件之前消费者
     */
    private BiConsumer<TableInfo, Map<String, Object>> beforeOutputFileBiConsumer;

    /**
     * 自定义配置 Map 对象
     */
    private Map<String, Object> customMap = new HashMap<>();

    /**
     * 自定义模板文件，key为文件名称，value为模板路径
     */
    private Map<String, String> customFile = new HashMap<>();

    @Nonnull
    public void beforeOutputFile(TableInfo tableInfo, Map<String, Object> objectMap) {
        if (!customMap.isEmpty()) {
            objectMap.putAll(customMap);
        }
        if (null != beforeOutputFileBiConsumer) {
            beforeOutputFileBiConsumer.accept(tableInfo, objectMap);
        }
    }

    @Nonnull
    public Map<String, Object> getCustomMap() {
        return customMap;
    }

    @Nonnull
    public Map<String, String> getCustomFile() {
        return customFile;
    }

    /**
     * 构建者
     */
    public static class Builder implements IConfigBuilder<InjectionConfig> {

        private final InjectionConfig injectionConfig;

        public Builder() {
            this.injectionConfig = new InjectionConfig();
        }

        /**
         * 输出文件之前消费者
         *
         * @param biConsumer 消费者
         * @return this
         */
        public Builder beforeOutputFile(@Nonnull BiConsumer<TableInfo, Map<String, Object>> biConsumer) {
            this.injectionConfig.beforeOutputFileBiConsumer = biConsumer;
            return this;
        }

        /**
         * 自定义配置 Map 对象
         *
         * @param customMap Map 对象
         * @return this
         */
        public Builder customMap(@Nonnull Map<String, Object> customMap) {
            this.injectionConfig.customMap = customMap;
            return this;
        }

        /**
         * 自定义配置模板文件
         *
         * @param customFile key为文件名称，value为文件路径
         * @return this
         */
        public Builder customFile(@Nonnull Map<String, String> customFile) {
            this.injectionConfig.customFile = customFile;
            return this;
        }

        @Override
        public InjectionConfig build() {
            return this.injectionConfig;
        }
    }
}
