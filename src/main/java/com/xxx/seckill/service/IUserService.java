package com.xxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.seckill.pojo.User;
import com.xxx.seckill.vo.LoginVo;
import com.xxx.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author initialize liu
 * @since 2022-07-05
 */
public interface IUserService extends IService<User> {

    /**
     * 登录
     * @param loginVo
     * @param request
     * @param response
     * @return
     */
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);


    /**
     * 根据cookie获取用户
     * @param userTicket
     * @return
     */
    User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);


    /**
     * 功能描述：更新用户密码
     * @param userTicket
     * @param password
     * @param request
     * @param response
     * @return
     */
    public RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response);


}
