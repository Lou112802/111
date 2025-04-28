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

import com.entity.ShequhuodongEntity;
import com.entity.view.ShequhuodongView;

import com.service.ShequhuodongService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;
import com.service.StoreupService;
import com.entity.StoreupEntity;

/**
 * 社区活动
 * 后端接口
 * @author 
 * @email 
 * @date 2025-01-10 22:13:25
 */
@RestController
@RequestMapping("/shequhuodong")
public class ShequhuodongController {
    @Autowired
    private ShequhuodongService shequhuodongService;

    @Autowired
    private StoreupService storeupService;





    



    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,ShequhuodongEntity shequhuodong,
		HttpServletRequest request){
        EntityWrapper<ShequhuodongEntity> ew = new EntityWrapper<ShequhuodongEntity>();



		PageUtils page = shequhuodongService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shequhuodong), params), params));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }
    
    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,ShequhuodongEntity shequhuodong, 
		HttpServletRequest request){
        EntityWrapper<ShequhuodongEntity> ew = new EntityWrapper<ShequhuodongEntity>();

		PageUtils page = shequhuodongService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shequhuodong), params), params));
		
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( ShequhuodongEntity shequhuodong){
       	EntityWrapper<ShequhuodongEntity> ew = new EntityWrapper<ShequhuodongEntity>();
      	ew.allEq(MPUtil.allEQMapPre( shequhuodong, "shequhuodong")); 
        return R.ok().put("data", shequhuodongService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ShequhuodongEntity shequhuodong){
        EntityWrapper< ShequhuodongEntity> ew = new EntityWrapper< ShequhuodongEntity>();
 		ew.allEq(MPUtil.allEQMapPre( shequhuodong, "shequhuodong")); 
		ShequhuodongView shequhuodongView =  shequhuodongService.selectView(ew);
		return R.ok("查询社区活动成功").put("data", shequhuodongView);
    }
	
    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ShequhuodongEntity shequhuodong = shequhuodongService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(shequhuodong,deSens);
        return R.ok().put("data", shequhuodong);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ShequhuodongEntity shequhuodong = shequhuodongService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(shequhuodong,deSens);
        return R.ok().put("data", shequhuodong);
    }
    


    /**
     * 赞或踩
     */
    @RequestMapping("/thumbsup/{id}")
    public R vote(@PathVariable("id") String id,String type){
        ShequhuodongEntity shequhuodong = shequhuodongService.selectById(id);
        if(type.equals("1")) {
        	shequhuodong.setThumbsupnum(shequhuodong.getThumbsupnum()+1);
        } else {
        	shequhuodong.setCrazilynum(shequhuodong.getCrazilynum()+1);
        }
        shequhuodongService.updateById(shequhuodong);
        return R.ok("投票成功");
    }

    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增社区活动") 
    public R save(@RequestBody ShequhuodongEntity shequhuodong, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(shequhuodong);
        shequhuodongService.insert(shequhuodong);
        return R.ok();
    }
    
    /**
     * 前台保存
     */
    @SysLog("新增社区活动")
    @RequestMapping("/add")
    public R add(@RequestBody ShequhuodongEntity shequhuodong, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(shequhuodong);
        shequhuodongService.insert(shequhuodong);
        return R.ok().put("data",shequhuodong.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改社区活动")
    public R update(@RequestBody ShequhuodongEntity shequhuodong, HttpServletRequest request){
        //ValidatorUtils.validateEntity(shequhuodong);
        //全部更新
        shequhuodongService.updateById(shequhuodong);

        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除社区活动")
    public R delete(@RequestBody Long[] ids){
        shequhuodongService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	












}
