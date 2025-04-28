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
import com.entity.TokenEntity;
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

import com.entity.FuwurenyuanEntity;
import com.entity.view.FuwurenyuanView;

import com.service.FuwurenyuanService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 服务人员
 * 后端接口
 * @author 
 * @email 
 * @date 2025-01-10 22:13:24
 */
@RestController
@RequestMapping("/fuwurenyuan")
public class FuwurenyuanController {
    @Autowired
    private FuwurenyuanService fuwurenyuanService;






    
	@Autowired
	private TokenService tokenService;
	
	/**
	 * 登录
	 */
	@IgnoreAuth
	@RequestMapping(value = "/login")
	public R login(String username, String password, String captcha, HttpServletRequest request) {
		FuwurenyuanEntity u = fuwurenyuanService.selectOne(new EntityWrapper<FuwurenyuanEntity>().eq("fuwugonghao", username));
        if(u!=null && u.getStatus().intValue()==1) {
            return R.error("账号已锁定，请联系管理员。");
        }
		if(u==null || !u.getMima().equals(password)) {
            if(u!=null) {
                u.setPasswordwrongnum(u.getPasswordwrongnum()+1);
                if(u.getPasswordwrongnum()>=3) {
                    u.setStatus(1);
                }
                fuwurenyuanService.updateById(u);
            }
			return R.error("账号或密码不正确");
		}
		
		String token = tokenService.generateToken(u.getId(), username,"fuwurenyuan",  "服务人员" );
		return R.ok().put("token", token);
	}


	
	/**
     * 注册
     */
	@IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody FuwurenyuanEntity fuwurenyuan){
    	//ValidatorUtils.validateEntity(fuwurenyuan);
    	FuwurenyuanEntity u = fuwurenyuanService.selectOne(new EntityWrapper<FuwurenyuanEntity>().eq("fuwugonghao", fuwurenyuan.getFuwugonghao()));
		if(u!=null) {
			return R.error("注册用户已存在");
		}
		Long uId = new Date().getTime();
		fuwurenyuan.setId(uId);
        fuwurenyuanService.insert(fuwurenyuan);
        return R.ok();
    }

	
	/**
	 * 退出
	 */
	@RequestMapping("/logout")
	public R logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return R.ok("退出成功");
	}
	
	/**
     * 获取用户的session用户信息
     */
    @RequestMapping("/session")
    public R getCurrUser(HttpServletRequest request){
    	Long id = (Long)request.getSession().getAttribute("userId");
        FuwurenyuanEntity u = fuwurenyuanService.selectById(id);
        return R.ok().put("data", u);
    }
    
    /**
     * 密码重置
     */
    @IgnoreAuth
	@RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request){
    	FuwurenyuanEntity u = fuwurenyuanService.selectOne(new EntityWrapper<FuwurenyuanEntity>().eq("fuwugonghao", username));
    	if(u==null) {
    		return R.error("账号不存在");
    	}
        u.setMima("123456");
        fuwurenyuanService.updateById(u);
        return R.ok("密码已重置为：123456");
    }



    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,FuwurenyuanEntity fuwurenyuan,
		HttpServletRequest request){
        EntityWrapper<FuwurenyuanEntity> ew = new EntityWrapper<FuwurenyuanEntity>();



		PageUtils page = fuwurenyuanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, fuwurenyuan), params), params));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }
    
    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,FuwurenyuanEntity fuwurenyuan, 
		HttpServletRequest request){
        EntityWrapper<FuwurenyuanEntity> ew = new EntityWrapper<FuwurenyuanEntity>();

		PageUtils page = fuwurenyuanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, fuwurenyuan), params), params));
		
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( FuwurenyuanEntity fuwurenyuan){
       	EntityWrapper<FuwurenyuanEntity> ew = new EntityWrapper<FuwurenyuanEntity>();
      	ew.allEq(MPUtil.allEQMapPre( fuwurenyuan, "fuwurenyuan")); 
        return R.ok().put("data", fuwurenyuanService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(FuwurenyuanEntity fuwurenyuan){
        EntityWrapper< FuwurenyuanEntity> ew = new EntityWrapper< FuwurenyuanEntity>();
 		ew.allEq(MPUtil.allEQMapPre( fuwurenyuan, "fuwurenyuan")); 
		FuwurenyuanView fuwurenyuanView =  fuwurenyuanService.selectView(ew);
		return R.ok("查询服务人员成功").put("data", fuwurenyuanView);
    }
	
    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        FuwurenyuanEntity fuwurenyuan = fuwurenyuanService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(fuwurenyuan,deSens);
        return R.ok().put("data", fuwurenyuan);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        FuwurenyuanEntity fuwurenyuan = fuwurenyuanService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(fuwurenyuan,deSens);
        return R.ok().put("data", fuwurenyuan);
    }
    



    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增服务人员") 
    public R save(@RequestBody FuwurenyuanEntity fuwurenyuan, HttpServletRequest request){
        if(fuwurenyuanService.selectCount(new EntityWrapper<FuwurenyuanEntity>().eq("fuwugonghao", fuwurenyuan.getFuwugonghao()))>0) {
            return R.error("服务工号已存在");
        }
    	fuwurenyuan.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(fuwurenyuan);
    	FuwurenyuanEntity u = fuwurenyuanService.selectOne(new EntityWrapper<FuwurenyuanEntity>().eq("fuwugonghao", fuwurenyuan.getFuwugonghao()));
		if(u!=null) {
			return R.error("用户已存在");
		}
		fuwurenyuan.setId(new Date().getTime());
        fuwurenyuanService.insert(fuwurenyuan);
        return R.ok();
    }
    
    /**
     * 前台保存
     */
    @SysLog("新增服务人员")
    @RequestMapping("/add")
    public R add(@RequestBody FuwurenyuanEntity fuwurenyuan, HttpServletRequest request){
        if(fuwurenyuanService.selectCount(new EntityWrapper<FuwurenyuanEntity>().eq("fuwugonghao", fuwurenyuan.getFuwugonghao()))>0) {
            return R.error("服务工号已存在");
        }
    	fuwurenyuan.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(fuwurenyuan);
    	FuwurenyuanEntity u = fuwurenyuanService.selectOne(new EntityWrapper<FuwurenyuanEntity>().eq("fuwugonghao", fuwurenyuan.getFuwugonghao()));
		if(u!=null) {
			return R.error("用户已存在");
		}
		fuwurenyuan.setId(new Date().getTime());
        fuwurenyuanService.insert(fuwurenyuan);
        return R.ok().put("data",fuwurenyuan.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改服务人员")
    public R update(@RequestBody FuwurenyuanEntity fuwurenyuan, HttpServletRequest request){
        //ValidatorUtils.validateEntity(fuwurenyuan);
        if(fuwurenyuanService.selectCount(new EntityWrapper<FuwurenyuanEntity>().ne("id", fuwurenyuan.getId()).eq("fuwugonghao", fuwurenyuan.getFuwugonghao()))>0) {
            return R.error("服务工号已存在");
        }
        //全部更新
        fuwurenyuanService.updateById(fuwurenyuan);
    if(null!=fuwurenyuan.getFuwugonghao())
    {
        // 修改token
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUsername(fuwurenyuan.getFuwugonghao());
        tokenService.update(tokenEntity, new EntityWrapper<TokenEntity>().eq("userid", fuwurenyuan.getId()));
    }


        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除服务人员")
    public R delete(@RequestBody Long[] ids){
        fuwurenyuanService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	








    /**
     * 总数量
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params,FuwurenyuanEntity fuwurenyuan, HttpServletRequest request){
        EntityWrapper<FuwurenyuanEntity> ew = new EntityWrapper<FuwurenyuanEntity>();
        int count = fuwurenyuanService.selectCount(MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, fuwurenyuan), params), params));
        return R.ok().put("data", count);
    }




}
