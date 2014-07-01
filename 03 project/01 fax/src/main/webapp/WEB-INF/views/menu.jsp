<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="shirox" uri="http://shirox.apache.org/tags"%>

<shirox:hasAnyPermissions name="homepage">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a class="head" href="javascript:addTabItem('首页', 'homepage', true)">首页</a>
			</h4>
		</div>
	</div>
</shirox:hasAnyPermissions>

<shirox:hasAnyPermissions name="outFax">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a class="head" href="javascript:addTabItem('发传真', 'outFax')">发传真</a>
			</h4>
		</div>
	</div>
</shirox:hasAnyPermissions>

<shirox:hasAnyPermissions name="user-manage,permit-manage">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a class="head" href="#platform-menu" data-toggle="collapse"
					data-parent="#left-menu">平台管理</a>
			</h4>
		</div>
		<div id="platform-menu" class="panel-collapse collapse">
			<shiro:hasPermission name="user-manage">
				<a href="javascript:addTabItem('用户管理', 'user-manage')"
					class="list-group-item"><span class="glyphicon glyphicon-user"></span>用户管理</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="permit-manage">
				<a href="javascript:addTabItem('权限管理', 'permit-manage')"
					class="list-group-item"><span class="glyphicon glyphicon-bell"></span>权限管理</a>
			</shiro:hasPermission>
		</div>
	</div>
</shirox:hasAnyPermissions>

<shirox:hasAnyPermissions name="addressBook">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a class="head" href="javascript:addTabItem('通讯录维护', 'addressBook')">通讯录维护</a>
			</h4>
		</div>
	</div>
</shirox:hasAnyPermissions>

<shirox:hasAnyPermissions name="inbox,outbox,draftbox,recyclebox">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a class="head" href="#fax-menu" data-toggle="collapse"
					data-parent="#left-menu">传真收发</a>
			</h4>
		</div>
		<div id="fax-menu" class="panel-collapse collapse">
			<shiro:hasPermission name="inbox">
				<a href="javascript:addTabItem('收件箱', 'inbox')"
					class="list-group-item"><span class="glyphicon ico inbox-ico"></span>收件箱<span
					id="inCnt" class="badge pull-right">0</span></a>
			</shiro:hasPermission>
			<shiro:hasPermission name="outbox">
				<a href="javascript:addTabItem('发件箱', 'outbox')"
					class="list-group-item"><span class="glyphicon ico outbox-ico"></span>发件箱<span
					id="outCnt" class="badge pull-right">0</span></a>
			</shiro:hasPermission>
			<shiro:hasPermission name="draftbox">
				<a href="javascript:addTabItem('草稿箱', 'draftbox')"
					class="list-group-item"><span
					class="glyphicon ico draftbox-ico"></span>草稿箱<span id="draftCnt"
					class="badge pull-right">0</span></a>
			</shiro:hasPermission>
			<shiro:hasPermission name="recyclebox">
				<a href="javascript:addTabItem('回收箱', 'recyclebox')"
					class="list-group-item"><span
					class="glyphicon ico recyclebox-ico"></span>回收箱<span
					id="recycleCnt" class="badge pull-right">0</span></a>
			</shiro:hasPermission>
		</div>
	</div>
</shirox:hasAnyPermissions>
