
import scala.concurrent.ExecutionContext.Implicits.global
import com.firebase4s.App



object Main extends App {
  Sandbox.run()
  Thread.sleep(5000)
}
