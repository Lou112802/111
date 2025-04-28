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

import com.entity.MeishidingdanEntity;
import com.entity.view.MeishidingdanView;

import com.service.MeishidingdanService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 美食订单
 * 后端接口
 * @author 
 * @email 
 * @date 2025-01-10 22:13:24
 */
@RestController
@RequestMapping("/meishidingdan")
public class MeishidingdanController {
    @Autowired
    private MeishidingdanService meishidingdanService;






    



    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,MeishidingdanEntity meishidingdan,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date diancanshijianstart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date diancanshijianend,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("laoren")) {
			meishidingdan.setLaorenzhanghao((String)request.getSession().getAttribute("username"));
		}
		if(tableName.equals("jiashu")) {
			meishidingdan.setJiashuzhanghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<MeishidingdanEntity> ew = new EntityWrapper<MeishidingdanEntity>();
                if(diancanshijianstart!=null) ew.ge("diancanshijian", diancanshijianstart);
                if(diancanshijianend!=null) ew.le("diancanshijian", diancanshijianend);



		PageUtils page = meishidingdanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, meishidingdan), params), params));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }
    
    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,MeishidingdanEntity meishidingdan, 
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date diancanshijianstart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date diancanshijianend,
		HttpServletRequest request){
        EntityWrapper<MeishidingdanEntity> ew = new EntityWrapper<MeishidingdanEntity>();
                if(diancanshijianstart!=null) ew.ge("diancanshijian", diancanshijianstart);
                if(diancanshijianend!=null) ew.le("diancanshijian", diancanshijianend);

		PageUtils page = meishidingdanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, meishidingdan), params), params));
		
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( MeishidingdanEntity meishidingdan){
       	EntityWrapper<MeishidingdanEntity> ew = new EntityWrapper<MeishidingdanEntity>();
      	ew.allEq(MPUtil.allEQMapPre( meishidingdan, "meishidingdan")); 
        return R.ok().put("data", meishidingdanService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(MeishidingdanEntity meishidingdan){
        EntityWrapper< MeishidingdanEntity> ew = new EntityWrapper< MeishidingdanEntity>();
 		ew.allEq(MPUtil.allEQMapPre( meishidingdan, "meishidingdan")); 
		MeishidingdanView meishidingdanView =  meishidingdanService.selectView(ew);
		return R.ok("查询美食订单成功").put("data", meishidingdanView);
    }
	
    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        MeishidingdanEntity meishidingdan = meishidingdanService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(meishidingdan,deSens);
        return R.ok().put("data", meishidingdan);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        MeishidingdanEntity meishidingdan = meishidingdanService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(meishidingdan,deSens);
        return R.ok().put("data", meishidingdan);
    }
    



    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增美食订单") 
    public R save(@RequestBody MeishidingdanEntity meishidingdan, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(meishidingdan);
        meishidingdanService.insert(meishidingdan);
        return R.ok();
    }
    
    /**
     * 前台保存
     */
    @SysLog("新增美食订单")
    @RequestMapping("/add")
    public R add(@RequestBody MeishidingdanEntity meishidingdan, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(meishidingdan);
        meishidingdanService.insert(meishidingdan);
        return R.ok().put("data",meishidingdan.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改美食订单")
    public R update(@RequestBody MeishidingdanEntity meishidingdan, HttpServletRequest request){
        //ValidatorUtils.validateEntity(meishidingdan);
        //全部更新
        meishidingdanService.updateById(meishidingdan);

        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除美食订单")
    public R delete(@RequestBody Long[] ids){
        meishidingdanService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	








        /**
     * （按值统计）
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}")
    public R value(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName,HttpServletRequest request) throws IOException {
        java.nio.file.Path path = java.nio.file.Paths.get("value_meishidingdan_" + xColumnName + "_" + yColumnName + "_timeType.json");
        if(java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }else{
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("yColumn", yColumnName);
        EntityWrapper<MeishidingdanEntity> ew = new EntityWrapper<MeishidingdanEntity>();
        String tableName = request.getSession().getAttribute("tableName").toString();
                                        if(tableName.equals("laoren")) {
            ew.eq("laorenzhanghao", (String)request.getSession().getAttribute("username"));
        }
                            if(tableName.equals("jiashu")) {
            ew.eq("jiashuzhanghao", (String)request.getSession().getAttribute("username"));
        }
                        List<Map<String, Object>> result = meishidingdanService.selectValue(params, ew);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        Collections.sort(result, (map1, map2) -> {
            // 假设 total 总是存在并且是数值类型
            Number total1 = (Number) map1.get("total");
            Number total2 = (Number) map2.get("total");
            return Double.compare(total2.doubleValue(), total1.doubleValue());
        });
        return R.ok().put("data", result);
        }
    }
    
    /**
     * （按值统计(多)）
     */
    @RequestMapping("/valueMul/{xColumnName}")
    public R valueMul(@PathVariable("xColumnName") String xColumnName,@RequestParam String yColumnNameMul,HttpServletRequest request)  throws IOException {
        java.nio.file.Path path = java.nio.file.Paths.get("value_meishidingdan_" + xColumnName + "_" + yColumnNameMul + "_timeType.json");
        if(java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }else{
        String[] yColumnNames = yColumnNameMul.split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String,Object>>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EntityWrapper<MeishidingdanEntity> ew = new EntityWrapper<MeishidingdanEntity>();
String tableName = request.getSession().getAttribute("tableName").toString();
                                        if(tableName.equals("laoren")) {
            ew.eq("laorenzhanghao", (String)request.getSession().getAttribute("username"));
        }
                            if(tableName.equals("jiashu")) {
            ew.eq("jiashuzhanghao", (String)request.getSession().getAttribute("username"));
        }
                    for(int i=0;i<yColumnNames.length;i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = meishidingdanService.selectValue(params, ew);
            for(Map<String, Object> m : result) {
                for(String k : m.keySet()) {
                    if(m.get(k) instanceof Date) {
                        m.put(k, sdf.format((Date)m.get(k)));
                    }
                }
            }
            result2.add(result);
        }
        return R.ok().put("data", result2);
    }
}
    
    /**
     * （按值统计）时间统计类型
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}/{timeStatType}")
    public R valueDay(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType,HttpServletRequest request) throws IOException {
        java.nio.file.Path path = java.nio.file.Paths.get("value_meishidingdan_" + xColumnName + "_" + yColumnName + "_"+timeStatType+".json");
        if(java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }else{
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("xColumn", xColumnName);
            params.put("yColumn", yColumnName);
            params.put("timeStatType", timeStatType);
            EntityWrapper<MeishidingdanEntity> ew = new EntityWrapper<MeishidingdanEntity>();
    String tableName = request.getSession().getAttribute("tableName").toString();
                                                                                                            if(tableName.equals("laoren")) {
                ew.eq("laorenzhanghao", (String)request.getSession().getAttribute("username"));
            }
                                                                    if(tableName.equals("jiashu")) {
                ew.eq("jiashuzhanghao", (String)request.getSession().getAttribute("username"));
            }
                                                        List<Map<String, Object>> result = meishidingdanService.selectTimeStatValue(params, ew);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for(Map<String, Object> m : result) {
                for(String k : m.keySet()) {
                    if(m.get(k) instanceof Date) {
                        m.put(k, sdf.format((Date)m.get(k)));
                    }
                }
            }
            return R.ok().put("data", result);
        }
    }
    
        /**
     * （按值统计）时间统计类型(多)
     */
    @RequestMapping("/valueMul/{xColumnName}/{timeStatType}")
    public R valueMulDay(@PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType,@RequestParam String yColumnNameMul,HttpServletRequest request) throws IOException
    {
        java.nio.file.Path path = java.nio.file.Paths.get("value_meishidingdan_" + xColumnName + "_" + yColumnNameMul + "_" + timeStatType + ".json");
        if (java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }else{
            String[] yColumnNames = yColumnNameMul.split(",");
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("xColumn", xColumnName);
            params.put("timeStatType", timeStatType);
            List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String,Object>>>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            EntityWrapper<MeishidingdanEntity> ew = new EntityWrapper<MeishidingdanEntity>();
    String tableName = request.getSession().getAttribute("tableName").toString();
                                                                                                            if(tableName.equals("laoren")) {
                ew.eq("laorenzhanghao", (String)request.getSession().getAttribute("username"));
            }
                                                                    if(tableName.equals("jiashu")) {
                ew.eq("jiashuzhanghao", (String)request.getSession().getAttribute("username"));
            }
                                                for(int i=0;i<yColumnNames.length;i++) {
                params.put("yColumn", yColumnNames[i]);
                List<Map<String, Object>> result = meishidingdanService.selectTimeStatValue(params, ew);
                for(Map<String, Object> m : result) {
                    for(String k : m.keySet()) {
                        if(m.get(k) instanceof Date) {
                            m.put(k, sdf.format((Date)m.get(k)));
                        }
                    }
                }
                result2.add(result);
            }
            return R.ok().put("data", result2);
        }
    }
    
        /**
     * 分组统计
     */
    @RequestMapping("/group/{columnName}")
    public R group(@PathVariable("columnName") String columnName,HttpServletRequest request) throws IOException {
        java.nio.file.Path path = java.nio.file.Paths.get("group_meishidingdan_" + columnName + "_timeType.json");
        if(java.nio.file.Files.exists(path)){
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }else{
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("column", columnName);
        EntityWrapper<MeishidingdanEntity> ew = new EntityWrapper<MeishidingdanEntity>();
String tableName = request.getSession().getAttribute("tableName").toString();
                                        if(tableName.equals("laoren")) {
            ew.eq("laorenzhanghao", (String)request.getSession().getAttribute("username"));
        }
                            if(tableName.equals("jiashu")) {
            ew.eq("jiashuzhanghao", (String)request.getSession().getAttribute("username"));
        }
                        List<Map<String, Object>> result = meishidingdanService.selectGroup(params, ew);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
        }
    }    
    
    




    /**
     * 总数量
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params,MeishidingdanEntity meishidingdan, HttpServletRequest request){
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("laoren")) {
            meishidingdan.setLaorenzhanghao((String)request.getSession().getAttribute("username"));
        }
        if(tableName.equals("jiashu")) {
            meishidingdan.setJiashuzhanghao((String)request.getSession().getAttribute("username"));
        }
        EntityWrapper<MeishidingdanEntity> ew = new EntityWrapper<MeishidingdanEntity>();
        int count = meishidingdanService.selectCount(MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, meishidingdan), params), params));
        return R.ok().put("data", count);
    }




}
