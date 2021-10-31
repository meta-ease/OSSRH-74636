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
package com.open.cloud.flow.autoconfigure;

import com.open.cloud.flow.api.BusinessEngine;
import com.open.cloud.flow.base.FlowExecutor;
import com.open.cloud.flow.base.ProcessBeanPostProcessor;
import com.open.cloud.flow.base.SimpleBusinessEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author leijian
 * @version 1.0
 * @date 2021/10/7 11:21
 */
@Configuration(proxyBeanMethods = false)
public class CloudBootCoreAutoConfiguration {

    @Bean
    public ProcessBeanPostProcessor processBeanPostProcessor() {
        return new ProcessBeanPostProcessor();
    }

    @Bean
    public BusinessEngine simpleBusinessEngine() {
        return new SimpleBusinessEngine();
    }

    @Bean
    public FlowExecutor flowExecutor(BusinessEngine businessEngine) {
        return new FlowExecutor();
    }

}
