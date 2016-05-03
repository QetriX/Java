package com.qetrix.apps.common.converters;

/* Copyright (c) 2015 QetriX. Licensed under MIT License, see /LICENSE.txt file.
 * QView to HTML Page Converter
 */

import com.qetrix.libs.components.QView;
import com.qetrix.libs.QApp;
import com.qetrix.libs.Util;

public class QView_html_page
{
	private QView QView = null;
	private QApp _app;
	final public static String NL = "\n";

	public QView_html_page()
	{
		this._app = QApp.getInstance();
	}
	
	protected QApp app()
	{
		return this._app;
	}
	
	public String convert(Object QView)
	{
		this.QView = (QView) QView;
		Util.log("QView_html_page.convert for "+this.QView.name());

		String html = "";
		html += this.head();
		if (this.QView.sections() != null) for (String section : this.QView.sections()) {
			html += this.section(section, null, null, null);
		}
		html += this.foot();
		return html;
	}

	protected String head()
	{
		String str = "";
		str += "<!DOCTYPE html>"+NL;
		str += "<html>"+NL;
		str += "<head>"+NL;
		str += "	<meta charset=\"UTF-8\" />"+NL;
		str += "	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">"+NL;
		str += "	<title>"+(this.QView.heading() != "" ? this.QView.heading()+" - " : "") + this.app().text()+"</title>"+NL;
		str += "	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"+NL;
		str += "	<link rel=\"stylesheet\" href=\""+Util.SD+"common.css\" type=\"text/css\" media=\"all\">"+NL;
		str += "	<link rel=\"stylesheet\" href=\""+Util.XD + this.app().name()+"/"+this.app().name()+".css\" type=\"text/css\" media=\"all\">"+NL;
		str += "	<link rel=\"stylesheet\" href=\""+Util.SD+"print.css\" type=\"text/css\" media=\"print\">"+NL;
		str += "	<link rel=\"shortcut icon\" href=\""+Util.XD + this.app().name()+"/"+"favicon.ico\">"+NL;
		str += "</head>"+NL;
		str += "<body><a name=\"pt\"></a>"+NL;
		str += "<div id=\""+this.QView.name()+"\" class=\"page loading\""+(this.QView.style() != "" ? " "+this.QView.style() : "")+">"+NL;
		str += "<h1>"+(true /* TODO */ ? "<a href=\""+Util.D+"\">"+this.app().text()+"</a>" : "<span>"+this.app().text()+"</span>")+"</h1>"+NL;
		str += (this.QView.heading() != "" ? "<h2>"+this.QView.heading()+"</h2>" : "")+NL;
		return str;
	}

	/** HTML page heading */
	protected String heading(String heading)
	{
		return this.heading(heading, null);
	}
	
	protected String heading(String heading, String image)
	{
		if (heading == null) return "";
		return "<h2>"+heading+"</h2>"+NL + (image != null ? "<div id=\"page_image\" style=\"height:200px;border-radius:6px;margin-bottom:10px;clear:both;background-position:center top;background-image:url("+image+");\"></div>"+NL : "");
	}

	protected String section(String contents, String name, String heading, String cls)
	{
		//if (is_object(contents)) contents = convert(contents, "html");
		if (name == null && heading == null) return contents + NL;

		return contents == "" ? "" : "<div"+(name != null ? " id=\""+name+"\"" : "") + (cls != null ? " cls=\""+cls+"\"" : "")+">"+NL + 
			(heading != null ? "<h3>"+heading+"</h3>"+NL : "") + 
			contents + 
			"</div>"+NL;
	}

	/** HTML page foot */
	protected String foot()
	{
		String str = "";
		str += "</div>"+NL;
		str += "<div id=\"mw\"><div id=\"mwc\"></div></div><div id=\"ac\"></div><div id=\"msg\"></div>"+NL;
		str += "<script type=\"text/javascript\" src=\""+Util.SD+"qetrix.js\"></script>"+NL;
		str += "<script type=\"text/javascript\" src=\""+Util.XD+Util.APP+"/"+Util.APP+".js\"></script>"+NL;
		str += "</body>"+NL;
		str += "</html>"+NL;
		return str;
	}
}
