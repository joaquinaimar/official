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
		url : contextPath + '/controller/system/searchRoleInfo.do',
		reader : {
			type : 'json',
			totalProperty : "totalCount",
			root : 'result'
		}
	},
	fields : ['rolename']
});

var menunameMultiselectStore = new Ext.data.Store({
	proxy : {
		type : 'ajax',
		url : contextPath + '/controller/system/getMenunameMultiselect.do',
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	fields : ['menuname']
});

var permissionMultiselectStore = new Ext.data.Store({
	proxy : {
		type : 'ajax',
		url : contextPath + '/controller/system/getPermissionMultiselect.do',
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	fields : ['menuname']
});

// *****************************************************************************
// function
// *****************************************************************************
var searchUserInfo = function() {
	var form = Ext.getCmp("searchPanel").getForm();
	var params = {
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

var addPermission = function(menu) {
	Ext.Ajax.request({
		url : contextPath + '/controller/system/addPermission.do',
		params : {
			rolename : Ext.getCmp("roleDetailForm").getForm().findField("rolename").getValue(),
			menu : menu
		},
		method : 'POST',
		success : function(response, options) {
			if (response.responseText) {
				menunameMultiselectStore.reload();
				permissionMultiselectStore.reload();
			}
		}
	});

};

var removePermission = function(menu) {
	Ext.Ajax.request({
		url : contextPath + '/controller/system/removePermission.do',
		params : {
			rolename : Ext.getCmp("roleDetailForm").getForm().findField("rolename").getValue(),
			menu : menu
		},
		method : 'POST',
		success : function(response, options) {
			if (response.responseText) {
				menunameMultiselectStore.reload();
				permissionMultiselectStore.reload();
			}
		}
	});
};

// *****************************************************************************
// view
// *****************************************************************************
Ext.ns("permit.manage");

Ext.onReady(function() {
	Ext.QuickTips.init();
	var mainPanel = new permit.manage.MainPanel();

	new Ext.Viewport({
		layout : 'fit',
		items : mainPanel
	});

});

permit.manage.MainPanel = Ext.extend(Ext.Panel, {
	id : 'mainPanel',
	layout : 'border',
	title : '权限管理',
	constructor : function(config) {
		var searchPanel = new permit.manage.SearchPanel();
		var gridPanel = new permit.manage.GridPanel();

		var group = {
			items : [searchPanel, gridPanel]
		};

		permit.manage.MainPanel.superclass.constructor.call(this, group);
	}
});

permit.manage.SearchPanel = Ext.extend(Ext.form.FormPanel, {
	id : 'searchPanel',
	region : 'north',
	buttonAlign : 'left',
	frame : true,
	layout : 'fit',
	items : [{
		frame : true,
		layout : 'column',
		defaults : {
			labelWidth : 70,
			columnWidth : .25
		},
		items : [{
			xtype : 'textfield',
			fieldLabel : '角色名',
			id : 'rolename',
			name : 'rolename'
		}]
	}],
	buttons : [{
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
	}]
});

permit.manage.GridPanel = Ext.extend(Ext.grid.GridPanel, {
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
	columns : [new Ext.grid.RowNumberer({
		header : 'NO.',
		width : 50
	}), {
		text : "角色名",
		width : 200,
		dataIndex : 'rolename'
	}],
	listeners : {
		itemdblclick : function(view, record, item, index, e, eOpts) {
			var win = new permit.manage.RoleDetailWindow();
			win.show();
			var form = Ext.getCmp("roleDetailForm").getForm();
			form.reset();
			for (var p in record.data) {
				if (form.findField(p))
					form.findField(p).setValue(record.get(p));
			}

			var rolename = Ext.getCmp("roleDetailForm").getForm().findField("rolename").getValue();
			Ext.apply(menunameMultiselectStore.proxy.extraParams, {
				rolename : rolename
			});
			menunameMultiselectStore.load();
			Ext.apply(permissionMultiselectStore.proxy.extraParams, {
				rolename : rolename
			});
			permissionMultiselectStore.load();
		}
	}
});

// *****************************************************************************
// window
// *****************************************************************************
permit.manage.RoleDetailWindow = Ext.extend(Ext.Window, {
	id : 'roleDetailWindow',
	title : '角色信息',
	autoScroll : true,
	resizable : false,
	maximizable : false,
	modal : true,
	constructor : function(config) {
		var group = {
			items : [{
				id : 'roleDetailForm',
				frame : true,
				xtype : 'form',
				defaults : {
					labelWidth : 70,
					width : 300
				},
				items : [{
					xtype : 'hiddenfield',
					id : 'childId',
					name : 'id'
				}, {
					xtype : 'textfield',
					fieldLabel : '角色名',
					id : 'childRolename',
					name : 'rolename',
					readOnly : true
				}, {
					xtype : 'panel',
					frame : true,
					layout : {
						type : 'hbox',
						align : 'stretch',
						pack : 'start'
					},
					width : 800,
					items : [{
						xtype : 'grid',
						title : '未分配权限',
						id : 'childMenuname',
						loadMask : true,
						store : menunameMultiselectStore,
						selModel : new Ext.selection.CheckboxModel(),
						columns : [{
							text : "菜单名",
							width : '80%',
							dataIndex : 'menuname'
						}],
						height : 200,
						flex : 45
					}, {
						frame : true,
						layout : 'form',
						region : 'center',
						defaults : {
							xtype : 'button',
							scale : 'large',
							width : '100%'
						},
						items : [{
							text : '>>',
							handler : function() {
								var menu = [];
								var store = Ext.getCmp("childMenuname").getStore();
								var length = store.getCount();
								if (0 == length) {
									Ext.Msg.alert("提示", "请至少选择一条！");
									return;
								}
								for (var i = 0; i < length; i++) {
									menu.push(store.getAt(i).get("menuname"));
								}
								addPermission(menu);
							}
						}, {
							text : '>',
							handler : function() {
								var menu = [];
								var rows = Ext.getCmp("childMenuname").getSelectionModel().getSelection();
								var length = rows.length;
								if (0 == length) {
									Ext.Msg.alert("提示", "请至少选择一条！");
									return;
								}
								for (var i = 0; i < length; i++) {
									menu.push(rows[i].get("menuname"));
								}
								addPermission(menu);
							}
						}, {
							text : '<',
							handler : function() {
								var menu = [];
								var rows = Ext.getCmp("childPermission").getSelectionModel().getSelection();
								var length = rows.length;
								if (0 == length) {
									Ext.Msg.alert("提示", "请至少选择一条！");
									return;
								}
								for (var i = 0; i < length; i++) {
									menu.push(rows[i].get("menuname"));
								}
								removePermission(menu);
							}
						}, {
							text : '<<',
							handler : function() {
								var menu = [];
								var store = Ext.getCmp("childPermission").getStore();
								var length = store.getCount();
								if (0 == length) {
									Ext.Msg.alert("提示", "请至少选择一条！");
									return;
								}
								for (var i = 0; i < length; i++) {
									menu.push(store.getAt(i).get("menuname"));
								}
								removePermission(menu);
							}
						}],
						flex : 10
					}, {
						xtype : 'grid',
						title : '已分配权限',
						id : 'childPermission',
						loadMask : true,
						store : permissionMultiselectStore,
						selModel : new Ext.selection.CheckboxModel(),
						columns : [{
							text : "菜单名",
							width : '80%',
							dataIndex : 'menuname'
						}],
						height : 200,
						flex : 45
					}]
				}]
			}]
		};
		permit.manage.RoleDetailWindow.superclass.constructor.call(this, group);
	},
	buttons : [{
		text : '关闭',
		handler : function() {
			Ext.getCmp("roleDetailWindow").close();
		}
	}]
});
