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
package io.github.meta.ease.generator.mybatis.config.converts.select;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 分支提供者
 *
 * @author hanchunlin
 * Created at 2020/6/11 17:19
 * @see BranchBuilder
 */
public interface Branch<P, T> {

    /**
     * 工厂方法，快速创建分支
     *
     * @param tester  测试器
     * @param factory 值工厂
     * @param <P>     参数类型
     * @param <T>     值类型
     * @return 返回一个新的分支
     */
    static <P, T> Branch<P, T> of(Predicate<P> tester, Function<P, T> factory) {
        return new Branch<P, T>() {

            @Override
            public Predicate<P> tester() {
                return tester;
            }

            @Override
            public Function<P, T> factory() {
                return factory;
            }

        };
    }

    /**
     * @return 分支进入条件
     */
    Predicate<P> tester();

    /**
     * @return 值工厂
     */
    Function<P, T> factory();
}
