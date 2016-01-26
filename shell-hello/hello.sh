#!/bin/sh
exec scala "$0" "$@"
!#

object Hello {
  def main(args: Array[String]): Unit = {
    println("Scala from shell script! " + args.toList)
  }
}

Hello.main(args)
