### IMPORTANT - This library is now obsolete, there is a 2.0 version written in kotlin. 

### Please migrate to [DroidSpeech2.0](https://github.com/vikramezhil/DroidSpeech2.0)

# DroidSpeech

Android library for continuous speech recognition with localizations.

<b>Supports from Android SDK version 16 and above</b>.

<b><h1>About</h1></b>
Google's default speech recognition library doesn't allow to continuously listen to users voice and a manual stop and start mechanism is involved to use the speech recognition again. This proved to be a downfall for third party developers to optimise the user experience of having continuous speech recognition after each speech result. Adding to this the speech recognition server throws up an error when called upon frequently thus preventing an error free experience to the end user. 

<b>Droid Speech</b> aims to close this gap and provide unparalleled optimisation of continuous speech recognition without any of the above said issues. It is developed keeping in mind all the loopholes which needs to be blocked to have the speech recognition run seamlessly in an android device.

<p align="center">
  <img src="https://user-images.githubusercontent.com/12429051/32413548-67f1805a-c239-11e7-8232-dd0a24e5c491.png" width="200"/>
    <img src="https://user-images.githubusercontent.com/12429051/32413549-68210c58-c239-11e7-96ec-62be7a944eba.png" width="200"/>
  <img src="https://user-images.githubusercontent.com/12429051/32413547-67beb4d6-c239-11e7-861f-a3808bd2c1e7.png" width="200"/>
  <img src="https://user-images.githubusercontent.com/12429051/32413550-684f8b46-c239-11e7-89d5-b4ab4905b369.png" width="200"/>
</p>

<b><h1>Usage</h1></b>
<b>Gradle dependency:</b>

Add the following to your project level build.gradle:

```java
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Add this to your app build.gradle:

```java
dependencies {
    compile 'com.github.vikramezhil:DroidSpeech:v2.0.3’
}
```

<b>Maven:</b>

Add the following to the <repositories> section of your pom.xml:

```xml
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>
```

Add the following to the <dependencies> section of your pom.xml:

```xml
<dependency>
    <groupId>com.github.vikramezhil</groupId>
    <artifactId>DroidSpeech</artifactId>
    <version>v2.0.3</version>
</dependency>
```

<b><h1>Documentation</h1></b>

For a detailed documentation 📔, please have a look at the [Wiki](https://github.com/vikramezhil/DroidSpeech/wiki).

In your activity, initialize the droid speech class and set the droid speech listener

```java
DroidSpeech droidSpeech = new DroidSpeech(this, null);
droidSpeech.setOnDroidSpeechListener(this);
```
To start droid speech to listen to user voice call the method `startDroidSpeechRecognition()`,

```java
droidSpeech.startDroidSpeechRecognition();
```
To close droid speech operations call the method `closeDroidSpeechOperations()`,

```java
droidSpeech.closeDroidSpeechOperations();
```

The speech result will be triggered at `onDroidSpeechFinalResult`,

```java
@Override
public void onDroidSpeechFinalResult(String finalSpeechResult)
{
  // Do whatever you want with the speech result
}
```

<b><h1>License</h1></b>

Copyright 2017 Vikram Ezhil

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
