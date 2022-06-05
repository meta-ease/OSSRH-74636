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
package cn.iocoder.yudao.module.bpm.controller.admin.task.vo.instance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@ApiModel("管理后台 - 流程实例的取消 Request VO")
@Data
public class BpmProcessInstanceCancelReqVO {

    @ApiModelProperty(value = "流程实例的编号", required = true, example = "1024")
    @NotEmpty(message = "流程实例的编号不能为空")
    private String id;

    @ApiModelProperty(value = "取消原因", required = true, example = "不请假了！")
    @NotEmpty(message = "取消原因不能为空")
    private String reason;
}
