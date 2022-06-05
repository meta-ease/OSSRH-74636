package io.github.meta.ease.web.apilog.core.service;


import io.github.meta.ease.web.apilog.core.service.dto.ApiErrorLogCreateReqDTO;

import javax.validation.Valid;

/**
 * API 错误日志 Framework Service 接口
 *
 * @author 芋道源码
 */
public interface ApiErrorLogFrameworkService {

    /**
     * 创建 API 错误日志
     *
     * @param createDTO 创建信息
     */
    void createApiErrorLogAsync(@Valid ApiErrorLogCreateReqDTO createDTO);
}
