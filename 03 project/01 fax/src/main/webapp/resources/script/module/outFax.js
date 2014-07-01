Ext.ns("fax.out");
// *****************************************************************************
// store
// *****************************************************************************
var faxNames = new Array();

var addressBookTreeStore = new Ext.data.TreeStore({
	idProperty : 'id',
	proxy : {
		type : 'ajax',
		reader : 'json',
		url : contextPath + '/controller/common/getAddressBookTreeGrid.do?flg=1'
	},
	lazyFill : true,
	fields : ['id', 'code', 'text', 'type']
});
// *****************************************************************************
// function
// *****************************************************************************
var initData = function(panel, records, node, operation, eOpts) {
	var id = window.request["id"];
	if (!id)
		return;

	getFax(id);
	getReceiveUser(id, node);
	getFaxNos(id);
	getFaxFiles(id);

};

var getFax = function(faxId) {
	Ext.Ajax.request({
		url : contextPath + '/controller/outFax/getFax.do',
		params : {
			id : faxId
		},
		method : 'POST',
		success : function(response, options) {
			if (response.responseText) {
				var result = Ext.JSON.decode(response.responseText).data;
				var form = Ext.getCmp("outboxForm").getForm();
				form.reset();
				if (result) {
					form.findField("id").setValue(result.id);
					form.findField("title").setValue(result.title);
					form.findField("content").setValue(result.content);
				}
			}
		}
	});
};

var getReceiveUser = function(faxId, node) {

	var checkNode = function(persons, orgs, temps, _node) {
		_node.expand();
		_node.eachChild(function(child) {
			var type = child.get("type");
			var code = child.get("code");
			if ("org" == type) {
				if (0 <= orgs.exist(code))
					checkChild(child, true);
				else
					checkNode(persons, orgs, temps, child);
			} else if ("addressList" == type) {
				if (0 <= temps.exist(code))
					checkChild(child, true);
				else
					checkNode(persons, orgs, temps, child);
			} else if ("orgPerson" == type || "addressListPerson" == type) {
				if (0 <= persons.exist(code))
					child.set('checked', true);
			}
		});

	};

	Ext.Ajax.request({
		url : contextPath + '/controller/outFax/getReceiveUser.do',
		params : {
			faxId : faxId
		},
		method : 'POST',
		success : function(response, options) {
			if (response.responseText) {
				var result = Ext.JSON.decode(response.responseText).data;
				var persons = [], orgs = [], temps = [];

				for (var i = 0; i < result.length; i++) {
					switch(result[i].type) {
						case 1:
							persons.push(result[i].receiveCode);
							break;
						case 2:
							orgs.push(result[i].receiveCode);
							break;
						case 3:
							temps.push(result[i].receiveCode);
							break;
					}
				}
				checkNode(persons, orgs, temps, node[0]);
				checkNode(persons, orgs, temps, node[1]);
			}
		}
	});

};

var getFaxNos = function(faxId) {
	Ext.Ajax.request({
		url : contextPath + '/controller/outFax/getFaxNos.do',
		params : {
			faxId : faxId
		},
		method : 'POST',
		success : function(response, options) {
			if (response.responseText) {
				var result = Ext.JSON.decode(response.responseText).message;
				var form = Ext.getCmp("outboxForm").getForm();
				form.findField("faxNos").setValue(result);
			}
		}
	});
};

var addFileTabItem = function(name) {

	if (Ext.getCmp(name)) {
		Ext.getCmp(name).show();
		return;
	}

	var item = {}, type = "", src = contextPath + "/controller/common/getFile.do?fileName=" + name;
	if (0 < name.lastIndexOf("\."))
		type = name.substr(name.lastIndexOf("\.") + 1);
	if ("pdf" == type) {
		item = {
			xtype : 'panel',
			html : '<object data="' + src + '" type="application/pdf" width="100%" height="400"></object>'
		};
	} else {
		item = Ext.create('Ext.Img', {
			height : 270,
			src : src,
			listeners : {
				dblclick : {
					element : 'el',
					fn : function() {
						previewImage(name);
					}
				}
			}
		});
	}

	faxNames.push(name);
	var groupItem = {
		title : faxNames.length,
		id : name,
		closable : true,
		autoScroll : true,
		items : item,
		listeners : {
			destroy : function(panel, eOpts) {
				faxNames = faxNames.remove(panel.id);
			}
		}
	};

	Ext.getCmp("faxFile").add(groupItem);
};

var previewImage = function(name) {

	var item = Ext.create('Ext.Img', {
		height : "100%",
		src : contextPath + "/controller/common/getImage.do?fileName=" + name
	});

	fax.out.PreviewWindow = Ext.extend(Ext.Window, {
		id : 'previewWindow',
		height : 800,
		width : 600,
		autoScroll : true,
		modal : true,
		items : item
	});

	var win = new fax.out.PreviewWindow();
	win.show();

};

var getFaxFiles = function(faxId) {
	faxNames = new Array();
	Ext.Ajax.request({
		url : contextPath + '/controller/outFax/getFaxFiles.do',
		params : {
			faxId : faxId
		},
		method : 'POST',
		success : function(response, options) {
			if (response.responseText) {
				var result = Ext.JSON.decode(response.responseText).data;
				for (var i = 0; i < result.length; i++) {
					addFileTabItem(result[i].faxFilePath);
				}
			}
		}
	});
};

var saveFax = function(type) {

	var receiveCodes = [], receiveTypes = [];

	var getReceive = function(_node) {
		_node.expand();
		_node.eachChild(function(child) {
			var type = child.get("type");
			var code = child.get("code");
			var checked = child.get("checked");
			if ("orgRoot" == type || "addressListRoot" == type) {
				getReceive(child);
			} else if ("org" == type) {
				if (checked) {
					receiveCodes.push(code);
					receiveTypes.push("2");
				} else
					getReceive(child);
			} else if ("addressList" == type) {
				if (checked) {
					receiveCodes.push(code);
					receiveTypes.push("3");
				} else
					getReceive(child);
			} else if ("orgPerson" == type || "addressListPerson" == type) {
				if (checked) {
					var index = receiveCodes.exist(code);
					if (index < 0 && "1" != receiveTypes[index]) {
						receiveCodes.push(code);
						receiveTypes.push("1");
					}
				}
			}
		});
	};

	var rootNode = Ext.getCmp("rightPanel").getRootNode();

	getReceive(rootNode);

	var form = Ext.getCmp("outboxForm").getForm();

	if (!form.isValid()) {
		Ext.Msg.alert('错误', '输入内容有误！');
		return;
	}

	if (2 == type && 0 == receiveCodes.length && 0 == receiveTypes.length) {
		Ext.Msg.alert('错误', '请至少填写一个传真号！');
		return;
	}

	form.submit({
		url : contextPath + '/controller/outFax/saveFax.do',
		params : {
			type : type,
			faxNames : faxNames,
			receiveCodes : receiveCodes,
			receiveTypes : receiveTypes
		},
		method : 'POST',
		success : function(from, action) {
			Ext.Msg.alert('提示', ((2 == type) ? "发送" : "保存") + '成功！', function() {
				closeTabItem();
			});
		}
	});

};

var addReceiveStore = function(record) {

	var length = receiveStore.getCount();
	var flg = true;
	for (var i = 0; i < length; i++) {
		flg = true;
		for (var p in record) {
			flg = flg && receiveStore.getAt(i).get(p) == record[p];
		}

		if (flg)
			return;
	}

	receiveStore.add(record);
};

var checkChild = function(node, checked) {
	node.set('checked', checked);
	node.expand();
	node.eachChild(function(child) {
		checkChild(child, checked);
		child.set('checked', checked);
	});
};

var checkParent = function(node) {
	var parent = node.parentNode;
	if ("root" != parent.get('id')) {
		parent.set('checked', false);
		checkParent(parent);
	}
};

var closeTabItem = function() {
	if (parent) {
		var path = "outFax";
		var id = window.request["id"];
		if (id)
			path += "?id=" + id;
		parent.removeTabItem(path.replaceAll("/", "").replaceAll("\\?", "-").replaceAll("=", "-"));
	}
};

// *****************************************************************************
// view
// *****************************************************************************
Ext.onReady(function() {
	Ext.QuickTips.init();
	var mainPanel = new fax.out.MainPanel();

	new Ext.Viewport({
		layout : 'fit',
		items : mainPanel
	});
});

fax.out.MainPanel = Ext.extend(Ext.Panel, {
	id : 'mainPanel',
	layout : 'border',
	frame : true,
	constructor : function(config) {
		var leftPanel = new fax.out.LeftPanel({
			region : 'center'
		});
		var rightPanel = new fax.out.RightPanel({
			region : 'east',
			width : 200
		});

		var group = {
			items : [leftPanel, rightPanel]
		};

		fax.out.MainPanel.superclass.constructor.call(this, group);
	}
});

fax.out.LeftPanel = Ext.extend(Ext.Panel, {
	layout : 'fit',
	tbar : {
		xtype : 'form',
		frame : true,
		buttonAlign : 'left',
		buttons : [{
			text : '发送',
			handler : function() {
				saveFax(2);
			}
		}, {
			text : '保存草稿箱',
			handler : function() {
				saveFax(3);
			}
		}]
	},
	items : [{
		layout : 'border',
		frame : true,
		items : [{
			id : 'outboxForm',
			xtype : 'form',
			frame : true,
			region : 'north',
			items : [{
				xtype : 'hiddenfield',
				id : 'id',
				name : 'id'
			}, {
				xtype : 'textfield',
				fieldLabel : '标题',
				id : 'title',
				width : 500,
				name : 'title'
			}, {
				xtype : 'textfield',
				fieldLabel : '号码（用,分割）',
				id : 'faxNos',
				width : 500,
				name : 'faxNos'
			}, {
				xtype : 'textarea',
				fieldLabel : '发件内容',
				id : 'content',
				width : 900,
				height : 200,
				name : 'content'
			}]
		}, {
			xtype : 'tabpanel',
			title : '传真文件',
			region : 'center',
			bbar : {
				xtype : 'form',
				buttonAlign : 'left',
				buttons : [{
					text : '上传',
					handler : function() {
						var win = new fax.out.UploadWindow();
						win.show();
					}
				}]
			},
			id : 'faxFile',
		}]
	}]

});

fax.out.RightPanel = Ext.extend(Ext.tree.Panel, {
	id : 'rightPanel',
	title : '通讯录',
	collapsible : true,
	useArrows : true,
	rootVisible : false,
	multiSelect : true,
	store : addressBookTreeStore,
	animate : false,
	plugins : [{
		ptype : 'bufferedrenderer'
	}],
	listeners : {
		load : initData,
		checkchange : function(node, checked) {
			checkChild(node, checked);
			if (!checked)
				checkParent(node);
		}
	}
});

// *****************************************************************************
// window
// *****************************************************************************
fax.out.UploadWindow = Ext.extend(Ext.Window, {
	id : 'uploadWindow',
	title : '上传文件',
	autoScroll : true,
	resizable : false,
	maximizable : false,
	modal : true,
	items : [{
		id : 'uploadForm',
		frame : true,
		xtype : 'form',
		defaults : {
			labelWidth : 50,
			width : 400
		},
		items : [{
			xtype : 'filefield',
			fieldLabel : '文件',
			id : 'uploadImageFile',
			name : 'imageFile',
			allowBlank : false
		}]
	}],
	buttons : [{
		text : '确定',
		handler : function() {
			var form = Ext.getCmp("uploadForm").getForm();
			if (!form.isValid()) {
				Ext.Msg.alert('错误', '输入内容有误！');
				return;
			}

			form.submit({
				url : contextPath + '/controller/common/upload.do',
				method : 'POST',
				success : function(from, action) {

					var name = action.result.message;
					addFileTabItem(name);
					Ext.getCmp("uploadWindow").close();
				}
			});
		}
	}, {
		text : '取消',
		handler : function() {
			Ext.getCmp("uploadWindow").close();
		}
	}]
});
