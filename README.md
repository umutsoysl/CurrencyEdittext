# CurrencyEdittext
CurrencyEditText is an extension of Android's EditText view object. It is a module designed to provide ease-of-use when using an EditText field for gathering currency information from a user.

# Overview
<img src="image/appSs.gif" width="300" height="490"/>

# Setup

* **Grade**  
Add it as a dependency to your `build.gradle`

```gradle

allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

```gradle

dependencies {
      implementation 'com.github.umutsoysl:CurrencyEdittext:1.0.1'
}
	
```
# Using The Module

* **Using the module is not much different from using any other EditText view. Simply define the view in your XML layout:**

```xml

 <com.umut.soysal.lib.CurrencyEditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
	    android:id="@+id/currencyEn"		       
            tools:ignore="MissingConstraints"/>

```
You're done! The CurrencyEditText module handles all the string manipulation and input monitoring required to allow for a clean, easy-to-use currency entry system.


* **You can choose any locale.**

CurrencyEditText relies on a Locale object to properly format the given value. There are two Locale variables that are exposed via getters and setters on a given CurrencyEditText object: locale and defaultLocale.

Locale is the users default locale setting based upon their Android configuration settings. This value is editable by the user in Android settings, as well as via the CurrencyEditText API. 

```kotlin
/*
set currency usd
*/
currencyEn.locale = Locale.US
	
```


License
--------


    Copyright 2019 Umut Soysal.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
