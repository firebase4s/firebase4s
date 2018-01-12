package com.firebase4s.util

import scala.concurrent.{Future, Promise}
import java.util.concurrent.Executors
import com.google.api.core.ApiFuture

object FutureConverters {

  def scalaFutureFromApiFuture[A](future: ApiFuture[A]): Future[A] = {
    val p = Promise[A]
    future.addListener(() => {
      try {
        p.success(future.get)
      } catch {
        case e: Exception => p.failure(e)
      }
    }, Executors.newFixedThreadPool(1))
    p.future
  }
}

