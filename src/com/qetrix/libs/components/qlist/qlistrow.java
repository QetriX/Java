package com.qetrix.libs.components.qlist;

/* Copyright (c) QetriX.com. Licensed under MIT License, see /LICENSE.txt file.
 * 2016-01-13 | QListCol part of QList component
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.qetrix.libs.Util;

public class QListRow
{
	Map<String, String> _data = new LinkedHashMap<String, String>();
	List<QListRow> _rows; // null = no childs, array() = possible childs
	Boolean _selected = false;

	public QListRow() {}
	
	//region CONSTRUCTORS
	public QListRow(Map<String, String> data)
	{
		Util.log(data);
		this._data = data;
	}
	
	public QListRow(String value, String text)
	{
		this._data.put("text", text);
		this._data.put("value", value);
	}
	//endregion
	
	//region GETTERS AND SETTERS
	public String text() // left text
	{
		return this._data.get("text");
	}
	public QListRow text(String value)
	{
		this._data.put("text", value);
		return this;
	}

	public String detail() // second row
	{
		return this._data.get("detail");
	}
	public QListRow detail(String value)
	{
		this._data.put("detail", value);
		return this;
	}

	public String action() // chevron to the right TODO: switch on/off
	{
		if (this._data.containsKey("_action")) return this._data.get("_action");
		return this._data.get("action");
	}
	public QListRow action(String value)
	{
		this._data.put("_action", value);
		return this;
	}

	public String value() // right text
	{
		return this._data.get("value");
	}
	public QListRow value(String value)
	{
		this._data.put("value", value);
		return this;
	}

	public String image() // left icon
	{
		return this._data.get("image");
	}
	public QListRow image(String value)
	{
		this._data.put("image", value);
		return this;
	}

	public Boolean selected()
	{
		return this._selected;
	}
	public QListRow selected(Boolean value)
	{
		this._selected = value;
		return this;
	}

	public List<QListRow> rows()
	{
		return this._rows;
	}
	public QListRow rows(List<QListRow> value)
	{
		this._rows = value;
		return this;
	}
	public String get(String key)
	{
		return this._data.get(key);
	}
	
	public List<String> cols()
	{
        List<String> list = new ArrayList<String>();
        list.addAll(this._data.keySet());
        return list;
	}
	//endregion

	//region PUBLIC METHODS
	public Boolean toggleSelected()
	{
		this._selected = !this._selected;
		return this._selected;
	}
	
	public Map<String, String> toMap()
	{
		return _data;
	}	
	//endregion
}
