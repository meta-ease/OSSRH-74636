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
package io.github.meta.ease.flow.engine;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.meta.ease.core.commons.ServiceHandler;
import io.github.meta.ease.domain.dto.BaseRequest;
import io.github.meta.ease.domain.dto.BaseResponse;
import io.github.meta.ease.flow.engine.base.BusinessEngine;
import io.github.meta.ease.flow.engine.process.IProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @author leijian
 * @version 1.0
 * @date 2021/9/30 22:49
 */
public class FlowExecutor {

    private static final Logger logger = LoggerFactory.getLogger(FlowExecutor.class);
    static Multimap<String, IProcess> ALL_IPROCESS = ArrayListMultimap.create();
    private static FlowExecutor flowExecutor;
    @Resource
    private BusinessEngine businessEngine;

    public FlowExecutor() {
        flowExecutor = this;
    }

    @lombok.SneakyThrows
    public synchronized static <T extends BaseRequest, R extends BaseResponse> R execute2Resp(T request) {
        String serviceName = ServiceHandler.getServiceName(request.getClass());
        if (ALL_IPROCESS.get(serviceName).size() > 1) {
            logger.warn("");
        }

        IProcess iProcess = ALL_IPROCESS.get(serviceName).stream().sorted().findFirst().get();
        return (R) flowExecutor.businessEngine.execFlow(iProcess, request);
    }
}
