package com.xxx.seckill.vo;


import com.xxx.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 详情返回对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailVo {
    private User user;

    private GoodsVo goodsVo;

    private int secKillStatus;

    private int remainSeconds;
}
