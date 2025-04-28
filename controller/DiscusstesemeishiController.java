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

import com.entity.DiscusstesemeishiEntity;
import com.entity.view.DiscusstesemeishiView;

import com.service.DiscusstesemeishiService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 特色美食评论表
 * 后端接口
 * @author 
 * @email 
 * @date 2025-01-10 22:13:27
 */
@RestController
@RequestMapping("/discusstesemeishi")
public class DiscusstesemeishiController {
    @Autowired
    private DiscusstesemeishiService discusstesemeishiService;






    



    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,DiscusstesemeishiEntity discusstesemeishi,
		HttpServletRequest request){
        EntityWrapper<DiscusstesemeishiEntity> ew = new EntityWrapper<DiscusstesemeishiEntity>();



		PageUtils page = discusstesemeishiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discusstesemeishi), params), params));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }
    
    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,DiscusstesemeishiEntity discusstesemeishi, 
		HttpServletRequest request){
        EntityWrapper<DiscusstesemeishiEntity> ew = new EntityWrapper<DiscusstesemeishiEntity>();

		PageUtils page = discusstesemeishiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discusstesemeishi), params), params));
		
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( DiscusstesemeishiEntity discusstesemeishi){
       	EntityWrapper<DiscusstesemeishiEntity> ew = new EntityWrapper<DiscusstesemeishiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( discusstesemeishi, "discusstesemeishi")); 
        return R.ok().put("data", discusstesemeishiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(DiscusstesemeishiEntity discusstesemeishi){
        EntityWrapper< DiscusstesemeishiEntity> ew = new EntityWrapper< DiscusstesemeishiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( discusstesemeishi, "discusstesemeishi")); 
		DiscusstesemeishiView discusstesemeishiView =  discusstesemeishiService.selectView(ew);
		return R.ok("查询特色美食评论表成功").put("data", discusstesemeishiView);
    }
	
    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        DiscusstesemeishiEntity discusstesemeishi = discusstesemeishiService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(discusstesemeishi,deSens);
        return R.ok().put("data", discusstesemeishi);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        DiscusstesemeishiEntity discusstesemeishi = discusstesemeishiService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(discusstesemeishi,deSens);
        return R.ok().put("data", discusstesemeishi);
    }
    



    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增特色美食评论表") 
    public R save(@RequestBody DiscusstesemeishiEntity discusstesemeishi, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(discusstesemeishi);
        discusstesemeishiService.insert(discusstesemeishi);
        return R.ok();
    }
    
    /**
     * 前台保存
     */
    @SysLog("新增特色美食评论表")
    @RequestMapping("/add")
    public R add(@RequestBody DiscusstesemeishiEntity discusstesemeishi, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(discusstesemeishi);
        discusstesemeishiService.insert(discusstesemeishi);
        return R.ok().put("data",discusstesemeishi.getId());
    }



     /**
     * 获取用户密保
     */
    @RequestMapping("/security")
    @IgnoreAuth
    public R security(@RequestParam String username){
        DiscusstesemeishiEntity discusstesemeishi = discusstesemeishiService.selectOne(new EntityWrapper<DiscusstesemeishiEntity>().eq("", username));
        return R.ok().put("data", discusstesemeishi);
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @IgnoreAuth
    public R update(@RequestBody DiscusstesemeishiEntity discusstesemeishi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(discusstesemeishi);
        //全部更新
        discusstesemeishiService.updateById(discusstesemeishi);

        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除特色美食评论表")
    public R delete(@RequestBody Long[] ids){
        discusstesemeishiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	
	/**
     * 前台智能排序
     */
	@IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,DiscusstesemeishiEntity discusstesemeishi, HttpServletRequest request,String pre){
        EntityWrapper<DiscusstesemeishiEntity> ew = new EntityWrapper<DiscusstesemeishiEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
		Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String newKey = entry.getKey();
			if (pre.endsWith(".")) {
				newMap.put(pre + newKey, entry.getValue());
			} else if (StringUtils.isEmpty(pre)) {
				newMap.put(newKey, entry.getValue());
			} else {
				newMap.put(pre + "." + newKey, entry.getValue());
			}
		}
		params.put("sort", "clicktime");
        params.put("order", "desc");
		PageUtils page = discusstesemeishiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discusstesemeishi), params), params));
        return R.ok().put("data", page);
    }












}
