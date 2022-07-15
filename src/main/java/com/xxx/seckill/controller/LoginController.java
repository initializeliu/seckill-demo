package com.xxx.seckill.controller;


import com.xxx.seckill.service.IUserService;
import com.xxx.seckill.vo.LoginVo;
import com.xxx.seckill.vo.RespBean;
import com.xxx.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 登录
 *
 *
 */
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private IUserService userService;


    /**
     * 功能描述: 跳转到登录页面
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }


    /**
     * 功能描述：成功返回结果
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){

        //打日志
        log.info("()" + loginVo);
        return userService.doLogin(loginVo, request, response);
    }


    public static RespBean success(Object obj){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMessage(), obj);
    }

    /**
     * 功能描述:失败返回结果
     */
    public static RespBean error(RespBeanEnum respBeanEnum){
        return new RespBean(respBeanEnum.getCode(), respBeanEnum.getMessage(), null);
    }

    /**
     * 功能描述：失败的返回结果
     * @param respBeanEnum
     * @param obj
     * @return
     */
    public static RespBean error(RespBeanEnum respBeanEnum, Object obj){
        return new RespBean(respBeanEnum.getCode(), respBeanEnum.getMessage(), obj);
    }
}
