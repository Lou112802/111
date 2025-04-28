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

import com.entity.FuwuleibieEntity;
import com.entity.view.FuwuleibieView;

import com.service.FuwuleibieService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 服务类别
 * 后端接口
 * @author 
 * @email 
 * @date 2025-01-10 22:13:24
 */
@RestController
@RequestMapping("/fuwuleibie")
public class FuwuleibieController {
    @Autowired
    private FuwuleibieService fuwuleibieService;






    



    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,FuwuleibieEntity fuwuleibie,
		HttpServletRequest request){
        EntityWrapper<FuwuleibieEntity> ew = new EntityWrapper<FuwuleibieEntity>();



		PageUtils page = fuwuleibieService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, fuwuleibie), params), params));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }
    
    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,FuwuleibieEntity fuwuleibie, 
		HttpServletRequest request){
        EntityWrapper<FuwuleibieEntity> ew = new EntityWrapper<FuwuleibieEntity>();

		PageUtils page = fuwuleibieService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, fuwuleibie), params), params));
		
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( FuwuleibieEntity fuwuleibie){
       	EntityWrapper<FuwuleibieEntity> ew = new EntityWrapper<FuwuleibieEntity>();
      	ew.allEq(MPUtil.allEQMapPre( fuwuleibie, "fuwuleibie")); 
        return R.ok().put("data", fuwuleibieService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(FuwuleibieEntity fuwuleibie){
        EntityWrapper< FuwuleibieEntity> ew = new EntityWrapper< FuwuleibieEntity>();
 		ew.allEq(MPUtil.allEQMapPre( fuwuleibie, "fuwuleibie")); 
		FuwuleibieView fuwuleibieView =  fuwuleibieService.selectView(ew);
		return R.ok("查询服务类别成功").put("data", fuwuleibieView);
    }
	
    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        FuwuleibieEntity fuwuleibie = fuwuleibieService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(fuwuleibie,deSens);
        return R.ok().put("data", fuwuleibie);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        FuwuleibieEntity fuwuleibie = fuwuleibieService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(fuwuleibie,deSens);
        return R.ok().put("data", fuwuleibie);
    }
    



    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增服务类别") 
    public R save(@RequestBody FuwuleibieEntity fuwuleibie, HttpServletRequest request){
        if(fuwuleibieService.selectCount(new EntityWrapper<FuwuleibieEntity>().eq("fuwuleibie", fuwuleibie.getFuwuleibie()))>0) {
            return R.error("服务类别已存在");
        }
    	//ValidatorUtils.validateEntity(fuwuleibie);
        fuwuleibieService.insert(fuwuleibie);
        return R.ok();
    }
    
    /**
     * 前台保存
     */
    @SysLog("新增服务类别")
    @RequestMapping("/add")
    public R add(@RequestBody FuwuleibieEntity fuwuleibie, HttpServletRequest request){
        if(fuwuleibieService.selectCount(new EntityWrapper<FuwuleibieEntity>().eq("fuwuleibie", fuwuleibie.getFuwuleibie()))>0) {
            return R.error("服务类别已存在");
        }
    	//ValidatorUtils.validateEntity(fuwuleibie);
        fuwuleibieService.insert(fuwuleibie);
        return R.ok().put("data",fuwuleibie.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改服务类别")
    public R update(@RequestBody FuwuleibieEntity fuwuleibie, HttpServletRequest request){
        //ValidatorUtils.validateEntity(fuwuleibie);
        if(fuwuleibieService.selectCount(new EntityWrapper<FuwuleibieEntity>().ne("id", fuwuleibie.getId()).eq("fuwuleibie", fuwuleibie.getFuwuleibie()))>0) {
            return R.error("服务类别已存在");
        }
        //全部更新
        fuwuleibieService.updateById(fuwuleibie);

        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除服务类别")
    public R delete(@RequestBody Long[] ids){
        fuwuleibieService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	












}
