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

import io.github.meta.ease.generator.mybatis.ITemplate;
import io.github.meta.ease.generator.mybatis.config.ConstVal;
import io.github.meta.ease.generator.mybatis.config.StrategyConfig;
import io.github.meta.ease.generator.mybatis.config.po.TableInfo;
import io.github.meta.ease.generator.mybatis.function.ConverterFileName;
import io.github.meta.ease.generator.mybatis.util.ClassUtils;
import io.github.meta.ease.generator.mybatis.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器属性配置
 *
 * @author nieqiurong 2020/10/11.
 * @since 3.5.0
 */
public class Controller implements ITemplate {

    /**
     * 生成 <code>@RestController</code> 控制器（默认 false）
     * <pre>
     *      <code>@Controller</code> -> <code>@RestController</code>
     * </pre>
     */
    private boolean restStyle;
    /**
     * 驼峰转连字符（默认 false）
     * <pre>
     *      <code>@RequestMapping("/managerUserActionHistory")</code> -> <code>@RequestMapping("/manager-user-action-history")</code>
     * </pre>
     */
    private boolean hyphenStyle;
    /**
     * 自定义继承的Controller类全称，带包名
     */
    private String superClass;
    /**
     * 转换输出控制器文件名称
     *
     * @since 3.5.0
     */
    private ConverterFileName converterFileName = (entityName -> entityName + ConstVal.CONTROLLER);

    private Controller() {
    }

    public boolean isRestStyle() {
        return restStyle;
    }

    public boolean isHyphenStyle() {
        return hyphenStyle;
    }

    @Nullable
    public String getSuperClass() {
        return superClass;
    }

    @Nonnull
    public ConverterFileName getConverterFileName() {
        return converterFileName;
    }

    @Override
    @Nonnull
    public Map<String, Object> renderData(@Nonnull TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();
        data.put("controllerMappingHyphen", StringUtils.camelToHyphen(tableInfo.getEntityPath()));
        data.put("controllerMappingHyphenStyle", this.hyphenStyle);
        data.put("restControllerStyle", this.restStyle);
        data.put("superControllerClassPackage", StringUtils.isBlank(superClass) ? null : superClass);
        data.put("superControllerClass", ClassUtils.getSimpleName(this.superClass));
        return data;
    }

    public static class Builder extends BaseBuilder {

        private final Controller controller = new Controller();

        public Builder(@Nonnull StrategyConfig strategyConfig) {
            super(strategyConfig);
        }

        /**
         * 父类控制器
         *
         * @param clazz 父类控制器
         * @return this
         */
        public Builder superClass(@Nonnull Class<?> clazz) {
            return superClass(clazz.getName());
        }

        /**
         * 父类控制器
         *
         * @param superClass 父类控制器类名
         * @return this
         */
        public Builder superClass(@Nonnull String superClass) {
            this.controller.superClass = superClass;
            return this;
        }

        /**
         * 开启驼峰转连字符
         *
         * @return this
         * @since 3.5.0
         */
        public Builder enableHyphenStyle() {
            this.controller.hyphenStyle = true;
            return this;
        }

        /**
         * 开启生成@RestController控制器
         *
         * @return this
         * @since 3.5.0
         */
        public Builder enableRestStyle() {
            this.controller.restStyle = true;
            return this;
        }

        /**
         * 转换输出文件名称
         *
         * @param converter 　转换处理
         * @return this
         * @since 3.5.0
         */
        public Builder convertFileName(@Nonnull ConverterFileName converter) {
            this.controller.converterFileName = converter;
            return this;
        }

        /**
         * 格式化文件名称
         *
         * @param format 　格式
         * @return this
         * @since 3.5.0
         */
        public Builder formatFileName(@Nonnull String format) {
            return convertFileName((entityName) -> String.format(format, entityName));
        }

        @Nonnull
        public Controller get() {
            return this.controller;
        }
    }
}
