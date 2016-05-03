package com.qetrix.libs.components;

/* Copyright (c) QetriX.com. Licensed under MIT License, see /LICENSE.txt file.
 * 16.05.03 | QetriX Component class
 */

import com.qetrix.libs.DataStore;
import com.qetrix.libs.QApp;
import com.qetrix.libs.Util;

public class Component
{
	protected DataStore datastore = null;
	protected String _name = null;
	protected String _heading = null;
	protected String _action = null;
	
	protected String _style = null;
	protected QApp _app = null;

	public Component()
	{
		this._app = QApp.getInstance();
	}
	
	public String name()
	{
		return this._name;
	}

	public String heading()
	{
		return this._heading;
	}

	public Component heading(String value)
	{
		this._heading = value;
		return this;
	}

	public Object convert()
	{
		return this.convert(_app.outputFormat(), null);
	}
	
	public Object convert(String toType)
	{
		return this.convert(_app.outputFormat(), toType);
	}

	public Object convert(String toFormat, String toType)
	{
		try {
			// Util.log(this.getClass().getName() + " to " + toFormat + "_" + toType, "Comp.convert");
			return com.qetrix.libs.Util.convert(this, this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1), toFormat, toType);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
