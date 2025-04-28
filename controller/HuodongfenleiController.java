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

import com.entity.HuodongfenleiEntity;
import com.entity.view.HuodongfenleiView;

import com.service.HuodongfenleiService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 活动分类
 * 后端接口
 * @author 
 * @email 
 * @date 2025-01-10 22:13:25
 */
@RestController
@RequestMapping("/huodongfenlei")
public class HuodongfenleiController {
    @Autowired
    private HuodongfenleiService huodongfenleiService;






    



    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,HuodongfenleiEntity huodongfenlei,
		HttpServletRequest request){
        EntityWrapper<HuodongfenleiEntity> ew = new EntityWrapper<HuodongfenleiEntity>();



		PageUtils page = huodongfenleiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, huodongfenlei), params), params));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }
    
    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,HuodongfenleiEntity huodongfenlei, 
		HttpServletRequest request){
        EntityWrapper<HuodongfenleiEntity> ew = new EntityWrapper<HuodongfenleiEntity>();

		PageUtils page = huodongfenleiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, huodongfenlei), params), params));
		
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( HuodongfenleiEntity huodongfenlei){
       	EntityWrapper<HuodongfenleiEntity> ew = new EntityWrapper<HuodongfenleiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( huodongfenlei, "huodongfenlei")); 
        return R.ok().put("data", huodongfenleiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(HuodongfenleiEntity huodongfenlei){
        EntityWrapper< HuodongfenleiEntity> ew = new EntityWrapper< HuodongfenleiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( huodongfenlei, "huodongfenlei")); 
		HuodongfenleiView huodongfenleiView =  huodongfenleiService.selectView(ew);
		return R.ok("查询活动分类成功").put("data", huodongfenleiView);
    }
	
    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        HuodongfenleiEntity huodongfenlei = huodongfenleiService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(huodongfenlei,deSens);
        return R.ok().put("data", huodongfenlei);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        HuodongfenleiEntity huodongfenlei = huodongfenleiService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(huodongfenlei,deSens);
        return R.ok().put("data", huodongfenlei);
    }
    



    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增活动分类") 
    public R save(@RequestBody HuodongfenleiEntity huodongfenlei, HttpServletRequest request){
        if(huodongfenleiService.selectCount(new EntityWrapper<HuodongfenleiEntity>().eq("huodongfenlei", huodongfenlei.getHuodongfenlei()))>0) {
            return R.error("活动分类已存在");
        }
    	//ValidatorUtils.validateEntity(huodongfenlei);
        huodongfenleiService.insert(huodongfenlei);
        return R.ok();
    }
    
    /**
     * 前台保存
     */
    @SysLog("新增活动分类")
    @RequestMapping("/add")
    public R add(@RequestBody HuodongfenleiEntity huodongfenlei, HttpServletRequest request){
        if(huodongfenleiService.selectCount(new EntityWrapper<HuodongfenleiEntity>().eq("huodongfenlei", huodongfenlei.getHuodongfenlei()))>0) {
            return R.error("活动分类已存在");
        }
    	//ValidatorUtils.validateEntity(huodongfenlei);
        huodongfenleiService.insert(huodongfenlei);
        return R.ok().put("data",huodongfenlei.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改活动分类")
    public R update(@RequestBody HuodongfenleiEntity huodongfenlei, HttpServletRequest request){
        //ValidatorUtils.validateEntity(huodongfenlei);
        if(huodongfenleiService.selectCount(new EntityWrapper<HuodongfenleiEntity>().ne("id", huodongfenlei.getId()).eq("huodongfenlei", huodongfenlei.getHuodongfenlei()))>0) {
            return R.error("活动分类已存在");
        }
        //全部更新
        huodongfenleiService.updateById(huodongfenlei);

        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除活动分类")
    public R delete(@RequestBody Long[] ids){
        huodongfenleiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	












}
