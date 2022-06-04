package cn.iocoder.yudao.framework.sms.core.client.dto;

import lombok.Data;

/**
 * 短信模板 Response DTO
 *
 * @author 芋道源码
 */
@Data
public class SmsTemplateRespDTO {

    /**
     * 模板编号
     */
    private String id;
    /**
     * 短信内容
     */
    private String content;
    /**
     * 审核状态
     * <p>
     * 枚举 {@link cn.iocoder.yudao.framework.sms.core.enums.SmsTemplateAuditStatusEnum}
     */
    private Integer auditStatus;
    /**
     * 审核未通过的理由
     */
    private String auditReason;

}