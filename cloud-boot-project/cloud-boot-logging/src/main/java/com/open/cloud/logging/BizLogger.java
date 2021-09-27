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
package com.open.cloud.logging;


import com.open.cloud.logging.logger.DebugLog;
import com.open.cloud.logging.logger.ErrorLog;
import com.open.cloud.logging.logger.InfoLog;
import com.open.cloud.logging.logger.MethodLog;
import com.open.cloud.logging.logger.ParmLog;
import com.open.cloud.logging.logger.PlatformLog;
import com.open.cloud.logging.logger.ProfileLog;
import com.open.cloud.logging.logger.SqlLog;
import com.open.cloud.logging.logger.TraceLog;
import com.open.cloud.logging.logger.WarnLog;

/**
 * The six logging levels used by Log are (in order):
 * 1. TRACE (the least serious)
 * 2. DEBUG
 * 3. INFO
 * 4. WARN
 * 5. ERROR
 * 6. FATAL (the most serious)
 *
 * @author Leijian
 * @date 2020 /11/15
 */
public abstract class BizLogger implements TraceLog, DebugLog, InfoLog, WarnLog, ErrorLog, ParmLog,
        MethodLog, SqlLog, ProfileLog, PlatformLog {

}