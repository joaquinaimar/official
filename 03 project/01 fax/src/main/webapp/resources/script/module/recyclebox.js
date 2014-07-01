// *****************************************************************************
// store
// *****************************************************************************
var gridStore = new Ext.data.Store({
	pageSize : 50,
	autoLoad : {
		params : {
			start : 0,
			limit : 50
		}
	},
	proxy : {
		type : 'ajax',
		url : contextPath + '/controller/faxBox/searchRecycleFax.do',
		reader : {
			type : 'json',
			totalProperty : "totalCount",
			root : 'result'
		}
	},
	fields : ['id', 'sendUser', 'title', {
		name : 'saveTime',
		type : 'date',
		dateFormat : 'time'
	}, 'content']
});

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
// *****************************************************************************
// function
// *****************************************************************************
var restoreFax = function() {

	var rows = Ext.getCmp("gridPanel").getSelectionModel().getSelection();
	var length = rows.length;
	if (0 == length) {
		Ext.Msg.alert("提示", "请至少选择一条！");
		return;
	}

	var ids = [];
	for (var i = 0; i < length; i++) {
		ids.push(rows[i].get("id"));
	}
	Ext.Ajax.request({
		url : contextPath + '/controller/faxBox/restoreFax.do',
		params : {
			ids : ids
		},
		method : 'POST',
		success : function(response, options) {
			if (response.responseText) {
				Ext.Msg.alert("提示", '该传真已还原！');
				gridStore.reload();
			}
		}
	});
};

var deleteFax = function() {

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
				url : contextPath + '/controller/faxBox/deleteFax.do',
				params : {
					ids : ids
				},
				method : 'POST',
				success : function(response, options) {
					if (response.responseText) {
						Ext.Msg.alert("提示", '该传真已彻底删除！');
						gridStore.reload();
					}
				}
			});
		}
	}, this);
};

// *****************************************************************************
// view
// *****************************************************************************
Ext.ns("recyclebox.manage");

Ext.onReady(function() {
	Ext.QuickTips.init();
	var mainPanel = new recyclebox.manage.MainPanel();

	new Ext.Viewport({
		layout : 'fit',
		items : mainPanel
	});

});

recyclebox.manage.MainPanel = Ext.extend(Ext.Panel, {
	id : 'mainPanel',
	layout : 'border',
	frame : true,
	constructor : function(config) {
		var searchPanel = new recyclebox.manage.SearchPanel();
		var gridPanel = new recyclebox.manage.GridPanel();

		var group = {
			items : [searchPanel, gridPanel]
		};

		recyclebox.manage.MainPanel.superclass.constructor.call(this, group);
	}
});

recyclebox.manage.SearchPanel = Ext.extend(Ext.form.FormPanel, {
	id : 'searchPanel',
	region : 'north',
	buttonAlign : 'left',
	layout : 'fit',
	items : [{
		layout : 'column',
		defaults : {
			labelWidth : 70,
			columnWidth : .25
		},
		frame : true,
		items : [{
			xtype : 'textfield',
			fieldLabel : '关键字',
			id : 'key',
			name : 'key'
		}]
	}],
	buttons : [{
		text : '查询',
		handler : function() {
			searchFax();
		}
	}, {
		text : '重置',
		handler : function() {
			Ext.getCmp("searchPanel").getForm().reset();
			searchFax();
		}
	}, {
		text : '还原',
		handler : function() {
			restoreFax();
		}
	}, {
		text : '彻底删除',
		handler : function() {
			deleteFax();
		}
	}]
});

recyclebox.manage.GridPanel = Ext.extend(Ext.grid.GridPanel, {
	id : 'gridPanel',
	region : 'center',
	frame : true,
	layout : 'fit',
	loadMask : true,
	store : gridStore,
	selModel : new Ext.selection.CheckboxModel(),
	bbar : {
		xtype : 'pagingtoolbar',
		displayInfo : true,
		store : gridStore
	},
	columns : [{
		text : "标题",
		width : 200,
		dataIndex : 'title'
	}, {
		text : "内容",
		width : 300,
		dataIndex : 'content'
	}, {
		text : "收件时间",
		width : 200,
		xtype : 'datecolumn',
		format : 'Y-m-d H:i:s',
		dataIndex : 'saveTime'
	}],
	listeners : {
		itemdblclick : function(view, record, item, index, e, eOpts) {
			var win = new faxbox.manage.FaxDetailWindow();
			win.show();
			initFaxDetailWindow(record.data);
		}
	}
});
