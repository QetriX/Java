package com.qetrix.libs;

/* Copyright (c) QetriX.com. Licensed under MIT License, see /LICENSE.txt file.
 * 2016-01-28 | QApp Class
 */

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QPage
{
	private static QPage _instance; /// Static reference to this QetriX App
	
	private String _name = ""; /// App Name. Recommended 1-16 chars, [a-z][0-9] only. Used in paths, keys, possibly ds conn etc.
	private String _text = ""; /// Language specific, appropriate to $_name
	private Auth _auth = null;
	private DataStore _ds = null;
	
	private String _outputFormat = "html"; /// In what format should the output be rendered (what set of converters it uses)
	private String _lang = "en"; /// English as a default language
	private String _g11n; /// Internationalization and localization settings: date format, time format, units in general, temperature...
	private Map<String, String> _lbl = new HashMap<String, String>(); /// Array of labels in current language
	private Map<String, String> _data = new HashMap<String, String>();
	
	private Map<String, DataStore> _dsList = new HashMap<String, DataStore>();
	private int _statusCode = 200;
	
	private String _path = "";
	private String _pathApp = "";
	private String _pathAppCommon = "";
	private String _pathData = "";
	private String _pathContent = "";
	
	private Boolean _isMultiApp = true;
	private String defaultModule = "";

	private List<String> _allowedCookies = new ArrayList<String>() {{ add("_c"); add("_f"); }};
	private List<String> _features = new ArrayList<String>();
	
	public QPage()
	{
		init();
	}
	
	public static QPage getInstance()
	{
		if (_instance == null) _instance = new QPage();
		return _instance;
	}
	
	private void init()
	{
		this._path = System.getProperty("user.dir");
		this._isMultiApp = true;
		
		this._outputFormat = this.suggestOutputFormat();
		this._lang = this.suggestLanguage();
		
		_instance = this;
	}
	
	public void setAppName(String name, String text) // TODO: TEMP!!!
	{
		if (this._name != null && !this._name.isEmpty()) Util.log("WARNING: QApp.name already defined!"); 
		else this._name = name;
		this._text = text; 
	}
	
	public String pathApp()
	{
		return this._pathApp;
	}
	
	public String pathAppCommon()
	{
		return this._pathAppCommon;
	}
	
	public String pathData()
	{
		return this._pathData;
	}
	
	public String pathContent()
	{
		return this._pathContent;
	}
	
	public String path()
	{
		return this._path;
	}
	
	public String name()
	{
		return this._name;
	}
	
	public String text()
	{
		return this._text;
	}
	
	public String outputFormat()
	{
		return _outputFormat;
	}
	
	public String lang()
	{
		return this._lang;
	}
	
	public DataStore ds()
	{
		return this._ds;
	}
	
	public QPage ds(DataStore value)
	{
		this._ds = value;
		return this;
	}
	
	public Auth auth()
	{
		if (this._auth == null && this._ds != null) {
			this._auth = new Auth(this._ds);
		}

		return this._auth;
	}
	
	public Boolean isMultiApp()
	{
		return this._isMultiApp;
	}
	
	public Map<String, Object> parsePath(String path) throws Exception
	{
		if (!(this.name() != null) && !this.name().trim().isEmpty()) {
			Util.log("Name: " + this.name());
			throw new Exception("Err#0101: App has been already loaded.");
		}
		if (path == null) path = "";
		List<String> aPath = new ArrayList<String>(Arrays.asList(path.split("/")));
		aPath.remove("");
		Map<String, Object> modVars = new HashMap<String, Object>();
		modVars.put("args", new HashMap<String, String>());
		
		// Set the App
		if (this.isMultiApp()) {
			if (aPath.isEmpty()) aPath.add("arab");
			modVars.put("app", (String) aPath.get(0));
			aPath.remove(0);
			this._pathAppCommon = this._path + "apps/common/";
			
			if (modVars.get("app") == null) {
				if (this.get("localhost") == "1") { // localhost + multiapp
					//fsds = this.loadDataStore("filesystem", "", "", "");
				}
				throw new Exception("Err#xy: Undefined App!");
			}
			this._name = (String) modVars.get("app");
			this._pathApp = this._path + "apps/" + this._name + "/";
		} else {
			modVars.put("app", "qetrix"); // TODO
			this._name = "qetrix";
			this._pathApp = this._path;
			this._pathAppCommon = this._path + "apps/common/";
		}
		this._pathContent = this.pathApp() + "content/";
		this._pathData = this.pathApp() + "data/";
		
		// Load config
		String sPath = Util.implode("/", aPath);
		//for (String sp : aPath) sPath += sp + "/";
		//modVars.put("path", sPath);
		((HashMap<String, String>) modVars.get("args")).put("path", sPath);
		Util.log(sPath, "sPath");
		
		// Mod
		if (aPath.size() > 0 && Util.isNumeric(aPath.get(0))) {
			modVars.put("id", (String) aPath.get(0));
			aPath.remove(0);
		} else if (aPath.size() > 0) {
			modVars.put("mod", (String) aPath.get(0));
			aPath.remove(0);
		}
		if (!modVars.containsKey("mod") || (modVars.containsKey("mod") && (modVars.get("mod").equals(null) || modVars.get("mod").equals("module")))) modVars.put("mod", modVars.get("app"));
		
		if (aPath.size() > 0) {
			// Func
			if (Util.isNumeric(aPath.get(0))) {
				if (modVars.containsKey("id")) {
					//((HashMap) modVars.get("args")).
				} else {
					modVars.put("id", (String) aPath.get(0));
					aPath.remove(0);
					
					if (aPath.size() > 0) {
						modVars.put("func", (String) aPath.get(0));
						aPath.remove(0);
					}
				}
			} else {
				modVars.put("func", (String) aPath.get(0));
				aPath.remove(0);

				if (aPath.size() > 0 && modVars.containsKey("id") && Util.isNumeric(aPath.get(0))) {
					modVars.put("id", (String) aPath.get(0));
					aPath.remove(0);
				}
			}
			if (modVars.containsKey("func")) modVars.put("func", ((String) modVars.get("func")).replace("-", "_"));
			//if (modVars.get("func") == null) modVars.put("func", "main");
		
			// Args
			for (int i = 0; i < aPath.size(); i++) modVars.put(String.valueOf(i), aPath.get(i));
		}
		

		// Config
		return modVars;
	}
	
	private QPage set(String key, String value) {
		key = key.toLowerCase();
		_data.put(key, value);
		return this;
	}

	private String get(String key) {
		key = key.toLowerCase();
		if (_data.containsKey(key)) return _data.get(key);
		return "";
	}

	private Map<String, String> applyQueAppConfig(Map<String, String> modVars)
	{
		List<String> cfgData;
		try {
			cfgData = Files.readAllLines(Paths.get(this.getClass().getName().replace(".", "/") + "/app.qua"), StandardCharsets.UTF_8);
			for (int rowNum = 0; rowNum < cfgData.size(); rowNum++) {
				if (cfgData.get(rowNum).trim() == "") continue;
				String fname = null;
				String func = null;
				switch (func.toLowerCase()) {
				case "app.name":
					this._name = fname;
					modVars.put("name", fname);
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return modVars;
	}
	
	public String loadModule(Map<String, Object> page) throws Exception
	{
		// Handle keywords, add leading underscore; '/list' in URL => public String _list(Map<String, String> args)
		if (page.containsKey("func") && Arrays.asList("abstract", "and", "array", "as", "bool", "break", "callable", "case", "catch", "class", "clone", "const", "continue", "declare", "default", "die", "do", "echo", "else", "elseif", "empty", "enddeclare", "endfor", "endforeach", "endif", "endswitch", "endwhile", "eval", "exit", "extends", "false", "final", "float", "for", "foreach", "function", "global", "goto", "if", "implements", "include", "include_once", "int", "instanceof", "insteadof", "interface", "isset", "list", "mixed", "namespace", "new", "null", "numeric", "or", "print", "private", "protected", "public", "require", "require_once", "resource", "return", "scalar", "static", "string", "switch", "throw", "trait", "true", "try", "unset", "use", "var", "while", "xor").contains(page.get("func")))
				page.put("func", "_" + page.get("func"));
		
		// If DS is defined and allows path searches ('getpath' feature), search it there ONLY, do not use classNames
		if (this.ds() != null && page.containsKey("dspath")) {
			String path = "";//this.ds().pageUrl(page.get("path"));
			if (path != null) {
				
			} else {
				page.put("404", page.get("mod") != "" ? page.get("mod") : page.get("app"));
				page.put("mod", "err404");
			}
			page.put("dspath", path);
		}
		
		// Auth User
		// ...
		
		
		// Add the rest of page variables
		if (page.containsKey("id") && Integer.parseInt(page.get("id").toString()) > 0) ((HashMap<String, String>) page.get("args")).put("id", page.get("id").toString());
		//((HashMap<String, String>) page.get("args")).put("path", page.get("path").toString());

		//if (isset($page["id"]) && $page["id"] > 0) $page["args"]["id"] = $page["id"];
		//$page["args"]["path"] = $page["path"];

		// Unspecified func => call main
		if (!page.containsKey("func") || page.get("func") == null || page.get("func").toString() == "") {
			page.put("func", "main");
		}

		String output = "";
		Class<?> cls = null;
		
		for (int clsi = 0; clsi < 2; clsi++) {
			try {
				Util.log("com.qetrix.apps." + this._name + ".modules." + page.get("mod").toString().substring(0, 1).toUpperCase() + page.get("mod").toString().substring(1), "Loading mod");
				cls = Class.forName("com.qetrix.apps." + this._name + ".modules." + page.get("mod").toString().substring(0, 1).toUpperCase() + page.get("mod").toString().substring(1));
			} catch (ClassNotFoundException e) {
				switch (clsi) {
				case 0:// Mod not found, try default mod
					page.put("func", page.get("mod"));
					page.put("mod", this._name);
					break;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		if (cls == null) {
			//resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "Err#AP02: Invalid method defined: " + this._name + ".modules." + page.get("mod").toString().substring(0, 1).toUpperCase() + page.get("mod").toString().substring(1);
		}
		QModule modClass = (QModule) cls.newInstance();
		
		Method[] m = modClass.getClass().getDeclaredMethods();
		if (!Util.methodExists(m, page.get("func").toString())) page.put("func", "main");
		
		output = (String) modClass.getClass().getMethod(page.get("func").toString(), Map.class).invoke(modClass, (HashMap) page.get("args"));
		
		
		if (/*modClass.stage() != QModuleStage.prod &&*/ output == "") throw new Exception("Err#4001: No output. Did you forget a return statement in " + page.get("mod") + "." + page.get("func") + "?");
		return output;
	}
	
	/*public DataStore loadDataStore(String dataStoreClassName, String host, String scope, String prefix, String user, String password)
	{
		String scriptPath = "";
		Class<?> ds = Class.forName("com.qetrix.apps." + (scriptPath == this.pathApp() ? this.name() : "common") + ".datastores." + dataStoreClassName);
		ds.conn(host, scope, prefix, user, password);
		this._dsList.put(dataStoreClassName.toLowerCase(), ds);
		return ds;
	}*/

	private String suggestOutputFormat()
	{
		return "html";
	}
	
	private String suggestLanguage()
	{
		return "en";
	}

	public String lbl(String var)
	{
		return var;
	}

	public void setDataStore(DataStore ds)
	{
		this._ds = ds;
	}

	public int statusCode() {
		return this._statusCode;
	}
}
