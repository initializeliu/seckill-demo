package com.xxx.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.seckill.mapper.GoodsMapper;
import com.xxx.seckill.pojo.Goods;
import com.xxx.seckill.service.IGoodsService;
import com.xxx.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author initialize liu
 * @since 2022-07-07
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {


    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 功能描述：获取商品列表
     * @return
     */
    @Override
    public List<GoodsVo> findGoodsVo() {


        return goodsMapper.findGoodsVo();
    }

    /**
     * 功能描述：获取指定商品详情信息
     * @param goodsId
     * @return
     */
    @Override
    public GoodsVo findGoodsVoByGoodsId(Long goodsId) {
        return goodsMapper.findGoodsVoByGoodsId(goodsId);
    }
}
