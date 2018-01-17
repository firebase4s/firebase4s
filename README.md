
<img src="https://github.com/firebase4s/firebase4s/blob/develop/logo/logo.png?raw=true" width=48px align="left"  />
#&nbsp;firebase4s
----------

### Overview

Firebase4s aims to provide a Scala-friendly alternative to the [Firebase Java SDK](https://firebase.google.com/docs/admin/setup).  By providing more idiomatic API interfaces automatically handling the , Firebase4s willproviding more more idiomatic API interfaces and eliminating boilerplate required to convert values automatically handling data conversions to/from Java

----------
<a name="initialize"></a>
## Initialize

Initialize the `App` with your Firebase service account credentials and database url, which may be obtained from the [Firebase console](https://console.firebase.google.com).

```scala
import com.firebase4s.App

val serviceAccount = getClass.getResourceAsStream("/serviceAccountCredentials.json")

App.initialize(serviceAccount, "https://<MY_INSTANCE>.firebaseio.com")
```
*The above code snippet assumes that you Firebase service account credentials exist in your project's `resources` directory.*


<a name="database"></a>
## Realtime Database

To get an instance of a `DatabaseReference`:
```scala
import com.firebase4s.database.Database

val db: Database = Database.getInstance()
val fooRef: DatabaseReference = db.ref("foo")
```
There are four *categories* of supported data types:

##### &nbsp;&nbsp;&nbsp;&nbsp;*Simple Types*:  `String`, `Boolean`, `Int`, `Long`, `Double`
##### &nbsp;&nbsp;&nbsp;&nbsp;*Maps containing Simple Types*: `Map[String, String]`, `Map[String, Boolean]`, etc.
##### &nbsp;&nbsp;&nbsp;&nbsp;*Iterables containing Simple Types*: `List[String]` , `Vector[Long]`, etc.
##### &nbsp;&nbsp;&nbsp;&nbsp;*Classes*

*Currently, only [`JavaBean`](https://en.wikipedia.org/wiki/JavaBeans) classes are supported.  Future versions of Firebase4s will support the use of `case classes` through the use of [Scala Macro Annotations](https://docs.scala-lang.org/overviews/macros/annotations.html).*

#### Examples:

Set and retrieve a `List[Int]`:
```scala
/** Get a database reference */
val listRef: DatabaseReference = db.ref("myList")

/** Set value at reference location */
val result: Future[List[Int]] = liftRef.set(List(1, 2, 3))

/** Get value at ref location */
ref.get()
  .map(snapshot => snapshot.getValue)
  .map((list: Option[List[Int]]) => ???) // handle result


```

Set and retrieve a `User`:

```scala
import scala.beans.BeanProperty

/** JavaBean Class Constructor */
class User() {
  @BeanProperty var name: String = _
  @BeanProperty var email: String = _
}

/** Get a database reference */
val userRef = db.ref(s"user")

/** Create user */
val user = new User()
user.name = "timothy"
user.email = "tim@example.com"

/** Set user at ref location */
userRef.set(user).foreach(println) // User("timothy","tim@example.com")

/** Get user at ref location */
userRef.get()
  .map(snapshot => snapshot.getValue(classOf[User]))
  .map((user: Option[UserRecord]) => ???) // handle result

```
