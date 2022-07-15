package com.xxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.seckill.pojo.Goods;
import com.xxx.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author initialize liu
 * @since 2022-07-07
 */
public interface IGoodsService extends IService<Goods> {


    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    /**
     * 功能描述：获取商品详情
     * @param goodsId
     * @return
     */
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
