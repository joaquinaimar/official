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
		url : contextPath + '/controller/faxBox/searchDraftFax.do',
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
// *****************************************************************************
// function
// *****************************************************************************

// *****************************************************************************
// view
// *****************************************************************************
Ext.ns("draftbox.manage");

Ext.onReady(function() {
	Ext.QuickTips.init();
	var mainPanel = new draftbox.manage.MainPanel();

	new Ext.Viewport({
		layout : 'fit',
		items : mainPanel
	});

});

draftbox.manage.MainPanel = Ext.extend(Ext.Panel, {
	id : 'mainPanel',
	layout : 'border',
	frame : true,
	constructor : function(config) {
		var searchPanel = new draftbox.manage.SearchPanel();
		var gridPanel = new draftbox.manage.GridPanel();

		var group = {
			items : [searchPanel, gridPanel]
		};

		draftbox.manage.MainPanel.superclass.constructor.call(this, group);
	}
});

draftbox.manage.SearchPanel = Ext.extend(Ext.form.FormPanel, {
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
		text : '删除',
		handler : function() {
			moveToRecycleFaxBox();
		}
	}]
});

draftbox.manage.GridPanel = Ext.extend(Ext.grid.GridPanel, {
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
			showOutFaxTab(record.get("id"));
		}
	}
});
