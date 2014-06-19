Ext.define('wizard.lib.extjs.ex.RowEditingPlugin', {
	extend : 'Ext.grid.plugin.RowEditing',
	alias : 'RowEditingPlugin',
	clicksToEdit : 1,
	saveBtnText : '保存',
	cancelBtnText : '取消',
	completeEdit : function() {
		var me = this;
		if (me.editing && me.validateEdit()) {
			me.editing = false;
			me.fireEvent('edit', me.context);
		}
		me.adding = false;
	}
});

Ext.form.field.File.override({
	buttonText : '浏览'
});

Ext.form.TextField.override({
	initComponent : function() {
		var me = this;
		Ext.form.field.Base.superclass.initComponent.call(this);

		me.subTplData = me.subTplData || {};

		this.addEvents('focus', 'blur', 'specialkey');
		me.initLabelable();
		me.initField();

		if (!me.name) {
			me.name = me.getInputId();
		}
		if (this.allowBlank === false && this.fieldLabel) {
			this.fieldLabel += '<font color=red>*</font>';
		}
	}
});

Ext.apply(Ext.form.VTypes, {
	number : function(val, field) {
		return /^\d+$/.test(val);
	},
	numberText : '该输入项必须为数字'
});

var getRequestParams = function() {
	var url = location.search;
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		if (str.indexOf("&") != -1) {
			strs = str.split("&");
			for (var i = 0; i < strs.length; i++) {
				theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
			}
		} else {
			theRequest[str.split("=")[0]] = unescape(str.split("=")[1]);
		}
	}
	return theRequest;
};
window.request = getRequestParams();