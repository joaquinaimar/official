window.onload = function() {
	document.getElementById("btnSubmit").onclick = function() {
		document.getElementById("frmLogin").submit();
	};

	document.getElementById("btnRegister").onclick = function() {
		window.location.href = contextPath + "/register";
	};

};