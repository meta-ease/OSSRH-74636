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
package io.github.meta.ease.generator.mybatis.config.builder;

import io.github.meta.ease.generator.mybatis.config.IConfigBuilder;
import io.github.meta.ease.generator.mybatis.config.StrategyConfig;

import javax.annotation.Nonnull;

/**
 * 配置构建
 *
 * @author nieqiurong 2020/10/11.
 * @since 3.5.0
 */
public class BaseBuilder implements IConfigBuilder<StrategyConfig> {

    private final StrategyConfig strategyConfig;

    public BaseBuilder(@Nonnull StrategyConfig strategyConfig) {
        this.strategyConfig = strategyConfig;
    }

    @Nonnull
    public Entity.Builder entityBuilder() {
        return strategyConfig.entityBuilder();
    }

    @Nonnull
    public Controller.Builder controllerBuilder() {
        return strategyConfig.controllerBuilder();
    }

    @Nonnull
    public Mapper.Builder mapperBuilder() {
        return strategyConfig.mapperBuilder();
    }

    @Nonnull
    public Service.Builder serviceBuilder() {
        return strategyConfig.serviceBuilder();
    }

    @Nonnull
    @Override
    public StrategyConfig build() {
        this.strategyConfig.validate();
        return this.strategyConfig;
    }
}
