package com.xxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.seckill.pojo.Order;
import com.xxx.seckill.pojo.User;
import com.xxx.seckill.vo.GoodsVo;
import com.xxx.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author initialize liu
 * @since 2022-07-07
 */
public interface IOrderService extends IService<Order> {

    /**
     * 功能描述：秒杀
     */
    Order secKill(User user, GoodsVo goods);

    /**
     * 功能描述：订单详情
     * @param orderId
     * @return
     */
    OrderDetailVo detail(Long orderId);


    /**
     * 功能描述：获取秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    String createPath(User user, Long goodsId);

    /**
     * 功能描述：校验秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    boolean checkPath(User user, Long goodsId, String path);

    /**
     * 功能描述：校验验证码
     * @param user
     * @param goodsId
     * @param captcha
     * @return
     */
    boolean checkCaptcha(User user, Long goodsId, String captcha);
}
