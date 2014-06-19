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
	href="${pageContext.request.contextPath}/platform/style/register.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/platform/script/register.js"></script>

</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-6 col-md-offset-3">
				<form id="frmRegister" role="form">
					<fieldset>
						<legend>注册</legend>
						<div class="form-group">
							<label for="username">用户名：</label> <input type="text"
								class="form-control" name="username">
						</div>
						<div class="form-group">
							<label for="password">密码：</label> <input type="password"
								class="form-control" name="password">
						</div>
						<div class="form-group">
							<label for="rePassword">重输密码：</label> <input type="password"
								class="form-control" name="rePassword">
						</div>
						<div class="form-group">
							<label for="code">编码：</label> <input type="text"
								class="form-control" name="code">
						</div>
						<div class="form-group">
							<label for="name">姓名：</label> <input type="text"
								class="form-control" name="name">
						</div>
						<button id="btnRegister" type="button" class="btn btn-success">注册</button>
						<button id="btnBack" type="button" class="btn btn-default">返回</button>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</body>
</html>