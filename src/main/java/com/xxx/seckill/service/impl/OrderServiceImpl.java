package com.xxx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.seckill.exception.GlobalException;
import com.xxx.seckill.mapper.OrderMapper;
import com.xxx.seckill.pojo.*;
import com.xxx.seckill.service.IGoodsService;
import com.xxx.seckill.service.IOrderService;
import com.xxx.seckill.service.ISeckillGoodsService;
import com.xxx.seckill.service.ISeckillOrderService;
import com.xxx.seckill.util.MD5Util;
import com.xxx.seckill.util.UUIDUtil;
import com.xxx.seckill.vo.GoodsVo;
import com.xxx.seckill.vo.OrderDetailVo;
import com.xxx.seckill.vo.RespBeanEnum;
import net.sf.jsqlparser.util.validation.metadata.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author initialize liu
 * @since 2022-07-07
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ISeckillGoodsService seckillGoodsService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 功能描述：秒杀
     * @param user
     * @param goods
     * @return
     */
    @Override
    @Transactional
    public Order secKill(User user, GoodsVo goods) {
        //秒杀商品表减库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
        boolean updateResult = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count = stock_count - 1").eq("goods_id", goods.getId()).gt("stock_count", 0));

        if(seckillGoods.getStockCount() < 1){
            //判断是否还有库存
            redisTemplate.opsForValue().set("isStockEmpty:" + goods.getId(), "0");
            return null;
        }

        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);

        //生成的订单缓存到redis
        redisTemplate.opsForValue().set("order:" + user.getId() + ":" + goods.getId(), seckillOrder);
        return order;
    }

    /**
     * 功能描述：订单详情
     * @param orderId
     * @return
     */
    @Override
    public OrderDetailVo detail(Long orderId) {
        if(orderId == null){
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo detail = new OrderDetailVo();
        detail.setOrder(order);
        detail.setGoodsVo(goodsVo);

        return detail;
    }

    /**
     * 功能描述：获取秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    @Override
    public String createPath(User user, Long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" +
                goodsId, str, 60, TimeUnit.SECONDS);


        return str;
    }

    /**
     * 功能描述：检查秒杀地址
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    @Override
    public boolean checkPath(User user, Long goodsId, String path) {

        if(user == null || goodsId < 0 || StringUtils.isEmpty(path)){
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() +":" + goodsId);
        return path.equals(redisPath);
    }

    /**
     * 功能描述：校验验证码
     * @param user
     * @param goodsId
     * @param captcha
     * @return
     */
    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if(StringUtils.isEmpty(captcha) || user == null || goodsId < 0){
            return false;
        }
        String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);

        return captcha.equals(redisCaptcha);
    }


}
