package com.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import java.util.Collections;

import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import com.utils.ValidatorUtils;
import com.utils.DeSensUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;
import com.annotation.SysLog;

import com.entity.JinjiqiujiuEntity;
import com.entity.view.JinjiqiujiuView;

import com.service.JinjiqiujiuService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 紧急求救
 * 后端接口
 * @author 
 * @email 
 * @date 2025-01-10 22:13:25
 */
@RestController
@RequestMapping("/jinjiqiujiu")
public class JinjiqiujiuController {
    @Autowired
    private JinjiqiujiuService jinjiqiujiuService;






    



    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,JinjiqiujiuEntity jinjiqiujiu,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("laoren")) {
			jinjiqiujiu.setLaorenzhanghao((String)request.getSession().getAttribute("username"));
		}
		if(tableName.equals("jiashu")) {
			jinjiqiujiu.setJiashuzhanghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<JinjiqiujiuEntity> ew = new EntityWrapper<JinjiqiujiuEntity>();



		PageUtils page = jinjiqiujiuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jinjiqiujiu), params), params));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }
    
    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,JinjiqiujiuEntity jinjiqiujiu, 
		HttpServletRequest request){
        EntityWrapper<JinjiqiujiuEntity> ew = new EntityWrapper<JinjiqiujiuEntity>();

		PageUtils page = jinjiqiujiuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jinjiqiujiu), params), params));
		
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( JinjiqiujiuEntity jinjiqiujiu){
       	EntityWrapper<JinjiqiujiuEntity> ew = new EntityWrapper<JinjiqiujiuEntity>();
      	ew.allEq(MPUtil.allEQMapPre( jinjiqiujiu, "jinjiqiujiu")); 
        return R.ok().put("data", jinjiqiujiuService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(JinjiqiujiuEntity jinjiqiujiu){
        EntityWrapper< JinjiqiujiuEntity> ew = new EntityWrapper< JinjiqiujiuEntity>();
 		ew.allEq(MPUtil.allEQMapPre( jinjiqiujiu, "jinjiqiujiu")); 
		JinjiqiujiuView jinjiqiujiuView =  jinjiqiujiuService.selectView(ew);
		return R.ok("查询紧急求救成功").put("data", jinjiqiujiuView);
    }
	
    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        JinjiqiujiuEntity jinjiqiujiu = jinjiqiujiuService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(jinjiqiujiu,deSens);
        return R.ok().put("data", jinjiqiujiu);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        JinjiqiujiuEntity jinjiqiujiu = jinjiqiujiuService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(jinjiqiujiu,deSens);
        return R.ok().put("data", jinjiqiujiu);
    }
    



    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增紧急求救") 
    public R save(@RequestBody JinjiqiujiuEntity jinjiqiujiu, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(jinjiqiujiu);
        jinjiqiujiuService.insert(jinjiqiujiu);
        return R.ok();
    }
    
    /**
     * 前台保存
     */
    @SysLog("新增紧急求救")
    @RequestMapping("/add")
    public R add(@RequestBody JinjiqiujiuEntity jinjiqiujiu, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(jinjiqiujiu);
        jinjiqiujiuService.insert(jinjiqiujiu);
        return R.ok().put("data",jinjiqiujiu.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改紧急求救")
    public R update(@RequestBody JinjiqiujiuEntity jinjiqiujiu, HttpServletRequest request){
        //ValidatorUtils.validateEntity(jinjiqiujiu);
        //全部更新
        jinjiqiujiuService.updateById(jinjiqiujiu);

        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除紧急求救")
    public R delete(@RequestBody Long[] ids){
        jinjiqiujiuService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	












}
