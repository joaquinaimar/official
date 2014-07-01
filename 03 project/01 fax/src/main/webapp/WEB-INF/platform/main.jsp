<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="shirox" uri="http://shirox.apache.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<!DOCTYPE html>
<html>
<head>
<title><tiles:insertAttribute name="title" /></title>

<meta content="text/html; charset=utf-8" http-equiv="Content-Type">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE10" />

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/webjars/jquery-ui/themes/base/jquery-ui.css" />

<script type="text/javascript"
	src="${pageContext.request.contextPath}/webjars/jquery/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/webjars/jquery-ui/ui/jquery-ui.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/webjars/jquery-form/jquery.form.js"></script>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/webjars/bootstrap/css/bootstrap-theme.min.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/webjars/bootstrap/css/bootstrap.min.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/webjars/bootstrap/js/bootstrap.min.js"></script>

<script type="text/javascript">
	var contextPath = "${pageContext.request.contextPath}";
</script>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/platform/script/common.js"></script>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/platform/style/main.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/platform/script/main.js"></script>

</head>
<body>
	<header class="navbar navbar-default navbar-fixed-top title"
		role="navigation">
		<div class="container ">
			<div class="collapse navbar-collapse">
				<form class="navbar-form navbar-right" method="post"
					action="${pageContext.request.contextPath}/controller/system/logout.do">
					<button type="submit" class="btn btn-warning">注销</button>
				</form>
				<ul class="nav navbar-nav navbar-right">
					<li><a href="#" class="username">${sessionScope.userInfo.name}</a></li>
				</ul>
			</div>
		</div>
	</header>

	<div id="doc">
		<div id="left-menu" class="panel-group">
			<div class="panel-heading">
				<h4>菜单</h4>
			</div>
			<tiles:insertAttribute name="menu" />
		</div>
		<div id="right-module">
			<ul id="tab-title" class="nav nav-tabs"></ul>
			<div id="tab-body" class="tab-content"></div>
		</div>
	</div>

</body>
</html>