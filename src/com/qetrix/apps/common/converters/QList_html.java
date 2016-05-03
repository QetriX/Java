package com.qetrix.apps.common.converters;

/* Copyright (c) QetriX.com. Licensed under MIT License, see /LICENSE.txt file.
 * 2016-01-13 | QList to HTML Converter
 */

import java.util.*;

import com.qetrix.libs.components.QList;
import com.qetrix.libs.components.qlist.QListRow;
import com.qetrix.libs.Util;

public class QList_html
{
	public final String NL = "\n";

	public String convert(Object listObj)
	{
		return convert(listObj, null);
	}
	public String convert(Object listObj, HashMap<String, String> args)
	{
		QList list = (QList) listObj;
		//Util.log("QList_html.convert for " + qlist.name() + " with " + qlist.rows().size() + " items");
		String str = "";
		if (list.heading() != null && list.heading() != "") str += "<h3>" + list.heading() + "</h3>";
		
		//$str .= "<ul onclick=\"".$this->settingsToJS($list->action())."\"".($list->name() != "" ? " id=\"".$list->name()."\"" : "")."".($list->style() != "" ? " class=\"".$list->style()."\"" : "").">".self::NL;

		str += "<ul" + (list.name() != "" ? " id=\"" + list.name() + "\"" : "") +
				(list.style() != "" ? " class=\"" + list.style() + "\"" : "") +
				(list.action() != "" ? " onclick=\"" + this.actionToJS(list.action()) + "\"" : "") +
				">" + NL;
		str += this.items(list.rows(), list);
		str += "</ul>" + NL;
		return str;
	}

	private String items(List<QListRow> items, QList list)
	{
		return items(items, list, 1);
	}

	private String items(List<QListRow> items, QList list, int level)
	{
		String str = "";
		if (level > 1) str += NL + "<ul>" + NL;
		for (QListRow item : items) {
			if (item.text() == null || item.text().isEmpty()) continue;
			str += "<li" + (item.selected() ? " class=\"sel\"" : "") + ">";
			
			if (item.action() != null && !item.action().contains("/")) { 
				str += "<a href=\"#\" onclick=\"" + this.actionToJS(item.action(), item.toMap()) + "\">" + item.text() + "</a>";
			} else if (list.action() != null && list.action() != "") { /// "action" is a link
				str += "<a href=\"#\" data-value=\"" + item.value() + "\" data-text=\"" + item.text() + "\">" + item.text() + "</a>";
			} else /// Not a link
				str += (item.action() == "" || item.action() == list.value() ? "<span>" + item.text() + "</span>" : "<a href=\"" + list.linkBase()  + item.action() + "\">" + item.text() + "</a>");
			str += "</li>" + NL;
		}
		if (level > 1) str += "</ul>" + NL;
		return str;
	}

	private String actionToJS(String settings)
	{
		return actionToJS(settings, null);
	}
	
	private String actionToJS(String settings, Map<String, String> data)
	{
		if (settings == "") return "";
		String func = "";
		String args = "this,event";
		return "return !" + func + "(" + args + ")";
	}
}
