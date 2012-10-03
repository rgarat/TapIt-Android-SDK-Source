Using this plugin requires Android PhoneGap

 

To install the plugin, move www/tapit.js to your project's www folder and include a reference to it in your html file after cordova-2.x.x.js.

 

      <script type="text/javascript" charset="utf-8" src="cordova-2.x.x.js"></script>
      <script type="text/javascript" charset="utf-8" src="tapit.js"></script>


Create a directory within your project called "src/com/tapit/android" and move TapItAndroid.java into it.


        Add this method to your phonegap app's activity class
        public void setupTapit(){
            AdView ad;
            ad= new AdView(this,"");
            ad.setId(2323);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            ad.setLayoutParams(layoutParams);
            LinearLayout layout = super.root;
            layout.addView(ad);
        }
        


Call this method after the super.loadUrl(file:///android_asset/www/index.html);
something like this


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            super.loadUrl("file:///android_asset/www/index.html");
            setupTapit();
        }

 
In your res/xml/plugins.xml file add the following line:


        <plugin name="TapItAndroid" value="com.tapit.android.TapItAndroid"/>    


Add the following activity to your AndroidManifest.xml file. It should be added inside the <application/> tag.


        <!--Tap it AdActivity-->
        <activity
                android:name="com.tapit.adview.AdActivity"
                android:configChanges="keyboard|keyboardHidden|orientation" >
        </activity>
        
        


Using the plugin
The plugin creates the object window.plugins.TapItAndroid. To use, call one of the following, available methods:


        window.plugins.TapItAndroid.AdVideo(function(s){
            //success callback
            },function(e){
            //error callback
            },"zone_id");

        window.plugins.TapItAndroid.Adfullscreen(function(s){
            //success callback
            },function(e){
            //error callback
            },"zone_id");

        window.plugins.TapItAndroid.AdInterstitial(function(s){
            //success callback
            },function(e){
            //error callback
            },"zone_id");

        window.plugins.TapItAndroid.AdOfferWall(function(s){
            //success callback
            },function(e){
            //error callback
            },"zone_id");

        window.plugins.TapItAndroid.AlertAd(function(s){
            //success callback
            },function(e){
            //error callback
            },"zone_id");


*************Here zone_id is the zone id provided in the developer console of your TapIt account.*******************