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
package io.github.meta.ease.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author leijian
 * @version 1.0
 * @date 2021/10/1 11:18
 */
public class ExceptionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

    public static BusinessException creatBusinessException(String errorCode, String errorMsg) {
        return new BusinessException(errorCode, errorMsg);
    }

    public static BusinessException creatBusinessException(String errorCode) {
        return new BusinessException(errorCode);
    }
}
