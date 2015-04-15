# Google Cloud Messaging Android Quickstart

Google Cloud Messaging Android Quickstart app demonstrates registering
an Android app for GCM and handling the receipt of a GCM message.
InstanceID allows easy registration while GcmReceiver and
GcmListenerService provide simple means of receiving and handling
messages.

### Quickstart setup
- Import project from android studio.
- Set SENDER_ID and API_KEY
    - **Option 1**
    Import project configuration file from the [documentation][1]. SENDER_ID
    and API_KEY should be set automatically.
    - **Option 2**
    Setup your project yourself in the [Google Developer Console][2]. Then
    copy and paste the SENDER_ID and API key from your project into the
    placeholders in the quickstart.

### Using the quickstart
- Run sample on your android device or emulator.
- See the generated token in logcat.
- Run the following command from Android Studio terminal:

On Windows:

    .\gradlew.bat run -Pargs="<message>,<token>"

On Linux/Mac:

    ./gradlew run -Pargs="<message>,<token>"

- A notification containing the GCM message should be displayed on the
  device or emulator. There should also be a logcat message containing
  the GCM message.

[1]: https://developers.google.com/gmp/greenhouse
[2]: https://console.developers.google.com

