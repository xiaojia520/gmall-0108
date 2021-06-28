package com.atguigu.gmall.pms.service.impl;

import com.alibaba.nacos.client.utils.StringUtils;
import com.atguigu.gmall.pms.entity.*;
//import com.atguigu.gmall.pms.feign.GmallSmsClient;
import com.atguigu.gmall.pms.mapper.*;
import com.atguigu.gmall.pms.service.*;
import com.atguigu.gmall.pms.vo.SkuSaleVo;
import com.atguigu.gmall.pms.vo.SkuVo;
import com.atguigu.gmall.pms.vo.SpuAttrValueVo;
import com.atguigu.gmall.pms.vo.SpuVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;


@Service("spuService")
public class SpuServiceImpl extends ServiceImpl<SpuMapper, SpuEntity> implements SpuService {
    @Resource
    SpuAttrValueService spuAttrValueService;
    @Resource
    SpuDescMapper spuDescMapper;
    @Resource
    SpuMapper spuMapper;
    @Resource
    SpuAttrValueMapper spuAttrValueMapper;
    @Resource
    SkuMapper skuMapper;
    @Resource
    SkuImagesService skuImagesService;
    @Resource
    SkuAttrValueService skuAttrValueService;
    //@Resource
//    GmallSmsClient gmallSmsClient;

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<SpuEntity> page = this.page(//这个方法是谁提供的呢？？去找
                paramVo.getPage(),
                new QueryWrapper<SpuEntity>()
        );

        return new PageResultVo(page);
    }

    //照猫画狗 看上面怎么生成的
    @Override
    public PageResultVo querySpuByCidAndPage(PageParamVo paramVo, Long categoryId) {
        QueryWrapper<SpuEntity> queryWrapper = new QueryWrapper<>();//封装查询方法
        //根据sql语句来查询 select * from pms_spu where category_id=255 and(id =7 or name like '%7%')
        //如果没有分类条件 则不需要category_id的条件
        //如果不为空 则查本类
        if (categoryId != 0) {
            queryWrapper.eq("category_id", categoryId);
        }
        //查询关键字
        String key = paramVo.getKey();
        //如果不为空则需要查询 进行关键字查询
        if (StringUtils.isNotBlank(key)) {
            //and(id =7 or name like '%7%')
            //wrapper后面直接写条件，条件之间默认是and并且没有();消费型 供给型 函数式
            queryWrapper.and(t -> t.eq("id", key).or().like("name", key));
        }
        IPage<SpuEntity> page = this.page(//这个方法是谁提供的呢？？去找 Iservice提供的;这里需要的是一个ipage对象
                paramVo.getPage(),//获取ipage对象
                queryWrapper
        );
        return new PageResultVo(page);
    }

    @Override
    public void bigSave(SpuVo spu) {
        //1   保存spu三张表
        //1-1 保存pms_spu
        //这里面缺少了两个参数 主要是两个系统时间没有所以需要设置机智的做法
        spu.setCreateTime(new Date());
        spu.setUpdateTime(spu.getCreateTime());
        this.spuMapper.insert(spu);
        this.save(spu);//这个就是默认的单表操作由Iservice提供的
        //获取spuId 以供后面使用
        Long spuId = spu.getId();
        //1-2 保存pms_spu_desc
        List<String> spuImages = spu.getSpuImages();
        System.out.println(spuImages);
        //首先判断是否为空 不为空才有继续的意义
        if (!CollectionUtils.isEmpty(spuImages)) {
            SpuDescEntity spuDescEntity = new SpuDescEntity();
            spuDescEntity.setSpuId(spuId);
            spuDescEntity.setDecript(StringUtils.join(spuImages, ","));
            this.spuDescMapper.insert(spuDescEntity);
        }
        //1-3 保存pms_attr_value
        List<SpuAttrValueVo> baseAttrs = spu.getBaseAttrs();
        //判断是否 为空
        if (!CollectionUtils.isEmpty(baseAttrs)) {
            //利用stream表达式来将集合转换成另一个集合 把vo集合变成entity集合
            List<SpuAttrValueEntity> spuAttrValueEntities = baseAttrs.stream()
                    .filter(spuAttrValueVo -> spuAttrValueVo.getAttrValue() != null)//增加过滤设置
                    .map(spuAttrValueVo -> {
                        SpuAttrValueEntity spuAttrValueEntity = new SpuAttrValueEntity();
                        BeanUtils.copyProperties(spuAttrValueVo, spuAttrValueEntity);
                        spuAttrValueEntity.setSpuId(spuId);//增加设置同时要在类上增加主键类型
                        return spuAttrValueEntity;
                    }).collect(Collectors.toList());
            spuAttrValueService.saveBatch(spuAttrValueEntities);
            //不使用mapper方法 因为没有批量保存：spuAttrValueMapper.insert();
        }
        //2   保存和sku相关的3张表
        //2-1 保存pms_sku
        //我们已经封装了skuVo来作为我们的skus的对象 他已经获取来
        List<SkuVo> skus = spu.getSkus();//这是主体部分;直接从大对象里拿分发到各个表中
        //判断skus是否为空，防止空指针
        if (CollectionUtils.isEmpty(skus)) {
            return;//如果为空则直接退出了
        }
        //遍历skus设置对应属性值，每一个skus都是保存对应的数据 已经是单个的了！！！！！
        skus.forEach(skuVo -> {
            skuVo.setBrandId(spu.getBrandId());
            System.out.println(spu.getBrandId());
            skuVo.setCategoryId(spu.getCategoryId());
            skuVo.setSpuId(spuId);
            //获取页面的图片列表
            List<String> images = skuVo.getImages();
            System.out.println("images:"+images);
            //判断是否为空集合，不为空则设置第一张图片为默认图片
            if (!CollectionUtils.isEmpty(images)) {
                //取第一张图片为默认图片;这里老司机会先进行一个判断，是否存在默认图片，如果没有则设置第一张为默认图片，否则默认图为默认
                skuVo.setDefaultImage(StringUtils.isBlank(skuVo.getDefaultImage()) ? images.get(0) : skuVo.getDefaultImage());
            }
            //保存内容
            this.skuMapper.insert(skuVo);//我们需要保存的是skuVo 到数据库，所以获取了skus了我们直接有实体类skuEntity
            //获取skuId
            Long skuId = skuVo.getId();

            //2-2 保存pms_sku_images 这是另一张表了 可以批量保存 可以使用service
            //先判断是否为空
            if (!CollectionUtils.isEmpty(images)) {
                //这里需要的是entities的实体类 把字符串地址集合转换为 实体类对象集合
                skuImagesService.saveBatch(images.stream().map(image -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    //设置实体类参数
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setUrl(image);
                    //判断是否是默认图片的地址
                    skuImagesEntity.setDefaultStatus(StringUtils.equals(skuVo.getDefaultImage(), image) ? 1 : 0);
                    return skuImagesEntity;
                }).collect(Collectors.toList()));
            }
            //2-3 保存pms_sku_attr_value
            //首先判断是否为空 防止空指针
            List<SkuAttrValueEntity> saleAttrs = skuVo.getSaleAttrs();//skuVo是我们用来接收数据的 没有这个表格是自己封装的，凡是自己封装的那就是用来接收数据的
            if (!CollectionUtils.isEmpty(saleAttrs)) {//但最后我们保存数据的不是这些 而是保存到各个表中的切记切记
                saleAttrs.forEach(skuAttrValueEntity -> {
                    skuAttrValueEntity.setSkuId(skuId);//每个设置个skuId 就好了
                });
            }
            this.skuAttrValueService.saveBatch(saleAttrs);//每一个skus都要保存一个skuAttrValue的集合;无需map方法 因为我们无需转换集合类型 只要每一个多加一个skuId即可
            //保存单3张表销售表啦！！！
            //因为是别的库，所以需要远程调用；使用fegin 先注入这个接口GmallSmsClient
//            SkuSaleVo skuSaleVo = new SkuSaleVo();
//            skuSaleVo.setSkuId(skuId);
//            this.gmallSmsClient.saleSales(skuSaleVo);
        });
    }
}