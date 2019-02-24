package grigoris.anastasios.punk

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import android.R.attr.start
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import android.system.Os.shutdown
import org.junit.After





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
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("grigoris.anastasios.punk", appContext.packageName)
    }

//    private var webServer: MockWebServer? = null
//
//    @Before
//    @Throws(Exception::class)
//    fun setup() {
//
//        webServer = MockWebServer()
//        webServer!!.start(8080)
//
//    }
//
//    @After
//    @Throws(Exception::class)
//    fun tearDown() {
//        webServer!!.shutdown()
//    }


}
