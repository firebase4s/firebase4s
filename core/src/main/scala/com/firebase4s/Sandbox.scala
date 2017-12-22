import java.io.InputStream

import scala.concurrent.ExecutionContext.Implicits.global
import com.firebase4s.App
import com.firebase4s.database.{DataSnapshot, Database, DatabaseReference}
import scala.concurrent.Future
//import com.google.firebase.database._
//import macros.ToStringObfuscate/

import scala.beans.BeanProperty

object User {
  class Name() {
    @BeanProperty var first: String = _
    @BeanProperty var last: String = _
    @BeanProperty var middle: String = _
  }

  class User() {
    @BeanProperty var name: Name = _
    @BeanProperty var email: String = _
  }

  val timsName = new Name()
  timsName.first = "timothy"
  timsName.middle = "d"
  timsName.last = "pike"
  val tim = new User()
  tim.name = timsName
  tim.email = "tim@timtime.com"

  val jessicasName = new Name()
  jessicasName.first = "jessica"
  jessicasName.last = "livingston"
  jessicasName.middle = "umm"

  val jessica = new User()
  jessica.name = jessicasName
  jessica.email = "jess@jgmail.com"

  def getTim(): User = tim
  def getJessica(): User = jessica
}

object Sandbox {

  def run(): Unit = {
    val serviceAccount: InputStream =
      getClass.getResourceAsStream("/firebase-service-account-key.json")

    App.initialize(serviceAccount, "https://fir-4s.firebaseio.com")
    val db: Database = Database.getInstance()
    val user = User.getTim
    val ref: DatabaseReference = db.ref("users")
    val children: Future[Stream[DataSnapshot[AnyRef]]] = ref.get().map((s: DataSnapshot[AnyRef]) => s.getChildren)
    children.map(_.take(1).toList).map(_.map(_.getValue)).foreach(println)
  }

}
