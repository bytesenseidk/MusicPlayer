<<<<<<<< HEAD:app/src/androidTest/java/io/github/larsrosenkilde/musicplayer/ExampleInstrumentedTest.kt
package io.github.larsrosenkilde.musicplayer
========
package io.github.larsrosenkilde.kotlinmusic
>>>>>>>> origin/master:app/src/androidTest/java/io/github/larsrosenkilde/kotlinmusic/ExampleInstrumentedTest.kt

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
<<<<<<<< HEAD:app/src/androidTest/java/io/github/larsrosenkilde/musicplayer/ExampleInstrumentedTest.kt
        assertEquals("io.github.larsrosenkilde.musicplayer", appContext.packageName)
========
        assertEquals("io.github.larsrosenkilde.kotlinmusic", appContext.packageName)
>>>>>>>> origin/master:app/src/androidTest/java/io/github/larsrosenkilde/kotlinmusic/ExampleInstrumentedTest.kt
    }
}