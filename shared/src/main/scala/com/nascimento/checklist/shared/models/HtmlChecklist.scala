package com.nascimento.checklist.shared.models

import com.nascimento.checklist.shared.models.ValidField.validatedField
import play.api.libs.json._
import scalatags.Text
import scalatags.Text.all.{label, _}

sealed trait Field extends Product with Serializable
case class TextField( value: String, required: Boolean, label:String) extends Field
case class Radio(label: String, selected: Boolean) extends Field
case class CheckBox(label: String, selected: Boolean) extends Field
case class RadioList(label: String, radioList: Seq[Radio]) extends Field
case class CheckBoxList(label: String, checkBoxes: Seq[CheckBox]) extends Field
case class Document( ver:String, creator: String, fields: Seq[Field])


trait ValidField[T <: Field] {
  def validate( value:T): Either[String,T]
}

object Document {


  def validateDocument( document:Document): Seq[Either[String, Field]] = {
    document.fields.map(validatedField)
  }

  def renderDocument(document:Document): Text.TypedTag[String] = {
    div( p(document.creator),
      document.fields.map(Render.renderField)
    )
  }
  implicit val deriveField: OFormat[Field] =  Json.format[Field]
  //derived.flat.oformat((__ \ "type").format[String])
  implicit val deriveTextField: OFormat[TextField] =  Json.format[TextField]
  //derived.flat.oformat((__ \ "type").format[String])
  implicit val deriveRadioField: OFormat[Radio] =  Json.format[Radio]
  //derived.flat.oformat((__ \ "type").format[String])
  implicit val deriveRadioCheckBox: OFormat[CheckBox] = Json.format[CheckBox]
  //derived.flat.oformat((__ \ "type").format[String])
  implicit val deriveRadioRadioList: OFormat[RadioList] = Json.format[RadioList]
  //derived.flat.oformat((__ \ "type").format[String])
  implicit val deriveCheckList: OFormat[CheckBoxList] =  Json.format[CheckBoxList]
  //derived.flat.oformat((__ \ "type").format[String])

  implicit val formatDocument = Json.format[Document]
}

object ValidField {

  def validateField[ T <: Field](field: T)(implicit validField: ValidField[T]): Either[String, T] = {
    validField.validate(field)
  }

  def validatedField(field: Field): Either[String, Field] = {
    field match {
      case textField:TextField => validateField(textField)
      case radio:Radio => validateField(radio)
      case check:CheckBox => validateField(check)
      case radioList:RadioList => validateField(radioList)
      case checkBoxList: CheckBoxList => validateField(checkBoxList)
    }
  }
  implicit val validatedTextField:ValidField[TextField] = textField => {
    if (textField.required && textField.value.isEmpty) Left("text field is required")
    else Right(textField)
  }

  implicit val validatedRadio:ValidField[Radio] = radio => Right(radio)
  implicit val validatedCheckBox:ValidField[CheckBox] = checkBox => Right(checkBox)
  implicit val validatedRadioList:ValidField[RadioList] = radioList => {
    if(radioList.radioList.count(_.selected) == 1)
      Right(radioList)
    else
      Left("only one option should be selected")
  }
  implicit val validatedCheckBoxList: ValidField[CheckBoxList] = checkBoxList => Right(checkBoxList)


}

trait Render[-T] {
  def render( value:T): Text.TypedTag[String]
}

object Render {
  def render[T](field: T)(implicit render: Render[T]): Text.TypedTag[String] ={
    render.render(field)
  }
  def renderField(field: Field): Text.TypedTag[String] = {
    field match {
      case textField: TextField => render(textField)
      case radio:Radio => render(radio)
      case checkBox:CheckBox => render(checkBox)
      case renderRadioList: RadioList => render(renderRadioList)
      case checkBoxList: CheckBoxList => render(checkBoxList)
    }
  }
  implicit val renderTextField: Render[TextField] = textField => input(`type` := "text", placeholder := textField.label, readonly)
  implicit val renderCheckBox: Render[CheckBox] = checkBox => {
    val isChecked = if (checkBox.selected)Some(checked) else None
    div(
      input( `type`:= "checkbox", isChecked, readonly),
      label(`for` := checkBox.label )(checkBox.label)
    )
  }
  implicit val renderCheckBoxList: Render[CheckBoxList] = checkBoxList => {
    div(
      p(checkBoxList.label),
      render(checkBoxList.checkBoxes)
    )
  }
  implicit val renderRadio: Render[Radio] = radio => {
    val isChecked = if (radio.selected) Some(checked) else None
    div(
      input( `type` := "radio", isChecked, readonly),
      label(`for` := radio.label )(radio.label),
      br
    )
  }
  import scala.language.implicitConversions
  implicit def listRender[T](implicit render: Render[T] ): Render[Seq[T]] = {
    fields => p(fields.map(render.render))
  }
  implicit val renderRadioList: Render[RadioList] = (radioList)=> {
    div(
      p(radioList.label),
      render(radioList.radioList)
    )
  }

}

object Test {

  def mainn(args: Array[String]): Unit = {
    val document = Document("1","me",
      Seq(
        TextField(value = "", required = true, label = "Nome"),
        RadioList("label",Seq(Radio(label = "sim?", selected = true)))
      )
    )
    val result = Json.toJson(document)
    val doc = result.as[Document]
    val validresult = Document.validateDocument(doc)
    val resulRender = Document.renderDocument(doc).toString()
    print("")
  }
}
