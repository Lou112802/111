package com.dao;

import com.entity.FuwuleibieEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.FuwuleibieVO;
import com.entity.view.FuwuleibieView;


/**
 * 服务类别
 * 
 * @author 
 * @email 
 * @date 2025-01-10 22:13:24
 */
public interface FuwuleibieDao extends BaseMapper<FuwuleibieEntity> {
	
	List<FuwuleibieVO> selectListVO(@Param("ew") Wrapper<FuwuleibieEntity> wrapper);
	
	FuwuleibieVO selectVO(@Param("ew") Wrapper<FuwuleibieEntity> wrapper);
	
	List<FuwuleibieView> selectListView(@Param("ew") Wrapper<FuwuleibieEntity> wrapper);

	List<FuwuleibieView> selectListView(Pagination page,@Param("ew") Wrapper<FuwuleibieEntity> wrapper);

	
	FuwuleibieView selectView(@Param("ew") Wrapper<FuwuleibieEntity> wrapper);
	

}
