TapIt Android SDK 
=================

Version 1.7.0

``/dist`` Library files to be included in your app<br/>
``/doc`` Installation Documentation (out of date)<br/>
``/src`` SDK and example project source


Alert Ad Usage
--------------
Alert ads are a simple ad unit designed to have a native feel. The user is given the option to download an app, and if they accept, they are taken to the app within the app marketplace.

````java
AlertAd alertAd = new AlertAd(this, "YOUR_ZONE_ID");
Hashtable<String, String> params = new Hashtable<String, String>();
params.put("mode", "test"); // Alert ads only in test mode during beta phase
alertAd.setCustomParameters(params);
alertAd.showAlertAd();
````

Sample implementation can be found here: https://github.com/tapit/TapIt-Android-SDK-Source/blob/master/src/example/src/com/yourcompany/TapItTestActivity.java#L87