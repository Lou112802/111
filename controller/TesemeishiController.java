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

import com.entity.TesemeishiEntity;
import com.entity.view.TesemeishiView;

import com.service.TesemeishiService;
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
 * 特色美食
 * 后端接口
 * @author 
 * @email 
 * @date 2025-01-10 22:13:24
 */
@RestController
@RequestMapping("/tesemeishi")
public class TesemeishiController {
    @Autowired
    private TesemeishiService tesemeishiService;

    @Autowired
    private StoreupService storeupService;





    



    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,TesemeishiEntity tesemeishi,
		HttpServletRequest request){
        EntityWrapper<TesemeishiEntity> ew = new EntityWrapper<TesemeishiEntity>();



		PageUtils page = tesemeishiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, tesemeishi), params), params));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }
    
    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,TesemeishiEntity tesemeishi, 
		HttpServletRequest request){
        EntityWrapper<TesemeishiEntity> ew = new EntityWrapper<TesemeishiEntity>();

		PageUtils page = tesemeishiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, tesemeishi), params), params));
		
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( TesemeishiEntity tesemeishi){
       	EntityWrapper<TesemeishiEntity> ew = new EntityWrapper<TesemeishiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( tesemeishi, "tesemeishi")); 
        return R.ok().put("data", tesemeishiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(TesemeishiEntity tesemeishi){
        EntityWrapper< TesemeishiEntity> ew = new EntityWrapper< TesemeishiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( tesemeishi, "tesemeishi")); 
		TesemeishiView tesemeishiView =  tesemeishiService.selectView(ew);
		return R.ok("查询特色美食成功").put("data", tesemeishiView);
    }
	
    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        TesemeishiEntity tesemeishi = tesemeishiService.selectById(id);
		tesemeishi.setClicknum(tesemeishi.getClicknum()+1);
		tesemeishi.setClicktime(new Date());
		tesemeishiService.updateById(tesemeishi);
        tesemeishi = tesemeishiService.selectView(new EntityWrapper<TesemeishiEntity>().eq("id", id));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(tesemeishi,deSens);
        return R.ok().put("data", tesemeishi);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        TesemeishiEntity tesemeishi = tesemeishiService.selectById(id);
		tesemeishi.setClicknum(tesemeishi.getClicknum()+1);
		tesemeishi.setClicktime(new Date());
		tesemeishiService.updateById(tesemeishi);
        tesemeishi = tesemeishiService.selectView(new EntityWrapper<TesemeishiEntity>().eq("id", id));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(tesemeishi,deSens);
        return R.ok().put("data", tesemeishi);
    }
    


    /**
     * 赞或踩
     */
    @RequestMapping("/thumbsup/{id}")
    public R vote(@PathVariable("id") String id,String type){
        TesemeishiEntity tesemeishi = tesemeishiService.selectById(id);
        if(type.equals("1")) {
        	tesemeishi.setThumbsupnum(tesemeishi.getThumbsupnum()+1);
        } else {
        	tesemeishi.setCrazilynum(tesemeishi.getCrazilynum()+1);
        }
        tesemeishiService.updateById(tesemeishi);
        return R.ok("投票成功");
    }

    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增特色美食") 
    public R save(@RequestBody TesemeishiEntity tesemeishi, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(tesemeishi);
        tesemeishiService.insert(tesemeishi);
        return R.ok();
    }
    
    /**
     * 前台保存
     */
    @SysLog("新增特色美食")
    @RequestMapping("/add")
    public R add(@RequestBody TesemeishiEntity tesemeishi, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(tesemeishi);
        tesemeishiService.insert(tesemeishi);
        return R.ok().put("data",tesemeishi.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改特色美食")
    public R update(@RequestBody TesemeishiEntity tesemeishi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(tesemeishi);
        //全部更新
        tesemeishiService.updateById(tesemeishi);

        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除特色美食")
    public R delete(@RequestBody Long[] ids){
        tesemeishiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {

        Wrapper<TesemeishiEntity> wrapper = new EntityWrapper<TesemeishiEntity>();

        // 从map中获取remindStart和remindEnd
    Object remindStart = map.get("remindstart") != null ? map.get("remindstart").toString() : null;
    Object remindEnd = map.get("remindend") != null ? map.get("remindend").toString() : null;
        if ("2".equals(type)) {
            Date startDate = null;
            Date endDate = null;
            if (null != remindStart) {
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(remindStart.toString()));
                startDate = c.getTime();
            }
            if (null != remindEnd) {
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(remindEnd.toString()));
                endDate = c.getTime();
            }
            if (startDate != null && endDate != null) {
                if (startDate.before(endDate)) {
                    wrapper.ge(columnName, startDate).le(columnName, endDate);
                } else {
                    wrapper.ge(columnName, startDate).or().le(columnName, endDate);
                }
            } else if (startDate == null && endDate != null) {
                wrapper.le(columnName, endDate);
            } else if (startDate != null && endDate == null) {
                wrapper.ge(columnName, startDate);
            }
        } else {
            if (remindStart != null && remindEnd != null) {
                if (Double.parseDouble(remindStart.toString()) < Double.parseDouble(remindEnd.toString())) {
                    wrapper.ge(columnName, remindStart).le(columnName, remindEnd);
                } else {
                    wrapper.ge(columnName, remindStart).or().le(columnName, remindEnd);
                }
            } else if (remindStart == null && remindEnd != null) {
                wrapper.le(columnName, remindEnd);
            } else if (remindStart != null && remindEnd == null) {
                wrapper.ge(columnName, remindStart);
            }
        }

        List<TesemeishiEntity> list = tesemeishiService.selectList(wrapper);
        Map<String,Object> res= new HashMap<>();
        res.put("count", list.size());
        res.put("data",list.stream().map(TesemeishiEntity::getMeishimingcheng).collect(Collectors.toList()));
        return R.ok(res);
	}
	
	/**
     * 前台智能排序
     */
	@IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,TesemeishiEntity tesemeishi, HttpServletRequest request,String pre){
        EntityWrapper<TesemeishiEntity> ew = new EntityWrapper<TesemeishiEntity>();
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
		params.put("sort", "clicknum");
        params.put("order", "desc");
		PageUtils page = tesemeishiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, tesemeishi), params), params));
        return R.ok().put("data", page);
    }












}
