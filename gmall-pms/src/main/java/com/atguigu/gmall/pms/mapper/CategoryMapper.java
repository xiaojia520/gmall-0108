package com.atguigu.gmall.pms.mapper;

import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author jia.xiao
 * @email xiao18217169801@163.com
 * @date 2021-06-22 19:46:03
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryEntity> {
	
}
