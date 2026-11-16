package com.example

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Guards the most important regression reported on the owner's Android 12 phone:
 * the app must reach its first screen even when optional Firebase configuration
 * is absent. Cloud services are not required for local startup.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityStartupTest {

  @Test
  fun mainActivityStartsWithoutFirebaseConfiguration() {
    ActivityScenario.launch(MainActivity::class.java).use { scenario ->
      scenario.onActivity { activity ->
        assertFalse("MainActivity unexpectedly finished during startup", activity.isFinishing)
        assertFalse("MainActivity was destroyed during startup", activity.isDestroyed)
      }
    }
  }
}
