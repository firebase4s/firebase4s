package firebase4s

import java.io.InputStream

import com.google.api.core.ApiFuture

import scala.concurrent.{Future, Promise}
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database._

import scala.beans.BeanProperty

object Firebase {

  private val serviceAccount: InputStream =
    getClass.getResourceAsStream("/firebase-service-account-key.json")

  private val options = new FirebaseOptions.Builder()
    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
    .setDatabaseUrl("https://fir-4s.firebaseio.com")
    .build()

  FirebaseApp.initializeApp(options)
  private val db = FirebaseDatabase.getInstance()

  def dbRef(path: String): DatabaseReference = db.getReference(path)

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

  def set(user: User = tim): Future[User] = {
    val p = Promise[User]()
    val ref = dbRef(s"users/${user.name.first}")
    ref.setValue(user, new DatabaseReference.CompletionListener() {
        override def onComplete(databaseError: DatabaseError, databaseReference: DatabaseReference) {
          if (databaseError != null) {
            p.failure(new Exception(databaseError.getMessage))
          } else {
            p.success(user)
          }
        }
      }
    )

    p.future
  }

  def get(): Future[AnyRef] = {
    val p = Promise[AnyRef]()
    val ref = dbRef("things")
    ref.addListenerForSingleValueEvent(new ValueEventListener() {
      override def onDataChange(snapshot: DataSnapshot): Unit = {
        p.success(snapshot.getValue())
      }
      override def onCancelled(databaseError: DatabaseError): Unit = {
        p.failure(new Exception(databaseError.getMessage))
      }
    })
    p.future
  }

}
