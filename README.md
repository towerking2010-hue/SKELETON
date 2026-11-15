# SKELETON

SKELETON is an offline-first Android learning environment built with Kotlin and Jetpack Compose. It combines a discovery feed, conversational learning tools, a local knowledge vault, spaced repetition, cognitive-skill challenges, language learning, and device-powered multimedia utilities.

## Project status

This repository is under active development. Some screens are prototypes or intentionally disabled. Do not treat a visible screen as proof that the underlying service is available.

- **Zero-billing policy:** cloud inference, search/maps grounding, image generation, video generation, and other paid model requests are disabled in the current build.
- **Local-first:** the learning curriculum, Vault, Arena progress, and several study tools are designed to work on-device.
- **Sinhala:** localization is in progress; not every screen or lesson is fully localized yet.
- **External services:** Firebase authentication and Firestore sync require valid project configuration and provider setup.
- **Device services:** camera, OCR, speech recognition, and text-to-speech depend on Android permissions and installed device services.

## Main features

- **CORE:** conversational learning interface with local fallback content.
- **DEEPDIVE:** contextual exploration of a selected learning card.
- **Discovery Feed:** full-height vertical learning cards across the curriculum.
- **Knowledge Vault:** Room-backed local storage for saved learning cards.
- **Yggdrasil:** visual relationships between knowledge cards.
- **Spaced Repetition:** scheduled recall reviews.
- **THE ARENA:** local cognitive-skills challenges, XP, levels, and skill evidence. It is not an IQ or clinical assessment.
- **FluentPath:** early-stage language-learning tracks for Japanese, German, Ancient Greek, Latin, and Sinhala.
- **Bioscience Lab, Research Desk, OCR, voice, audiobook, and other utilities:** see the implementation and screen-level limitations before relying on a capability.

## Knowledge domains

The curriculum is organized around 19 domains: Physics; Chemistry; Biology; Etymology and Linguistics; Mythology and Comparative Archetypes; Sacred Geometry; Numerology; Gematria; Alchemy; the Trivium; the Quadrivium; Freemasonry and Architectural Allegory; Hermeticism; Metaphysics and Ontology; Music Theory; Colour Theory; Visual Composition; Psychology and Cognitive Science; and Dark Psychology, Manipulation, and Defense.

Scientific evidence must be distinguished from historical, symbolic, philosophical, spiritual, and speculative interpretations.

## Build requirements

- JDK 17
- Android SDK with compile/target API 36
- Gradle wrapper / Gradle 9.3.1 as configured by CI

Run the unit tests:

```bash
./gradlew testDebugUnitTest
```

Build a debug APK:

```bash
./gradlew assembleDebug
```

On Windows, use `gradlew.bat`.

The GitHub Actions workflow at `.github/workflows/android-build.yml` runs unit tests, assembles a side-by-side QA APK (`com.aistudio.skeleton.vzkrpx.qa`), runs an Android 12 startup smoke test, and uploads it as the `skeleton-debug-apk` artifact. Configure the optional `SKELETON_DEBUG_KEYSTORE_BASE64` repository secret to keep the QA signing identity stable across builds.

## Firebase configuration

The app is intended to remain usable locally when Firebase is not configured. Cloud authentication and Firestore features require a correctly configured Firebase project and a `google-services.json` matching the Android application ID:

`com.aistudio.skeleton.vzkrpx`

Never commit private service-account keys, signing credentials, or API secrets. Firebase App Check and Firestore rules must be configured and verified separately before enabling cloud functionality.

## Data safety

Room is the local persistence layer. Schema changes must use explicit, tested migrations. Do not use destructive migration fallbacks or delete user progress during upgrades.

## Development

Active work is tracked in Git branches and pull requests. Review the current source and CI results rather than relying on old screenshots, reports, or feature descriptions. The zero-billing gate in `GeminiCognitiveService.kt` is intentional and must not be bypassed without explicit product-owner approval and a verified no-cost architecture.
