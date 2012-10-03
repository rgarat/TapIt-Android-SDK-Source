/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2011-2012 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

package com.tapit;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.IdFunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollInvocation;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.KrollProxySupport;
import org.appcelerator.kroll.KrollRuntime;

import org.appcelerator.kroll.common.TiConfig;

import org.appcelerator.kroll.runtime.rhino.KrollBindings;
import org.appcelerator.kroll.runtime.rhino.KrollGeneratedBindings;
import org.appcelerator.kroll.runtime.rhino.Proxy;
import org.appcelerator.kroll.runtime.rhino.ProxyFactory;
import org.appcelerator.kroll.runtime.rhino.RhinoRuntime;
import org.appcelerator.kroll.runtime.rhino.TypeConverter;
import org.appcelerator.kroll.common.Log;

import java.util.HashMap;

import com.tapit.TapitandroidModule;

import org.appcelerator.kroll.KrollModulePrototype;


public class TapitandroidModulePrototype extends KrollModulePrototype
{
	// GENERATE_SUID

	private static final boolean DBG = TiConfig.LOGD;
	private static final String TAG = "TapitandroidModulePrototype";
	private static final String CLASS_TAG = "TapitandroidModule";
	private static TapitandroidModulePrototype tapitandroidModulePrototype;

	// Lazy loaded child APIs
	private Object API_InterstitialAd = null;
	private Object API_BannerView = null;
	private Object API_AlertAd = null;

	public static TapitandroidModulePrototype getProxyPrototype()
	{
		return tapitandroidModulePrototype;
	}

	public static void dispose()
	{
		if (DBG) {
			Log.d(TAG, "dispose()");
		}
		tapitandroidModulePrototype = null;
	}

	public TapitandroidModulePrototype()
	{
		if (tapitandroidModulePrototype == null && getClass().equals(TapitandroidModulePrototype.class)) {
			tapitandroidModulePrototype = this;
			KrollGeneratedBindings.registerUsedPrototypeClass(getClass());
		}

		isModule = true;
	}

	public Scriptable getPrototype()
	{
		if (this == tapitandroidModulePrototype) {
			return KrollModulePrototype.getProxyPrototype();
		}
		return tapitandroidModulePrototype;
	}

	protected Class<? extends Proxy> getParent()
	{
		return KrollModulePrototype.class;
	}

	protected KrollProxySupport createProxy(String creationUrl, Object[] args)
	{
		return KrollProxy.createProxy(TapitandroidModule.class, getRhinoObject(), args, creationUrl);
	}

	// Methods
	public Object example(Context context, Scriptable thisObj, Object[] args)
	{
		if (DBG) {
			Log.d(TAG, "example()");
		}

		try {
		TapitandroidModule proxy = (TapitandroidModule) ((Proxy) thisObj).getProxy();

		java.lang.String javaResult = proxy.example();

		String rhinoResult = (String) javaResult;
	
			return rhinoResult;

		} catch (Throwable throwable) {
			Context.throwAsScriptRuntimeEx(throwable);
			return Undefined.instance;
		}
	}


	// Dynamic properties
	public String getter_exampleProp()
	{
		if (DBG) {
			Log.d(TAG, "get exampleProp");
		}

		TapitandroidModule proxy = (TapitandroidModule) getProxy();
		Scriptable thisObj = this;
		java.lang.String javaResult = proxy.getExampleProp();

		String rhinoResult = (String) javaResult;
		return rhinoResult;
	}
	public void setter_exampleProp(Object value)
	{
		if (DBG) {
			Log.d(TAG, "set exampleProp");
		}

		TapitandroidModule proxy = (TapitandroidModule) getProxy();
		Scriptable thisObj = this;
		java.lang.String arg0;
		arg0 = (java.lang.String) TypeConverter.jsObjectToJavaString(value, thisObj) ;
		proxy.setExampleProp(arg0);
		//proxy.setProperty("exampleProp", value); // Commenting out for parity with V8 (don't retain from a custom setter)
	}

// #string_id_map#

	// Prototype IDs
	private static final int
		Id_constructor = 1
,		// Property IDs
		// Method IDs
		Id_example = 2,
		Id_createAlertAd = 3,
				Id_createBannerView = 4,
				Id_createInterstitialAd = 5
		;
		

	public static final int MAX_PROTOTYPE_ID = 5;

	protected int getMaxPrototypeId()
	{
		return MAX_PROTOTYPE_ID;
	}

	@Override
	protected int findPrototypeId(String s)
	{
		int id = 0;
// #generated# Last update: 2012-08-24 09:04:36 GMT+07:00
        L0: { id = 0; String X = null;
            L: switch (s.length()) {
            case 7: X="example";id=Id_example; break L;
            case 11: X="constructor";id=Id_constructor; break L;
            case 13: X="createAlertAd";id=Id_createAlertAd; break L;
            case 16: X="createBannerView";id=Id_createBannerView; break L;
            case 20: X="createInterstitialAd";id=Id_createInterstitialAd; break L;
            }
            if (X!=null && X!=s && !X.equals(s)) id = 0;
            break L0;
        }
// #/generated#
		return id;
	}

// #/string_id_map#

	@Override
	protected void initPrototypeId(int id)
	{
		String name;
		int arity;
		switch (id) {
			case Id_constructor:
				arity = 0;
				name = "constructor";
				break;
			case Id_example:
				arity = 0;
				name = "example";
				break;
			case Id_createAlertAd:
				arity = 1;
				name = "createAlertAd";
				break;
			case Id_createBannerView:
				arity = 1;
				name = "createBannerView";
				break;
			case Id_createInterstitialAd:
				arity = 1;
				name = "createInterstitialAd";
				break;
			default:
				super.initPrototypeId(id);
				return;
		}
		initPrototypeMethod(CLASS_TAG, id, name, arity);
	}

	@Override
	public Object execIdCall(IdFunctionObject f,
		Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
	{
		if (!f.hasTag(CLASS_TAG)) {
			return super.execIdCall(f, cx, scope, thisObj, args);
		}

		while (thisObj != null && !(thisObj instanceof TapitandroidModulePrototype)) {
			thisObj = thisObj.getPrototype();
		}

		TapitandroidModulePrototype proxy = (TapitandroidModulePrototype) thisObj;
		int id = f.methodId();
		switch (id) {
			case Id_constructor:
				return jsConstructor(scope, args);
			case Id_example:
				return example(cx, thisObj, args);
				
			case Id_createAlertAd:
				return ProxyFactory.createRhinoProxy(cx, scope,
					"com.tapit.AlertAdProxy", args);
			case Id_createBannerView:
				return ProxyFactory.createRhinoProxy(cx, scope,
					"com.tapit.BannerViewProxy", args);
			case Id_createInterstitialAd:
				return ProxyFactory.createRhinoProxy(cx, scope,
					"com.tapit.InterstitialAdProxy", args);
			default:
				throw new IllegalArgumentException(String.valueOf(id));
		}
	}



// #string_id_map#

	// Instance IDs
	private static final int ISTART = 0;
	// Dynamic property IDs
	private static final int

		Id_exampleProp = 1
		;

;

	// Child APIs
	private static final int

		Id_InterstitialAd = 2
		,		
		Id_BannerView = 3
		,		
		Id_AlertAd = 4
		;

	public static final int MAX_INSTANCE_ID = 4;

	@Override
	protected int getMaxInstanceId()
	{
		return MAX_INSTANCE_ID;
	}

	@Override
	protected int findInstanceIdInfo(String s)
	{
		int id = 0;
// #generated# Last update: 2012-08-24 09:04:36 GMT+07:00
        L0: { id = 0; String X = null;
            L: switch (s.length()) {
            case 7: X="AlertAd";id=Id_AlertAd; break L;
            case 10: X="BannerView";id=Id_BannerView; break L;
            case 11: X="exampleProp";id=Id_exampleProp; break L;
            case 14: X="InterstitialAd";id=Id_InterstitialAd; break L;
            }
            if (X!=null && X!=s && !X.equals(s)) id = 0;
            break L0;
        }
// #/generated#
		if (id == 0) {
			return id;
		}
		return instanceIdInfo(PERMANENT, id);
	}

// #/string_id_map#

	@Override
	protected String getInstanceIdName(int id)
	{
		switch (id) {
			case Id_exampleProp:
				return "exampleProp"; 
			case Id_InterstitialAd:
				return "InterstitialAd";
			case Id_BannerView:
				return "BannerView";
			case Id_AlertAd:
				return "AlertAd";
			default:
				throw new IllegalArgumentException(String.valueOf(id));
		}
	}

	@Override
	protected Object getInstanceIdValue(int id, Scriptable start)
	{
		TapitandroidModulePrototype proxy = this;
		while (start != null && !(start instanceof TapitandroidModulePrototype)) {
			start = start.getPrototype();
		}

		if (start instanceof TapitandroidModulePrototype) {
			proxy = (TapitandroidModulePrototype) start;
		}

		switch (id) {
			case Id_exampleProp:
				return proxy.getter_exampleProp();
			case Id_InterstitialAd:
				if (proxy.API_InterstitialAd == null) {
					Context context = Context.getCurrentContext();
					Scriptable scope = RhinoRuntime.getGlobalScope();

					proxy.API_InterstitialAd = KrollBindings.getProxyBinding(context, scope,
						"com.tapit.InterstitialAdProxy", com.tapit.InterstitialAdProxyPrototype.class); 
				}
				return proxy.API_InterstitialAd;
			case Id_BannerView:
				if (proxy.API_BannerView == null) {
					Context context = Context.getCurrentContext();
					Scriptable scope = RhinoRuntime.getGlobalScope();

					proxy.API_BannerView = KrollBindings.getProxyBinding(context, scope,
						"com.tapit.BannerViewProxy", com.tapit.BannerViewProxyPrototype.class); 
				}
				return proxy.API_BannerView;
			case Id_AlertAd:
				if (proxy.API_AlertAd == null) {
					Context context = Context.getCurrentContext();
					Scriptable scope = RhinoRuntime.getGlobalScope();

					proxy.API_AlertAd = KrollBindings.getProxyBinding(context, scope,
						"com.tapit.AlertAdProxy", com.tapit.AlertAdProxyPrototype.class); 
				}
				return proxy.API_AlertAd;
			default:
				throw new IllegalArgumentException(String.valueOf(id));
		}
	}

	@Override
	protected void setInstanceIdValue(int id, Scriptable start, Object value)
	{
		Scriptable original = start;
		TapitandroidModulePrototype proxy = this;
		while (start != null && !(start instanceof TapitandroidModulePrototype)) {
			start = start.getPrototype();
		}

		if (start instanceof TapitandroidModulePrototype) {
			proxy = (TapitandroidModulePrototype) start;
		}

		switch (id) {
			case Id_exampleProp:
				proxy.setter_exampleProp(value);
				break; 
			case Id_InterstitialAd:
				if (value instanceof Proxy) {
					proxy.API_InterstitialAd = value;
				} else {
					defineProperty(original, "InterstitialAd", value, 0);
				}
				break;
			case Id_BannerView:
				if (value instanceof Proxy) {
					proxy.API_BannerView = value;
				} else {
					defineProperty(original, "BannerView", value, 0);
				}
				break;
			case Id_AlertAd:
				if (value instanceof Proxy) {
					proxy.API_AlertAd = value;
				} else {
					defineProperty(original, "AlertAd", value, 0);
				}
				break;
			default:
				throw new IllegalArgumentException(String.valueOf(id));
		}
	}

	@Override
	public String getClassName()
	{
		return CLASS_TAG;
	}
}
