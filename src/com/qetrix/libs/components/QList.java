package com.qetrix.libs.components;

/* Copyright (c) QetriX.com. Licensed under MIT License, see /LICENSE.txt file.
 * 2016-01-13 | QList Component PHP class
 */

import java.util.*;

import com.qetrix.libs.components.qlist.QListCol;
import com.qetrix.libs.components.qlist.QListRow;
import com.qetrix.libs.Util;

public class QList extends Component
{
	public Map<String, QListCol> _cols = new LinkedHashMap<String, QListCol>(); /// array of hash table col settings: name, dsname=name, heading=name, text=null, action=null
	protected List<QListRow> _data = new ArrayList<QListRow>(); /// array of hash tables, representing whole dataset for the list, keys must match cols names
	public List<String> _orderBy = new ArrayList<String>(); /// Column Names, e.g. ["name", "
	private String _value; /// Current selection
	private String _linkBase = "";

	private int _page = 1; /// Current page
	private int _rows = 20; /// Visible rows (per page)
	private int _colsType = 0; /// For cols autogen
	
	
	private boolean _autoloadCols = true;

	//region Constructors
	public QList()
	{
	}
	public QList(String name)
	{
		this._name = name;
	}
	public QList(String name, String heading)
	{
		this._name = name;
		this._heading = heading;
	}
	//endregion

	//region Getters and setters
	public QList orderBy(String colName)
	{
		_orderBy.add(colName);
		return this;
	}

	public QList name(String value)
	{
		this._name = value;
		return this;
	}

	public String name()
	{
		return this._name;
	}

	/** Remove one row from the List */
	public QList remove(int what)
	{
		_data.remove(what);
		return this;
	}

	/** How many rows (items, entries) are in the list */
	public int length()
	{
		return this._data.size();
	}

	public List<QListRow> rows()
	{
		return this._data;
	}
	
	public List<QListCol> cols()
	{
		return new ArrayList<QListCol>(this._cols.values());
	}
	//endregion

	/** Add column */
	public QList add(QListCol col)
	{
		this._autoloadCols = false;
		_cols.put(col.name(), col);
		return this;
	}
	public QList addCol(String name)
	{
		this.add(this.col(name, name));
		return this;
	}
	public void addCol(String name, String heading)
	{
		this.add(this.col(name, heading));
	}

	private QListCol col(String name, String heading)
	{
		QListCol col = new QListCol(name, heading);
		return col;
	}

	/** Add row */
	public QList add(Map<String, String> row)
	{
		this._data.add(new QListRow(row));
		return this;
	}

	/*public QList add(List<QListRow> rows) 
	{
		for (QListRow row : rows) {
			if (this._autoloadCols) {
				List<String> cols = row.cols();
				for (String col : cols) if (!this._cols.containsKey(col)) this.addCol(col);
			}
			this._data.add(row);
		}
		return this;
	}*/

	public QList add(List<Map<String, String>> rows)
	{
		for (Map<String, String> row : rows) {
			if (this._autoloadCols) {
				for (Map.Entry<String, String> entry : row.entrySet()) {
					if (!this._cols.containsKey(entry.getKey())) this.addCol(entry.getKey());
				}
			}
			this._data.add(new QListRow(row));
		}
		return this;
	}
	
	public QList add(String[] rows)
	{
		for (String row : rows) this._data.add(new QListRow(row, row));
		return this;
	}

	public String style()
	{
		return this._style;
	}

	public String action()
	{
		return this._action;
	}
	
	public String linkBase()
	{
		return this._linkBase;
	}
	
	public String value()
	{
		return this._value;
	}
	
	public QList value(String value)
	{
		this._value = value;
		return this;
	}
}
