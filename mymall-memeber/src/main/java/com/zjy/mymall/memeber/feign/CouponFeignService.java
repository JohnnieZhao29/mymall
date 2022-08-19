package com.zjy.mymall.memeber.feign;

import com.zjy.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zjy
 * @version 1.0
 */

@FeignClient("mymall-coupon")
/**
 * 告诉SpringCloud，这个接口是一个需要进行远程调用的接口
 * 调用的服务的名字，将被传入到注册中心进行查找
 */
public interface CouponFeignService {

    // 传入完整的想要调用的远程服务的接口名
    @RequestMapping("/coupon/coupon/member/list")
    public R membercoupons();
}
