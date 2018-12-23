package com.qetrix.libs;

/* Copyright (c) QetriX.com. Licensed under MIT License, see /LICENSE.txt file.
 * 18.12.23 | Dict Class
 */

import java.util.HashMap;
import java.util.Map;

public class Dict {
	protected Map<String, String> _data = new HashMap<String, String>();
	
	public Dict()
	{
	}
	
	public Dict(Map<String, String> value) {
		this(value, false);
	}

	public Dict(Map<String, String> value, boolean add) {
		if (!add) this._data = value;
		else this._data.putAll(value);
	}
	
	public Dict(String key, String value)
	{
		set(key, value);
	}
	
	public Dict set(String key, String value)
	{
		this._data.put(key.toLowerCase(), value);
		return this;
	}

	public String get(String key)
	{
		key = key.toLowerCase();
		if (_data.containsKey(key)) return this._data.get(key);
		return "";
	}

	public Map<String, String> toArray() {
		return _data;
	}
}
