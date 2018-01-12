package com.firebase4s.util

import scala.concurrent.{Future, Promise}
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
    }, ExecutionContextExecutorServiceBridge(scala.concurrent.ExecutionContext.global))
    p.future
  }
}

