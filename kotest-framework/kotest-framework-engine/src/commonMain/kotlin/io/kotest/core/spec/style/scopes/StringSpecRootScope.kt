package io.kotest.core.spec.style.scopes

import io.kotest.core.Tag
import io.kotest.core.extensions.TestCaseExtension
import io.kotest.core.names.TestNameBuilder
import io.kotest.core.spec.KotestTestScope
import io.kotest.core.test.EnabledIf
import io.kotest.core.test.EnabledOrReasonIf
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestCaseSeverityLevel
import io.kotest.core.test.TestScope
import io.kotest.datatest.WithDataRegistrar
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * Defines the DSL for creating tests in the 'StringSpec' style.
 *
 * Example:
 * ```
 * "my test" {
 *   1 + 1 shouldBe 2
 * }
 * ```
 */
interface StringSpecRootScope : RootScope, WithDataRegistrar {

   fun String.config(
      enabled: Boolean? = null,
      invocations: Int? = null,
      threads: Int? = null,
      tags: Set<Tag>? = null,
      timeout: Duration? = null,
      extensions: List<TestCaseExtension>? = null,
      enabledIf: EnabledIf? = null,
      invocationTimeout: Duration? = null,
      severity: TestCaseSeverityLevel? = null,
      enabledOrReasonIf: EnabledOrReasonIf? = null,
      coroutineDebugProbes: Boolean? = null,
      blockingTest: Boolean? = null,
      test: suspend TestScope.() -> Unit,
   ) {
      RootTestWithConfigBuilder(
         context = this@StringSpecRootScope,
         name = TestNameBuilder.builder(this).build(),
         xdisabled = false
      ).config(
         enabled = enabled,
         invocations = invocations,
         tags = tags,
         timeout = timeout,
         extensions = extensions,
         enabledIf = enabledIf,
         invocationTimeout = invocationTimeout,
         severity = severity,
         enabledOrReasonIf = enabledOrReasonIf,
         coroutineDebugProbes = coroutineDebugProbes,
         blockingTest = blockingTest,
         test = test
      )
   }

   /**
    * Adds a String Spec test using the default test case config.
    */
   operator fun String.invoke(test: suspend StringSpecScope.() -> Unit) {
      addTest(
         testName = TestNameBuilder.builder(this).build(),
         disabled = false,
         config = null
      ) {
         StringSpecScope(this.coroutineContext, testCase).test()
      }
   }

   override fun registerWithDataTest(
      name: String,
      test: suspend TestScope.() -> Unit
   ) {
      name.invoke { test() }
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


/**
 * This scope exists purely to stop nested string scopes.
 */
@KotestTestScope
class StringSpecScope(
   override val coroutineContext: CoroutineContext,
   override val testCase: TestCase,
) : TerminalScope(), WithDataRegistrar {

   @Deprecated(
      WithDataRegistrar.NO_NESTED_WITH_DATA,
      level = DeprecationLevel.ERROR
   )
   override fun registerWithDataTest(
      name: String,
      test: suspend TestScope.() -> Unit
   ): Nothing = error(WithDataRegistrar.NO_NESTED_WITH_DATA)

   @Deprecated(
      WithDataRegistrar.NO_NESTED_WITH_DATA,
      level = DeprecationLevel.ERROR
   )
   override suspend fun registerSuspendWithDataTest(
      name: String,
      test: suspend TestScope.() -> Unit
   ): Nothing = error(WithDataRegistrar.NO_NESTED_WITH_DATA)
}
