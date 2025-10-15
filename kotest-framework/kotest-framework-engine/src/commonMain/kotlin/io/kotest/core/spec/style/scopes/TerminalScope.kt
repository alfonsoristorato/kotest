package io.kotest.core.spec.style.scopes

import io.kotest.core.test.NestedTest
import io.kotest.core.test.TestScope
import io.kotest.datatest.WithDataRegistrar

abstract class TerminalScope : TestScope, WithDataRegistrar() {
   override suspend fun registerTestCase(nested: NestedTest) {
      error("Cannot add nested tests at this level")
   }
}
