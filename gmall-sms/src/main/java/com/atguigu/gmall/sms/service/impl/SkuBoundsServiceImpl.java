package com.atguigu.gmall.sms.service.impl;

import com.atguigu.gmall.sms.entity.SkuFullReductionEntity;
import com.atguigu.gmall.sms.entity.SkuLadderEntity;
import com.atguigu.gmall.sms.mapper.SkuFullReductionMapper;
import com.atguigu.gmall.sms.mapper.SkuLadderMapper;
import com.atguigu.gmall.sms.vo.SkuSaleVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.sms.mapper.SkuBoundsMapper;
import com.atguigu.gmall.sms.entity.SkuBoundsEntity;
import com.atguigu.gmall.sms.service.SkuBoundsService;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;


@Service("skuBoundsService")
public class SkuBoundsServiceImpl extends ServiceImpl<SkuBoundsMapper, SkuBoundsEntity> implements SkuBoundsService {

    @Resource
    SkuFullReductionMapper skuFullReductionMapper;
    @Resource
    SkuLadderMapper skuLadderMapper;
    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<SkuBoundsEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<SkuBoundsEntity>()
        );

        return new PageResultVo(page);
    }

//    @Override
//    public void saveSales(SkuSaleVo saleVo) {//传入的参数是自己封装的实体类 并非原装
//        //3   保存和营销满减有关的3张表
//        //3-1 保存sms_sku_bounds
//        //2⃣️我们的mybatisplus为我们提供了单表操作
//        SkuBoundsEntity skuBoundsEntity = new SkuBoundsEntity();
//        //3⃣️为原装实体类赋值
//        BeanUtils.copyProperties(saleVo, skuBoundsEntity);
//        //4⃣️注意work字段 在skuSaleVo里是List类型（前端为我们提供的）；但原装的实体类是Integer类型
//        //要把List集合的0011转换成integer类型四位从右到左 要转成integer类型
//        //转为10进制否则高位丢失
//        //获取work集合
//        System.out.println(saleVo.toString());
//        System.out.println(saleVo.getBuyBounds());
//        System.out.println("----"+saleVo.getGrowBounds());
//        List<Integer> work = saleVo.getWork();
//        if(!CollectionUtils.isEmpty(work)||work.size()==4){
//            skuBoundsEntity.setWork(work.get(3)*8+work.get(2)*4+work.get(1)*2
//                    +work.get(0));
//        }//全部赋值完毕！！
//        //1⃣️因为mybatisplus提供的是单表操作，只有原装的表（实体类）才管用
//        this.save(skuBoundsEntity);
//        //3-1 保存sms_sku_full_reduction
//        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
//        BeanUtils.copyProperties(saleVo,skuFullReductionEntity);
//        skuFullReductionEntity.setAddOther(saleVo.getFullAddOther());
//        skuFullReductionMapper.insert(skuFullReductionEntity);
//        //3-2 保存sms_sku_ladder
//        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
//        BeanUtils.copyProperties(saleVo,skuLadderEntity);
//        skuLadderEntity.setAddOther(saleVo.getLadderAddOther());
//        skuLadderMapper.insert(skuLadderEntity);
//    }
}