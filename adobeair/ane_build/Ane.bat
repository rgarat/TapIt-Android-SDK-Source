set native_directory=android
set default_directory=default
set destination_ANE=TapItAir.ane
set extension_XML=extension.xml
set library_SWC=TapItAirLibrary.swc
set library_JAR=TapItAir.jar
set library_SWF=library.swf
adt -package -target ane "%destination_ANE%" "%extension_XML%" -swc "%library_SWC%" -platform Android-ARM -C "%native_directory%" "%library_SWF%" "%library_JAR%" -platform default -C "%default_directory%" "%library_SWF%"