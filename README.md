<h1 align="center">Services</h1></br>
<p align="center" >  
It teaches how to use different types of services in conjunction with multi-module architecture.
</p>
</br>

<p align="center">
 <a href="https://medium.com/@bahadir.kayis24/android-service-584e1311b61c"><img alt="Medium" src="https://img.shields.io/badge/Medium-Services-black.svg"/></a>
 <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-red.svg"/></a>
 <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat"/></a>
 <a href="https://github.com/BahadirKayis"><img alt="Profile" src="https://img.shields.io/badge/GitHub-BahadirKayis-darkblue"/></a> 
</p>

## Video
https://github.com/BahadirKayis/Services/assets/66027016/db982aad-51ba-4f99-8185-78f0e124b56a



## Permission
<pre>
&lt;uses-permission android:name="android.permission.FOREGROUND_SERVICE" /&gt;
&lt;uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" android:maxSdkVersion="32" /&gt;  SDK <=32
&lt;uses-permission android:name="android.permission.READ_MEDIA_AUDIO" /&gt;  SDK =>33
</pre>

## Tech stack & Open-source libraries
- Minimum SDK level 24
- [Kotlin](https://kotlinlang.org/) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) and [Flow](https://developer.android.com/kotlin/flow)
- Android Effect-Event-State(EES) - It is an architectural approach to UI management that organizes the relationship of effects, events and states.
- [Jetpack](https://developer.android.com/jetpack/getting-started)- It is a collection of libraries, tools and guidelines that simplify and empower Android app development.
  - [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
    - [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - perform an action when lifecycle state changes
    - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes. 
    - [UseCases](https://developer.android.com/topic/architecture/domain-layer) - Located domain layer that sits between the UI layer and the data layer. 
    - [Repository](https://developer.android.com/topic/architecture/data-layer) - Located in data layer that contains application data and business logic.
  - [DataStore](https://developer.android.com/jetpack/androidx/releases/datastore) - It is an important component used to manage data and facilitate data storage operations.
- [Dependency Injection](https://developer.android.com/training/dependency-injection) - Is a design pattern that facilitates the management of dependencies and the integration of components in a flexible, testable, and maintainable manner
- [Android Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Dependency Injection Library
- [Broadcasts](https://developer.android.com/guide/components/broadcasts) - It enables receiving information by subscribing to desired broadcasts in the Android system. Here, I used Dynamic Broadcast Receiver to subscribe to the channels "ACTION_SCREEN_OFF" and "ACTION_SCREEN_ON," which indicate events related to the device's screen state, allowing me to listen
- [Sensor Manager](https://developer.android.com/guide/topics/sensors/sensors_overview) - They are components that measure motion, orientation, and environmental conditions, providing raw data with high precision and accuracy.
- [Notification](https://developer.android.com/develop/ui/views/notifications) - is a system feature that allows displaying informative messages, alerts, or updates to users.
- [Firebase Crashlytics](https://firebase.google.com/docs/crashlytics?hl=tr) - is a error tracking and reporting tool used to monitor, report, and analyze crashes and errors in your application.
## Dependency graph
<p align="center">
<img src="/previews/dependency.png" width="80%" height="500px"/>
</p>

# License
```xml
Designed and developed by 2023 BahadirKayis

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

