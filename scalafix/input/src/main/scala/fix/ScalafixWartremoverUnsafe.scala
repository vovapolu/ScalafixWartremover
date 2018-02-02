/*
rules = [
  Disable
  NoInfer
  DisableSyntax
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
]

NoInfer.symbols = [
  "scala.Any"                  // Wartremover.Any
  "java.io.Serializable"       // Wartremover.Serializable
  "scala.Product."             // Wartremover.Product
  "scala.Predef.any2stringadd" // Wartremover.StringPlusAny
]

DisableSyntax.keywords = [
  var    // Wartremover.Var
  null   // Wartremover.Null
  return // Wartremover.Return
  throw  // Wartremover.Throw
]

*/
package fix

import scala.util.{ Failure, Success, Try }

object ScalafixWartremover {
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

  Try { 1 }.get  // assert: Disable.get
  Success(1).get // assert: Disable.get
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

}
