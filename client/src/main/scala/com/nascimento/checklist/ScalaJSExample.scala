package com.nascimento.checklist

import com.nascimento.checklist.shared.SharedMessages
import org.scalajs.dom

object ScalaJSExample {

  def main(args: Array[String]): Unit = {
    dom.document.getElementById("scalajsShoutOut").innerHTML = SharedMessages.itWorks
  }
}
