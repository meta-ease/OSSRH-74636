package cn.iocoder.yudao.module.bpm.service.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceCancelReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceCreateReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceMyPageReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.task.vo.instance.BpmProcessInstancePageItemRespVO;
import cn.iocoder.yudao.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceRespVO;
import cn.iocoder.yudao.module.bpm.convert.task.BpmProcessInstanceConvert;
import cn.iocoder.yudao.module.bpm.dal.dataobject.definition.BpmProcessDefinitionExtDO;
import cn.iocoder.yudao.module.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import cn.iocoder.yudao.module.bpm.dal.mysql.task.BpmProcessInstanceExtMapper;
import cn.iocoder.yudao.module.bpm.enums.task.BpmProcessInstanceDeleteReasonEnum;
import cn.iocoder.yudao.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import cn.iocoder.yudao.module.bpm.enums.task.BpmProcessInstanceStatusEnum;
import cn.iocoder.yudao.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventPublisher;
import cn.iocoder.yudao.module.bpm.service.definition.BpmProcessDefinitionService;
import cn.iocoder.yudao.module.bpm.service.message.BpmMessageService;
import cn.iocoder.yudao.module.system.api.dept.DeptApi;
import cn.iocoder.yudao.module.system.api.dept.dto.DeptRespDTO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import io.github.meta.ease.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import io.github.meta.ease.common.pojo.PageResult;
import io.github.meta.ease.common.util.number.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.event.FlowableCancelledEvent;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static io.github.meta.ease.bpm.enums.ErrorCodeConstants.PROCESS_DEFINITION_IS_SUSPENDED;
import static io.github.meta.ease.bpm.enums.ErrorCodeConstants.PROCESS_DEFINITION_NOT_EXISTS;
import static io.github.meta.ease.bpm.enums.ErrorCodeConstants.PROCESS_INSTANCE_CANCEL_FAIL_NOT_EXISTS;
import static io.github.meta.ease.bpm.enums.ErrorCodeConstants.PROCESS_INSTANCE_CANCEL_FAIL_NOT_SELF;
import static io.github.meta.ease.common.exception.util.ServiceExceptionUtil.exception;
import static io.github.meta.ease.common.util.collection.CollectionUtils.convertList;

/**
 * 流程实例 Service 实现类
 * <p>
 * ProcessDefinition & ProcessInstance & Execution & Task 的关系：
 * 1. https://blog.csdn.net/bobozai86/article/details/105210414
 * <p>
 * HistoricProcessInstance & ProcessInstance 的关系：
 * 1.https://my.oschina.net/843294669/blog/719024
 * 简单来说，前者 = 历史 + 运行中的流程实例，后者仅是运行中的流程实例
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class BpmProcessInstanceServiceImpl implements BpmProcessInstanceService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private BpmProcessInstanceExtMapper processInstanceExtMapper;

    @Resource
    @Lazy // 解决循环依赖
    private BpmTaskService taskService;

    @Resource
    private BpmProcessDefinitionService processDefinitionService;

    @Resource
    private HistoryService historyService;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private DeptApi deptApi;

    @Resource
    private BpmProcessInstanceResultEventPublisher processInstanceResultEventPublisher;

    @Resource
    private BpmMessageService messageService;

    @Override
    public ProcessInstance getProcessInstance(String id) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();
    }

    @Override
    public List<ProcessInstance> getProcessInstances(Set<String> ids) {
        return runtimeService.createProcessInstanceQuery().processInstanceIds(ids).list();
    }

    @Override
    public PageResult<BpmProcessInstancePageItemRespVO> getMyProcessInstancePage(Long userId,
                                                                                 BpmProcessInstanceMyPageReqVO pageReqVO) {
        // 通过 BpmProcessInstanceExtDO 表，先查询到对应的分页
        PageResult<BpmProcessInstanceExtDO> pageResult = processInstanceExtMapper.selectPage(userId, pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return new PageResult<>(pageResult.getTotal());
        }

        // 获得流程 Task Map
        List<String> processInstanceIds = convertList(pageResult.getList(), BpmProcessInstanceExtDO::getProcessInstanceId);
        Map<String, List<Task>> taskMap = taskService.getTaskMapByProcessInstanceIds(processInstanceIds);
        // 转换返回
        return BpmProcessInstanceConvert.INSTANCE.convertPage(pageResult, taskMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProcessInstance(Long userId, @Valid BpmProcessInstanceCreateReqVO createReqVO) {
        // 获得流程定义
        ProcessDefinition definition = processDefinitionService.getProcessDefinition(createReqVO.getProcessDefinitionId());
        // 发起流程
        return createProcessInstance0(userId, definition, createReqVO.getVariables(), null);
    }

    @Override
    public String createProcessInstance(Long userId, @Valid BpmProcessInstanceCreateReqDTO createReqDTO) {
        // 获得流程定义
        ProcessDefinition definition = processDefinitionService.getActiveProcessDefinition(
                createReqDTO.getProcessDefinitionKey());
        // 发起流程
        return createProcessInstance0(userId, definition, createReqDTO.getVariables(), createReqDTO.getBusinessKey());
    }

    @Override
    public BpmProcessInstanceRespVO getProcessInstanceVO(String id) {
        // 获得流程实例
        HistoricProcessInstance processInstance = getHistoricProcessInstance(id);
        if (processInstance == null) {
            return null;
        }
        BpmProcessInstanceExtDO processInstanceExt = processInstanceExtMapper.selectByProcessInstanceId(id);
        Assert.notNull(processInstanceExt, "流程实例拓展({}) 不存在", id);

        // 获得流程定义
        ProcessDefinition processDefinition = processDefinitionService
                .getProcessDefinition(processInstance.getProcessDefinitionId());
        Assert.notNull(processDefinition, "流程定义({}) 不存在", processInstance.getProcessDefinitionId());
        BpmProcessDefinitionExtDO processDefinitionExt = processDefinitionService.getProcessDefinitionExt(
                processInstance.getProcessDefinitionId());
        Assert.notNull(processDefinitionExt, "流程定义拓展({}) 不存在", id);
        String bpmnXml = processDefinitionService.getProcessDefinitionBpmnXML(processInstance.getProcessDefinitionId());

        // 获得 User
        AdminUserRespDTO startUser = adminUserApi.getUser(NumberUtils.parseLong(processInstance.getStartUserId()));
        DeptRespDTO dept = null;
        if (startUser != null) {
            dept = deptApi.getDept(startUser.getDeptId());
        }

        // 拼接结果
        return BpmProcessInstanceConvert.INSTANCE.convert2(processInstance, processInstanceExt,
                processDefinition, processDefinitionExt, bpmnXml, startUser, dept);
    }

    @Override
    public void cancelProcessInstance(Long userId, @Valid BpmProcessInstanceCancelReqVO cancelReqVO) {
        // 校验流程实例存在
        ProcessInstance instance = getProcessInstance(cancelReqVO.getId());
        if (instance == null) {
            throw exception(PROCESS_INSTANCE_CANCEL_FAIL_NOT_EXISTS);
        }
        // 只能取消自己的
        if (!Objects.equals(instance.getStartUserId(), String.valueOf(userId))) {
            throw exception(PROCESS_INSTANCE_CANCEL_FAIL_NOT_SELF);
        }

        // 通过删除流程实例，实现流程实例的取消,
        // 删除流程实例，正则执行任务ACT_RU_TASK. 任务会被删除。通过历史表查询
        deleteProcessInstance(cancelReqVO.getId(),
                BpmProcessInstanceDeleteReasonEnum.CANCEL_TASK.format(cancelReqVO.getReason()));
    }

    /**
     * 获得历史的流程实例
     *
     * @param id 流程实例的编号
     * @return 历史的流程实例
     */
    @Override
    public HistoricProcessInstance getHistoricProcessInstance(String id) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceId(id).singleResult();
    }

    @Override
    public List<HistoricProcessInstance> getHistoricProcessInstances(Set<String> ids) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceIds(ids).list();
    }

    @Override
    public void createProcessInstanceExt(ProcessInstance instance) {
        // 获得流程定义
        ProcessDefinition definition = processDefinitionService.getProcessDefinition2(instance.getProcessDefinitionId());
        // 插入 BpmProcessInstanceExtDO 对象
        BpmProcessInstanceExtDO instanceExtDO = new BpmProcessInstanceExtDO()
                .setProcessInstanceId(instance.getId())
                .setProcessDefinitionId(definition.getId())
                .setName(instance.getProcessDefinitionName())
                .setStartUserId(Long.valueOf(instance.getStartUserId()))
                .setCategory(definition.getCategory())
                .setStatus(BpmProcessInstanceStatusEnum.RUNNING.getStatus())
                .setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());

        processInstanceExtMapper.insert(instanceExtDO);
    }

    @Override
    public void updateProcessInstanceExtCancel(FlowableCancelledEvent event) {
        // 判断是否为 Reject 不通过。如果是，则不进行更新
        if (BpmProcessInstanceDeleteReasonEnum.isRejectReason((String) event.getCause())) {
            return;
        }

        // 需要主动查询，因为 instance 只有 id 属性
        // 另外，此时如果去查询 ProcessInstance 的话，字段是不全的，所以去查询了 HistoricProcessInstance
        HistoricProcessInstance processInstance = getHistoricProcessInstance(event.getProcessInstanceId());
        // 更新拓展表
        BpmProcessInstanceExtDO instanceExtDO = new BpmProcessInstanceExtDO()
                .setProcessInstanceId(event.getProcessInstanceId())
                .setEndTime(new Date()) // 由于 ProcessInstance 里没有办法拿到 endTime，所以这里设置
                .setStatus(BpmProcessInstanceStatusEnum.FINISH.getStatus())
                .setResult(BpmProcessInstanceResultEnum.CANCEL.getResult());

        processInstanceExtMapper.updateByProcessInstanceId(instanceExtDO);

        // 发送流程实例的状态事件
        processInstanceResultEventPublisher.sendProcessInstanceResultEvent(
                BpmProcessInstanceConvert.INSTANCE.convert(this, processInstance, instanceExtDO.getResult()));
    }

    @Override
    public void updateProcessInstanceExtComplete(ProcessInstance instance) {
        // 需要主动查询，因为 instance 只有 id 属性
        // 另外，此时如果去查询 ProcessInstance 的话，字段是不全的，所以去查询了 HistoricProcessInstance
        HistoricProcessInstance processInstance = getHistoricProcessInstance(instance.getId());
        // 更新拓展表
        BpmProcessInstanceExtDO instanceExtDO = new BpmProcessInstanceExtDO()
                .setProcessInstanceId(instance.getProcessInstanceId())
                .setEndTime(new Date()) // 由于 ProcessInstance 里没有办法拿到 endTime，所以这里设置
                .setStatus(BpmProcessInstanceStatusEnum.FINISH.getStatus())
                .setResult(BpmProcessInstanceResultEnum.APPROVE.getResult()); // 如果正常完全，说明审批通过
        processInstanceExtMapper.updateByProcessInstanceId(instanceExtDO);

        // 发送流程被通过的消息
        messageService.sendMessageWhenProcessInstanceApprove(
                BpmProcessInstanceConvert.INSTANCE.convert2ApprovedReq(instance));

        // 发送流程实例的状态事件
        processInstanceResultEventPublisher.sendProcessInstanceResultEvent(
                BpmProcessInstanceConvert.INSTANCE.convert(this, processInstance, instanceExtDO.getResult()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProcessInstanceExtReject(String id, String reason) {
        // 需要主动查询，因为 instance 只有 id 属性
        ProcessInstance processInstance = getProcessInstance(id);
        // 删除流程实例，以实现驳回任务时，取消整个审批流程
        deleteProcessInstance(id, StrUtil.format(BpmProcessInstanceDeleteReasonEnum.REJECT_TASK.format(reason)));

        // 更新 status + result
        // 注意，不能和上面的逻辑更换位置。因为 deleteProcessInstance 会触发流程的取消，进而调用 updateProcessInstanceExtCancel 方法，
        // 设置 result 为 BpmProcessInstanceStatusEnum.CANCEL，显然和 result 不一定是一致的
        BpmProcessInstanceExtDO instanceExtDO = new BpmProcessInstanceExtDO().setProcessInstanceId(id)
                .setStatus(BpmProcessInstanceStatusEnum.FINISH.getStatus())
                .setResult(BpmProcessInstanceResultEnum.REJECT.getResult());
        processInstanceExtMapper.updateByProcessInstanceId(instanceExtDO);

        // 发送流程被不通过的消息
        messageService.sendMessageWhenProcessInstanceReject(
                BpmProcessInstanceConvert.INSTANCE.convert2RejectReq(processInstance, reason));

        // 发送流程实例的状态事件
        processInstanceResultEventPublisher.sendProcessInstanceResultEvent(
                BpmProcessInstanceConvert.INSTANCE.convert(this, processInstance, instanceExtDO.getResult()));
    }

    private void deleteProcessInstance(String id, String reason) {
        runtimeService.deleteProcessInstance(id, reason);
    }

    private String createProcessInstance0(Long userId, ProcessDefinition definition,
                                          Map<String, Object> variables, String businessKey) {
        // 校验流程定义
        if (definition == null) {
            throw exception(PROCESS_DEFINITION_NOT_EXISTS);
        }
        if (definition.isSuspended()) {
            throw exception(PROCESS_DEFINITION_IS_SUSPENDED);
        }

        // 创建流程实例
        ProcessInstance instance = runtimeService.startProcessInstanceById(definition.getId(), businessKey, variables);
        // 设置流程名字
        runtimeService.setProcessInstanceName(instance.getId(), definition.getName());

        // 补全流程实例的拓展表
        processInstanceExtMapper.updateByProcessInstanceId(
                new BpmProcessInstanceExtDO().setProcessInstanceId(instance.getId())
                        .setFormVariables(variables));

        return instance.getId();
    }
}
