package com.louisblogs.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.louisblogs.commonutils.JwtUtils;
import com.louisblogs.commonutils.MD5;
import com.louisblogs.educenter.entity.UcenterMember;
import com.louisblogs.educenter.entity.vo.RegisterVo;
import com.louisblogs.educenter.mapper.UcenterMemberMapper;
import com.louisblogs.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.louisblogs.servicebase.exceptionhandler.LouisblogsException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.management.monitor.StringMonitor;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author luqi
 * @since 2021-05-01
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

	//登陆的方法
	@Override
	public String login(UcenterMember member) {
		//获取登录手机号和密码
		String mobile = member.getMobile();
		String password = member.getPassword();

		//手机号和密码非空判断
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
			throw new LouisblogsException(20001, "登录失败");
		}

		//判断手机号是否正确
		QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
		wrapper.eq("mobile", mobile);
		UcenterMember mobileMember = baseMapper.selectOne(wrapper);
		//判断查询对象是否为空
		if (mobileMember == null) { //没有这个手机号
			throw new LouisblogsException(20001, "手机号不存在");
		}

		//判断密码
		//因为储存到数据库密码肯定加密的
		//把输入的密码进行加密，再和数据库密码进行比较
		//加密方式 MD5
		if (!MD5.encrypt(password).equals(mobileMember.getPassword())) {
			throw new LouisblogsException(20001, "密码不正确");
		}

		//判断用户是否禁用
		if (mobileMember.getIsDisabled()) {
			throw new LouisblogsException(20001, "您被禁止访问");
		}

		//登录成功
		//生成token字符串，使用jwt工具类
		String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());


		return jwtToken;
	}

	//注册的方法
	@Override
	public void register(RegisterVo registerVo) {
		//获取注册的数据
		String code = registerVo.getCode();   //验证码
		String mobile = registerVo.getMobile(); //手机号
		String nickname = registerVo.getNickname(); //昵称
		String password = registerVo.getPassword(); //密码

		//非空判断
		if (StringUtils.isEmpty(code) || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(nickname)
				|| StringUtils.isEmpty(password)) {
			throw new LouisblogsException(20001, "注册失败");
		}

		//判断手机号是否重复，表里面存在相同手机号不进行添加
		QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
		wrapper.eq("mobile", mobile);
		Integer count = baseMapper.selectCount(wrapper);
		if (count > 0) {
			throw new LouisblogsException(20001,"注册失败");
		}

		//数据添加数据库中
		UcenterMember member = new UcenterMember();
		member.setMobile(mobile);
		member.setNickname(nickname);
		member.setPassword(MD5.encrypt(password));
		member.setIsDisabled(false);
		member.setAvatar("https://louisblogs-academy.oss-cn-beijing.aliyuncs.com/2021/04/08/00142d2f5cbc444983479bf8779dc3e9myPhoto.png");
		baseMapper.insert(member);

	}

	//根据openid判断
	@Override
	public UcenterMember getOpenIdMember(String openid) {
		QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
		wrapper.eq("openid",openid);
		UcenterMember member = baseMapper.selectOne(wrapper);
		return member;
	}

	//查询某一天注册人数
	@Override
	public Integer countRegisterDay(String day) {
		return baseMapper.countRegisterDay(day);
	}

}
