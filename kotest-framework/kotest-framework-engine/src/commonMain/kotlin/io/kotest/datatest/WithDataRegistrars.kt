package io.kotest.datatest

import io.kotest.core.spec.style.scopes.ContainerScope
import io.kotest.core.spec.style.scopes.RootScope
import io.kotest.core.test.TestScope
import io.kotest.engine.stable.StableIdents
import kotlin.jvm.JvmName


interface WithDataRegistrar {
   fun registerWithDataTest(name: String, test: suspend TestScope.() -> Unit)
   suspend fun registerSuspendWithDataTest(name: String, test: suspend TestScope.() -> Unit)
   enum class Scope {
      CONTAINER,
      TEST
   }

   companion object {
      const val NO_NESTED_WITH_DATA = "Nested withData is not supported for this spec style."
      const val NO_SUSPEND_WITH_DATA = "Suspend withData is not supported for this spec style."
      const val NO_BLOCKING_WITH_DATA = "Regular (blocking) withData is not supported for this spec style."
   }
}

/**
 * Registers tests at the root level for each element.
 * The test name will be generated from the stable properties of the elements. See [StableIdents].
 */
fun <T> WithDataRegistrar.withData(
   first: T,
   second: T, // we need two elements here so the compiler can disambiguate from the sequence version
   vararg rest: T,
   test: suspend TestScope.(T) -> Unit
) {
   withData(listOf(first, second) + rest, test)
}

/**
 * Registers tests inside the given test context for each element.
 * The test name will be generated from the stable properties of the elements. See [StableIdents].
 */
suspend fun <T> WithDataRegistrar.withData(
   first: T,
   second: T, // we need two elements here so the compiler can disambiguate from the sequence version
   vararg rest: T,
   test: suspend TestScope.(T) -> Unit,
   isSuspend: Boolean = true
) {
   require(isSuspend)
   withData(listOf(first, second) + rest, test, isSuspend = true)
}

///**
// * Disallows to register tests inside the given test context for each element.
// */
//@Deprecated(
//   WithDataTerminalRegistrar.ERROR_MESSAGE,
//   level = DeprecationLevel.ERROR
//)
//fun <T, C : TerminalScope> WithDataTerminalRegistrar<C>.withData(
//   first: T,
//   second: T, // we need two elements here so the compiler can disambiguate from the sequence version
//   vararg rest: T,
//   test: suspend C.(T) -> Unit
//): Nothing = error(WithDataTerminalRegistrar.ERROR_MESSAGE)

/**
 * Registers tests at the root level for each element.
 * The test name will be generated from the given [nameFn] function.
 */
fun <T> WithDataRegistrar.withData(
   nameFn: (T) -> String,
   first: T,
   second: T, // we need two elements here so the compiler can disambiguate from the sequence version
   vararg rest: T,
   test: suspend TestScope.(T) -> Unit
) {
   withData(nameFn, listOf(first, second) + rest, test)
}

/**
 * Registers tests inside the given test context for each element.
 * The test name will be generated from the given [nameFn] function.
 */
suspend fun <T> WithDataRegistrar.withData(
   nameFn: (T) -> String,
   first: T,
   second: T, // we need two elements here so the compiler can disambiguate from the sequence version
   vararg rest: T,
   test: suspend TestScope.(T) -> Unit,
   isSuspend: Boolean = true
) {
   require(isSuspend)
   withData(nameFn, listOf(first, second) + rest, test, isSuspend = true)
}

///**
// * Disallows to register tests inside the given test context for each element.
// */
//@Deprecated(
//   WithDataTerminalRegistrar.ERROR_MESSAGE,
//   level = DeprecationLevel.ERROR
//)
//fun <T, C : TerminalScope> WithDataTerminalRegistrar<C>.withData(
//   nameFn: (T) -> String,
//   first: T,
//   second: T,
//   vararg rest: T,
//   test: suspend C.(T) -> Unit
//): Nothing = error(WithDataTerminalRegistrar.ERROR_MESSAGE)

/**
 * Registers tests at the root level for each element of [ts].
 * The test name will be generated from the stable properties of the elements. See [StableIdents].
 */
fun <T> WithDataRegistrar.withData(
   ts: Sequence<T>,
   test: suspend TestScope.(T) -> Unit
) {
   withData(ts.toList(), test)
}

/**
 * Registers tests inside the given test context for each element of [ts].
 * The test names will be generated from the stable properties of the elements. See [StableIdents].
 */
suspend fun <T> WithDataRegistrar.withData(
   ts: Sequence<T>,
   test: suspend TestScope.(T) -> Unit,
   isSuspend: Boolean = true
) {
   require(isSuspend)
   withData(ts.toList(), test, isSuspend = true)
}

///**
// * Disallows to register tests inside the given test context for each element of [ts].
// */
//@Deprecated(
//   WithDataTerminalRegistrar.ERROR_MESSAGE,
//   level = DeprecationLevel.ERROR
//)
//fun <T, C : TerminalScope> WithDataTerminalRegistrar<C>.withData(
//   ts: Sequence<T>,
//   test: suspend C.(T) -> Unit
//): Nothing = error(WithDataTerminalRegistrar.ERROR_MESSAGE)

/**
 * Registers tests at the root level for each element of [ts].
 * The test name will be generated from the given [nameFn] function.
 */
fun <T> WithDataRegistrar.withData(
   nameFn: (T) -> String,
   ts: Sequence<T>,
   test: suspend TestScope.(T) -> Unit
) {
   withData(nameFn, ts.toList(), test)
}

/**
 * Registers tests inside the given test context for each element of [ts].
 * The test name will be generated from the given [nameFn] function.
 */
suspend fun <T> WithDataRegistrar.withData(
   nameFn: (T) -> String,
   ts: Sequence<T>,
   test: suspend TestScope.(T) -> Unit,
   isSuspend: Boolean = true
) {
   require(isSuspend)
   withData(nameFn, ts.toList(), test, isSuspend = true)
}

///**
// * Disallows to register tests inside the given test context for each element of [ts].
// */
//@Deprecated(
//   WithDataTerminalRegistrar.ERROR_MESSAGE,
//   level = DeprecationLevel.ERROR
//)
//fun <T, C : TerminalScope> WithDataTerminalRegistrar<C>.withData(
//   nameFn: (T) -> String,
//   ts: Sequence<T>,
//   test: suspend C.(T) -> Unit
//): Nothing = error(WithDataTerminalRegistrar.ERROR_MESSAGE)

/**
 * Registers tests at the root level for each element of [ts].
 * The test name will be generated from the stable properties of the elements. See [StableIdents].
 */
fun <T> WithDataRegistrar.withData(
   ts: Iterable<T>,
   test: suspend TestScope.(T) -> Unit
) {
   withData({ StableIdents.getStableIdentifier(it) }, ts, test)
}

//fun <T> TestScope.withData(
//   ts: Iterable<T>,
//   test: suspend TestScope.(T) -> Unit
//) {
//
//}
//
//fun <T> RootScope.withData(
//   ts: Iterable<T>,
//   test: suspend TestScope.(T) -> Unit
//) {
//
//}
//
//fun <T> ContainerScope.withData(
//   ts: Iterable<T>,
//   test: suspend TestScope.(T) -> Unit
//) {
//
//}

/**
 * Registers tests inside the given test context for each element of [ts].
 * The test names will be generated from the stable properties of the elements. See [StableIdents].
 */
suspend fun <T> WithDataRegistrar.withData(
   ts: Iterable<T>,
   test: suspend TestScope.(T) -> Unit,
   isSuspend: Boolean = true
) {
   require(isSuspend)
   withData({ StableIdents.getStableIdentifier(it) }, ts, test, isSuspend = true)
}

///**
// * Disallows to register tests inside the given test context for each element of [ts].
// */
//@Deprecated(
//   WithDataTerminalRegistrar.ERROR_MESSAGE,
//   level = DeprecationLevel.ERROR
//)
//fun <T, C : TerminalScope> WithDataTerminalRegistrar<C>.withData(
//   ts: Iterable<T>,
//   test: suspend C.(T) -> Unit
//): Nothing = error(WithDataTerminalRegistrar.ERROR_MESSAGE)

/**
 * Registers tests at the root level for each element of [ts].
 * The test name will be generated from the given [nameFn] function.
 */
fun <T> WithDataRegistrar.withData(
   nameFn: (T) -> String,
   ts: Iterable<T>,
   test: suspend TestScope.(T) -> Unit
) {
   ts.forEach { t ->
      registerWithDataTest(nameFn(t)) { this.test(t) }
   }
}

/**
 * Registers tests inside the given test context for each element of [ts].
 * The test name will be generated from the given [nameFn] function.
 */
suspend fun <T> WithDataRegistrar.withData(
   nameFn: (T) -> String,
   ts: Iterable<T>,
   test: suspend TestScope.(T) -> Unit,
   isSuspend: Boolean = true
) {
   require(isSuspend)
   ts.forEach { t ->
      registerSuspendWithDataTest(nameFn(t)) { this.test(t) }
   }
}

/**
 * Registers tests at the root level for each tuple of [data], with the first value of the tuple
 * used as the test name, and the second value passed to the test.
 */
@JvmName("withDataMap")
fun <T> WithDataRegistrar.withData(
   data: Map<String, T>,
   test: suspend TestScope.(T) -> Unit
) {
   data.forEach { (name, t) ->
      registerWithDataTest(name) { this.test(t) }
   }
}

/**
 * Registers tests inside the given test context for each tuple of [data], with the first value of the tuple
 * used as the test name, and the second value passed to the test.
 */
@JvmName("withDataMap")
suspend fun <T> WithDataRegistrar.withData(
   data: Map<String, T>,
   test: suspend TestScope.(T) -> Unit,
   isSuspend: Boolean = true
) {
   require(isSuspend)
   data.forEach { (name, t) ->
      registerWithDataTest(name) { this.test(t) }
   }
}
