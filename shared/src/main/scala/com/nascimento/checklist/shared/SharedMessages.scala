package com.nascimento.checklist.shared

import com.nascimento.checklist.shared.models.{Document, Radio, RadioList, TextField}
import play.api.libs.json._
object SharedMessages {



  case class Hi(msg: String)

  implicit val hiFormat = Json.format[Hi]

  val document = Document("1","me",
    Seq(
      TextField(value = "Hello", required = true, label = "Nome"),
      RadioList("radio list",Seq(Radio(label = "sim", selected = true), Radio("Não", false)))
    )
  )
  val result = Json.toJson(document)
  val doc = result.as[Document]
  val validResult = Document.validateDocument(doc)
  val resultRender = Document.renderDocument(doc).toString()

  def itWorks = resultRender



}
