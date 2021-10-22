# UserPostsDemoApp

A Demo App to display posts and comments from an API service https://jsonplaceholder.typicode.com/ 

## About
The Demo App is written entirely in Kotlin and requires minimum Android API 23 (6.0).

Developed using Android Studio Arctic Fox | 2020.3.1 Patch 2 with Gradle 7.0.2


## Architecture
The app is implemented using clean architecture with MVVM and coroutines

Data, Domain and App (Presentation) layers each have their own modules with separation of concerns

Domain layer is the inner-most with commonly shared models and interfaces

Data layer has the network, repository and cache classes

The app (presentation) layer has the ViewModels and UI

These Android Architecture components are used - MVVM, LiveData, Room, Navigation, View Binding

## UI/UX

Two screens are implemented, `UserPosts` and `PostComments`

Used a single activity with two fragments. Android Nav for native navigation support using `nav_graph`, easy to pass in arguments such as post id.
Basic transition animations are included.

Swipe refresh layout for pull-to-refresh on Posts screen.

## Caching
The app uses time-expiry (60 seconds) caching for the Posts screen to avoid unnecessary network calls.

## Error Handling
Errors are propagated and handled using `ResultState.Failure(throwable)`.
The UI currently just displays a toast message.

## Dependencies
All dependencies are referenced in common\dependencies.gradle

DI - Koin used due to simplicity.

UI - Android navigation, Swipe/Constraint layouts using XML (faster to integrate than Compose).

Coroutines - Flow used for relative integration simplicity with imperative code. Dispatchers injected where used.

Retrofit/OKHttp - Standard Android network libraries.

Cache - Room DB.

Clock - KotlinX Date/Time to inject Clock for ease of testing

Unit tests - Mockito, Turbine for testing Flow, Coroutines test library.

Note: Although the app doesn't support Kotlin Multiplatform, by using Koin and Coroutines the transition would be smoother. Would also need to replace Retrofit with something like Ktor.

## Unit tests

`ViewModels`, `PostsRepositoryImpl` and `PostsCacheImpl` have unit tests.

## Improvements and Modifications

Expresso UI tests.

Use Jetpack Compose.

Refactoring unit test classes to share common code.

Better UI for errors.

Get user details from end point and use them when displaying comments. Currently only email is used but that email doesn't appear to be referenced in the current user data set.
