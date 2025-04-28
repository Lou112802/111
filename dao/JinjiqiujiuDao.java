package com.dao;

import com.entity.JinjiqiujiuEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.JinjiqiujiuVO;
import com.entity.view.JinjiqiujiuView;


/**
 * 紧急求救
 * 
 * @author 
 * @email 
 * @date 2025-01-10 22:13:25
 */
public interface JinjiqiujiuDao extends BaseMapper<JinjiqiujiuEntity> {
	
	List<JinjiqiujiuVO> selectListVO(@Param("ew") Wrapper<JinjiqiujiuEntity> wrapper);
	
	JinjiqiujiuVO selectVO(@Param("ew") Wrapper<JinjiqiujiuEntity> wrapper);
	
	List<JinjiqiujiuView> selectListView(@Param("ew") Wrapper<JinjiqiujiuEntity> wrapper);

	List<JinjiqiujiuView> selectListView(Pagination page,@Param("ew") Wrapper<JinjiqiujiuEntity> wrapper);

	
	JinjiqiujiuView selectView(@Param("ew") Wrapper<JinjiqiujiuEntity> wrapper);
	

}
