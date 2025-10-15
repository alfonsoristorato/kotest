package io.kotest.core.spec.style.scopes

import io.kotest.core.names.TestNameBuilder
import io.kotest.core.test.TestScope
import io.kotest.datatest.WithDataRegistrar

/**
 * A context that allows tests to be registered using the syntax:
 *
 * ```
 * given("some test")
 * xgiven("some disabled test")
 * ```
 */
interface BehaviorSpecRootScope : RootScope, WithDataRegistrar {

   /**
    * Adds a top level [BehaviorSpecGivenContainerScope] to this spec.
    */
   @Suppress("FunctionName")
   fun Given(name: String, test: suspend BehaviorSpecGivenContainerScope.() -> Unit) = addGiven(name, false, test)

   /**
    * Adds a top level [BehaviorSpecGivenContainerScope] to this spec.
    */
   fun given(name: String, test: suspend BehaviorSpecGivenContainerScope.() -> Unit) = addGiven(name, false, test)

   /**
    * Adds a top level disabled [BehaviorSpecGivenContainerScope] to this spec.
    */
   fun xgiven(name: String, test: suspend BehaviorSpecGivenContainerScope.() -> Unit) = addGiven(name, true, test)

   /**
    * Adds a top level disabled [BehaviorSpecGivenContainerScope] to this spec.
    */
   fun xGiven(name: String, test: suspend BehaviorSpecGivenContainerScope.() -> Unit) = addGiven(name, true, test)

   fun addGiven(name: String, xdisabled: Boolean, test: suspend BehaviorSpecGivenContainerScope.() -> Unit) {
      addContainer(
         testName = TestNameBuilder.builder(name).withPrefix("Given: ").withDefaultAffixes().build(),
         disabled = xdisabled,
         config = null
      ) { BehaviorSpecGivenContainerScope(this).test() }
   }

   /**
    * Adds a top level [BehaviorSpecContextContainerScope] to this spec.
    */
   @Suppress("FunctionName")
   fun Context(name: String, test: suspend BehaviorSpecContextContainerScope.() -> Unit) =
      addContext(name = name, xdisabled = false, test = test)

   /**
    * Adds a top level [BehaviorSpecContextContainerScope] to this spec.
    */
   fun context(name: String, test: suspend BehaviorSpecContextContainerScope.() -> Unit) =
      addContext(name = name, xdisabled = false, test = test)

   /**
    * Adds a top level disabled [BehaviorSpecContextContainerScope] to this spec.
    */
   fun xcontext(name: String, test: suspend BehaviorSpecContextContainerScope.() -> Unit) =
      addContext(name = name, xdisabled = true, test = test)

   /**
    * Adds a top level disabled [BehaviorSpecContextContainerScope] to this spec.
    */
   fun xContext(name: String, test: suspend BehaviorSpecContextContainerScope.() -> Unit) =
      addContext(name = name, xdisabled = true, test = test)

   fun addContext(name: String, xdisabled: Boolean, test: suspend BehaviorSpecContextContainerScope.() -> Unit) {
      addContainer(
         testName = TestNameBuilder.builder(name).withPrefix("Context: ").withDefaultAffixes().build(),
         disabled = xdisabled,
         config = null
      ) { BehaviorSpecContextContainerScope(this).test() }
   }

   override fun registerWithDataTest(
      name: String,
      test: suspend TestScope.() -> Unit
   ) {
      context(name) { test() }
   }

   @Deprecated(
      WithDataRegistrar.NO_SUSPEND_WITH_DATA,
      level = DeprecationLevel.ERROR
   )
   override suspend fun registerSuspendWithDataTest(
      name: String,
      test: suspend TestScope.() -> Unit
   ): Nothing = error(WithDataRegistrar.NO_SUSPEND_WITH_DATA)
}
