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
package cn.iocoder.yudao.module.bpm.service.message.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * BPM 发送任务被分配 Request DTO
 */
@Data
public class BpmMessageSendWhenTaskCreatedReqDTO {

    /**
     * 流程实例的编号
     */
    @NotEmpty(message = "流程实例的编号不能为空")
    private String processInstanceId;
    /**
     * 流程实例的名字
     */
    @NotEmpty(message = "流程实例的名字不能为空")
    private String processInstanceName;
    @NotNull(message = "发起人的用户编号")
    private Long startUserId;
    @NotEmpty(message = "发起人的昵称")
    private String startUserNickname;

    /**
     * 流程任务的编号
     */
    @NotEmpty(message = "流程任务的编号不能为空")
    private String taskId;
    /**
     * 流程任务的名字
     */
    @NotEmpty(message = "流程任务的名字不能为空")
    private String taskName;

    /**
     * 审批人的用户编号
     */
    @NotNull(message = "审批人的用户编号不能为空")
    private Long assigneeUserId;

}