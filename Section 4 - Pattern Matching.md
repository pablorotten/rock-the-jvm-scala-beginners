# Section 4: Pattern Matching

## Pattern Matching

* Cases are matched in order
* What if no cases match? MatchError
* Type of the PM expression? unified type of all the types in all the cases
* Wildcard ```case _ =>``` is very important to avoid pattern matching errors
```scala
  case class Person(name: String, age: Int)
  val bob = Person("Bob", 20)

  val greeting = bob match {
    case Person(n, a) if a < 21 => s"Hi, my name is $n and I can't drink in the US"
    case Person(n, a) => s"Hi, my name is $n and I am $a years old"
    case _ => "I don't know who I am"
  }
  println(greeting)
```
Warning:(45, 3) match may not be exhaustive.
It would fail on the following inputs: Animal(), Parrot(_)
  animal match {

Works really well with sealed and case classes. If no Wildcard case is present and any of the case classes extending the
sealed is not in the PM, compiler will show the warning ```match may not be exhaustive```

For instance, the Parrot is not present in the PM and there's no wildcard
```scala
  sealed class Animal
  case class Dog(breed: String) extends Animal
  case class Parrot(greeting: String) extends Animal
  case class Cat(color: String) extends Animal

  val animal: Animal = Dog("Terra Nova")
  animal match {
    case Dog(someBreed) => println(s"Matched a dog of the $someBreed breed")
    case Cat(someColor) => println(s"Matched a dog of the $someColor color")
    case _ => println("")
  }
```

Shows the warning:
```
Warning: match may not be exhaustive.
It would fail on the following inputs: Animal(), Parrot(_)
  animal match {
```
