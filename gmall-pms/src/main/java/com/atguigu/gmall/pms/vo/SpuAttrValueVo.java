package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.SpuAttrValueEntity;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import java.util.List;
//@Data 因为自己重写了set方法 所以不需要再加这个注解了
public class SpuAttrValueVo extends SpuAttrValueEntity {
    private List<String> valueSelected;

    public void setValueSelected(List<String> valueSelected) {
        //先判断是否为空
        if(CollectionUtils.isEmpty(valueSelected)){
            return;
        }
        //因为继承了SpuAttrBalueEntity所以可以直接去使用attrValue这个属性
        this.setAttrValue(StringUtils.join(valueSelected,","));
    }
}
