package com.xxx.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.seckill.exception.GlobalException;
import com.xxx.seckill.mapper.UserMapper;
import com.xxx.seckill.pojo.User;
import com.xxx.seckill.service.IUserService;
import com.xxx.seckill.util.CookieUtil;
import com.xxx.seckill.util.MD5Util;
import com.xxx.seckill.util.UUIDUtil;
import com.xxx.seckill.util.ValidatorUtil;
import com.xxx.seckill.vo.LoginVo;
import com.xxx.seckill.vo.RespBean;
import com.xxx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author initialize liu
 * @since 2022-07-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 登录验证
     * @param loginVo
     * @return
     */
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        //参数校验
//        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//
//        if(!ValidatorUtil.isMobile(mobile)){
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }
        //根据手机号获取用户
        User user = userMapper.selectById(mobile);
        if(null == user){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }

        if(!MD5Util.formPassToDBPass(password, user.getSlat()).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }

        //生成cookie
        String ticket = UUIDUtil.uuid();
        //将用户信息存入redis中
        redisTemplate.opsForValue().set("user:" + ticket, user);

//        request.getSession().setAttribute(ticket, user);
        CookieUtil.setCookie(request, response, "userTicket", ticket);

        return RespBean.success(ticket);
    }


    /**
     * 功能描述：根据cookie获取用户
     * @param userTicket
     * @return
     */
    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isEmpty(userTicket)){
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);

        if(user != null){
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }

    /**
     *  功能描述：更新用户密码
     * @param userTicket
     * @param password
     * @param request
     * @param response
     * @return
     */
    @Override
    public RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response) {

        User user = getUserByCookie(userTicket, request, response);
        if(user == null){
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }

        user.setPassword(MD5Util.inputPassToDBPass(password, user.getSlat()));
        int result = userMapper.updateById(user);
        if(1 == result){
            //更新Redis
            redisTemplate.delete("user:" + userTicket);
            return RespBean.success();
        }

        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }




}
