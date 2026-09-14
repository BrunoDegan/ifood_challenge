# Intent Security Audit — ifood_challenge

**Skill used:** `android-intent-security`
**Date:** 2026-09-14
**Scope:** `app/src/main/AndroidManifest.xml` component declarations + source handling of `Intent` / `getIntent` / `getParcelableExtra` / `PendingIntent` / receivers / providers / services.

## Summary

No Intent redirection, PendingIntent hijacking, or unauthorized-component-access vulnerabilities found. The app is a single-Activity app with no custom Intent-based attack surface.

## Component inventory

| Component | Declared? | Exported | Notes |
|---|---|---|---|
| Activity | `.base.MainActivity` (only) | `true` | MAIN/LAUNCHER filter only, no data/scheme, no extras read |
| Service | none | — | — |
| BroadcastReceiver | none | — | — |
| ContentProvider | none | — | — |
| PendingIntent | none created | — | — |
| `onNewIntent` | not overridden | — | — |

Current manifest:

```xml
<activity
    android:name=".base.MainActivity"
    android:exported="true"
    android:label="@string/app_name"
    android:theme="@style/Theme.AppCompat.NoActionBar">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

- `exported="true"` is correct/required here — it's the LAUNCHER activity, declares no other action/data filters, so it can't be driven by arbitrary implicit intents from other apps.
- `MainActivity.kt` never calls `getIntent()`, reads no extras, forwards nothing to `startActivity`. No redirection surface exists.
- Codebase-wide grep for `Intent|PendingIntent|onNewIntent|BroadcastReceiver|ContentProvider|getParcelableExtra|resolveActivity|IntentSanitizer` across all `.kt` sources: zero matches.
- Navigation layer (`AppNavHost`, Navigation3): no `deepLink`/`NavDeepLink`/URI scheme usage.

## Antipattern checklist (per skill)

| Antipattern | Present? |
|---|---|
| Launching nested Intent from untrusted extra without validation | No — no nested intents exist |
| Sticky broadcasts | No |
| Exported component assumed safe due to background thread | No exported components beyond LAUNCHER activity |
| Sensitive functionality without signature permission | N/A — no sensitive IPC surface |
| `onNewIntent` skipping validation | N/A — not overridden |
| Mutable `PendingIntent` without explicit component | N/A — no PendingIntents created |
| String-concatenated ContentProvider selection | N/A — no ContentProviders |
| `Binder.getCallingUid()` misused in `onReceive` | N/A — no BroadcastReceivers |

## Best-practice checklist (per skill)

| MUST | Status |
|---|---|
| `exported="false"` on internal-only components | N/A — only component is the required LAUNCHER activity |
| Signature permission on exported components | N/A — no IPC surface |
| Validate intent extras | N/A — no extras read |
| Protected Broadcast for system events | N/A — no receivers |
| `RECEIVER_NOT_EXPORTED` / signature perms on custom receivers | N/A |
| `setIntent(newIntent)` in `onNewIntent` | N/A — not overridden |
| `FLAG_IMMUTABLE` default on PendingIntent | N/A |
| `readPermission`/`writePermission` on providers | N/A |
| Parameterized ContentProvider queries | N/A |
| Signature check on exported service binds | N/A |
| `IntentSanitizer` on dynamic intents | N/A |

## Resolved since prior audit (2026-09-11)

- `android:debuggable="true"` was hardcoded in the manifest — **removed**; debuggability now controlled per build variant (`isDebuggable` in `build-logic/src/main/kotlin/build.logic.gradle.kts` `buildTypes` block, defaulting to AGP's debug/release behavior). Verified via `assembleDebug`: merged manifest carries `android:debuggable="true"` only in the debug variant.

## Out of scope (noted, not audited here)

- `android:networkSecurityConfig="@xml/network_security_config"` was added to the manifest. Network security config is outside this skill's scope (Intent/IPC security only) — review separately if needed.

## Recommendation

No hardening changes required. Re-run this audit when the project adds:
- A `Service` / `BroadcastReceiver` / `ContentProvider`,
- Deep-link `intent-filter`s (data/scheme) on an Activity,
- Any `PendingIntent` (notifications, alarms, widgets),
- Code that reads a nested `Intent` from extras.

At that point apply the patterns in `android-intent-security` (`IntentSanitizer`, signature permissions, `FLAG_IMMUTABLE` PendingIntents, parameterized ContentProvider queries) proactively rather than retrofitting.
