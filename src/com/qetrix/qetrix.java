package com.qetrix;

/* Copyright (c) 2015 QetriX. Licensed under MIT License, see /LICENSE.txt file.
 * Index file
 */

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.Map.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

public class QetriX extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	public QetriX()
	{
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		Util.log("doGet");

		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		resp.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
		resp.setHeader("Expires", "Thu, 19 Nov 2009 14:30:00 GMT");
		resp.setHeader("Pragma", "no-cache");
		
    	// TODO
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		
		this.inputDS = new Http();
		((Http)this.inputDS).set(req.getParameterMap(), "post");
		this.doGet(req, resp);
	}
}

