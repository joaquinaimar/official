$(document).ready(function() {
	init();
});

var init = function() {
	swtichModule("homepage");
};

var swtichModule = function(url) {
	$("#module")[0].src = contextPath + "/" + url;
};