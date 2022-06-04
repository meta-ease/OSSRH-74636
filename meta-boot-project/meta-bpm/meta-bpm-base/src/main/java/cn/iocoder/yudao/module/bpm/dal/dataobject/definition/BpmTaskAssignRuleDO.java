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
package cn.iocoder.yudao.module.bpm.dal.dataobject.definition;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.meta.ease.mybatis.mybatis.core.dataobject.BaseDO;
import io.github.meta.ease.mybatis.mybatis.core.type.JsonLongSetTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

/**
 * Bpm 任务分配的规则表，用于自定义配置每个任务的负责人、候选人的分配规则。
 * 也就是说，废弃 BPMN 原本的 UserTask 设置的 assignee、candidateUsers 等配置，而是通过使用该规则进行计算对应的负责人。
 * <p>
 * 1. 默认情况下，{@link #processDefinitionId} 为 {@link #PROCESS_DEFINITION_ID_NULL} 值，表示贵改则与流程模型关联
 * 2. 在流程模型部署后，会将他的所有规则记录，复制出一份新部署出来的流程定义，通过设置 {@link #processDefinitionId} 为新的流程定义的编号进行关联
 *
 * @author 芋道源码
 */
@TableName(value = "bpm_task_assign_rule", autoResultMap = true)
@KeySequence("bpm_task_assign_rule_seq")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BpmTaskAssignRuleDO extends BaseDO {

    /**
     * {@link #processDefinitionId} 空串，用于标识属于流程模型，而不属于流程定义
     * 不使用空串的原因，Oracle 针对空串，会处理成 null，进而导致无法检索
     */
    public static final String PROCESS_DEFINITION_ID_NULL = "DEFAULT";

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 流程模型编号
     * <p>
     * 关联 Model 的 id 属性
     */
    private String modelId;
    /**
     * 流程定义编号
     * <p>
     * 关联 ProcessDefinition 的 id 属性
     */
    private String processDefinitionId;
    /**
     * 流程任务的定义 Key
     * <p>
     * 关联 Task 的 taskDefinitionKey 属性
     */
    private String taskDefinitionKey;

    /**
     * 规则类型
     * <p>
     * 枚举 {@link cn.iocoder.yudao.module.bpm.enums.definition.BpmTaskAssignRuleTypeEnum}
     */
    private Integer type;
    /**
     * 规则值数组，一般关联指定表的编号
     * 根据 type 不同，对应的值是不同的：
     * <p>
     * 1. {@link cn.iocoder.yudao.module.bpm.enums.definition.BpmTaskAssignRuleTypeEnum#ROLE} 时：角色编号
     * 2. {@link cn.iocoder.yudao.module.bpm.enums.definition.BpmTaskAssignRuleTypeEnum#DEPT_MEMBER} 时：部门编号
     * 3. {@link cn.iocoder.yudao.module.bpm.enums.definition.BpmTaskAssignRuleTypeEnum#DEPT_LEADER} 时：部门编号
     * 4. {@link cn.iocoder.yudao.module.bpm.enums.definition.BpmTaskAssignRuleTypeEnum#USER} 时：用户编号
     * 5. {@link cn.iocoder.yudao.module.bpm.enums.definition.BpmTaskAssignRuleTypeEnum#USER_GROUP} 时：用户组编号
     * 6. {@link cn.iocoder.yudao.module.bpm.enums.definition.BpmTaskAssignRuleTypeEnum#SCRIPT} 时：脚本编号，目前通过 {@link cn.iocoder.yudao.module.bpm.enums.definition.BpmTaskRuleScriptEnum#getId()} 标识
     */
    @TableField(typeHandler = JsonLongSetTypeHandler.class)
    private Set<Long> options;

}