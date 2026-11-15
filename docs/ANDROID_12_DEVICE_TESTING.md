# Android 12 validation checklist

This checklist is for validating SKELETON on the owner's Samsung Galaxy A31 running Android 12.

## Before installing

1. Keep the currently installed app until you know whether it contains data you need.
2. Export or otherwise back up any important Vault content and progress first.
3. The GitHub Actions debug workflow currently creates a fresh debug keystore for each run. APKs from different runs may therefore have different signing identities and may not install as in-place updates.
4. Do not uninstall an existing installation until its data is backed up and the owner has explicitly chosen to proceed.

## Downloading the test APK

1. Open the GitHub Actions run linked from the development branch.
2. Wait until the Android build, unit tests, emulator startup test, and artifact upload have completed successfully.
3. Open the run's Artifacts section.
4. Download `skeleton-debug-apk` and extract the APK.

## First-launch checks

- App opens without an immediate crash.
- Home feed appears and can scroll vertically.
- CORE, Vault, and drawer open.
- The app can start without Firebase project configuration.
- No paid AI, grounding, image, or video request is sent.
- Language can switch to Sinhala and back.
- THE ARENA opens, accepts an answer, shows feedback, and advances.
- FluentPath opens, reads lesson content, and stores progress.
- Denying camera or microphone permission does not crash the app.
- Returning from another app does not lose local progress.

## If startup fails

Capture the crash immediately:

1. Open Android Settings → Apps → SKELETON.
2. Note the app version and Android version.
3. If USB debugging and a computer are available, run:

```bash
adb logcat -c
adb shell monkey -p com.aistudio.skeleton.vzkrpx 1
adb logcat -d -v threadtime > skeleton-crash-log.txt
```

4. Share the `FATAL EXCEPTION` section and the first relevant `Caused by` section. Redact personal information and tokens.

Do not guess at the crash cause without a stack trace.
