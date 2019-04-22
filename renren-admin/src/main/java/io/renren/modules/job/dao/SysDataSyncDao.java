package io.renren.modules.job.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.job.entity.SysDataSyncEntity;
import org.apache.ibatis.annotations.Select;

/**
 * 
 * 
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2019-01-16 19:01:34
 */
public interface SysDataSyncDao extends BaseMapper<SysDataSyncEntity> {

    @Select("Select * from  sys_data_sync  where sync_type=#{sysType}")
    SysDataSyncEntity findOneSysDataSync(String sysType);
	
}
