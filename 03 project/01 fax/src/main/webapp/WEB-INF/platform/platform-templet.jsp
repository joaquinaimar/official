<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<!DOCTYPE html>
<html>
<head>
<title><tiles:insertAttribute name="title" /></title>

<meta content="text/html; charset=utf-8" http-equiv="Content-Type">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE10" />

<link rel="stylesheet" type="text/css"
	href="<%=application.getContextPath()%>/webjars/extjs/resources/ext-theme-classic-ex/ext-theme-classic-all-ex.css" />

<script type="text/javascript"
	src="<%=application.getContextPath()%>/webjars/extjs/ext-all.js"></script>
<script type="text/javascript"
	src="<%=application.getContextPath()%>/webjars/extjs/locale/ext-lang-zh_CN.js"></script>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/webjars/ckeditor/ckeditor.js"></script>

<link rel="stylesheet" type="text/css" media="screen"
	href="${pageContext.request.contextPath}/resources/style/common.css" />

<script type="text/javascript"
	src="${pageContext.request.contextPath}/platform/script/common.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/script/common.js"></script>

<script type="text/javascript">
	var contextPath = "${pageContext.request.contextPath}";
	Ext.BLANK_IMAGE_URL = contextPath
			+ '/webjars/extjs/resources/themes/images/default/tree/s.gif';
</script>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}<tiles:insertAttribute name="style" />">
<script type="text/javascript"
	src="${pageContext.request.contextPath}<tiles:insertAttribute name="script" />"></script>

</head>
<body>
	<tiles:insertAttribute name="body" />
</body>
</html>