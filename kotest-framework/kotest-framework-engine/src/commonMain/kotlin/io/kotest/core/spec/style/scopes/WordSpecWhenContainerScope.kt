package io.kotest.core.spec.style.scopes

import io.kotest.core.names.TestNameBuilder
import io.kotest.core.spec.KotestTestScope
import io.kotest.core.test.TestScope
import io.kotest.datatest.WithDataRegistrar

@Suppress("FunctionName")
@KotestTestScope
class WordSpecWhenContainerScope(
   val testScope: TestScope,
) : AbstractContainerScope(testScope) {

   @Suppress("FunctionName")
   suspend infix fun String.When(init: suspend WordSpecWhenContainerScope.() -> Unit) = `when`(this, false, init)
   suspend infix fun String.xWhen(init: suspend WordSpecWhenContainerScope.() -> Unit) = `when`(this, true, init)

   suspend infix fun String.`when`(init: suspend WordSpecWhenContainerScope.() -> Unit) = `when`(this, false, init)
   suspend infix fun String.xwhen(init: suspend WordSpecWhenContainerScope.() -> Unit) = `when`(this, true, init)

   private suspend fun `when`(name: String, xdisabled: Boolean, test: suspend WordSpecWhenContainerScope.() -> Unit) {
      registerContainer(
         name = TestNameBuilder.builder(name).withSuffix(" when").withDefaultAffixes().build(),
         disabled = xdisabled,
         config = null,
      ) { WordSpecWhenContainerScope(this).test() }
   }

   suspend infix fun String.Should(test: suspend WordSpecShouldContainerScope.() -> Unit) {
      should(name = this, test = test, xdisabled = false)
   }

   suspend infix fun String.should(test: suspend WordSpecShouldContainerScope.() -> Unit) {
      should(name = this, test = test, xdisabled = false)
   }

   suspend infix fun String.xshould(test: suspend WordSpecShouldContainerScope.() -> Unit) {
      should(name = this, test = test, xdisabled = true)
   }

   private suspend fun should(
      name: String,
      test: suspend WordSpecShouldContainerScope.() -> Unit,
      xdisabled: Boolean
   ) {
      registerContainer(
         name = TestNameBuilder.builder(name).withSuffix(" should").withDefaultAffixes().build(),
         disabled = xdisabled,
         config = null
      ) { WordSpecShouldContainerScope(this).test() }
   }

   @Deprecated(
      WithDataRegistrar.NO_BLOCKING_WITH_DATA,
      level = DeprecationLevel.ERROR
   )
   override fun registerWithDataTest(
      name: String,
      test: suspend TestScope.() -> Unit
   ): Nothing = error(WithDataRegistrar.NO_BLOCKING_WITH_DATA)

   override suspend fun registerSuspendWithDataTest(
      name: String,
      test: suspend TestScope.() -> Unit
   ) {
      name `when` { test() }
   }
}

