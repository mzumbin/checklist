package com.nascimento.checklist.shared.models

import play.api.libs.json._

case class Book(name: String)


object Book{
  implicit val formatBook: Format[Book] = Json.format[Book]
}