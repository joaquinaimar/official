package com.wizard.official.platform.spring.hibernate.security;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public abstract class SpringHibernateRealm extends AuthorizingRealm {

	public SpringHibernateRealm() {
		super();
		setAuthenticationTokenClass(UsernamePasswordToken.class);
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String userName = token.getUsername();
		if (userName != null && !"".equals(userName)) {
			UserInfo userInfo = getUserInfo(userName,
					String.valueOf(token.getPassword()));
			if (userInfo != null) {
				String password = userInfo.getPassword();
				SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
						userName, password, getName());
				return info;
			}
		}
		return null;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		String username = (String) principals.fromRealm(getName()).iterator()
				.next();
		List<RoleInfo> roleInfos = getRoleInfo(username);
		List<PermissionInfo> permissionInfos = null;
		if (null == roleInfos || 0 == roleInfos.size()) {
			return null;
		} else {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			for (RoleInfo roleInfo : roleInfos) {
				info.addRole(roleInfo.getRolename());
				permissionInfos = getPermissionInfo(roleInfo.getRolename());
				for (PermissionInfo permissionInfo : permissionInfos) {
					info.addStringPermission(permissionInfo.getPermissionname());
				}
			}
			return info;
		}

	}

	@Override
	public AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals) {
		return super.getAuthorizationInfo(principals);
	}

	public abstract UserInfo getUserInfo(String username, String password);

	public abstract List<RoleInfo> getRoleInfo(String username);

	public abstract List<PermissionInfo> getPermissionInfo(String rolename);

}
