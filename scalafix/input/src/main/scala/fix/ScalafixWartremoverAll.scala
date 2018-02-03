/*
rules = [
  Disable
  NoInfer
  DisableSyntax
  MissingFinal  // Wartremover.FinalCaseClass, Wartremover.LeakingSealed
]

Disable.symbols = [
  "scala.Any.asInstanceOf" // Wartremover.AsInstanceOf

  "scala.Any.isInstanceOf" // Wartremover.IsInstanceOf

  "scala.util.Either.LeftProjection.get"  // Wartremover.EitherProjectionPartial
  "scala.util.Either.RightProjection.get" // Wartremover.EitherProjectionPartial

  "scala.Option.get" // Wartremover.OptionPartial
  "scala.Some.get"   // Wartremover.OptionPartial
  "scala.None.get"   // Wartremover.OptionPartial

  "scala.util.Try.get"      // Wartremover.TryPartial
  "scala.util.Failure.get"  // Wartremover.TryPartial
  "scala.util.Success.get"  // Wartremover.TryPartial

  "java.lang.Object.toString" // Wartremover.ToString

  "java.lang.Object.equals",   // Wartremover.Equals
  "java.lang.Object#`==`",     // Wartremover.Equals
  "java.lang.Object.eq",       // Wartremover.Equals
  "java.lang.Object.ne"        // Wartremover.Equals

  "scala.Enumeration"          // Wartremover.Enumeration
]

NoInfer.symbols = [
  "scala.AnyVal"               // Wartremover.AnyVal
  "scala.Any"                  // Wartremover.Any
  "java.io.Serializable"       // Wartremover.Serializable
  "scala.Product."             // Wartremover.Product
  "scala.Predef.any2stringadd" // Wartremover.StringPlusAny

  "scala.Option.option2Iterable" // Wartremover.Option2Iterable
]

DisableSyntax.keywords = [
  var    // Wartremover.Var
  null   // Wartremover.Null
  return // Wartremover.Return
  throw  // Wartremover.Throw
  while  // Wartremover.While
]

DisableSyntax.noDefaultArgs = true  // Wartremover.DefaultArguments
DisableSyntax.noFinalVal = true     // Wartremover.FinalVal
DisableSyntax.noImplicitConversion = true // Wartremover.ImplicitConversion
*/
package fix

import scala.util.{ Failure, Success, Try }

object ScalafixWartremoverAll {
  // Unsfase rules

  val x: Any = "123"
  x.asInstanceOf[String]        // assert: Disable.asInstanceOf
  if (x.isInstanceOf[String]) { // assert: Disable.isInstanceOf
    println("don't do this")
  }

  Left("a").left.get  // assert: Disable.get
  Left("a").right.get // assert: Disable.get

  Option(1).get // assert: Disable.get
  Some(1).get   // assert: Disable.get
  None.get      // assert: Disable.get

  Try { 1 }.get                    // assert: Disable.get
  Success(1).get                   // assert: Disable.get
  Failure(new Exception("ok")).get // assert: Disable.get

  var a = 1                 // assert: DisableSyntax.keywords.var
  val n = null              // assert: DisableSyntax.keywords.null
  def foo: Unit = return    // assert: DisableSyntax.keywords.return
  throw new Exception("ok") // assert: DisableSyntax.keywords.throw

  val any = List(1, true, "three")   // assert: NoInfer.any
  val prod = List((1, 2, 3), (1, 2)) // assert: NoInfer.product

  object O extends Serializable
  val mistake = List("foo", "bar", O /* forgot O.toString */) // assert: NoInfer.serializable

  // "foo" + {} // assert: NoInfer.any2stringadd
  {} + "bar"    // assert: NoInfer.any2stringadd

  // Additional rules

  val anyVal = List(1, false, 1.0) // assert: NoInfer.anyval

  def defaultArgs(i: Int = 1, s: String = "string") = ??? // assert: DisableSyntax.defaultArgs

  object WeekDay extends Enumeration { // assert: Disable.Enumeration
    type WeekDay = Value
    val Mon, Tue, Wed, Thu, Fri, Sat, Sun = Value
  }
  
  Array(1, 2, 3).equals(Array(4, 5, 6)) // assert: Disable.equals
  Array(1, 2, 3) == Array(4, 5, 6)      // assert: Disable.==
  Array(1, 2, 3) eq Array(4, 5, 6)      // assert: Disable.eq
  Array(1, 2, 3) ne Array(4, 5, 6)      // assert: Disable.ne

  final case class A(a: Int, s: String)
  A(1, "1").equals(A(2, "2")) // OK: equals is overridden
  A(1, "1") == A(2, "2")      // assert: Disable.==

  sealed trait T
  class D extends T // assert: MissingFinal.class
  trait E extends T // assert: MissingFinal.trait

  implicit def str2int(s: String): Int = ??? // assert: DisableSyntax.implicitConversion
  
  Some(1) zip Some(2) // assert: NoInfer.option2iterable
  
  while (true) { println("at least try FP...") } // assert: DisableSyntax.keywords.while
  
  class F
  new F().toString   // assert: Disable.toString
  case object G
  G.toString 
  1.toString         // ok
  A(1, "1").toString // ok 
}
