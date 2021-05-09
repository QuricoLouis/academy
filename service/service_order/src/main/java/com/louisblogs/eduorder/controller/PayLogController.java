package com.louisblogs.eduorder.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.louisblogs.commonutils.R;
import com.louisblogs.eduorder.entity.Order;
import com.louisblogs.eduorder.service.OrderService;
import com.louisblogs.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author luqi
 * @since 2021-05-03
 */
@RestController
@RequestMapping("/eduorder/paylog")
//@CrossOrigin
public class PayLogController {

	@Autowired
	private PayLogService payLogService;

	@Autowired
	private OrderService orderService;

	//生成微信支付二维码接口
	@GetMapping("createNative/{orderNo}")
	public R createNative(@PathVariable String orderNo) {
		//返回信息，包含二维码地址，还有其他信息
		Map map = payLogService.createNative(orderNo);
		return R.ok().data(map);
	}

	//查询订单支付状态
	@GetMapping("queryPayStatus/{orderNo}")
	public R queryPayStatus(@PathVariable String orderNo) {
		Map<String, String> map = payLogService.queryPayStatus(orderNo);
		if (map == null) {
			return R.error().message("支付出错了");
		}
		//如果返回map里面不为空，通过map获取订单呢状态
		if (map.get("trade_state").equals("SUCCESS")) {  //支付成功
			//添加记录到支付表，更新订单表订单状态
			payLogService.updateOrdersStatus(map);
			return R.ok().message("支付成功");
		}
		return R.ok().code(25000).message("支付中");
	}

	//根据课程id和用户id查询订单表中订单状态
	@GetMapping("isBuyCourse/{courseId}/{memberId}")
	public Boolean isBuyCourse(@PathVariable String courseId, @PathVariable String memberId) {
		QueryWrapper<Order> wrapper = new QueryWrapper<>();
		wrapper.eq("course_id",courseId);
		wrapper.eq("member_id",memberId);
		wrapper.eq("status",1); //支付状态
		int count = orderService.count(wrapper);
		if (count>0){  //已经支付
			return true;
		}else {
			return false;
		}
	}

}
