package com.tapit.air
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;
	import com.tapit.air.BannerSizes;
	
	public class TapItAir extends EventDispatcher
	{
		private static var _exContext:ExtensionContext;
		private static var _bannerExists:Boolean;
		private static var _fullscreenExists:Boolean;
		private static var _initialized:Boolean;
		private static var _dispatch:EventDispatcher = new EventDispatcher();
		
		public static function start():void
		{
			exContext = ExtensionContext.createExtensionContext("com.tapit.air", "");
			exContext.addEventListener(StatusEvent.STATUS, statusHandler);
			initialized = true;
		}
		
		public static function stop():void
		{
			if (bannerExists) removeBanner();
			if (fullscreenExists) removeFullScreen();
			
			exContext.removeEventListener(StatusEvent.STATUS, statusHandler);
			exContext = null;
			initialized = false;
		}
		
		public static function removeBanner():void
		{
			if (!bannerExists) return;
			exContext.call("removeBanner");
			bannerExists = false;
		}
		
		public static function addBanner(size:String = BannerSizes.AUTOSIZE_AD, placement:String="Bottom", zone:String = "7979"):void
		{
			if (bannerExists) return;
			if (!initialized) start();
			exContext.call("addBanner", zone, size, placement.toLowerCase());
			bannerExists = true;
		}
				
		public static function addAlert(zone:String = "7984"):void
		{
			if (!initialized) start();
			exContext.call("addAlert", zone);
		}		
				
		public static function removeFullScreen():void
		{
			if (!fullscreenExists) return;
			exContext.call("removeFullScreen");
			fullscreenExists = false;
		}
				
		public static function addFullScreen(zone:String = "7979"):void
		{
			if (!initialized) start();
			exContext.call("addFullScreen", zone);
			fullscreenExists = true;
		}
				
		private static function statusHandler(e:StatusEvent):void
		{						
			if (e.code == "FULLSCREEN_DISMISSED") fullscreenExists = false;
			
			dispatchEvent(e);
		}
		
		public static function addEventListener(p_type:String, p_displayListener:Function, p_useCapture:Boolean = false, p_priority:int = 0, p_useWeakReference:Boolean = false):void
		{
			dispatch.addEventListener(p_type, p_displayListener, p_useCapture, p_priority, p_useWeakReference);
		}
		
		public static function removeEventListener(p_type:String, p_displayListener:Function, p_useCapture:Boolean = false):void
		{
			dispatch.removeEventListener(p_type, p_displayListener, p_useCapture);
		}
		
		public static function dispatchEvent(event:*):void
		{
			dispatch.dispatchEvent(event);
		}
		
		static public function get dispatch():EventDispatcher {return _dispatch;}
		static public function set dispatch(value:EventDispatcher):void {_dispatch = value;}
		
		static public function get exContext():ExtensionContext {return _exContext;}
		static public function set exContext(value:ExtensionContext):void {_exContext = value;}
		
		static public function get bannerExists():Boolean {return _bannerExists;}
		static public function set bannerExists(value:Boolean):void {_bannerExists = value;}
		
		static public function get fullscreenExists():Boolean {return _fullscreenExists;}
		static public function set fullscreenExists(value:Boolean):void {_fullscreenExists = value;}
		
		static public function get initialized():Boolean {return _initialized;}
		static public function set initialized(value:Boolean):void {_initialized = value;}
	}
}