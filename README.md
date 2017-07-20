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
compile 'com.github.huyjackson:NavigationDrivingLibrary:v1.0'
```

## Usage
If you have already the Google map object:
```
NavigationDiriving navigationDriving;

navigationDriving = new NavigationDriving(this, googleMap);

//1.Set API KEY for Google Direction
navigationDriving.setDirectionFinderAPIKey(GOOGLE_API_KEY);

//2.Set navigation driving listenter for your activity and override 2 method
navigationDriving.setOnNavigationDrivingListener(this);

//3.Override 2 method:
public void onLocationChanged(Location location); //The method will call if current location change
public void onDirectionFinderSuccess(); //The method will call if direction finder sucess and you can start navagation.

//4.Find direction with start place and destination place
navigationDriving.findDirection(startPlace, destPlace);

//5.After find direction you can start navigation diriving
navigationDriving.startNavigationDriving();
 
//6.If you want to stop navigation diriving, you can use method
navigationDriving.stopNavigationDriving();

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
