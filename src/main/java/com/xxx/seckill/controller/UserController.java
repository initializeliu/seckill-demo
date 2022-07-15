package com.xxx.seckill.controller;


import com.xxx.seckill.pojo.User;
import com.xxx.seckill.rabbitmq.MQSender;
import com.xxx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author initialize liu
 * @since 2022-07-05
 */
@Controller
@RequestMapping("/user")
public class UserController {

//    @Autowired
//    private MQSender mqSender;
//
//    /**
//     * 功能描述：压力测试接口
//     */
//    @RequestMapping("/info")
//    @ResponseBody
//    public RespBean info(User user){
//        return RespBean.success(user);
//    }
//
//
//    /**
//     * 功能描述：测试发送RabbitMQ消息
//     */
//    @RequestMapping("/mq")
//    @ResponseBody
//    public void mq(){
//        mqSender.send("Hello RabbitMQ");
//    }
//
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public void fanout(){
//        mqSender.send("hello fanout model");
//    }
//
//    /**
//     * 功能描述：direct模式
//     */
//    @RequestMapping("/mq/direct01")
//    @ResponseBody
//    public void mq02(){
//        mqSender.send01("Hello, red");
//    }
//
//    /**
//     * 功能描述：direct模式
//     */
//    @RequestMapping("/mq/direct02")
//    @ResponseBody
//    public void mq03(){
//        mqSender.send02("Hello, green");
//    }
//
//
//    /**
//     * 功能描述：topic模式
//     */
//    @RequestMapping("/mq/topic01")
//    @ResponseBody
//    public void m104(){
//        mqSender.send03("Hello, Red");
//    }
//
//
//    /**
//     * 功能描述：topic模式
//     */
//    @RequestMapping("/mq/topic02")
//    @ResponseBody
//    public void m105(){
//        mqSender.send04("Hello, Green");
//    }
//
//
//    /**
//     * 功能描述：Header模式
//     */
//    @RequestMapping("/mq/header01")
//    @ResponseBody
//    public void mq06(){
//        mqSender.send05("Hello, Header01");
//    }
//
//    /**
//     * 功能描述：Header模式
//     */
//    @RequestMapping("/mq/header02")
//    @ResponseBody
//    public void mq07(){
//        mqSender.send06("Hello, Header02");
//    }
}
