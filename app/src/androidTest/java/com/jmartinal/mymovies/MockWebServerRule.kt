package com.jmartinal.mymovies

import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.concurrent.thread

class MockWebServerRule : TestRule {
    val server = MockWebServer()

    private fun replaceBaseUrl() {
        val testModule = module {
            single(named("baseUrl"), override = true) { askMockServerUrlOnAnotherThread() }
        }
        loadKoinModules(testModule)
    }

    private fun askMockServerUrlOnAnotherThread(): String {
        /**
         * This can't be done on the main thread, so we need to hack it to prevent the app from
         * crashing. It blocks the app, but it should not affect our tests
         */
        var url = ""
        val t = thread { url = server.url("/").toString() }
        t.join()
        return url
    }

    override fun apply(base: Statement, description: Description) = object : Statement() {
        override fun evaluate() {
            server.start()
            replaceBaseUrl()
            base.evaluate()
            server.shutdown()
        }
    }
}