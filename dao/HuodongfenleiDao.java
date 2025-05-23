package com.dao;

import com.entity.HuodongfenleiEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.HuodongfenleiVO;
import com.entity.view.HuodongfenleiView;


/**
 * 活动分类
 * 
 * @author 
 * @email 
 * @date 2025-01-10 22:13:25
 */
public interface HuodongfenleiDao extends BaseMapper<HuodongfenleiEntity> {
	
	List<HuodongfenleiVO> selectListVO(@Param("ew") Wrapper<HuodongfenleiEntity> wrapper);
	
	HuodongfenleiVO selectVO(@Param("ew") Wrapper<HuodongfenleiEntity> wrapper);
	
	List<HuodongfenleiView> selectListView(@Param("ew") Wrapper<HuodongfenleiEntity> wrapper);

	List<HuodongfenleiView> selectListView(Pagination page,@Param("ew") Wrapper<HuodongfenleiEntity> wrapper);

	
	HuodongfenleiView selectView(@Param("ew") Wrapper<HuodongfenleiEntity> wrapper);
	

}
