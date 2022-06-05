package cn.iocoder.yudao.module.infra.dal.mysql.config;

import cn.iocoder.yudao.module.infra.controller.admin.config.vo.ConfigExportReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.config.vo.ConfigPageReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import io.github.meta.ease.common.pojo.PageResult;
import io.github.meta.ease.mybatis.mybatis.core.mapper.BaseMapperX;
import io.github.meta.ease.mybatis.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ConfigMapper extends BaseMapperX<ConfigDO> {

    default ConfigDO selectByKey(String key) {
        return selectOne(ConfigDO::getConfigKey, key);
    }

    default PageResult<ConfigDO> selectPage(ConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ConfigDO>()
                .likeIfPresent(ConfigDO::getName, reqVO.getName())
                .likeIfPresent(ConfigDO::getConfigKey, reqVO.getKey())
                .eqIfPresent(ConfigDO::getType, reqVO.getType())
                .betweenIfPresent(ConfigDO::getCreateTime, reqVO.getBeginTime(), reqVO.getEndTime()));
    }

    default List<ConfigDO> selectList(ConfigExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ConfigDO>()
                .likeIfPresent(ConfigDO::getName, reqVO.getName())
                .likeIfPresent(ConfigDO::getConfigKey, reqVO.getKey())
                .eqIfPresent(ConfigDO::getType, reqVO.getType())
                .betweenIfPresent(ConfigDO::getCreateTime, reqVO.getBeginTime(), reqVO.getEndTime()));
    }
}
