# Android Research and Develop
[![Kotlin Version](https://img.shields.io/badge/Kotlin-1.3.61-blue.svg)](https://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/Gradle-4-blue?style=flat)](https://gradle.org)

## Project characteristics and tech-stack

* Tech-stack
  * [Kotlin](https://kotlinlang.org/) + [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - perform background operations
  * [Retrofit](https://square.github.io/retrofit/) - networking
  * [Jetpack](https://developer.android.com/jetpack)
    * [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) - in-app navigation
    * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - notify views about database change
    * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - perform an action when lifecycle state changes
    * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - store and manage UI-related data in a lifecycle conscious way
    * [Room](https://developer.android.com/jetpack/androidx/releases/room) - store offline cache
* Modern Architecture
  * MVVM
* CI
  * [GitHub Actions](https://github.com/features/actions)
  * Automatic PR verification including tests, linters and 3rd online tools
* CD
  * Firebase Distribution
* Testing
    * [Unit Tests](https://en.wikipedia.org/wiki/Unit_testing) ([JUnit 5](https://junit.org/junit5/) via
      [android-junit5](https://github.com/mannodermaus/android-junit5))
    * [UT Tests](https://en.wikipedia.org/wiki/Graphical_user_interface_testing) ([Espresso](https://developer.android.com/training/testing/espresso))
* Static analysis tools
    * [Ktlint](https://github.com/pinterest/ktlint) - validate code formating
    * [Detekt](https://github.com/arturbosch/detekt#with-gradle) - verify complexity look for and code smell
## License
    