$(document).ready(function() {
	init();
});

var init = function() {
	$("#btnRegister").bind("click", function() {
		$("#frmRegister").ajaxSubmit({
			url : contextPath + "/controller/common/register.do",
			method : 'post',
			success : function(data) {
				if (data.success) {
					alert(data.message);
					window.location.href = contextPath + "/";
				} else {
					alert(data.message);
				}
			}
		});
	});

	$("#btnBack").bind("click", function() {
		window.location.href = contextPath + "/";
	});

};