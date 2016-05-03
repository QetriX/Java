package com.qetrix.apps.common.converters;

/* Copyright (c) QetriX.com. Licensed under MIT License, see /LICENSE.txt file.
 * 16.05.03 | QetriX Converter from QForm to HTML5 form
 */

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.qetrix.libs.components.QForm;
import com.qetrix.libs.components.qform.QFormControl;
import com.qetrix.libs.components.qform.QFormControl.QFormControlMode;
import com.qetrix.libs.components.qlist.QListRow;
import com.qetrix.libs.Util;

public class QForm_html
{
	public final String NL = "\n";

	private int order = -10;
	private Boolean _hasFiles = false; // For adding enctype=multipart/form-data
	
	private QForm form;

	public String convert(Object qform)
	{
		this.form = (QForm) qform;
		//if (count(this.form.controls()) == 0) if (APP_STATE == \QState::Prod) return ""; else throw new \Exception("QForm \"" + this.form.name() + "\" has no controls!");

		String str = "";

		// Form Heading
		if (this.form.heading() != "") str += "<h3>"+this.form.heading()+"</h3>"+NL;
		
		// Form Controls
		for (QFormControl control : form.controls()) {
			str += this.part(control);
		}
		return this.head()+str+this.foot();
	}

	public String head()
	{
		return "<form method=\"post\" action=\"" + this.form.path() + "\" target=\"" + this.form.target() + "\" id=\"" + this.form.name() + "\" class=\"qform\""+ 
				(this.form.action() != null && !this.form.action().isEmpty() ? " onsubmit=\"" + this.settingsToJS(this.form.action()) + "\"" : "")
				+ "><div>" + NL;
	}

	public String part(QFormControl control)
	{
		String str = "";
		int order = (int) (Math.floor(control.order() / 100) + 1);

		if (this.order != -1 && this.order < control.order()) str += "</div>" + NL; // Closing tag for div + gfl

		if (this.order == -1 || Math.floor(this.order / 100) + 1 < order) {
			if (this.order != -1) str += "</div>" + NL; // Closing tag for the "part" div below
			str += "<div id=\"" + this.form.name() + "_part" + order + "\" class=\"qform_part\">" + NL;
			/// Add section heading, if exists in lang, as: "Heading_" + div id (from generated HTML source code)
			String lbl = this.form.app().lbl("Heading_"+this.form.name()+"_section"+order+"");
			if (lbl.substring(0,  1) != "-") str += (this.form.heading() == "" ? "<h3>"+lbl+"</h3>" : "<h4>"+lbl+"</h4>")+NL;
		}

		//if (controls instanceof QFormControl) controls = array(controls);
		if (this.order < control.order()) {
			str += "<div class=\"qfl\">";
			if (control.label() != "") str += "<label for=\"" + control.name() + "\">" + control.label() + (control.required() ? "<span class=\"req\" title=\"Required field\">*</span>" : "") + ":</label> "; // TODO: Lang for title ("Required field")
		}

		try {
			str += "<span>";
			str += this.drawControl(control);
			str += "</span>";
		} catch (Exception ex) {}
		//if (this.order < order) str += "</div>" + NL;

		this.order = control.order();

		return str;
	}

	protected String drawControl(QFormControl control) throws Exception
	{
		switch (control.type()) {
			case "text":
				return this.textBox(control);
			case "wikitext":
				return this.wikiTextBox(control);
			case "password":
				return this.password(control);
			case "number":
				return this.numBox(control);
			case "checkbox":
				return this.checkBox(control);
			case "button":
				return this.button(control);
			case "list":
				return this.dropDownList(control);
			case "date":
				return this.datePicker(control);
			default:
				throw new Exception("Control type \"" + control.type() + "\" is not renderable in "+this.getClass().toString());
		}
	}

	protected String textBox(QFormControl control)
	{
		// action = onkeyup
		return "<input type=\"text\" name=\"" + control.name() + "\" id=\"" + control.name() + "\""
				+ (control.value() != null ? " value=\"" + control.value() + "\"" : "")
				+ (control.text() != null ? " placeholder=\"" + control.text() + "\"" : "")
				+ ">";
	}

	protected String wikiTextBox(QFormControl control)
	{
		return "<textarea name=\"" + control.name() + "\" id=\"" + control.name() + "\">" + control.value() + "</textarea>";
	}

	protected String password(QFormControl control)
	{
		return "<input type=\"password\" name=\"" + control.name() + "\" id=\"" + control.name() + "\" value=\"" + control.value() + "\" placeholder=\"" + control.text() + "\">";
	}

	protected String file(QFormControl control)
	{
		this._hasFiles = true;
		return "<input type=\"file\" name=\"" + control.name() + "\" id=\"" + control.name() + "\">";
	}

	protected String button(QFormControl control)
	{
		// action = onclick
		return "<input type=\"submit\" name=\"" + control.name() + "\" id=\"" + control.name() + "\" class=\"btn\" value=\"" + control.value() + "\"" 
		+(control.action() != "" ? " onclick=\"return " + control.action() + "(this,event);\"" : "") 
		+">";
	}

	protected String checkBox(QFormControl control)
	{
		return "<input type=\"checkbox\" name=\"" + control.name() + "\" id=\"" + control.name() + "\" value=\"1\""+(control.value() == "1" ? " checked" : "")+">";
	}

	protected String dropDownList(QFormControl control)
	{
		// action = onchange / selectedindexchange / SelectionChanged
		String options = "";
		for (QListRow item : control.items().rows()) {
			//if (item.value() != null ? item["value"] = item["text"];
			options += "	<option value=\"" + item.value() + "\"" + (item.value().equals(control.value()) ? " selected" : "") + ">" + item.text() + "</option>"+NL;
		}
		return "<select name=\"" + control.name() + "\" id=\"" + control.name() + "\"" + 
		(control.action() != "" ? " onchange=\"return " + control.action() + "(this,event);\"" : "") + 
		">" + NL + options + "</select>";
	}

	protected String datePicker(QFormControl control)
	{
		String format = "%3$d.%2$d.%1$d %4$02d:%5$02d";
		switch (control.valuePrecision()) {
			case 1:
				format = "%3$d";
				break;
			case 7:
				format = "%3$d.%2$d.%1$d";
				break;
			case 9:
				format = "%3$d.%2$d.%1$d %4$02d:%5$02d";
				break;
			case 10:
				format = "%3$d.%2$d.%1$d %4$02d:%5$02d:%6$02d";
				break;
		}
		return "<input type=\"date\" name=\"" + control.name() + "\" id=\"" + control.name() + "\" value=\"" + Util.formatDateTime(control.value(), format)
				+(control.mode() == QFormControlMode.disabled.getValue() ? " disabled" : "")
				+(control.mode() == QFormControlMode.required.getValue() ? " required" : "")
				+"\">";
	}

	protected String numBox(QFormControl control)
	{
		return "<input type=\"text\" name=\"" + control.name() + "\" id=\"" + control.name() + "\" value=\"" + control.value() + "\" autocomplete=\"off\""
				+(control.mode() == QFormControlMode.disabled.getValue() ? " disabled" : "")
				+(control.mode() == QFormControlMode.required.getValue() ? " required" : "")
				+">";
	}

	protected String foot()
	{
		return "</div></div>" // Because of sections in Part
		+"</div></form>"+NL;
	}
	
	private String settingsToJS(String settings)
	{
		return settingsToJS(settings, new HashMap<String, String>());
	}

	private String settingsToJS(String settings, Map<String, String> data)
	{
		if (settings == null || settings == "") return null;
		if (!settings.contains("\t") && settings.contains("/")) return "return goTo('" + Util.processVars(settings, data) + "');";
		LinkedHashMap<String, String> arr = Util.getSettings(settings, "\t", data);
		String func = arr.keySet().iterator().next();
		String args = "this,event";
		for (Map.Entry<String, String> entry : arr.entrySet()) args += "," + (Util.isNumeric(entry.getValue()) ? entry.getValue()  : "'" + entry.getValue() + "'");
		return "return "+func+"("+args+");";
	}
}
