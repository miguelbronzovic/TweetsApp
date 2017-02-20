# TWEETS SEARCH APP #

Android application that based on keywords entered by the user shows the Tweets related to that terms.


### Required Features ###

* Show the list of tweets listed by the based on the search term
* Support rotation of the phone without losing any data on screen
* Even though it sounds unnecessary; use a Fragment for the UI
* Have the app support Android OS 2.3 and up (API 10)
* **You cannot use any Android Twitter SDK**

### Optional Features ###

* Show the first image associated to a tweet (or all of them if you want)
* Whatever you want :)

### Solution ###

* Project Type: *Android Studio*
* Platform: *Android*
* Min SDK: *10*
* Target SDK: *23*
* Gradle: *2.1.3*

### Third party libs ###

Using the following dependencies:

* **Android Support Libs**
    * Appcompat-v7
    * Support Design
    * CardView-v7
* **Square Libs**
    * Retrofit 2
    * Okio
    * OkHttp3
    * Picasso
    * Retrofit 2 Adapter RxJava
    * OkHttp3 Downloader
    * Logging Interceptor
* **RxJava**
    * RxAndroid Lib
    * RxJava Lib
* **Joda Time**
    * Joda Time Lib
* **Parceler**
    * Parceler Api Lib
* **Logging**
    * Timber
* **Butterknife**
    * Butterknife Lib


### Tested Devices ###

The project has been tested on the following devices in ** *DEBUG BUILD VARIANT* ** :

* **Genymotion**
    * *Android v2.3.7/SDK 10*
    * *Android v4.4.4/SDK 19*
* **Motorola X Style**
    * *Android v6.0/SDK 23*

### Things to improve ###

* Although the apps is structured in a decoupled way, there are still some coupled dependencies.
* Implement some kind of UI pattern like MVP
* Better UI design