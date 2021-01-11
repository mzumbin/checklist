package com.nascimento.checklist.services

import akka.actor.ActorSystem
import com.google.inject.Inject
import com.nascimento.checklist.shared.models.Book
import com.nascimento.checklist.shared.endpoints.Endpoints
import sttp.tapir._
import sttp.tapir.server.play._

import scala.concurrent.{ExecutionContext, Future}

class BookServices @Inject ()(implicit executionContext: ExecutionContext, system: ActorSystem) {


  def addBook( book: Book): Future[Right[String, Unit]] = Future {
    Right(println(book))
  }



   val bookRoute = Endpoints.addBook.toRoute(addBook)
}
