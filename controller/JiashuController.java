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

import com.entity.JiashuEntity;
import com.entity.view.JiashuView;

import com.service.JiashuService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 家属
 * 后端接口
 * @author 
 * @email 
 * @date 2025-01-10 22:13:24
 */
@RestController
@RequestMapping("/jiashu")
public class JiashuController {
    @Autowired
    private JiashuService jiashuService;






    
	@Autowired
	private TokenService tokenService;
	
	/**
	 * 登录
	 */
	@IgnoreAuth
	@RequestMapping(value = "/login")
	public R login(String username, String password, String captcha, HttpServletRequest request) {
		JiashuEntity u = jiashuService.selectOne(new EntityWrapper<JiashuEntity>().eq("jiashuzhanghao", username));
        if(u!=null && u.getStatus().intValue()==1) {
            return R.error("账号已锁定，请联系管理员。");
        }
		if(u==null || !u.getMima().equals(password)) {
            if(u!=null) {
                u.setPasswordwrongnum(u.getPasswordwrongnum()+1);
                if(u.getPasswordwrongnum()>=3) {
                    u.setStatus(1);
                }
                jiashuService.updateById(u);
            }
			return R.error("账号或密码不正确");
		}
		
		String token = tokenService.generateToken(u.getId(), username,"jiashu",  "家属" );
		return R.ok().put("token", token);
	}


	
	/**
     * 注册
     */
	@IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody JiashuEntity jiashu){
    	//ValidatorUtils.validateEntity(jiashu);
    	JiashuEntity u = jiashuService.selectOne(new EntityWrapper<JiashuEntity>().eq("jiashuzhanghao", jiashu.getJiashuzhanghao()));
		if(u!=null) {
			return R.error("注册用户已存在");
		}
		Long uId = new Date().getTime();
		jiashu.setId(uId);
        jiashuService.insert(jiashu);
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
        JiashuEntity u = jiashuService.selectById(id);
        return R.ok().put("data", u);
    }
    
    /**
     * 密码重置
     */
    @IgnoreAuth
	@RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request){
    	JiashuEntity u = jiashuService.selectOne(new EntityWrapper<JiashuEntity>().eq("jiashuzhanghao", username));
    	if(u==null) {
    		return R.error("账号不存在");
    	}
        u.setMima("123456");
        jiashuService.updateById(u);
        return R.ok("密码已重置为：123456");
    }



    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,JiashuEntity jiashu,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("laoren")) {
			jiashu.setLaorenzhanghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<JiashuEntity> ew = new EntityWrapper<JiashuEntity>();



		PageUtils page = jiashuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiashu), params), params));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }
    
    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,JiashuEntity jiashu, 
		HttpServletRequest request){
        EntityWrapper<JiashuEntity> ew = new EntityWrapper<JiashuEntity>();

		PageUtils page = jiashuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiashu), params), params));
		
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( JiashuEntity jiashu){
       	EntityWrapper<JiashuEntity> ew = new EntityWrapper<JiashuEntity>();
      	ew.allEq(MPUtil.allEQMapPre( jiashu, "jiashu")); 
        return R.ok().put("data", jiashuService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(JiashuEntity jiashu){
        EntityWrapper< JiashuEntity> ew = new EntityWrapper< JiashuEntity>();
 		ew.allEq(MPUtil.allEQMapPre( jiashu, "jiashu")); 
		JiashuView jiashuView =  jiashuService.selectView(ew);
		return R.ok("查询家属成功").put("data", jiashuView);
    }
	
    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        JiashuEntity jiashu = jiashuService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(jiashu,deSens);
        return R.ok().put("data", jiashu);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        JiashuEntity jiashu = jiashuService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(jiashu,deSens);
        return R.ok().put("data", jiashu);
    }
    



    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增家属") 
    public R save(@RequestBody JiashuEntity jiashu, HttpServletRequest request){
        if(jiashuService.selectCount(new EntityWrapper<JiashuEntity>().eq("jiashuzhanghao", jiashu.getJiashuzhanghao()))>0) {
            return R.error("家属账号已存在");
        }
    	jiashu.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(jiashu);
    	JiashuEntity u = jiashuService.selectOne(new EntityWrapper<JiashuEntity>().eq("jiashuzhanghao", jiashu.getJiashuzhanghao()));
		if(u!=null) {
			return R.error("用户已存在");
		}
		jiashu.setId(new Date().getTime());
        jiashuService.insert(jiashu);
        return R.ok();
    }
    
    /**
     * 前台保存
     */
    @SysLog("新增家属")
    @RequestMapping("/add")
    public R add(@RequestBody JiashuEntity jiashu, HttpServletRequest request){
        if(jiashuService.selectCount(new EntityWrapper<JiashuEntity>().eq("jiashuzhanghao", jiashu.getJiashuzhanghao()))>0) {
            return R.error("家属账号已存在");
        }
    	jiashu.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(jiashu);
    	JiashuEntity u = jiashuService.selectOne(new EntityWrapper<JiashuEntity>().eq("jiashuzhanghao", jiashu.getJiashuzhanghao()));
		if(u!=null) {
			return R.error("用户已存在");
		}
		jiashu.setId(new Date().getTime());
        jiashuService.insert(jiashu);
        return R.ok().put("data",jiashu.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改家属")
    public R update(@RequestBody JiashuEntity jiashu, HttpServletRequest request){
        //ValidatorUtils.validateEntity(jiashu);
        if(jiashuService.selectCount(new EntityWrapper<JiashuEntity>().ne("id", jiashu.getId()).eq("jiashuzhanghao", jiashu.getJiashuzhanghao()))>0) {
            return R.error("家属账号已存在");
        }
        //全部更新
        jiashuService.updateById(jiashu);
    if(null!=jiashu.getJiashuzhanghao())
    {
        // 修改token
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUsername(jiashu.getJiashuzhanghao());
        tokenService.update(tokenEntity, new EntityWrapper<TokenEntity>().eq("userid", jiashu.getId()));
    }


        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除家属")
    public R delete(@RequestBody Long[] ids){
        jiashuService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	








    /**
     * 总数量
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params,JiashuEntity jiashu, HttpServletRequest request){
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("laoren")) {
            jiashu.setLaorenzhanghao((String)request.getSession().getAttribute("username"));
        }
        EntityWrapper<JiashuEntity> ew = new EntityWrapper<JiashuEntity>();
        int count = jiashuService.selectCount(MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiashu), params), params));
        return R.ok().put("data", count);
    }




}
