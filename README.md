# brc-android
Bare-bones remote configs for Android.  Check out the [basic-remote-configs](https://github.com/BradPatras/basic-remote-configs) repo for more context.

## Usage
The usage is pretty straightforward:
1. Create an instance of BasicRemoteConfigs
2. Call `.fetchConfigs()`. (This is a `suspend` function so you'll need to call it in a coroutine scope)
3. Access your configs.
```kotlin
val CONFIG_URL = URL("https://github.com/BradPatras/basic-remote-configs/raw/main/examples/simple.json")

// #1
val brc = BasicRemoteConfigs(configUrl)

// ...

lifecycleScope { 
  brc.fetchConfigs() // #2
  val someFlag = brc.getBoolean("someFlag") // #3
}
```

## Error handling
The call to `.fetchConfigs()` makes a network request and does some deserialization, so it's bound to fail at some point. BasicRemoteConfigs will log errors under the key "BasicRemoteConfigs" using `Log.e` with a hint as to where the error happened in regards to fetching configs. It won't do any handling or masking of the exceptions so you need to wrap it in a try/catch or use a CoroutineExceptionHandler yourself.
```kotlin
try {
  brc.fetchConfigs()
} catch (exception: Exception) {
  // What happens here is up to you
}
```

## Important:
- BasicRemoteConfigs is not a global/static resource out of the box. It makes no assumptions about scope or lifecycle, so you are in charge of keeping an instance of the class alive/stored somewhere.
