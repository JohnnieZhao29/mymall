package com.zjy.mymall.product;
//

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.zjy.mymall.product.entity.BrandEntity;
import com.zjy.mymall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.List;



@SpringBootTest
@RunWith(SpringRunner.class)
class MymallProductApplicationTests {

    @Autowired
    BrandService brandService;


    @Test
    void contextLoads() {
//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setBrandId(1l);
//        brandEntity.setDescript("华为技术有限公司");
//        brandService.updateById(brandEntity);
//        System.out.println("update success");
        List<BrandEntity> list = brandService.list(
                new QueryWrapper<BrandEntity>()
                        .eq("brand_id", 1));
        for(BrandEntity entity : list){
            System.out.println(entity);
        }
    }

}
