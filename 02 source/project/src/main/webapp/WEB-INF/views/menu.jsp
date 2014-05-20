<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="shirox" uri="http://shirox.apache.org/tags"%>

<shirox:hasAnyPermissions name="homepage">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a class="head" href="javascript:swtichModule('homepage')">首页</a>
			</h4>
		</div>
	</div>
</shirox:hasAnyPermissions>

<shirox:hasAnyPermissions name="user-manage,permit-manage">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a class="head" href="#system-menu" data-toggle="collapse"
					data-parent="#left-menu">系统管理</a>
			</h4>
		</div>
		<div id="system-menu" class="panel-collapse collapse">
			<shiro:hasPermission name="user-manage">
				<a href="javascript:swtichModule('/manage/user-manage')"
					class="list-group-item">用户管理</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="permit-manage">
				<a href="javascript:swtichModule('/manage/permit-manage')"
					class="list-group-item">权限管理</a>
			</shiro:hasPermission>
		</div>
	</div>
</shirox:hasAnyPermissions>
