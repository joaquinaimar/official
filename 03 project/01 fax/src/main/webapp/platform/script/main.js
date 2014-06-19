$(document).ready(function() {
	init();
	initData();
});

var init = function() {
	addTabItem("首页", "homepage", true);
};

var initData = function() {
	$.ajax({
		type : "post",
		dataType : "json",
		url : contextPath + "/controller/common/getFaxCount.do",
		success : function(obj) {
			var result = obj.data;
			$("span#inCnt").html(result["inCnt"]);
			$("span#outCnt").html(result["outCnt"]);
			$("span#draftCnt").html(result["draftCnt"]);
			$("span#recycleCnt").html(result["recycleCnt"]);
		}
	});
};

var addTabItem = function(name, path, flg) {

	var id = path.replaceAll("/", "").replaceAll("\\?", "-").replaceAll("=", "-");

	var tab = $("a[href=#" + id + "]");
	if (tab.length) {
		tab.tab("show");
		return;
	}

	var tabTitle = $("#tab-title");

	var liwidth = (name.charLength() * 0.5) + 2.5;

	liwidth = flg ? liwidth : (liwidth + 1);

	var strTitle = "<li style='width:" + liwidth + "em;'><a href='#" + id
			+ "' data-toggle='tab'>" + name;
	if (!flg) {
		strTitle = strTitle + "<button class='close' onClick='removeTabItem(\""
				+ id + "\")'>&times;</button>";
	}
	strTitle = strTitle + "</a></li>";

	tabTitle.append(strTitle);

	var tabBody = $("#tab-body");
	tabBody.append("<div class='tab-pane fade' id='" + id
			+ "'><iframe frameBorder='0' class='module' src=" + contextPath
			+ "/" + path + "></iframe></div>");

	$("a[href=#" + id + "]").tab("show");

};

var removeTabItem = function(id) {
	var parent = $("a[href=#" + id + "]").parent();

	if ("active" == parent[0].className) {
		var tab = parent.prev().children()[0].href;
		tab = tab.substr(tab.indexOf("#"));

		$("a[href=" + tab + "]").tab("show");
	}

	$("a[href=#" + id + "]").parent().remove();
	$("#" + id).remove();

};