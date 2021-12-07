# brc-android
Bare-bones remote configs for Android.  Check out the [basic-remote-configs](https://github.com/BradPatras/basic-remote-configs) repo for more context.

🚧 &nbsp; Under active development &nbsp; 🚧

## Usage
The usage is pretty straightforward:
1. Create an instance of BasicRemoteConfigs
2. Call `.fetchConfigs()`. (This is a `suspend` function so you'll need to call it in a coroutine scope)
3. Access your configs.
```kotlin
val configUrl = URL("https://github.com/BradPatras/basic-remote-configs/raw/main/examples/simple.json")

// #1
val brc = BasicRemoteConfigs(configUrl)

// ...

lifecycleScope { 
  brc.fetchConfigs() // #2
  val someFlag = brc.getBoolean("someFlag") // #3
}
```

## Caching
Configs are stored locally in the app's private storage once they've been fetched from the network.  Calling `.fetchConfigs()` will fetch the locally cached version of configs if either of the following are true:
1. The cache exists and is not expired (cache expires after one day)
2. The call to fetch configs from the network failed for any reason.


If you'd like to bypass the cached version and fetch the latest configs from the network, there's an optional param available `.fetchConfigs(ignoreCache: Boolean)`

## Error handling
The call to `.fetchConfigs()` map make a network request and do some deserialization, so it's bound to fail at some point. BasicRemoteConfigs will log errors under the key "BasicRemoteConfigs" using `Log.e` with a hint as to where the error happened in regards to fetching configs. It won't do any handling or masking of the exceptions so you need to wrap it in a try/catch or use a CoroutineExceptionHandler yourself.
```kotlin
try {
  brc.fetchConfigs()
} catch (exception: Exception) {
  // What happens here is up to you
}
```

## Important:
- BasicRemoteConfigs is not a global/static resource out of the box. It makes no assumptions about scope or lifecycle, so you are in charge of keeping an instance of the class alive/stored somewhere.
- BasicRemoteConfigs utilizes the [App Startup](https://developer.android.com/topic/libraries/app-startup) Jetpack library in order to access Context (so it can grab the app's file directory path).  Some people may have an issue with this approach, but it seems pretty harmless to me and it looks to be working for the [Realm Kotlin](https://github.com/realm/realm-kotlin) project. 🤷🏻‍♂️ 
