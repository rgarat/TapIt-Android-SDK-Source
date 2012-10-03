var win = Ti.UI.createWindow({
	backgroundColor : 'white'
});
win.open();

// TODO: write your module tests here
var tapit = require('com.tapit');

var bannerView = tapit.createBannerView({
	adzone : '7527',
	width : Ti.UI.FILL,
	height : 120,
	top : 0,
	left : 0
});

win.add(bannerView);

var btnAlert = Ti.UI.createButton({
	title : 'Alert Ads',
	top : 100,
	width : 220,
	height : 80
});

btnAlert.addEventListener('click', function() {
	var ad = tapit.createAlertAd();
	ad.showAd('7527');
});

win.add(btnAlert);


var btnAlert3 = Ti.UI.createButton({
	title : 'Interstitial',
	top : 180,
	width : 220,
	height : 80
});

btnAlert3.addEventListener('click', function() {
	var ad = tapit.createInterstitialAd();
	ad.showAd('7527');
});

win.add(btnAlert3); 