package cn.iocoder.yudao.module.system.controller.admin.dict;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.type.DictTypeCreateReqVO;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.type.DictTypeExcelVO;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.type.DictTypeExportReqVO;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.type.DictTypePageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.type.DictTypeRespVO;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.type.DictTypeSimpleRespVO;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.type.DictTypeUpdateReqVO;
import cn.iocoder.yudao.module.system.convert.dict.DictTypeConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.dict.DictTypeDO;
import cn.iocoder.yudao.module.system.service.dict.DictTypeService;
import io.github.meta.ease.common.pojo.CommonResult;
import io.github.meta.ease.common.pojo.PageResult;
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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;
import static io.github.meta.ease.common.pojo.CommonResult.success;

@Api(tags = "管理后台 - 字典类型")
@RestController
@RequestMapping("/system/dict-type")
@Validated
public class DictTypeController {

    @Resource
    private DictTypeService dictTypeService;

    @PostMapping("/create")
    @ApiOperation("创建字典类型")
    @PreAuthorize("@ss.hasPermission('system:dict:create')")
    public CommonResult<Long> createDictType(@Valid @RequestBody DictTypeCreateReqVO reqVO) {
        Long dictTypeId = dictTypeService.createDictType(reqVO);
        return success(dictTypeId);
    }

    @PutMapping("/update")
    @ApiOperation("修改字典类型")
    @PreAuthorize("@ss.hasPermission('system:dict:update')")
    public CommonResult<Boolean> updateDictType(@Valid @RequestBody DictTypeUpdateReqVO reqVO) {
        dictTypeService.updateDictType(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除字典类型")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('system:dict:delete')")
    public CommonResult<Boolean> deleteDictType(Long id) {
        dictTypeService.deleteDictType(id);
        return success(true);
    }

    @ApiOperation("/获得字典类型的分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('system:dict:query')")
    public CommonResult<PageResult<DictTypeRespVO>> pageDictTypes(@Valid DictTypePageReqVO reqVO) {
        return success(DictTypeConvert.INSTANCE.convertPage(dictTypeService.getDictTypePage(reqVO)));
    }

    @ApiOperation("/查询字典类型详细")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @GetMapping(value = "/get")
    @PreAuthorize("@ss.hasPermission('system:dict:query')")
    public CommonResult<DictTypeRespVO> getDictType(@RequestParam("id") Long id) {
        return success(DictTypeConvert.INSTANCE.convert(dictTypeService.getDictType(id)));
    }

    @GetMapping("/list-all-simple")
    @ApiOperation(value = "获得全部字典类型列表", notes = "包括开启 + 禁用的字典类型，主要用于前端的下拉选项")
    // 无需添加权限认证，因为前端全局都需要
    public CommonResult<List<DictTypeSimpleRespVO>> listSimpleDictTypes() {
        List<DictTypeDO> list = dictTypeService.getDictTypeList();
        return success(DictTypeConvert.INSTANCE.convertList(list));
    }

    @ApiOperation("导出数据类型")
    @GetMapping("/export")
    @PreAuthorize("@ss.hasPermission('system:dict:query')")
    @OperateLog(type = EXPORT)
    public void export(HttpServletResponse response, @Valid DictTypeExportReqVO reqVO) throws IOException {
        List<DictTypeDO> list = dictTypeService.getDictTypeList(reqVO);
        List<DictTypeExcelVO> data = DictTypeConvert.INSTANCE.convertList02(list);
        // 输出
        ExcelUtils.write(response, "字典类型.xls", "类型列表", DictTypeExcelVO.class, data);
    }
}
