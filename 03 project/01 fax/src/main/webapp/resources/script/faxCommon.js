Ext.ns("faxbox.manage");
// *****************************************************************************
// store
// *****************************************************************************
var faxStatStore = new Ext.data.Store({
	proxy : {
		type : 'ajax',
		url : contextPath + '/controller/faxBox/getFaxStat.do',
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	fields : ['sendAccount', 'sendBegin', 'retryTime', 'totalPage', 'status', 'ordNo', 'recvIndex', 'recvNumber', 'recvName', 'recvCompany', 'sendCnt', 'sendedPage', 'message', 'page']
});

// *****************************************************************************
// function
// *****************************************************************************
var faxNames = new Array();

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
			html : '<object data="' + src + '" type="application/pdf" width="100%" height="250"></object>'
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
		closable : false,
		autoScroll : true,
		items : item
	};

	Ext.getCmp("faxDetailFaxFile").add(groupItem);
};

var previewImage = function(name) {

	var item = Ext.create('Ext.Img', {
		height : "100%",
		src : contextPath + "/controller/common/getImage.do?fileName=" + name
	});

	faxbox.manage.PreviewWindow = Ext.extend(Ext.Window, {
		id : 'previewWindow',
		height : 800,
		width : 600,
		autoScroll : true,
		modal : true,
		items : item
	});

	var win = new faxbox.manage.PreviewWindow();
	win.show();

};

var initFaxDetailWindow = function(data) {
	faxNames = new Array();
	var form = Ext.getCmp("faxDetailForm").getForm();

	for (var p in data) {
		if (form.findField(p))
			form.findField(p).setValue(data[p]);
	}

	Ext.apply(faxStatStore.proxy.extraParams, {
		faxId : form.findField('id').getValue()
	});
	faxStatStore.load();

	Ext.Ajax.request({
		url : contextPath + '/controller/outFax/getFaxFiles.do',
		params : {
			faxId : data.id
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

var searchFax = function() {
	var form = Ext.getCmp("searchPanel").getForm();
	var params = {
		key : form.findField("key").getValue(),
		start : 0,
		limit : 50
	};

	Ext.apply(gridStore.proxy.extraParams, params);

	gridStore.load();
};

var showOutFaxTab = function(id) {
	if (parent) {
		var param = "";
		if (id)
			param = "?id=" + id;
		parent.addTabItem('发传真', 'outFax' + param);
	}
};

var moveToRecycleFaxBox = function() {

	var rows = Ext.getCmp("gridPanel").getSelectionModel().getSelection();
	var length = rows.length;
	if (0 == length) {
		Ext.Msg.alert("提示", "请至少选择一条！");
		return;
	}
	Ext.Msg.confirm('删除', '确定删除吗？', function(btn) {
		if (btn == 'yes') {
			var ids = [];
			for (var i = 0; i < length; i++) {
				ids.push(rows[i].get("id"));
			}
			Ext.Ajax.request({
				url : contextPath + '/controller/faxBox/moveToRecycleFaxBox.do',
				params : {
					ids : ids
				},
				method : 'POST',
				success : function(response, options) {
					if (response.responseText) {
						Ext.Msg.alert("提示", '该传真已移至回收箱！');
						gridStore.reload();
					}
				}
			});
		}
	}, this);

};

if (gridStore) {
	gridStore.on("load", function(store, records, successful, operation, eOpts) {
		if (parent)
			parent.initData();
	});
}

// *****************************************************************************
// window
// *****************************************************************************
faxbox.manage.FaxDetailWindow = Ext.extend(Ext.Window, {
	id : 'faxDetailWindow',
	autoScroll : true,
	resizable : false,
	maximizable : false,
	height : 400,
	width : 850,
	modal : true,
	constructor : function(config) {
		var group = {
			items : {
				xtype : 'tabpanel',
				frame : true,
				layout : 'fit',
				height : 700,
				width : 800,
				items : [{
					id : 'faxDetailForm',
					title : '邮件信息',
					xtype : 'form',
					frame : true,
					defaults : {
						labelWidth : 70,
						width : 500
					},
					items : [{
						xtype : 'hiddenfield',
						id : 'faxDetailId',
						name : 'id'
					}, {
						xtype : 'textfield',
						fieldLabel : '标题',
						id : 'faxDetailTitle',
						name : 'title',
						readOnly : true
					}, {
						xtype : 'textarea',
						fieldLabel : '发件内容',
						id : 'faxDetailContent',
						width : 700,
						height : 200,
						name : 'content',
						readOnly : true
					}, {
						xtype : 'datefield',
						fieldLabel : '时间',
						id : 'faxDetailSaveTime',
						name : 'saveTime',
						format : 'Y-m-d',
						readOnly : true
					}, {
						xtype : 'tabpanel',
						id : 'faxDetailFaxFile',
						width : 700,
						height : 300
					}]
				}, {
					id : 'faxStatus',
					title : '发送任务明细',
					xtype : 'grid',
					width : 700,
					layout : 'fit',
					frame : true,
					loadMask : true,
					tbar : {
						items : [{
							xtype : 'button',
							text : '刷新',
							handler : function() {
								faxStatStore.reload();
							}
						}]
					},
					store : faxStatStore,
					columns : [{
						text : "接收号码",
						width : 150,
						dataIndex : 'recvNumber'
					}, {
						text : "状态",
						width : 300,
						dataIndex : 'message'
					}, {
						text : "页数",
						width : 100,
						dataIndex : 'page'
					}, {
						text : "发送时间",
						width : 200,
						dataIndex : 'sendBegin'
					}]
				}]
			}
		};
		faxbox.manage.FaxDetailWindow.superclass.constructor.call(this, group);
	},
	buttons : [{
		text : '关闭',
		handler : function() {
			Ext.getCmp("faxDetailWindow").close();
		}
	}]
});
