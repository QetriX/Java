package com.qetrix.libs.components;

/* Copyright (c) QetriX.com. Licensed under MIT License, see /LICENSE.txt file.
 * 16.05.03 | QView Component
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QView extends Component
{
	private Map<String, List<String>>_sections = new HashMap<String, List<String>>();

	/** Class Constructor */
	public QView(String name)
	{
		this._name = name;
	}
	public QView(String name, String heading)
	{
		super();
		this._name = name;
		this._heading = heading;
	}

	/** Add a section */
	public QView add(Object value)
	{
		return this.add(value, "page");
	}
	public QView add(Object value, String key)
	{
		if (!this._sections.containsKey(key)) {
			this._sections.put(key, new ArrayList<String>());
		}
		List<String> vals = this._sections.get(key);
		vals.add((String) value);
		this._sections.put(key, vals);
		return this;
	}

	/** Get sections (used by convert) */
	public List<String> sections()
	{
		return this.sections("page");
	}
	public List<String> sections(String key)
	{
		return this._sections.get(key);
	}
	public String style()
	{
		return _style;
	}
}
