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

import com.entity.JiazhengfuwuEntity;
import com.entity.view.JiazhengfuwuView;

import com.service.JiazhengfuwuService;
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
 * 家政服务
 * 后端接口
 * @author 
 * @email 
 * @date 2025-01-10 22:13:24
 */
@RestController
@RequestMapping("/jiazhengfuwu")
public class JiazhengfuwuController {
    @Autowired
    private JiazhengfuwuService jiazhengfuwuService;

    @Autowired
    private StoreupService storeupService;





    



    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,JiazhengfuwuEntity jiazhengfuwu,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("fuwurenyuan")) {
			jiazhengfuwu.setFuwugonghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<JiazhengfuwuEntity> ew = new EntityWrapper<JiazhengfuwuEntity>();



		PageUtils page = jiazhengfuwuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiazhengfuwu), params), params));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }
    
    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,JiazhengfuwuEntity jiazhengfuwu, 
		HttpServletRequest request){
        EntityWrapper<JiazhengfuwuEntity> ew = new EntityWrapper<JiazhengfuwuEntity>();

		PageUtils page = jiazhengfuwuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiazhengfuwu), params), params));
		
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( JiazhengfuwuEntity jiazhengfuwu){
       	EntityWrapper<JiazhengfuwuEntity> ew = new EntityWrapper<JiazhengfuwuEntity>();
      	ew.allEq(MPUtil.allEQMapPre( jiazhengfuwu, "jiazhengfuwu")); 
        return R.ok().put("data", jiazhengfuwuService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(JiazhengfuwuEntity jiazhengfuwu){
        EntityWrapper< JiazhengfuwuEntity> ew = new EntityWrapper< JiazhengfuwuEntity>();
 		ew.allEq(MPUtil.allEQMapPre( jiazhengfuwu, "jiazhengfuwu")); 
		JiazhengfuwuView jiazhengfuwuView =  jiazhengfuwuService.selectView(ew);
		return R.ok("查询家政服务成功").put("data", jiazhengfuwuView);
    }
	
    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        JiazhengfuwuEntity jiazhengfuwu = jiazhengfuwuService.selectById(id);
		jiazhengfuwu.setClicknum(jiazhengfuwu.getClicknum()+1);
		jiazhengfuwu.setClicktime(new Date());
		jiazhengfuwuService.updateById(jiazhengfuwu);
        jiazhengfuwu = jiazhengfuwuService.selectView(new EntityWrapper<JiazhengfuwuEntity>().eq("id", id));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(jiazhengfuwu,deSens);
        return R.ok().put("data", jiazhengfuwu);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        JiazhengfuwuEntity jiazhengfuwu = jiazhengfuwuService.selectById(id);
		jiazhengfuwu.setClicknum(jiazhengfuwu.getClicknum()+1);
		jiazhengfuwu.setClicktime(new Date());
		jiazhengfuwuService.updateById(jiazhengfuwu);
        jiazhengfuwu = jiazhengfuwuService.selectView(new EntityWrapper<JiazhengfuwuEntity>().eq("id", id));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(jiazhengfuwu,deSens);
        return R.ok().put("data", jiazhengfuwu);
    }
    


    /**
     * 赞或踩
     */
    @RequestMapping("/thumbsup/{id}")
    public R vote(@PathVariable("id") String id,String type){
        JiazhengfuwuEntity jiazhengfuwu = jiazhengfuwuService.selectById(id);
        if(type.equals("1")) {
        	jiazhengfuwu.setThumbsupnum(jiazhengfuwu.getThumbsupnum()+1);
        } else {
        	jiazhengfuwu.setCrazilynum(jiazhengfuwu.getCrazilynum()+1);
        }
        jiazhengfuwuService.updateById(jiazhengfuwu);
        return R.ok("投票成功");
    }

    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增家政服务") 
    public R save(@RequestBody JiazhengfuwuEntity jiazhengfuwu, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(jiazhengfuwu);
        jiazhengfuwuService.insert(jiazhengfuwu);
        return R.ok();
    }
    
    /**
     * 前台保存
     */
    @SysLog("新增家政服务")
    @RequestMapping("/add")
    public R add(@RequestBody JiazhengfuwuEntity jiazhengfuwu, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(jiazhengfuwu);
        jiazhengfuwuService.insert(jiazhengfuwu);
        return R.ok().put("data",jiazhengfuwu.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改家政服务")
    public R update(@RequestBody JiazhengfuwuEntity jiazhengfuwu, HttpServletRequest request){
        //ValidatorUtils.validateEntity(jiazhengfuwu);
        //全部更新
        jiazhengfuwuService.updateById(jiazhengfuwu);

        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除家政服务")
    public R delete(@RequestBody Long[] ids){
        jiazhengfuwuService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	
	/**
     * 前台智能排序
     */
	@IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,JiazhengfuwuEntity jiazhengfuwu, HttpServletRequest request,String pre){
        EntityWrapper<JiazhengfuwuEntity> ew = new EntityWrapper<JiazhengfuwuEntity>();
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
		PageUtils page = jiazhengfuwuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiazhengfuwu), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 协同算法（按收藏推荐）
     */
    @RequestMapping("/autoSort2")
    public R autoSort2(@RequestParam Map<String, Object> params,JiazhengfuwuEntity jiazhengfuwu, HttpServletRequest request){
        String userId = request.getSession().getAttribute("userId").toString();
        String inteltypeColumn = "fuwuleibie";
        List<StoreupEntity> storeups = storeupService.selectList(new EntityWrapper<StoreupEntity>().eq("type", 1).eq("userid", userId).eq("tablename", "jiazhengfuwu").orderBy("addtime", false));
        List<String> inteltypes = new ArrayList<String>();
        Integer limit = params.get("limit")==null?10:Integer.parseInt(params.get("limit").toString());
        List<JiazhengfuwuEntity> jiazhengfuwuList = new ArrayList<JiazhengfuwuEntity>();
        //去重
        if(storeups!=null && storeups.size()>0) {
            List<String> typeList = new ArrayList<String>();
            for(StoreupEntity s : storeups) {
                if(typeList.contains(s.getInteltype())) continue;
                typeList.add(s.getInteltype());
                jiazhengfuwuList.addAll(jiazhengfuwuService.selectList(new EntityWrapper<JiazhengfuwuEntity>().eq(inteltypeColumn, s.getInteltype())));
            }
        }
        EntityWrapper<JiazhengfuwuEntity> ew = new EntityWrapper<JiazhengfuwuEntity>();
        params.put("sort", "id");
        params.put("order", "desc");
        PageUtils page = jiazhengfuwuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiazhengfuwu), params), params));
        List<JiazhengfuwuEntity> pageList = (List<JiazhengfuwuEntity>)page.getList();
        if(jiazhengfuwuList.size()<limit) {
            int toAddNum = (limit-jiazhengfuwuList.size())<=pageList.size()?(limit-jiazhengfuwuList.size()):pageList.size();
            for(JiazhengfuwuEntity o1 : pageList) {
                boolean addFlag = true;
                for(JiazhengfuwuEntity o2 : jiazhengfuwuList) {
                    if(o1.getId().intValue()==o2.getId().intValue()) {
                        addFlag = false;
                        break;
                    }
                }
                if(addFlag) {
                    jiazhengfuwuList.add(o1);
                    if(--toAddNum==0) break;
                }
            }
        } else if(jiazhengfuwuList.size()>limit) {
            jiazhengfuwuList = jiazhengfuwuList.subList(0, limit);
        }
        page.setList(jiazhengfuwuList);
        return R.ok().put("data", page);
    }










}
