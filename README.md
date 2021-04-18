# Venue Finder Android App

Simple Android application which is build on top of Model-View-Presenter (MVP) architecture, which helps searching for local venues

## Prerequisites

- [JDK 8](https://www.oracle.com/java/technologies/javase-jsp-downloads.html) or later
- [Android Studio](https://developer.android.com/studio)

## Installation instruction

- Clone the repository.
- Open **foursquare.properties** from the project root, add your foursquare client id and client secret to the fields FOURSQUARE_CLIENT_ID and FOURSQUARE_CLIENT_SECRET respectively.
- Open this project with Android Studio.
- Choose your device or an emulator as a target device for running the app, then press "Run" icon.
- The application will be installed and launched on the chosen device.

## Used library

| Syntax                                                       | Version   | Note                                       |
| ------------------------------------------------------------ | --------- | ------------------------------------------ |
| [ Hilt ](https://dagger.dev/hilt/)                           | 2.34-beta | For Dependency Injection (DI)              |
| [ RxJava ](https://github.com/ReactiveX/RxJava)              | 3.0.12    |
| [ RxBinding ](https://github.com/JakeWharton/RxBinding)      | 4.0.0     | RxJava binding APIs for Android UI widgets |
| [ Retrofit ](https://square.github.io/retrofit/)             | 2.9.0     | HTTP client                                |
| [ Lombok ](https://projectlombok.org/)                       | 1.18.20   |                                            |
| [ JUnit ](https://junit.org/junit4/)                         | 4.13.2    | Unit Testing library                       |
| [ JUnitParams ](https://github.com/Pragmatists/JUnitParams/) | 1.1.1     | Parameterized Test support for JUnit 4     |
