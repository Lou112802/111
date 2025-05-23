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

import com.entity.LaorenEntity;
import com.entity.view.LaorenView;

import com.service.LaorenService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 老人
 * 后端接口
 * @author 
 * @email 
 * @date 2025-01-10 22:13:24
 */
@RestController
@RequestMapping("/laoren")
public class LaorenController {
    @Autowired
    private LaorenService laorenService;






    
	@Autowired
	private TokenService tokenService;
	
	/**
	 * 登录
	 */
	@IgnoreAuth
	@RequestMapping(value = "/login")
	public R login(String username, String password, String captcha, HttpServletRequest request) {
		LaorenEntity u = laorenService.selectOne(new EntityWrapper<LaorenEntity>().eq("laorenzhanghao", username));
        if(u!=null && u.getStatus().intValue()==1) {
            return R.error("账号已锁定，请联系管理员。");
        }
		if(u==null || !u.getMima().equals(password)) {
            if(u!=null) {
                u.setPasswordwrongnum(u.getPasswordwrongnum()+1);
                if(u.getPasswordwrongnum()>=3) {
                    u.setStatus(1);
                }
                laorenService.updateById(u);
            }
			return R.error("账号或密码不正确");
		}
		
		String token = tokenService.generateToken(u.getId(), username,"laoren",  "老人" );
		return R.ok().put("token", token);
	}


	
	/**
     * 注册
     */
	@IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody LaorenEntity laoren){
    	//ValidatorUtils.validateEntity(laoren);
    	LaorenEntity u = laorenService.selectOne(new EntityWrapper<LaorenEntity>().eq("laorenzhanghao", laoren.getLaorenzhanghao()));
		if(u!=null) {
			return R.error("注册用户已存在");
		}
		Long uId = new Date().getTime();
		laoren.setId(uId);
        laorenService.insert(laoren);
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
        LaorenEntity u = laorenService.selectById(id);
        return R.ok().put("data", u);
    }
    
    /**
     * 密码重置
     */
    @IgnoreAuth
	@RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request){
    	LaorenEntity u = laorenService.selectOne(new EntityWrapper<LaorenEntity>().eq("laorenzhanghao", username));
    	if(u==null) {
    		return R.error("账号不存在");
    	}
        u.setMima("123456");
        laorenService.updateById(u);
        return R.ok("密码已重置为：123456");
    }



    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,LaorenEntity laoren,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("jiashu")) {
			laoren.setJiashuzhanghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<LaorenEntity> ew = new EntityWrapper<LaorenEntity>();



		PageUtils page = laorenService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, laoren), params), params));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }
    
    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,LaorenEntity laoren, 
		HttpServletRequest request){
        EntityWrapper<LaorenEntity> ew = new EntityWrapper<LaorenEntity>();

		PageUtils page = laorenService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, laoren), params), params));
		
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( LaorenEntity laoren){
       	EntityWrapper<LaorenEntity> ew = new EntityWrapper<LaorenEntity>();
      	ew.allEq(MPUtil.allEQMapPre( laoren, "laoren")); 
        return R.ok().put("data", laorenService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(LaorenEntity laoren){
        EntityWrapper< LaorenEntity> ew = new EntityWrapper< LaorenEntity>();
 		ew.allEq(MPUtil.allEQMapPre( laoren, "laoren")); 
		LaorenView laorenView =  laorenService.selectView(ew);
		return R.ok("查询老人成功").put("data", laorenView);
    }
	
    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        LaorenEntity laoren = laorenService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(laoren,deSens);
        return R.ok().put("data", laoren);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        LaorenEntity laoren = laorenService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(laoren,deSens);
        return R.ok().put("data", laoren);
    }
    



    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增老人") 
    public R save(@RequestBody LaorenEntity laoren, HttpServletRequest request){
        if(laorenService.selectCount(new EntityWrapper<LaorenEntity>().eq("laorenzhanghao", laoren.getLaorenzhanghao()))>0) {
            return R.error("老人账号已存在");
        }
    	laoren.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(laoren);
    	LaorenEntity u = laorenService.selectOne(new EntityWrapper<LaorenEntity>().eq("laorenzhanghao", laoren.getLaorenzhanghao()));
		if(u!=null) {
			return R.error("用户已存在");
		}
		laoren.setId(new Date().getTime());
        laorenService.insert(laoren);
        return R.ok();
    }
    
    /**
     * 前台保存
     */
    @SysLog("新增老人")
    @RequestMapping("/add")
    public R add(@RequestBody LaorenEntity laoren, HttpServletRequest request){
        if(laorenService.selectCount(new EntityWrapper<LaorenEntity>().eq("laorenzhanghao", laoren.getLaorenzhanghao()))>0) {
            return R.error("老人账号已存在");
        }
    	laoren.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(laoren);
    	LaorenEntity u = laorenService.selectOne(new EntityWrapper<LaorenEntity>().eq("laorenzhanghao", laoren.getLaorenzhanghao()));
		if(u!=null) {
			return R.error("用户已存在");
		}
		laoren.setId(new Date().getTime());
        laorenService.insert(laoren);
        return R.ok().put("data",laoren.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改老人")
    public R update(@RequestBody LaorenEntity laoren, HttpServletRequest request){
        //ValidatorUtils.validateEntity(laoren);
        if(laorenService.selectCount(new EntityWrapper<LaorenEntity>().ne("id", laoren.getId()).eq("laorenzhanghao", laoren.getLaorenzhanghao()))>0) {
            return R.error("老人账号已存在");
        }
        //全部更新
        laorenService.updateById(laoren);
    if(null!=laoren.getLaorenzhanghao())
    {
        // 修改token
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUsername(laoren.getLaorenzhanghao());
        tokenService.update(tokenEntity, new EntityWrapper<TokenEntity>().eq("userid", laoren.getId()));
    }


        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除老人")
    public R delete(@RequestBody Long[] ids){
        laorenService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	








    /**
     * 总数量
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params,LaorenEntity laoren, HttpServletRequest request){
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("jiashu")) {
            laoren.setJiashuzhanghao((String)request.getSession().getAttribute("username"));
        }
        EntityWrapper<LaorenEntity> ew = new EntityWrapper<LaorenEntity>();
        int count = laorenService.selectCount(MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, laoren), params), params));
        return R.ok().put("data", count);
    }




}
