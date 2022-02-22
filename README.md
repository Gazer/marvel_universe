An Android app that uses Marvel APIs to browse on MCU characters

---
>Made with 💙 by Ricardo Markiewicz // [@gazeria](https://twitter.com/gazeria).

## Features

- Pagination support
- Browser and Search characters by name
- App state-preservation/restoration when the screen rotate
- Phone and Table layouts
- Browse character details (comics, events, etc)

## Screenshots

### Phone Portrait

<img src="/screenshots/Screenshot_20220222_165512.png?raw=true" width="200" /> <img src="/screenshots/Screenshot_20220222_165528.png?raw=true" width="200" /> <img src="/screenshots/Screenshot_20220222_165540.png?raw=true" width="200" />

### Phone Landscape

<img src="/screenshots/Screenshot_20220222_165600.png?raw=true" width="300" /> <img src="/screenshots/Screenshot_20220222_165620.png?raw=true" width="300" />

### Table Landscape

<img src="/screenshots/Screenshot_20220222_171016.png?raw=true" width="300" /> <img src="/screenshots/Screenshot_20220222_165806.png?raw=true" width="300" />

## Dependencies

* 100% Kotlin
* MVVM Design Pattern
* Retrofit for HTTP request
* Paging3 for infinite list loading
* ViewModel, LiveData and stuff
* Coroutines for background work
* Picasso for image loading
* Hilt for Dependency Injection
* Jetpack Navigation for navigation
* JUnit for unit testing
* Espresso for Integration/UI testing

## Marvel API KEY

In order to use this app you need to register on the Marvel Developer portal and create an api key.

If you already have an account you can check you api key and secret at https://developer.marvel.com/account.

Once you have both values, create a file called apikey.properties in the root directory with:

```
API_KEY="your api key"
API_SECRET="your api secret"
```

## Design Choises

* The base design was inspired by https://dribbble.com/shots/4985712-Marvel-Heroes-Library
* UI was not very well planned so it may look weird, this can be avoided hiring a designer :)

## TODOs

The app is not perfect and can be improved:

* Room for Database as local cache for Offline support
* Move strings from Layout to string.xml for localiztion
* Use transition shared elements to improve expirience when transition to characater details
* Add more tests :)
* Code organization probable can be improved too. The current organization may not scale well becuse we do not have a strong convention for package names.

## Useful links

* https://proandroiddev.com/our-safe-approach-to-android-jetpack-navigation-in-a-multi-modular-app-fae7bf025423
* https://undraw.co/