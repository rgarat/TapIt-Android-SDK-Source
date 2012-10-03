/**
 * cordova is available under *either* the terms of the modified BSD license
 * *or* the MIT License (2008). See http://opensource.org/licenses/alphabetical
 * for full text.
 * 
 * Copyright (c) Matt Kane 2010 Copyright (c) 2011, IBM Corporation
 */
/**
 * Tapit Android plugin
 * @author Manish Kumar - manishkumar023@gmail.com
 * */


var TapItAndroid = function() {
};


/**
 * Function to call full screen ad
 * @param successcallback
 * @param errorcallback
 * @param zoneid 
 */
TapItAndroid.prototype.Adfullscreen = function(successCallback, errorCallback,zoneid) {
    if (errorCallback == null) { errorCallback = function() {}}
    if (typeof errorCallback != "function")  {
        console.log("TapItAndroid Full screen ad failure: failure parameter not a function");
        return
    }
    if (typeof successCallback != "function") {
        console.log("TapItAndroid Full screen ad failure: success callback parameter must be a function");
        return
    }

    cordova.exec(successCallback, errorCallback, 'TapItAndroid', 'Adfullscreen', [zoneid]);
};

/**
 * Function to call Interstitial ad
 * @param successcallback
 * @param errorcallback
 * @param zoneid 
 */
TapItAndroid.prototype.AdInterstitial = function(successCallback, errorCallback , zoneid) {
    if (errorCallback == null) { errorCallback = function() {}}
    if (typeof errorCallback != "function")  {
        console.log("TapItAndroid AdInterstitial failure: failure parameter not a function");
        return
    }
    if (typeof successCallback != "function") {
        console.log("TapItAndroid AdInterstitial failure: success callback parameter must be a function");
        return
    }

    cordova.exec(successCallback, errorCallback, 'TapItAndroid', 'AdInterstitial', [zoneid]);
};

/**
 * Function to call AdOfferWall 
 * @param successcallback
 * @param errorcallback
 * @param zoneid 
 */
TapItAndroid.prototype.AdOfferWall = function(successCallback, errorCallback , zoneid) {
    if (errorCallback == null) { errorCallback = function() {}}
    if (typeof errorCallback != "function")  {
        console.log("TapItAndroid AdOfferWall failure: failure parameter not a function");
        return
    }
    if (typeof successCallback != "function") {
        console.log("TapItAndroid AdOfferWall failure: success callback parameter must be a function");
        return
    }

    cordova.exec(successCallback, errorCallback, 'TapItAndroid', 'AdOfferWall', [zoneid]);
};

/**
 * Function to call AdVideo
 * @param successcallback
 * @param errorcallback
 * @param zoneid 
 */
TapItAndroid.prototype.AdVideo = function(successCallback, errorCallback , zoneid) {
    if (errorCallback == null) { errorCallback = function() {}}
    if (typeof errorCallback != "function")  {
        console.log("TapItAndroid AdVideo failure: failure parameter not a function");
        return
    }
    if (typeof successCallback != "function") {
        console.log("TapItAndroid  AdVideo failure: success callback parameter must be a function");
        return
    }

    cordova.exec(successCallback, errorCallback, 'TapItAndroid', 'AdVideo', [zoneid]);
};


/**
 * Function to call AlertAd
 * @param successcallback
 * @param errorcallback
 * @param zoneid 
 */
TapItAndroid.prototype.AlertAd = function(successCallback, errorCallback , zoneid) {
    if (errorCallback == null) { errorCallback = function() {}}
    if (typeof errorCallback != "function")  {
        console.log("TapItAndroid  AlertAd ad failure: failure parameter not a function");
        return
    }
    if (typeof successCallback != "function") {
        console.log("TapItAndroid  AlertAd ad failure: success callback parameter must be a function");
        return
    }

    cordova.exec(successCallback, errorCallback, 'TapItAndroid', 'AlertAd', [zoneid]);
};


// -------------------------------------------------------------------
if(!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.TapItAndroid) {
    window.plugins.TapItAndroid = new TapItAndroid();
}