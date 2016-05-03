package com.qetrix.apps.common.datastores;

/* Copyright (c) QetriX.com. Licensed under MIT License, see /LICENSE.txt file.
 * 16.05.03 | MySQL DataStore
 * Requires MySQL Java Connector JAR in CLASSPATH
 */

// datastore.table.row.col => mysql.p.231.v => in MySQL database, in "p" table, row 231 (PK) and col v.

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.qetrix.libs.components.qlist.QListRow;
import com.qetrix.libs.DataStore;
import com.qetrix.libs.QApp;
import com.qetrix.libs.QEntity;
import com.qetrix.libs.Util;

public class MySQL extends DataStore
{
	public MySQL(QApp app)
	{
		super(app);
	}
	
	public DataStore conn(String host, String scope, String prefix, String user, String password)
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + scope, user, password);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this;
	}

	public void open()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host + "/" + this.scope, this.username, this.password);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void close()
	{
		try {
			this.conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public QEntity loadEntity(int eid) throws SQLException
	{
		ResultSet res = this.query("SELECT p_pk, pv FROM p LEFT JOIN t ON t_pk = pt_fk WHERE pp_fk =  " + eid);
		QEntity e = new QEntity();
	    while (res.next()) {
	    	try {
				e.id(res.getInt("p_pk"));
				e.value(res.getString("pv"));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
	    }
	    return e;
    }
	
	protected List<QListRow> getListRows(String sql)
	{
		List<QListRow> data = new ArrayList<QListRow>();
		ResultSet res = this.query(sql);
	    try {
			while (res.next()) data.add(new QListRow(this.getRow(res)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    return data;
	}

	protected List<Map<String, String>> getData(String sql)
	{
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		ResultSet res = this.query(sql);
	    try {
			while (res.next()) data.add(this.getRow(res));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    return data;
	}
	
	public List<Map<String, String>> get(String query)
	{
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (query.indexOf(" ") == -1) query = "SELECT * FROM " + this.prefix + query;
		ResultSet res = this.query(query);
		if (res == null) {
			Util.log("MySQL ERROR: " + query);
		} else
	    try {
			while (res.next()) data.add(this.getRow(res));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
}
