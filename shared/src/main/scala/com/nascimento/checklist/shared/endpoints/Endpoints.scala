package com.nascimento.checklist.shared.endpoints

import com.nascimento.checklist.shared.models.Book
import sttp.tapir.json.play._
import sttp.tapir.generic.auto._
trait Endpoints
object Endpoints extends Endpoints {
  import sttp.tapir._

  private val baseEndpoint = endpoint.errorOut(stringBody).in("books")

  val addBook: Endpoint[Book, String, Unit, Any] = baseEndpoint.post
    .in("add")
    .in(jsonBody[Book])
//    .in(
//      jsonBody[Book]
//        .description("The book to add")
//
//    )

}
