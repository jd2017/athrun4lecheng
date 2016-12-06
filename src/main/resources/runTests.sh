#! /bin/sh

#@author taichan@taobao.com
sudo ./xcode-select -switch /Applications/Xcode.app/

XCODE_PATH=`xcode-select -print-path`  #/Applications/Xcode.app/Contents/Developer
echo $XCODE_PATH

# xcrun instruments -s templates    xcode-select --print-path
#/Applications/Xcode.app/Contents/Applications/Instruments.app/Contents/PlugIns/AutomationInstrument.xrplugin/Contents/Resources/Automation.tracetemplate
XCODE_VERSION=`xcodebuild -version | grep 'Xcode 4.2' || xcodebuild -version | grep 'Xcode 4.3' || xcodebuild -version | grep 'Xcode 4.4'|| xcodebuild -version | grep 'Xcode 7.2'`
if [ -z "$XCODE_VERSION" ] ; then
TRACETEMPLATE="$XCODE_PATH/../Applications/Instruments.app/Contents/PlugIns/AutomationInstrument.xrplugin/Contents/Resources/Automation.tracetemplate"   #AutomationInstrument.bundle
	echo $TRACETEMPLATE
else
	TRACETEMPLATE="$XCODE_PATH/Platforms/iPhoneOS.platform/Developer/Library/Instruments/PlugIns/AutomationInstrument.bundle/Contents/Resources/Automation.tracetemplate"
	echo $TRACETEMPLATE
fi

APP_LOCATION=$1
INSTRUMENT_ROOT=$2
isSimulator=$3
UDID=$4

#APP_LOCATION="/Users/komejun/Library/Application Support/iPhone Simulator/5.0/Applications/1622F505-8C07-47E0-B0F0-3A125A88B329/Recipes.app/"
#APP_LOCATION="/Users/athrun/Desktop/TaoTest/build/Debug-iphonesimulator/TaoTest.app"


if [ "$isSimulator" = "true" ] ; then
  echo instruments -t $TRACETEMPLATE -D instrumentsDriver "$APP_LOCATION" -e UIASCRIPT  "$INSTRUMENT_ROOT"/CSRunner.js -e UIARESULTSPATH  "$INSTRUMENT_ROOT"/log/ -v 
  instruments -t $TRACETEMPLATE -D instrumentsDriver "$APP_LOCATION" -e UIASCRIPT  "$INSTRUMENT_ROOT"/CSRunner.js -e UIARESULTSPATH  "$INSTRUMENT_ROOT"/log/ -v 
else
  echo instruments -w $UDID -t $TRACETEMPLATE -D instrumentsDriver "$APP_LOCATION" -e UIASCRIPT  "$INSTRUMENT_ROOT"/CSRunner.js -e UIARESULTSPATH  "$INSTRUMENT_ROOT"/log/ -v 
  instruments -w $UDID -t $TRACETEMPLATE -D instrumentsDriver "$APP_LOCATION" -e UIASCRIPT  "$INSTRUMENT_ROOT"/CSRunner.js -e UIARESULTSPATH  "$INSTRUMENT_ROOT"/log/ -v	
fi



