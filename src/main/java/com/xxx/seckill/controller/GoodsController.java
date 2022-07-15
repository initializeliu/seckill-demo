package com.xxx.seckill.controller;

import com.xxx.seckill.pojo.User;
import com.xxx.seckill.service.IGoodsService;
import com.xxx.seckill.service.IUserService;
import com.xxx.seckill.vo.DetailVo;
import com.xxx.seckill.vo.GoodsVo;
import com.xxx.seckill.vo.RespBean;
import net.sf.jsqlparser.util.validation.metadata.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 商品
 */

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;


    /**
     * 功能描述：跳转到商品列表页面
     * windows优化前QPS:613.6,617.9,594.6,614.1
     * Linux优化前QPS:
     *
     * windows静态页面优化后QPS:391.8
     *
     *
     *
     *
     *
     * @param model
     * @return
     */
    @RequestMapping(value="/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user, HttpServletRequest request, HttpServletResponse response){

        //Redis中获取页面，如果不为空，直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        //如果为空，手动渲染，存到Redis中并返回
        WebContext context = new WebContext(request, response, request.getServletContext(),request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", context);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList", html, 60, TimeUnit.SECONDS);

        }
        return html;
   }

//    @RequestMapping("/toList")
//    public String toList(Model model, User user){
//
//        model.addAttribute("user", user);
//
//        model.addAttribute("goodsList", goodsService.findGoodsVo());
//
//        return "goodsList";
//    }

//    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, @CookieValue("userTicket") String ticket){
//        if(StringUtils.isEmpty(ticket)){
//            return "login";
//        }
//        User user = (User)session.getAttribute(ticket);
//        User user = userService.getUserByCookie(ticket, request, response);
//        if(null == user){
//            return "login";
//        }
//        model.addAttribute("user", user);
//        return "goodsList";
//    }


    /**
     * 功能描述：商品详情信息页面静态化
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(Model model, User user, @PathVariable Long goodsId){
        model.addAttribute("user", user);

        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        //秒杀状态
        int secKillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        //秒杀还未开始
        if(nowDate.before(startDate)){
            remainSeconds = (int)((startDate.getTime() - nowDate.getTime()) / 1000);
        }else if(nowDate.after(endDate)){
            //秒杀已结束
            secKillStatus = 2;
            remainSeconds = -1;
        }else {
            secKillStatus = 1;
            remainSeconds = 0;
        }

        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSecKillStatus(secKillStatus);
        detailVo.setRemainSeconds(remainSeconds);
        return RespBean.success(detailVo);
    }


    /**
     * 功能描述：获取商品详情
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
//    @RequestMapping(value = "/toDetail/{goodsId}", produces = "text/html;charset=utf-8")
//    @ResponseBody
//    public String toList(Model model, User user, @PathVariable Long goodsId){
    public String toList(Model model, User user, @PathVariable Long goodsId, HttpServletRequest request, HttpServletResponse response){

        //先查找数据库中是否存在
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail:"+goodsId);
        if(!StringUtils.isEmpty(html)){
            return html;
        }


        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        //秒杀状态
        int secKillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        //秒杀还未开始
        if(nowDate.before(startDate)){
            remainSeconds = (int)((startDate.getTime() - nowDate.getTime()) / 1000);
        }else if(nowDate.after(endDate)){
            //秒杀已结束
            secKillStatus = 2;
            remainSeconds = -1;
        }else {
            secKillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("goods", goodsVo);

        //手动渲染
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());

        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", context);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsDetail:" + goodsId, html, 60, TimeUnit.SECONDS);
        }

        return html;

//        return "goodsDetail";
    }


}
