
import scala.concurrent.ExecutionContext.Implicits.global
import firebase4s.Firebase._

object Main extends App {
  set().foreach(println)
  Thread.sleep(5000)
}
