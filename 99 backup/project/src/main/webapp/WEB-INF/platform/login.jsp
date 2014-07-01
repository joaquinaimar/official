<%@ page contentType="text/html;charset=UTF-8" language="java"%>
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

<link rel="stylesheet" type="text/css" media="screen"
	href="${pageContext.request.contextPath}/resources/style/common.css" />

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/script/common.js"></script>

<script type="text/javascript">
	var contextPath = "${pageContext.request.contextPath}";
</script>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/platform/style/login.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/platform/script/login.js"></script>

</head>
<body>
	<section class="login-form-wrap">
		<h1>
			<tiles:insertAttribute name="title" />
		</h1>
		<form class="login-form" method="POST"
			action="${pageContext.request.contextPath}/controller/system/login.do">
			<label> <input type="text" name="user" required
				placeholder="用户名">
			</label> <label> <input type="password" name="pwd" required
				placeholder="密码">
			</label> <input type="submit" value="登录">
		</form>
		<h5>
			<a href="#">注册</a>
		</h5>
	</section>
</body>
</html>