package com.louisblogs.msmservice.controller;

import com.louisblogs.commonutils.R;
import com.louisblogs.msmservice.service.MsmService;
import com.louisblogs.msmservice.utils.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ：luqi
 * @description：TODO
 * @date ：2021/4/30 21:35
 */

@RestController
@RequestMapping("/edumsm/msm")
//@CrossOrigin
public class MsmController {

	@Autowired
	private MsmService msmService;

	@Autowired
	private RedisTemplate<String,String> redisTemplate;

	@GetMapping("send/{phone}")
	public R sendMsm(@PathVariable String phone) {
		//1 从redis获取验证码，如果获取到直接返回
		String code = redisTemplate.opsForValue().get(phone);
		if(!StringUtils.isEmpty(code)){
			return R.ok();
		}

		//2 redis获取不到验证码，传递阿里云进行发送
		//生成随机值，传递阿里云进行发送
		code = RandomUtil.getSixBitRandom();
		Map<String, Object> param = new HashMap<>();
		param.put("code", code);
		//调用service发送短信的方法
		boolean isSend = msmService.send(param, phone);
		if (isSend) {
			//发送成功，把发送成功验证码放到redis里面
			//设置有效时间
			redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
			return R.ok();
		} else {
			return R.error().message("短信发送失败");
		}
	}

}
