<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><tiles:insertAttribute name="title" /></title>
<script type="text/javascript">
	var contextPath = "${pageContext.request.contextPath}";
</script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/platform/style/login.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/platform/script/login.js"></script>
</head>
<body>
	<div class="bg">
		<table width="100%" border="0">
			<tr>
				<td height="218">&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td style="padding-top: 100px" class="dl" valign="top">
					<form id="frmLogin" method="POST"
						action="${pageContext.request.contextPath}/controller/system/login.do">
						<table style="padding-top: 10px" width="100%" height="208"
							border="0">
							<tr>
								<td height="23">&nbsp;</td>
								<td>&nbsp;</td>
								<td>&nbsp;<span style="color: red;">${sessionScope.loginInfo}</span></td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td width="24%" height="29">&nbsp;</td>
								<td class="font" width="16%">用&nbsp;户&nbsp;名</td>
								<td width="32%"><input class="input" type="text"
									name="user" id="user" /></td>
								<td width="28%">&nbsp;</td>
							</tr>
							<tr>
								<td height="24">&nbsp;</td>
								<td class="font">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码</td>
								<td><input class="input" type="password" name="pwd"
									id="password" /></td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td height="78">&nbsp;</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td height="40" colspan="4">
									<table width="100%" border="0">
										<tr>
											<td width="189" height="32">&nbsp;</td>
											<td width="91"><img style="cursor: pointer;"
												src="${pageContext.request.contextPath}/platform/image/login/bt_03.png"
												id="btnSubmit"></td>
											<td width="17">&nbsp;</td>
											<td width="92"><img style="cursor: pointer;"
												src="${pageContext.request.contextPath}/platform/image/login/bt_05.png"
												id="btnRegister"></td>
											<td width="206">&nbsp;</td>
										</tr>
									</table>
								</td>
						</table>
					</form>
				</td>
				<td>&nbsp;</td>
			</tr>

		</table>

	</div>
</body>
</html>
