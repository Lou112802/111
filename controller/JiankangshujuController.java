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

import com.entity.JiankangshujuEntity;
import com.entity.view.JiankangshujuView;

import com.service.JiankangshujuService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 健康数据
 * 后端接口
 * @author 
 * @email 
 * @date 2025-01-10 22:13:25
 */
@RestController
@RequestMapping("/jiankangshuju")
public class JiankangshujuController {
    @Autowired
    private JiankangshujuService jiankangshujuService;






    



    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,JiankangshujuEntity jiankangshuju,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("laoren")) {
			jiankangshuju.setLaorenzhanghao((String)request.getSession().getAttribute("username"));
		}
		if(tableName.equals("jiashu")) {
			jiankangshuju.setJiashuzhanghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<JiankangshujuEntity> ew = new EntityWrapper<JiankangshujuEntity>();



		PageUtils page = jiankangshujuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiankangshuju), params), params));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }
    
    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,JiankangshujuEntity jiankangshuju, 
		HttpServletRequest request){
        EntityWrapper<JiankangshujuEntity> ew = new EntityWrapper<JiankangshujuEntity>();

		PageUtils page = jiankangshujuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiankangshuju), params), params));
		
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( JiankangshujuEntity jiankangshuju){
       	EntityWrapper<JiankangshujuEntity> ew = new EntityWrapper<JiankangshujuEntity>();
      	ew.allEq(MPUtil.allEQMapPre( jiankangshuju, "jiankangshuju")); 
        return R.ok().put("data", jiankangshujuService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(JiankangshujuEntity jiankangshuju){
        EntityWrapper< JiankangshujuEntity> ew = new EntityWrapper< JiankangshujuEntity>();
 		ew.allEq(MPUtil.allEQMapPre( jiankangshuju, "jiankangshuju")); 
		JiankangshujuView jiankangshujuView =  jiankangshujuService.selectView(ew);
		return R.ok("查询健康数据成功").put("data", jiankangshujuView);
    }
	
    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        JiankangshujuEntity jiankangshuju = jiankangshujuService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(jiankangshuju,deSens);
        return R.ok().put("data", jiankangshuju);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        JiankangshujuEntity jiankangshuju = jiankangshujuService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(jiankangshuju,deSens);
        return R.ok().put("data", jiankangshuju);
    }
    



    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增健康数据") 
    public R save(@RequestBody JiankangshujuEntity jiankangshuju, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(jiankangshuju);
        jiankangshujuService.insert(jiankangshuju);
        return R.ok();
    }
    
    /**
     * 前台保存
     */
    @SysLog("新增健康数据")
    @RequestMapping("/add")
    public R add(@RequestBody JiankangshujuEntity jiankangshuju, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(jiankangshuju);
        jiankangshujuService.insert(jiankangshuju);
        return R.ok().put("data",jiankangshuju.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改健康数据")
    public R update(@RequestBody JiankangshujuEntity jiankangshuju, HttpServletRequest request){
        //ValidatorUtils.validateEntity(jiankangshuju);
        //全部更新
        jiankangshujuService.updateById(jiankangshuju);

        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除健康数据")
    public R delete(@RequestBody Long[] ids){
        jiankangshujuService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {

        Wrapper<JiankangshujuEntity> wrapper = new EntityWrapper<JiankangshujuEntity>();

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

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("laoren")) {
			wrapper.andNew().eq("laorenzhanghao", (String)request.getSession().getAttribute("username"));
		}
		if(tableName.equals("jiashu")) {
			wrapper.andNew().eq("jiashuzhanghao", (String)request.getSession().getAttribute("username"));
		}
        List<JiankangshujuEntity> list = jiankangshujuService.selectList(wrapper);
        Map<String,Object> res= new HashMap<>();
        res.put("count", list.size());
        res.put("data",list.stream().map(JiankangshujuEntity::getLaorenxingming).collect(Collectors.toList()));
        return R.ok(res);
	}
	












}
