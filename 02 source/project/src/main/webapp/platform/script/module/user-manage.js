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
		url : contextPath + '/controller/system/searchUserInfo.do',
		reader : {
			type : 'json',
			totalProperty : "totalCount",
			root : 'result'
		}
	},
	fields : [ 'id', 'username', 'rolename' ]
});

var roleComboxStore = new Ext.data.Store({
	autoLoad : true,
	proxy : {
		type : 'ajax',
		url : contextPath + '/controller/system/getRoleInfoCombox.do',
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	fields : [ 'rolename' ]
});

// *****************************************************************************
// function
// *****************************************************************************
var searchUserInfo = function() {
	var form = Ext.getCmp("searchPanel").getForm();
	var params = {
		username : form.findField("username").getValue(),
		rolename : form.findField("rolename").getValue(),
		start : 0,
		limit : 50
	};

	Ext.apply(gridStore.proxy.extraParams, params);

	gridStore.load();
};

var saveUserInfo = function() {
	var form = Ext.getCmp("userDetailForm").getForm();
	form.submit({
		url : contextPath + '/controller/system/saveUserInfo.do',
		method : 'POST',
		success : function(from, action) {
			Ext.Msg.alert('提示', '保存成功！', function() {
				gridStore.reload();
				Ext.getCmp("userDetailWindow").close();
			});
		}
	});
};

// *****************************************************************************
// view
// *****************************************************************************
Ext.ns("user.manage");

Ext.onReady(function() {
	Ext.QuickTips.init();
	var mainPanel = new user.manage.MainPanel();

	new Ext.Viewport({
		layout : 'fit',
		items : mainPanel
	});

});

user.manage.MainPanel = Ext.extend(Ext.Panel, {
	id : 'mainPanel',
	layout : 'border',
	title : '用户管理',
	constructor : function(config) {
		var searchPanel = new user.manage.SearchPanel();
		var gridPanel = new user.manage.GridPanel();

		var group = {
			items : [ searchPanel, gridPanel ]
		};

		user.manage.MainPanel.superclass.constructor.call(this, group);
	}
});

user.manage.SearchPanel = Ext.extend(Ext.form.FormPanel, {
	id : 'searchPanel',
	region : 'north',
	buttonAlign : 'left',
	frame : true,
	layout : 'fit',
	items : [ {
		frame : true,
		layout : 'column',
		defaults : {
			labelWidth : 70,
			columnWidth : .25
		},
		items : [ {
			xtype : 'textfield',
			fieldLabel : '用户名',
			id : 'username',
			name : 'username'
		}, {
			xtype : 'combobox',
			fieldLabel : '角色',
			id : 'rolename',
			name : 'rolename',
			store : roleComboxStore,
			emptyText : '请选择',
			valueField : 'rolename',
			displayField : 'rolename',
			queryMode : 'local',
			triggerAction : 'all',
			editable : false
		} ]
	} ],
	buttons : [ {
		text : '查询',
		handler : function() {
			searchUserInfo();
		}
	}, {
		text : '重置',
		handler : function() {
			Ext.getCmp("searchPanel").getForm().reset();
			searchUserInfo();
		}
	} ]
});

user.manage.GridPanel = Ext.extend(Ext.grid.GridPanel, {
	id : 'gridPanel',
	region : 'center',
	frame : true,
	layout : 'fit',
	loadMask : true,
	store : gridStore,
	bbar : {
		xtype : 'pagingtoolbar',
		displayInfo : true,
		store : gridStore
	},
	columns : [ new Ext.grid.RowNumberer({
		header : 'NO.',
		width : 50
	}), {
		text : "用户名",
		width : 200,
		dataIndex : 'username'
	}, {
		text : "角色名",
		width : 200,
		dataIndex : 'rolename'
	} ],
	listeners : {
		itemdblclick : function(view, record, item, index, e, eOpts) {
			var win = new user.manage.UserDetailWindow();
			win.show();
			var form = Ext.getCmp("userDetailForm").getForm();
			form.reset();
			for ( var p in record.data) {
				if (form.findField(p))
					form.findField(p).setValue(record.get(p));
			}
		}
	}
});

// *****************************************************************************
// window
// *****************************************************************************
user.manage.UserDetailWindow = Ext.extend(Ext.Window, {
	id : 'userDetailWindow',
	title : '用户信息',
	autoScroll : true,
	resizable : false,
	maximizable : false,
	modal : true,
	items : [ {
		id : 'userDetailForm',
		frame : true,
		xtype : 'form',
		defaults : {
			labelWidth : 70,
			width : 300
		},
		items : [ {
			xtype : 'hiddenfield',
			id : 'childId',
			name : 'id'
		}, {
			xtype : 'textfield',
			fieldLabel : '用户名',
			id : 'childUsername',
			name : 'username',
			readOnly : true
		}, {
			xtype : 'combobox',
			fieldLabel : '角色',
			id : 'childRolename',
			name : 'rolename',
			store : roleComboxStore,
			emptyText : '请选择',
			valueField : 'rolename',
			displayField : 'rolename',
			queryMode : 'local',
			triggerAction : 'all',
			editable : false
		} ]
	} ],
	buttons : [ {
		text : '保存',
		handler : function() {
			saveUserInfo();
		}
	}, {
		text : '关闭',
		handler : function() {
			Ext.getCmp("userDetailWindow").close();
		}
	} ]
});