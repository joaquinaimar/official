Ext.ns("fax.addressList.maintenance");
// *****************************************************************************
// store
// *****************************************************************************
var selectOrgCode;

var personPhoneNoRowEditingPlugin = Ext.create('RowEditingPlugin', {
	listeners : {
		beforeedit : function(editor, e, eOpts) {
			if (e.record.get("id"))
				return false;
		}
	}
});

var personFaxNoRowEditingPlugin = Ext.create('RowEditingPlugin', {
	listeners : {
		beforeedit : function(editor, e, eOpts) {
			if (e.record.get("id"))
				return false;
		}
	}
});

var addressBookTreeStore = new Ext.data.TreeStore({
	idProperty : 'id',
	proxy : {
		type : 'ajax',
		reader : 'json',
		url : contextPath + '/controller/common/getAddressBookTreeGrid.do?flg=0'
	},
	lazyFill : true,
	fields : ['id', 'code', 'text', 'type']
});

var personGridStore = new Ext.data.Store({
	pageSize : 50,
	autoLoad : {
		params : {
			start : 0,
			limit : 50
		}
	},
	proxy : {
		type : 'ajax',
		url : contextPath + '/controller/addressBook/searchPerson.do',
		reader : {
			type : 'json',
			totalProperty : "totalCount",
			root : 'result'
		}
	},
	fields : ['code', 'name', 'email', 'orgCode', 'org']
});

var orgComboxStore = new Ext.data.Store({
	autoLoad : true,
	proxy : {
		type : 'ajax',
		url : contextPath + '/controller/addressBook/getOrgCombox.do',
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	fields : ['code', 'name']
});

var personPhoneNoStore = new Ext.data.Store({
	proxy : {
		type : 'ajax',
		url : contextPath + '/controller/addressBook/getPersonPhoneNo.do',
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	fields : ['id', 'no', 'personCode']
});

var personFaxNoStore = new Ext.data.Store({
	proxy : {
		type : 'ajax',
		url : contextPath + '/controller/addressBook/getPersonFaxNo.do',
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	fields : ['id', 'no', 'personCode']
});

var unabsorbedMultiselectStore = new Ext.data.Store({
	proxy : {
		type : 'ajax',
		url : contextPath + '/controller/addressBook/getUnabsorbedMultiselect.do',
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	fields : ['code', 'name', 'phoneNo', 'faxNo', 'email', 'orgCode', 'org']
});

var absorbedMultiselectStore = new Ext.data.Store({
	proxy : {
		type : 'ajax',
		url : contextPath + '/controller/addressBook/getAbsorbedMultiselect.do',
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	fields : ['code', 'name', 'phoneNo', 'faxNo', 'email', 'orgCode', 'org']
});
// *****************************************************************************
// function
// *****************************************************************************
var searchPerson = function() {
	var form = Ext.getCmp("searchPanel").getForm();
	var params = {
		name : form.findField("name").getValue(),
		orgCode : selectOrgCode,
		start : 0,
		limit : 50
	};

	Ext.apply(personGridStore.proxy.extraParams, params);

	personGridStore.load();
};

var saveAddressList = function(params) {
	Ext.Ajax.request({
		url : contextPath + '/controller/addressBook/saveAddressList.do',
		params : params,
		method : 'POST',
		success : function(response, options) {
			if (response.responseText) {
				Ext.Msg.alert("提示", '保存成功！');
				addressBookTreeStore.reload();
			}
		}
	});
};

var addAddressListDetail = function(person) {
	Ext.Ajax.request({
		url : contextPath + '/controller/addressBook/addAddressListDetail.do',
		params : {
			code : Ext.getCmp("addressListDetailForm").getForm().findField("code").getValue(),
			person : person
		},
		method : 'POST',
		success : function(response, options) {
			if (response.responseText) {
				unabsorbedMultiselectStore.reload();
				absorbedMultiselectStore.reload();
			}
		}
	});

};

var removeAddressListDetail = function(person) {
	Ext.Ajax.request({
		url : contextPath + '/controller/addressBook/removeAddressListDetail.do',
		params : {
			code : Ext.getCmp("addressListDetailForm").getForm().findField("code").getValue(),
			person : person
		},
		method : 'POST',
		success : function(response, options) {
			if (response.responseText) {
				unabsorbedMultiselectStore.reload();
				absorbedMultiselectStore.reload();
			}
		}
	});

};

var exportPerson = function() {

	var downloadIframe = document.getElementById("downloadExcel");

	if (downloadIframe) {
		downloadIframe.parentNode.removeChild(downloadIframe);
	}

	downloadIframe = document.createElement("iframe");
	downloadIframe.id = "downloadExcel";
	downloadIframe.src = contextPath + "/controller/addressBook/exportPerson.do";
	downloadIframe.style.display = "none";
	document.body.appendChild(downloadIframe);
};
// *****************************************************************************
// view
// *****************************************************************************
Ext.onReady(function() {
	Ext.QuickTips.init();
	var mainPanel = new fax.addressList.maintenance.MainPanel();

	new Ext.Viewport({
		layout : 'fit',
		items : mainPanel
	});

});

fax.addressList.maintenance.MainPanel = Ext.extend(Ext.Panel, {
	id : 'mainPanel',
	layout : 'border',
	frame : true,
	constructor : function(config) {
		var leftPanel = new fax.addressList.maintenance.LeftPanel({
			region : 'west',
			width : 300
		});
		var rightPanel = new fax.addressList.maintenance.RightPanel({
			region : 'center'
		});

		var group = {
			items : [leftPanel, rightPanel]
		};

		fax.addressList.maintenance.MainPanel.superclass.constructor.call(this, group);
	}
});

fax.addressList.maintenance.LeftPanel = Ext.extend(Ext.tree.Panel, {
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
		itemcontextmenu : function(menutree, rec, items, index, e) {
			e.preventDefault();
			e.stopEvent();

			var type = rec.get("type");
			var items = new Array();

			if ("addressListRoot" == type) {
				items.push({
					text : "添加临时组",
					handler : function() {
						var win = new fax.addressList.maintenance.AddressListDetailWindow();
						win.show();
						Ext.getCmp("addressListDetailCode").setReadOnly(false);
						Ext.getCmp("addressListDetailGridGroup").setVisible(false);
						var form = Ext.getCmp("addressListDetailForm").getForm();
						form.reset();
						form.findField("saveFlg").setValue("0");
					}
				});
			}

			if ("addressList" == type) {

				items.push({
					text : "修改临时组",
					handler : function() {
						var win = new fax.addressList.maintenance.AddressListDetailWindow();
						win.show();
						Ext.getCmp("addressListDetailCode").setReadOnly(true);
						Ext.getCmp("addressListDetailGridGroup").setVisible(true);
						var form = Ext.getCmp("addressListDetailForm").getForm();
						form.reset();
						form.findField("saveFlg").setValue("1");
						form.findField("code").setValue(rec.get("code"));
						form.findField("name").setValue(rec.get("text"));

						var code = form.findField("code").getValue();
						Ext.apply(unabsorbedMultiselectStore.proxy.extraParams, {
							code : code
						});
						unabsorbedMultiselectStore.load();
						Ext.apply(absorbedMultiselectStore.proxy.extraParams, {
							code : code
						});
						absorbedMultiselectStore.load();
					}
				});
				items.push({
					text : "删除临时组",
					handler : function() {
						Ext.Msg.confirm('删除', '确定删除吗？', function(btn) {
							if (btn == 'yes') {
								var ids = [rec.get("code")];
								Ext.Ajax.request({
									url : contextPath + '/controller/addressBook/deleteAddressList.do',
									params : {
										ids : ids
									},
									method : 'POST',
									success : function(response, options) {
										if (response.responseText) {
											Ext.Msg.alert("提示", '删除成功！');
											addressBookTreeStore.reload();
										}
									}
								});
							}
						}, this);
					}
				});
			}

			if (items && 0 != items.length) {
				var nodemenu = new Ext.menu.Menu({
					floating : true,
					items : items
				});
				nodemenu.showAt(e.getXY());
			}
		}
	}
});

fax.addressList.maintenance.RightPanel = Ext.extend(Ext.Panel, {
	layout : 'border',
	items : [{
		region : 'north',
		buttonAlign : 'left',
		items : [{
			id : 'searchPanel',
			layout : 'column',
			xtype : 'form',
			frame : true,
			defaults : {
				labelWidth : 70,
				columnWidth : .25
			},
			items : [{
				xtype : 'textfield',
				fieldLabel : '姓名',
				id : 'name',
				name : 'name'
			}]
		}],
		buttons : [{
			text : '查询',
			handler : function() {
				searchPerson();
			}
		}, {
			text : '重置',
			handler : function() {
				Ext.getCmp("searchPanel").getForm().reset();
				searchPerson();
			}
		}, {
			text : '添加人员',
			handler : function() {
				var win = new fax.addressList.maintenance.PersonDetailWindow();
				win.show();
				Ext.getCmp("personDetailCode").setReadOnly(false);

				var form = Ext.getCmp("personDetailForm").getForm();
				form.reset();
				form.findField("saveFlg").setValue("0");
				if (selectOrgCode && "0" != selectOrgCode) {
					form.findField("orgCode").setValue(selectOrgCode);
				}
			}
		}, {
			text : '删除人员',
			handler : function() {
				var rows = Ext.getCmp("personGridPanel").getSelectionModel().getSelection();
				var length = rows.length;
				if (0 == length) {
					Ext.Msg.alert("提示", "请至少选择一条！");
					return;
				}
				Ext.Msg.confirm('删除', '确定删除吗？', function(btn) {
					if (btn == 'yes') {
						var ids = [];
						for (var i = 0; i < length; i++) {
							ids.push(rows[i].get("code"));
						}
						Ext.Ajax.request({
							url : contextPath + '/controller/addressBook/deletePerson.do',
							params : {
								ids : ids
							},
							method : 'POST',
							success : function(response, options) {
								if (response.responseText) {
									Ext.Msg.alert("提示", '删除成功！');
									personGridStore.reload();
								}
							}
						});
					}
				}, this);
			}
		}, {
			text : '导入人员',
			handler : function() {
				var win = new fax.addressList.maintenance.ImportWindow();
				win.show();
			}
		}, {
			text : '导出人员',
			handler : function() {
				exportPerson();
			}
		}]
	}, {
		id : 'personGridPanel',
		region : 'center',
		xtype : 'grid',
		layout : 'fit',
		loadMask : true,
		store : personGridStore,
		bbar : {
			xtype : 'pagingtoolbar',
			displayInfo : true,
			store : personGridStore
		},
		selModel : new Ext.selection.CheckboxModel(),
		columns : [{
			text : "编号",
			width : 100,
			dataIndex : 'code'
		}, {
			text : "姓名",
			width : 200,
			dataIndex : 'name'
		}, {
			text : "邮箱",
			width : 200,
			dataIndex : 'email'
		}, {
			text : "所属组织",
			width : 200,
			dataIndex : 'org'
		}],
		listeners : {
			itemdblclick : function(view, record, item, index, e, eOpts) {
				var win = new fax.addressList.maintenance.PersonDetailWindow();
				win.show();
				Ext.getCmp("personDetailCode").setReadOnly(true);
				var form = Ext.getCmp("personDetailForm").getForm();
				form.reset();
				form.findField("saveFlg").setValue("1");
				for (var p in record.data) {
					if (form.findField(p))
						form.findField(p).setValue(record.get(p));
				}

				var code = form.findField("code").getValue();
				Ext.apply(personPhoneNoStore.proxy.extraParams, {
					personCode : code
				});
				personPhoneNoStore.load();
				Ext.apply(personFaxNoStore.proxy.extraParams, {
					personCode : code
				});
				personFaxNoStore.load();
			}
		}
	}]
});

// *****************************************************************************
// window
// *****************************************************************************
fax.addressList.maintenance.PersonDetailWindow = Ext.extend(Ext.Window, {
	id : 'personDetailWindow',
	title : '用户信息',
	autoScroll : true,
	resizable : false,
	maximizable : false,
	modal : true,
	width : 400,
	constructor : function(config) {
		var group = {
			items : [{
				id : 'personDetailForm',
				xtype : 'form',
				frame : true,
				defaults : {
					labelWidth : 100,
					width : 300
				},
				items : [{
					xtype : 'hiddenfield',
					name : 'saveFlg'
				}, {
					xtype : 'textfield',
					fieldLabel : '员工编号',
					id : 'personDetailCode',
					name : 'code',
					allowBlank : false,
					vtype : 'alphanum'
				}, {
					xtype : 'textfield',
					fieldLabel : '姓名',
					id : 'personDetailName',
					name : 'name',
					allowBlank : false
				}, {
					xtype : 'textfield',
					fieldLabel : '邮箱',
					id : 'personDetailEmail',
					name : 'email',
					allowBlank : false,
					vtype : 'email'
				}, {
					xtype : 'combobox',
					fieldLabel : '所属组织',
					id : 'personDetailOrgCode',
					name : 'orgCode',
					store : orgComboxStore,
					emptyText : '请选择',
					valueField : 'code',
					displayField : 'name',
					queryMode : 'local',
					triggerAction : 'all',
					editable : false,
					allowBlank : false
				}, {
					xtype : 'tabpanel',
					height : 200,
					defaults : {
						autoScroll : true,
						autoHeight : true,
						style : "padding:3"
					},
					items : [{
						title : '电话号码',
						xtype : 'grid',
						id : 'personPhoneNoGridPanel',
						store : personPhoneNoStore,
						tbar : {
							defaults : {
								xtype : 'button'
							},
							items : [{
								text : '添加电话号码',
								handler : function() {
									personPhoneNoRowEditingPlugin.cancelEdit();
									personPhoneNoStore.add({
										personCode : Ext.getCmp("personDetailCode").getValue()
									});
									personPhoneNoRowEditingPlugin.startEdit(personPhoneNoStore.data.length - 1, 0);
								}
							}, {
								text : '删除电话号码',
								handler : function() {
									var rows = Ext.getCmp("personPhoneNoGridPanel").getSelectionModel().getSelection();
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
												url : contextPath + '/controller/addressBook/deletePersonPhoneNo.do',
												params : {
													ids : ids
												},
												method : 'POST',
												success : function(response, options) {
													if (response.responseText) {
														Ext.Msg.alert("提示", '删除成功！');
														personPhoneNoStore.reload();
													}
												}
											});
										}
									}, this);
								}
							}]
						},
						hideHeaders : true,
						selModel : new Ext.selection.CheckboxModel(),
						columns : [{
							dataIndex : 'no',
							flex : 1,
							editor : {
								xtype : 'textfield',
								maxLength : 11
							}
						}],
						plugins : [personPhoneNoRowEditingPlugin],
						listeners : {
							edit : function(editor, e) {
								Ext.Ajax.request({
									url : contextPath + '/controller/addressBook/savePersonPhoneNo.do',
									params : {
										no : editor.record.get('no'),
										personCode : editor.record.get('personCode')
									},
									method : 'POST',
									success : function(response, options) {
										if (response.responseText) {
											personPhoneNoStore.reload();
										}
									}
								});
							},
							canceledit : function(editor, e) {
								personPhoneNoStore.reload();
							}
						}
					}, {
						title : '传真号码',
						xtype : 'grid',
						id : 'personFaxNoGridPanel',
						store : personFaxNoStore,
						tbar : {
							defaults : {
								xtype : 'button'
							},
							items : [{
								text : '添加传真号码',
								handler : function() {
									personFaxNoRowEditingPlugin.cancelEdit();
									personFaxNoStore.add({
										personCode : Ext.getCmp("personDetailCode").getValue()
									});
									personFaxNoRowEditingPlugin.startEdit(personFaxNoStore.data.length - 1, 0);
								}
							}, {
								text : '删除传真号码',
								handler : function() {
									var rows = Ext.getCmp("personFaxNoGridPanel").getSelectionModel().getSelection();
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
												url : contextPath + '/controller/addressBook/deletePersonFaxNo.do',
												params : {
													ids : ids
												},
												method : 'POST',
												success : function(response, options) {
													if (response.responseText) {
														Ext.Msg.alert("提示", '删除成功！');
														personFaxNoStore.reload();
													}
												}
											});
										}
									}, this);
								}
							}]
						},
						hideHeaders : true,
						selModel : new Ext.selection.CheckboxModel(),
						columns : [{
							dataIndex : 'no',
							flex : 1,
							editor : {
								xtype : 'textfield',
								maxLength : 13
							}
						}],
						plugins : [personFaxNoRowEditingPlugin],
						listeners : {
							edit : function(editor, e) {
								Ext.Ajax.request({
									url : contextPath + '/controller/addressBook/savePersonFaxNo.do',
									params : {
										no : editor.record.get('no'),
										personCode : editor.record.get('personCode')
									},
									method : 'POST',
									success : function(response, options) {
										if (response.responseText) {
											personFaxNoStore.reload();
										}
									}
								});
							},
							canceledit : function(editor, e) {
								personFaxNoStore.reload();
							}
						}
					}]
				}]
			}]
		};
		fax.addressList.maintenance.PersonDetailWindow.superclass.constructor.call(this, group);
	},
	buttons : [{
		text : '保存',
		handler : function() {
			var form = Ext.getCmp("personDetailForm").getForm();
			if (!form.isValid()) {
				Ext.Msg.alert('错误', '输入内容有误！');
				return;
			}
			form.submit({
				url : contextPath + '/controller/addressBook/savePerson.do',
				method : 'POST',
				success : function(from, action) {
					Ext.Msg.alert('提示', '保存成功！', function() {
						personGridStore.reload();
						Ext.getCmp("personDetailWindow").close();
					});
				}
			});
		}
	}, {
		text : '关闭',
		handler : function() {
			Ext.getCmp("personDetailWindow").close();
		}
	}]
});

fax.addressList.maintenance.AddressListDetailWindow = Ext.extend(Ext.Window, {
	id : 'addressListDetailWindow',
	title : '临时组',
	autoScroll : true,
	resizable : false,
	maximizable : false,
	modal : true,
	constructor : function(config) {
		var group = {
			items : [{
				id : 'addressListDetailForm',
				xtype : 'form',
				frame : true,
				defaults : {
					labelWidth : 70,
					width : 300
				},
				items : [{
					xtype : 'hiddenfield',
					name : 'saveFlg'
				}, {
					xtype : 'textfield',
					fieldLabel : '编码',
					id : 'addressListDetailCode',
					name : 'code',
					allowBlank : false
				}, {
					xtype : 'textfield',
					fieldLabel : '名称',
					id : 'addressListDetailName',
					name : 'name',
					allowBlank : false
				}, {
					xtype : 'panel',
					id : 'addressListDetailGridGroup',
					frame : true,
					layout : {
						type : 'hbox',
						align : 'stretch',
						pack : 'start'
					},
					width : 1000,
					items : [{
						xtype : 'grid',
						title : '未分配人员',
						id : 'addressListDetailUnabsorbed',
						loadMask : true,
						store : unabsorbedMultiselectStore,
						selModel : new Ext.selection.CheckboxModel(),
						columns : [{
							text : "员工编号",
							width : 100,
							dataIndex : 'code'
						}, {
							text : "姓名",
							width : 150,
							dataIndex : 'name'
						}, {
							text : "所属组织",
							width : 150,
							dataIndex : 'org'
						}],
						height : 200,
						flex : 45
					}, {
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
								var person = [];
								var store = Ext.getCmp("addressListDetailUnabsorbed").getStore();
								var length = store.getCount();
								if (0 == length) {
									Ext.Msg.alert("提示", "请至少选择一条！");
									return;
								}
								for (var i = 0; i < length; i++) {
									person.push(store.getAt(i).get("code"));
								}
								addAddressListDetail(person);
							}
						}, {
							text : '>',
							handler : function() {
								var person = [];
								var rows = Ext.getCmp("addressListDetailUnabsorbed").getSelectionModel().getSelection();
								var length = rows.length;
								if (0 == length) {
									Ext.Msg.alert("提示", "请至少选择一条！");
									return;
								}
								for (var i = 0; i < length; i++) {
									person.push(rows[i].get("code"));
								}
								addAddressListDetail(person);
							}
						}, {
							text : '<',
							handler : function() {
								var person = [];
								var rows = Ext.getCmp("addressListDetailAbsorbed").getSelectionModel().getSelection();
								var length = rows.length;
								if (0 == length) {
									Ext.Msg.alert("提示", "请至少选择一条！");
									return;
								}
								for (var i = 0; i < length; i++) {
									person.push(rows[i].get("code"));
								}
								removeAddressListDetail(person);
							}
						}, {
							text : '<<',
							handler : function() {
								var person = [];
								var store = Ext.getCmp("addressListDetailAbsorbed").getStore();
								var length = store.getCount();
								if (0 == length) {
									Ext.Msg.alert("提示", "请至少选择一条！");
									return;
								}
								for (var i = 0; i < length; i++) {
									person.push(store.getAt(i).get("code"));
								}
								removeAddressListDetail(person);
							}
						}],
						flex : 10
					}, {
						xtype : 'grid',
						title : '已分配人员',
						id : 'addressListDetailAbsorbed',
						loadMask : true,
						store : absorbedMultiselectStore,
						selModel : new Ext.selection.CheckboxModel(),
						columns : [{
							text : "员工编号",
							width : 100,
							dataIndex : 'code'
						}, {
							text : "姓名",
							width : 150,
							dataIndex : 'name'
						}, {
							text : "所属组织",
							width : 150,
							dataIndex : 'org'
						}],
						height : 200,
						flex : 45
					}]
				}]
			}]
		};
		fax.addressList.maintenance.AddressListDetailWindow.superclass.constructor.call(this, group);
	},
	buttons : [{
		text : '保存',
		handler : function() {
			var form = Ext.getCmp("addressListDetailForm").getForm();
			if (!form.isValid()) {
				Ext.Msg.alert('错误', '输入内容有误！');
				return;
			}
			form.submit({
				url : contextPath + '/controller/addressBook/saveAddressList.do',
				method : 'POST',
				success : function(from, action) {
					Ext.Msg.alert('提示', '保存成功！', function() {
						Ext.getCmp("addressListDetailWindow").close();
						addressBookTreeStore.reload();
					});
				}
			});
		}
	}, {
		text : '关闭',
		handler : function() {
			Ext.getCmp("addressListDetailWindow").close();
			addressBookTreeStore.reload();
		}
	}]
});

fax.addressList.maintenance.ImportWindow = Ext.extend(Ext.Window, {
	id : 'importWindow',
	title : '导入',
	autoScroll : true,
	resizable : false,
	maximizable : false,
	modal : true,
	items : [{
		id : 'importForm',
		frame : true,
		xtype : 'form',
		defaults : {
			labelWidth : 50,
			width : 400
		},
		items : [{
			xtype : 'filefield',
			fieldLabel : '文件',
			id : 'importExcelFile',
			name : 'excelFile',
			allowBlank : false
		}]
	}],
	buttons : [{
		text : '导入',
		handler : function() {
			var form = Ext.getCmp("importForm").getForm();
			if (!form.isValid()) {
				Ext.Msg.alert('错误', '输入内容有误！');
				return;
			}

			form.submit({
				url : contextPath + '/controller/addressBook/importPerson.do',
				method : 'POST',
				success : function(from, action) {
					Ext.Msg.alert('提示', '导入成功！', function() {
						personGridStore.reload();
						Ext.getCmp("importWindow").close();
					});
				}
			});
		}
	}, {
		text : '取消',
		handler : function() {
			Ext.getCmp("importWindow").close();
		}
	}]
});
