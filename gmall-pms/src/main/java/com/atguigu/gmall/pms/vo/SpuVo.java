package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.SpuEntity;
import lombok.Data;

import java.util.List;

@Data
public class SpuVo extends SpuEntity {//直接继承spuEntity 那么就直接拥有了spu中的参数
    //只需要添加其它三个参数 参数名要与json解析出的对象一致;然后一次分析里面内容是什么
    private List<String> spuImages;//图片地址
    private List<SpuAttrValueVo> baseAttrs;//对应表格pms_spu_attr_value实体类SpuAttrValueEntity;但是少了一个字段则新创建一个SpuAttrValueVo
    private List<SkuVo> skus;//将扩展好之后的字段加入 形成一个新的类 对象
}
