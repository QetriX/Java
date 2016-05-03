/* Copyright (c) 2014 QetriX. Licensed under MIT License, see /LICENSE.txt file.
 * QetriX Form Control
 */
package com.qetrix.libs.components.qform;

import java.util.*;

import com.qetrix.libs.components.QList;
import com.qetrix.libs.QType;
import com.qetrix.libs.Util;

public class QFormControl
{
	protected String _action;
	protected String _ds; /// tds
	protected String _dsname;
	protected QList _items;
	protected String _label;
	protected String _unit; // tvu
	protected String _max; // tvx
	protected String _min; // tvn
	protected int _mode = 3; // tvm
	protected String _name; // tn
	protected String _oldValue; // prev pv
	protected int _order; // po
	protected int _property;
	protected QType _qtype;
	private boolean _required = false;
	protected String _text;
	protected String _type;
	protected String _validation;
	protected int _valuetype;
	protected String _value = "";
	private int _precision;
	protected String style;

	public QFormControl(QFormControlType type, String name)
	{
		this._type = type.toString();
		this._name = name;
	}

	public QFormControl(QFormControlType type, String name, String label, String value)
	{
		this._type = type.toString();
		this._name = name;
		this.label(label);
		this._value = value;
	}

	public QFormControl(QFormControlType type, String name, String label, String value, int order)
	{
		this._type = type.toString();
		this._name = name;
		this.label(label);
		this._value = value;
		this._order = order;
	}

	public QFormControl(QFormControlType type, String name, String label, String value, int order, QList items)
	{
		this._type = type.toString();
		this._name = name;
		this.label(label);
		this._value = value;
		this._order = order;
		this._items = items;
	}

	public QFormControl name(String value)
	{
		this._name = value;
		if (this.label() == null) this.label(value);
		return this;
	}

	public QFormControl label(String value)
	{
		this._label = value;
		return this;
	}

	public String label()
	{
		return this._label;
	}

	public String name()
	{
		return this._name;
	}

	public QFormControl dsname(String value)
	{
		this._dsname = value;
		return this;
	}

	public String dsname()
	{
		return this._dsname;
	}

	
	public QFormControl ds(String value)
	{
		this._ds = value;
		return this;
	}

	public String ds()
	{
		return this._ds;
	}

	public QFormControl value(String value)
	{
		this._value = value;
		return this;
	}

	public String value()
	{
		return this._value;
	}

	public QFormControl text(String value)
	{
		this._text = value;
		return this;
	}
	public String text()
	{
		return this._text;
	}

	public QFormControl valuetype(int value)
	{
		this._valuetype = value;
		return this;
	}
	public int valuetype()
	{
		return this._valuetype;
	}

	public QFormControl property(int value)
	{
		this._property = value;
		return this;
	}
	public int property()
	{
		return this._property;
	}
	
	public QFormControl required(boolean value)
	{
		this._required = value;
		return this;
	}

	public boolean required()
	{
		return this._required;
	}
	
	/**
	 * Autocomplete / Suggest options
	 * @param values
	 * @return
	 */
	public QFormControl items(Map<String, String> values)
	{
		QList items = new QList(this.name() + "_items"); 
		this._items = items;
		this._type = QFormControlType.menu.getValue();
		return this;
	}
	
	public QFormControl items(String[] values)
	{
		QList items = new QList(this.name() + "_items"); 
		Map<String, String> vals = new HashMap<String, String>();
		if (!this._required) vals.put("", "-");
		int i = 1;
		for (String val : values) {
			vals.put(i + "", val);
			i++;
		}
		this._items = items;
		return this;
	}
	
	public QList items()
	{
		return this._items;
	}

	public String action()
	{
		return this._action;
	}

	public int order()
	{
		return this._order;
	}

	public String type()
	{
		return this._type;
	}

	public int mode()
	{
		return this._mode;
	}
	
	public QFormControl mode(QFormControlMode value)
	{
		if (value == QFormControlMode.required && this._items.length() > 0) this._items.remove(0);
		return this;
	}

	public static enum QFormControlType
	{
		button("button"),
		datetime("datetime"),
		file("file"),
		longtext("longtext"),
		menu("menu"),
		number("number"),
		text("text"),
		rel("rel"),
		wikitext("wikitext");

		private String value;
		private QFormControlType(String value)
		{
			this.value = value;
		}

		public String getValue()
		{
			return value;
		}
}

	public static enum QFormControlMode
	{
		hidden(0),
		disabled(1),
		readonly(2),
		normal(3),
		required(4);

		private int value;
		private QFormControlMode(int value)
		{
			this.value = value;
		}
		
		public int getValue()
		{
			return value;
		}
	}

	public int valuePrecision()
	{
		return this._precision;
	}
}
