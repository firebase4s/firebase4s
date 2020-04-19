package com.firebase4s.util

import scala.concurrent.{Future, Promise}
import com.google.api.core.ApiFuture

object FutureConverters {
  private[firebase4s]def scalaFutureFromApiFuture[A](future: ApiFuture[A]): Future[A] = {
    val p = Promise[A]
    future.addListener(
      new Runnable {
        override def run(): Unit = try {
          p.success(future.get)
        } catch {
          case e: Exception => p.failure(e)
        }
      },
      ExecutionContextExecutorServiceBridge(scala.concurrent.ExecutionContext.global)
    )
    p.future
  }
}
