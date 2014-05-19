package com.wizard.official.platform.spring.hibernate.application.web;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.wizard.official.platform.spring.hibernate.application.service.SystemService;
import com.wizard.official.platform.spring.hibernate.entity.MenuEntity;
import com.wizard.official.platform.spring.hibernate.entity.PermissionEntity;
import com.wizard.official.platform.spring.hibernate.entity.RoleInfoEntity;
import com.wizard.official.platform.spring.hibernate.entity.UserInfoEntity;
import com.wizard.official.platform.spring.hibernate.io.PageResponse;
import com.wizard.official.platform.spring.hibernate.io.extjs.ExtPageRequest;
import com.wizard.official.platform.spring.hibernate.io.extjs.ExtPageResponse;
import com.wizard.official.platform.spring.hibernate.io.extjs.ExtResponse;

@Controller
@RequestMapping("controller/system/")
public class SystemController {

	@Autowired
	private SystemService systemService = null;

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public ModelAndView doLogin(HttpServletRequest request,
			@RequestParam String user, @RequestParam String pwd) throws IOException {

		UserInfoEntity userInfo = systemService.login(user, pwd);

		Properties prop = new Properties();
		prop.load(this.getClass().getClassLoader()
				.getResourceAsStream("config.properties"));
		if (null == userInfo) {
			return new ModelAndView(new RedirectView(request.getContextPath()
					+ prop.getProperty("shiro.loginUrl")));
		} else {
			return new ModelAndView(new RedirectView(request.getContextPath()
					+ prop.getProperty("shiro.successUrl")));
		}
	}

	@RequestMapping(value = "/logout.do", method = RequestMethod.POST)
	public ModelAndView logout(HttpServletRequest request) {
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		return new ModelAndView(new RedirectView(request.getContextPath()
				+ "/manage"));
	}

	@RequestMapping(value = "/searchUserInfo.do", method = RequestMethod.GET)
	@ResponseBody
	public ExtPageResponse<UserInfoEntity> searchUserInfo(
			UserInfoEntity userInfo, ExtPageRequest pageRequest) {
		PageResponse<UserInfoEntity> page = systemService.searchUserInfo(
				userInfo, pageRequest);
		return new ExtPageResponse<UserInfoEntity>(true, page);
	}

	@RequestMapping(value = "/getRoleInfoCombox.do", method = RequestMethod.GET)
	@ResponseBody
	public ExtResponse<List<RoleInfoEntity>> getRoleInfoCombox() {
		List<RoleInfoEntity> list = systemService.getRoleInfoCombox();
		return new ExtResponse<List<RoleInfoEntity>>(true, list);
	}

	@RequestMapping(value = "/saveUserInfo.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Boolean> saveUserInfo(UserInfoEntity userInfo) {
		systemService.saveUserInfo(userInfo);
		return new ExtResponse<Boolean>(true, true);
	}

	@RequestMapping(value = "/searchRoleInfo.do", method = RequestMethod.GET)
	@ResponseBody
	public ExtPageResponse<RoleInfoEntity> searchRoleInfo(
			RoleInfoEntity roleInfo, ExtPageRequest pageRequest) {
		PageResponse<RoleInfoEntity> page = systemService.searchRoleInfo(
				roleInfo, pageRequest);
		return new ExtPageResponse<RoleInfoEntity>(true, page);
	}

	@RequestMapping(value = "/getMenunameMultiselect.do", method = RequestMethod.GET)
	@ResponseBody
	public ExtResponse<List<MenuEntity>> getMenunameMultiselect(String rolename) {
		List<MenuEntity> list = systemService.getMenunameMultiselect(rolename);
		return new ExtResponse<List<MenuEntity>>(true, list);
	}

	@RequestMapping(value = "/getPermissionMultiselect.do", method = RequestMethod.GET)
	@ResponseBody
	public ExtResponse<List<PermissionEntity>> getPermissionMultiselect(
			String rolename) {
		List<PermissionEntity> list = systemService
				.getPermissionMultiselect(rolename);
		return new ExtResponse<List<PermissionEntity>>(true, list);
	}

	@RequestMapping(value = "/addPermission.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Boolean> addPermission(String rolename, String[] menu) {
		systemService.addPermission(rolename, menu);
		return new ExtResponse<Boolean>(true, true);
	}

	@RequestMapping(value = "/removePermission.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Boolean> removePermission(String rolename, String[] menu) {
		systemService.removePermission(rolename, menu);
		return new ExtResponse<Boolean>(true, true);
	}
}
