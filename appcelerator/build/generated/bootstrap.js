/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 * Warning: This file is GENERATED, and should not be modified
 */
var bootstrap = kroll.NativeModule.require("bootstrap"),
	invoker = kroll.NativeModule.require("invoker"),
	Titanium = kroll.binding("Titanium").Titanium;

function moduleBootstrap(moduleBinding) {
	function lazyGet(object, binding, name, namespace) {
		return bootstrap.lazyGet(object, binding,
			name, namespace, moduleBinding.getBinding);
	}

	var module = moduleBinding.getBinding("com.tapit.TapitandroidModule")["Tapitandroid"];
	var invocationAPIs = module.invocationAPIs = [];
	module.apiName = "Tapitandroid";

	function addInvocationAPI(module, moduleNamespace, namespace, api) {
		invocationAPIs.push({ namespace: namespace, api: api });
	}

		addInvocationAPI(module, "Tapitandroid", "Tapitandroid", "createAlertAd");
	addInvocationAPI(module, "Tapitandroid", "Tapitandroid", "createBannerView");
	addInvocationAPI(module, "Tapitandroid", "Tapitandroid", "createInterstitialAd");

			if (!("__propertiesDefined__" in module)) {		
		Object.defineProperties(module, {
			"InterstitialAd": {
				get: function() {
					var InterstitialAd = lazyGet(this, "com.tapit.InterstitialAdProxy", "InterstitialAd", "InterstitialAd");
					return InterstitialAd;
				},
				configurable: true
			},
			"AlertAd": {
				get: function() {
					var AlertAd = lazyGet(this, "com.tapit.AlertAdProxy", "AlertAd", "AlertAd");
					return AlertAd;
				},
				configurable: true
			},
			"BannerView": {
				get: function() {
					var BannerView = lazyGet(this, "com.tapit.BannerViewProxy", "BannerView", "BannerView");
					return BannerView;
				},
				configurable: true
			},
		
		});
		module.constructor.prototype.createAlertAd = function() {
			return new module.AlertAd(arguments);
		}
		module.constructor.prototype.createBannerView = function() {
			return new module.BannerView(arguments);
		}
		module.constructor.prototype.createInterstitialAd = function() {
			return new module.InterstitialAd(arguments);
		}
		}
		module.__propertiesDefined__ = true;
		return module;

}
exports.bootstrap = moduleBootstrap;
