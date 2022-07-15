package com.xxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.seckill.pojo.SeckillOrder;
import com.xxx.seckill.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author initialize liu
 * @since 2022-07-07
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    /**
     * 功能描述：获取秒杀结果
     * @param user
     * @param goodsId
     * @return: orderId:成功; -1:秒杀失败; 0:排队中;
     */
    Long getResult(User user, Long goodsId);
}
