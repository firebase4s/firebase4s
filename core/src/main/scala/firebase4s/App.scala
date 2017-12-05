
import scala.concurrent.ExecutionContext.Implicits.global
import firebase4s.Firebase._
import firebase4s.Testeroo

object Main extends App {
  Testeroo.run()
  set().foreach(println)
  Thread.sleep(5000)
}
