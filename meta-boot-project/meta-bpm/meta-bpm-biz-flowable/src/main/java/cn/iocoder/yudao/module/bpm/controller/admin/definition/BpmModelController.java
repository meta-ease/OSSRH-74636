package cn.iocoder.yudao.module.bpm.controller.admin.definition;

import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.model.BpmModeImportReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.model.BpmModelCreateReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.model.BpmModelPageItemRespVO;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.model.BpmModelPageReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.model.BpmModelRespVO;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.model.BpmModelUpdateReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.model.BpmModelUpdateStateReqVO;
import cn.iocoder.yudao.module.bpm.convert.definition.BpmModelConvert;
import cn.iocoder.yudao.module.bpm.service.definition.BpmModelService;
import io.github.meta.ease.common.pojo.CommonResult;
import io.github.meta.ease.common.pojo.PageResult;
import io.github.meta.ease.common.util.io.IoUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;

import static io.github.meta.ease.common.pojo.CommonResult.success;

@Api(tags = "管理后台 - 流程模型")
@RestController
@RequestMapping("/bpm/model")
@Validated
public class BpmModelController {

    @Resource
    private BpmModelService modelService;

    @GetMapping("/page")
    @ApiOperation(value = "获得模型分页")
    public CommonResult<PageResult<BpmModelPageItemRespVO>> getModelPage(BpmModelPageReqVO pageVO) {
        return success(modelService.getModelPage(pageVO));
    }

    @GetMapping("/get")
    @ApiOperation("获得模型")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = String.class)
    @PreAuthorize("@ss.hasPermission('bpm:model:query')")
    public CommonResult<BpmModelRespVO> getModel(@RequestParam("id") String id) {
        BpmModelRespVO model = modelService.getModel(id);
        return success(model);
    }

    @PostMapping("/create")
    @ApiOperation(value = "新建模型")
    @PreAuthorize("@ss.hasPermission('bpm:model:create')")
    public CommonResult<String> createModel(@Valid @RequestBody BpmModelCreateReqVO createRetVO) {
        return success(modelService.createModel(createRetVO, null));
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改模型")
    @PreAuthorize("@ss.hasPermission('bpm:model:update')")
    public CommonResult<Boolean> updateModel(@Valid @RequestBody BpmModelUpdateReqVO modelVO) {
        modelService.updateModel(modelVO);
        return success(true);
    }

    @PostMapping("/import")
    @ApiOperation(value = "导入模型")
    @PreAuthorize("@ss.hasPermission('bpm:model:import')")
    public CommonResult<String> importModel(@Valid BpmModeImportReqVO importReqVO) throws IOException {
        BpmModelCreateReqVO createReqVO = BpmModelConvert.INSTANCE.convert(importReqVO);
        // 读取文件
        String bpmnXml = IoUtils.readUtf8(importReqVO.getBpmnFile().getInputStream(), false);
        return success(modelService.createModel(createReqVO, bpmnXml));
    }

    @PostMapping("/deploy")
    @ApiOperation(value = "部署模型")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = String.class)
    @PreAuthorize("@ss.hasPermission('bpm:model:deploy')")
    public CommonResult<Boolean> deployModel(@RequestParam("id") String id) {
        modelService.deployModel(id);
        return success(true);
    }

    @PutMapping("/update-state")
    @ApiOperation(value = "修改模型的状态", notes = "实际更新的部署的流程定义的状态")
    @PreAuthorize("@ss.hasPermission('bpm:model:update')")
    public CommonResult<Boolean> updateModelState(@Valid @RequestBody BpmModelUpdateStateReqVO reqVO) {
        modelService.updateModelState(reqVO.getId(), reqVO.getState());
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除模型")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = String.class)
    @PreAuthorize("@ss.hasPermission('bpm:model:delete')")
    public CommonResult<Boolean> deleteModel(@RequestParam("id") String id) {
        modelService.deleteModel(id);
        return success(true);
    }
}