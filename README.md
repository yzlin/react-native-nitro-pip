# react-native-nitro-pip

Add picture-in-picture support to React Native for Android app

## Installation

```sh
npm install react-native-nitro-pip react-native-nitro-modules
```

> `react-native-nitro-modules` is required as this library relies on [Nitro Modules](https://nitro.margelo.com/).

## Usage

```tsx
import { enterPiP, isPiPSupported, isInPiP } from 'react-native-nitro-pip';

// Check if PiP is supported on the current device
if (isPiPSupported()) {
  // Enter PiP mode
  const success = enterPiP({
    aspectRatio: { width: 16, height: 9 },
  });
}

// Check if the app is currently in PiP mode
const inPiP = isInPiP();
```

## Android Requirements

To use Picture-in-Picture on Android, you must update your `AndroidManifest.xml`. The activity that enters PiP (usually `MainActivity`) requires the following attributes:

```xml
<activity
  android:name=".MainActivity"
  android:supportsPictureInPicture="true"
  android:configChanges="screenSize|smallestScreenSize|screenLayout|orientation"
  ... />
```

> **Note:** `enterPiP` must be called while the activity is in the foreground.

## Expo Usage

If you are using Expo, add the plugin to your `app.json` or `app.config.js`:

```json
{
  "expo": {
    "plugins": ["../app.plugin.js"]
  }
}
```

## Breaking Changes

- `multiply` has been removed. This library now focuses exclusively on Picture-in-Picture support.

## Contributing

- [Development workflow](CONTRIBUTING.md#development-workflow)
- [Sending a pull request](CONTRIBUTING.md#sending-a-pull-request)
- [Code of conduct](CODE_OF_CONDUCT.md)

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
