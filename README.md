# NavigationDrivingLibrary


## Installation

A step by step series of examples that tell you have to get the library:

### JitPack repository

Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Add the dependency
```
compile 'com.github.huyjackson:NavigationDrivingLibrary:v1.2'
```

## Changelog

All notable changes to this project will be documented in this file.

### [v1.2] - 2017-07-21
#### Added
- Fixed sync gradle
### [v1.1] - 2017-07-20
#### Added
- New support findDirection method with Location type parameter
- Add 2 method onDirectionFinderStart() and onDirectionFinderFailed() in OnNavigationDrivingListener interface
- Support set voice mode with 2 type are: GTTS (Google translate TTS) and Android TTS
- Support set custom navigation diriving
- Fixed a few bugs
#### Removed
- None
#### Changed
- None
### [v1.0] - 2017-07-18
#### Added
- Upload base library


## Usage
If you have already the Google map object:
```
NavigationDiriving navigationDriving;

navigationDriving = new NavigationDriving(this, googleMap);

//1.Set API KEY for Google Direction
	navigationDriving.setDirectionFinderAPIKey(GOOGLE_API_KEY);

//2.Set voice mode
	- Google translate TTS: navigationDriving.setVoiceMode(Constant.NAVIGATION_DIRIVING_GTTS_VOICE_MODE);
	- Android TTS: navigationDriving.setVoiceMode(Constant.NAVIGATION_DIRIVING_TTS_VOICE_MODE);

//3.In your activity implement NavigationDriving.OnNavigationDrivingListener interface.

//4.Set navigation driving listenter for your activity and override 2 method
	navigationDriving.setOnNavigationDrivingListener(this);

//5.Override 4 method:
	public void onLocationChanged(Location location); //The method will call if current location change
	public void onDirectionFinderSuccess(); //The method will call if direction finder sucess and you can start navagation.
	public void onDirectionFinderStart(); //The method will call if direction finder start
        public void onDirectionFinderFailed(); //The method will call if it can't find direction

//6.Find direction with start place and destination place
	navigationDriving.findDirection(startPlace, destPlace);
     or
	navigationDriving.findDirection(Location startLocation, Location destLocation);

//7.After find direction you can start navigation diriving
	navigationDriving.startNavigationDriving();
 
//8.If you want to stop navigation diriving, you can use method
	navigationDriving.stopNavigationDriving();
	
**Note**
- If you want to custom navigation driving whenever the location change, you can call method 
setCustomNavigationDiriving(true) and put your code in onLocationChanged of your activity 
implement NavigationDriving.OnNavigationDrivingListener interface.

```
## Note
### The project is under development so it is not yet complete

## Authors

* **Nguyen Vu Huy** - *Initial work* - [huyjackson](https://github.com/huyjackson)

## License
--------

    Copyright 2017 SuperClassGroup

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
