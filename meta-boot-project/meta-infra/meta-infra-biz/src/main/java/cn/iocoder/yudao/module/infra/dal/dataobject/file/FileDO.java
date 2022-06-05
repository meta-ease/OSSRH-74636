package cn.iocoder.yudao.module.infra.dal.dataobject.file;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.meta.ease.mybatis.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 文件表
 * 每次文件上传，都会记录一条记录到该表中
 *
 * @author 芋道源码
 */
@TableName("infra_file")
@KeySequence("infra_file_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDO extends BaseDO {

    /**
     * 编号，数据库自增
     */
    private Long id;

    /**
     * 配置编号
     * <p>
     * 关联 {@link FileConfigDO#getId()}
     */
    private Long configId;

    /**
     * 路径，即文件名
     */
    private String path;

    /**
     * 访问地址
     */
    private String url;

    /**
     * 文件类型
     * <p>
     * 通过 {@link cn.hutool.core.io.FileTypeUtil#getType(java.io.InputStream)} 获取
     */
    private String type;

    /**
     * 文件大小
     */
    private Integer size;
}
