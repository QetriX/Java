package com.qetrix.libs.components;

/* Copyright (c) QetriX.com. Licensed under MIT License, see /LICENSE.txt file.
 * 2016-01-14 | QForm Component, for editing data in a single DS scope (e.g. DB table) or single QPDB Entity
 */

import java.util.*;

import com.qetrix.libs.QApp;
import com.qetrix.libs.components.qform.QFormControl;
import com.qetrix.libs.components.qform.QFormControl.QFormControlType;

public class QForm extends Component
{
	protected boolean hasFiles = false;

	protected List<QFormControl> _controls = new ArrayList<QFormControl>();
	protected String _target = null;
	protected Map<String, String> _data = new HashMap<String, String>();
	protected int _rowsPerPart = 100; // Controls per section
	
	private int _entID;
	private String _classID;
	private String _className;
	
	protected int nextControlOrder = 1;

	public QForm(String name)
	{
		this._name = name;
	}

	public QForm(String name, String heading)
	{
		this._name = name;
		this._heading = heading;
	}

	public QForm add(QFormControl control)
	{
		this._controls.add(control);
		return this;
	}

	/*public QFormControl control(String name)
	{
		QFormControl c = new QFormControl();
		c.name(name);
		return c;
	}*/

	public QForm add(String name, int property, int proptype)
	{
		this._controls.add(new QFormControl(QFormControlType.text, name)); // TODO
		return this;
	}
	
	/*public QFormControl control(String name, int property, int proptype)
	{
		QFormControl control = new QFormControl();
		control.valuetype(proptype);
		control.property(property);
		control.name(name);
		return control;
	}*/

	public List<QFormControl> controls()
	{
		return this._controls;
	}

	public void name(String value)
	{
		this._name = value;
	}

	public String name()
	{
		return this._name;
	}

	public static enum Property
	{
		value(1),
		relation(2),
		order(3);

		private final int val;

		private Property(int v)
		{
			this.val = v;
		}

		public int toInt()
		{
			return val;
		}
	}

	public String path()
	{
		return "";
	}

	public String target()
	{
		return "";
	}
	
	public int entID()
	{
		return this._entID;
	}
	
	public String classID()
	{
		return this._classID;
	}
	
	public String className()
	{
		return _className;
	}
	
	public QForm className(String value)
	{
		this._className = value;
		return this;
	}
	
	public void data()
	{
		// TODO
	}
	
	public String action()
	{
		return this._action;
	}
	
	public QForm action(String value)
	{
		this._action = value;
		return this;
	}

	public QApp app()
	{
		return this._app;
	}
}
