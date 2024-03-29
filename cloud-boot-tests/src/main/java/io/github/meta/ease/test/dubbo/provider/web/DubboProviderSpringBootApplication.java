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
package io.github.meta.ease.test.dubbo.provider.web;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * @author leijian
 * @version 1.0
 * @date 2021/9/25 21:14
 */
@SpringBootApplication(scanBasePackages={"io.github.meta.ease.test.dubbo.provider.web","io.github.meta.ease.test.api"})
@MapperScan({"io.github.meta.ease.test.dubbo.provider.web","io.github.meta.ease.test.api"})
@EnableDubbo(scanBasePackages = "io.github.meta.ease.test.dubbo.provider.web")
@PropertySource("classpath:/config/dubbo-provider.properties")
public class DubboProviderSpringBootApplication {

    public static void main(String[] args) throws Exception {
        new SpringApplication(DubboProviderSpringBootApplication.class).run(args);

    }

}
