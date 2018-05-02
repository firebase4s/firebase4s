<img src="logo/logo-side-text.png?raw=true" width=400px />

[![Build Status](https://travis-ci.org/firebase4s/firebase4s.svg?branch=master)](https://travis-ci.org/firebase4s/firebase4s)
[![Latest version](https://index.scala-lang.org/firebase4s/firebase4s/firebase4s/latest.svg?color=orange&v=1)](https://index.scala-lang.org/firebase4s/firebase4s/firebase4s)
[![Gitter chat](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/firebase4s/firebase4s)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

### Overview

Firebase4s aims to provide a Scala-friendly alternative to the [Firebase Java SDK](https://github.com/firebase/firebase-admin-java).  By providing more idiomatic API interfaces and automatically handling Scala-to-Java data conversions, Firebase4s eliminates much of the [boilerplate](https://medium.com/@RICEaaron/scala-firebase-da433df93bd2) required to use the Java SDK.  Firebase4s currently provides the same [Realtime Database](#database) and [Authentication](#authentication) features as the Java SDK and aims to provide all of the same [functionality](https://firebase.google.com/docs/admin/setup) over time.


----------
<a name="getting-started"></a>
### Getting Started

To use `firebase4s` in your project, add the following to your `build.sbt`:

```scala
libraryDependencies += "com.github.firebase4s" %% "firebase4s" % "0.0.4"
```



----------
<a name="initialize"></a>
### Initialize

Initialize the `App` with your Firebase service account credentials and database url, which may be obtained from the [Firebase console](https://console.firebase.google.com).

```scala
import com.firebase4s.App

val serviceAccount = getClass.getResourceAsStream("/serviceAccountCredentials.json")

App.initialize(serviceAccount, "https://<MY_INSTANCE>.firebaseio.com")
```
*The above code snippet assumes that your Firebase service account credentials exist in your project's `resources` directory.*


<a name="database"></a>
### Realtime Database

To get an instance of a `DatabaseReference`:
```scala
import com.firebase4s.database.Database

val db: Database = Database.getInstance()
val fooRef: DatabaseReference = db.ref("foo")
```
There are five *categories* of supported data types:

##### &nbsp;&nbsp;&nbsp;&nbsp;*Simple Types*:  `String`, `Boolean`, `Int`, `Long`, `Double`
##### &nbsp;&nbsp;&nbsp;&nbsp;*Maps containing Simple Types*: `Map[String, String]`, `Map[String, Boolean]`, etc.
##### &nbsp;&nbsp;&nbsp;&nbsp;*Iterables containing Simple Types*: `List[String]` , `Vector[Long]`, etc.
##### &nbsp;&nbsp;&nbsp;&nbsp;*Options of any of the above*: `Option[Int]`, `Option[List[String]]`, etc.
##### &nbsp;&nbsp;&nbsp;&nbsp;*Classes*

*Currently, only [`JavaBean`](https://en.wikipedia.org/wiki/JavaBeans) classes are supported.  However, Firebase4s offers a [`toMap`](#toMap) helper method which makes working with non-nested case classes easier.  Future versions of Firebase4s will fully support the use of `case classes` through the use of [Scala Macro Annotations](https://docs.scala-lang.org/overviews/macros/annotations.html). and/or [Shapeless](https://github.com/milessabin/shapeless)*

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
val userRef = db.ref("user")

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
<a name="toMap"></a>
Working with  `case classes`:

```scala
/**
  * The Helpers module provides a `toMap` method which
  * we can invoke on our case class to convert it to a
  * Map[String, Any] that can be set as a value in the
  * in the database.  When retrieving the value as this
  * ref location, we will get back a Map[String, Any]
  * representation of our case class instance.
  */

import com.firebase4s.database.Helpers._

case class User(name: String, age: Int)

val user = User("tim", 44)
val userRef = db.ref("user")

userRef.set(user.toMap)

userRef.get().foreach(snapshot => {
  println(snapshot.getValue) // Map(name -> tim, age -> 44)
})

```


<a name="auth"></a>
### Authentication

##### Create a User:
```scala
import com.firebase4s.{Auth, UserCreationProps}

val auth: Auth = Auth.getInstance()

// User Props
val props = UserCreationProps(
  email = Some("tim@firebase4s.com"),
  displayName = Some("tim"),
  phoneNumber = Some("+1555555555"),
  photoUrl = Some("http://testing.com/photo.jpeg")
)

auth.createUser(props)
  .map((user: UserRecord) => ???) // handle results

```

##### Update a User:
```scala
import com.firebase4s.{Auth, UserUpdateProps}

val auth: Auth = Auth.getInstance()

// Props to update
val props = UserUpdateProps(
  email = Some("timothy@firebase4s.com"),
  phoneNumber = Some("+15655655555")
)

auth.updateUser(props)
  .map((user: UserRecord) => ???) // handle results

```
##### Delete a User:

```scala
auth.deleteUser(user.uid)
  .map(uid => ???) // handle result

```

##### Verify a Firebase ID Token:

```scala
auth.verifyIdToken(token)
  .map((token: FirebaseToken) => token.uid)
  .map(userId => ???)
```
##### Create a Custom Token:
```scala
val extraClaims = Map("admin" -> true)
auth.createCustomToken(uid, Some(extraClaims)))
  .map(token => ???]) // Provide token to client
```
