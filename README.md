# ScalafixWartremover
Demonstration of [wartremover](http://www.wartremover.org/doc/warts.html) porting to [scalafix](https://scalacenter.github.io/scalafix/docs/users/rules). 

This project shows the simple mapping from wartremover to scalafix rules. Not all the rules are suported now, but almost all Unsafe rules and significant subset of all wartremover rules are here. 
See [Unsafe test](https://github.com/vovapolu/ScalafixWartremover/blob/master/scalafix/input/src/main/scala/fix/ScalafixWartremoverUnsafe.scala) and
[All test](https://github.com/vovapolu/ScalafixWartremover/blob/master/scalafix/input/src/main/scala/fix/ScalafixWartremoverAll.scala) for actual examples of respective configs. 
