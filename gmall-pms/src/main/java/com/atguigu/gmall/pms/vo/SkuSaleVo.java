package com.atguigu.gmall.pms.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuSaleVo {
    private Long skuId ;

    //积分优惠字段 实体类：SkuBoundEntity
    private BigDecimal growBounds;
    private BigDecimal buyBounds;
    private List<Integer> work;//这个在json解析数据中是一个集合

    //满减打折 实体类：SkuLadderEntity
    private Integer fullCount;
    private BigDecimal discount;
    private Integer ladderAddOther;//注意这个位置要和数据一致json一致

    //满减多少钱信息 实体类：SkuFullReductionEntity
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer fullAddOther;//改成json数据一致




}
