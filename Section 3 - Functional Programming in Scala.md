# Section 3: Functional Programming in Scala

## What is a Function?

**ALL SCALA FUNCTIONS ARE OBJECTS** (Instances of classes Function1, Function2, etc)

```scala
trait MyFunction[A, B] {
  def apply(element: A): B
}

val doubler = new MyFunction[Int, Int] {
  override def apply(element: Int): Int = element * 2
}

println(doubler(2))
```

There're predefined Functions classes that are always used to define (instantiate) any function. The first areguments
are the function arguments and the last one is the return type.
Can be defined using `Function2[Int, Int, Int]` or with syntactic sugar: `(Int, Int) => Int`

```scala
val adder: ((Int, Int) => Int) = new Function2[Int, Int, Int] {
  override def apply(a: Int, b: Int): Int = a + b
}
```

### Higher order functions

Receive functions as parameters or return other functions as result

```scala
def map[B](transformer: (A => B)): MyList[B]
def flatMap[B](transformer: A => MyList[B]): MyList[B]
def filter(predicate: A => Boolean): MyList[A]
```

### Curried function

Can be called with multiple parameter list

```scala
val superAdder: Function1[Int, Function1[Int, Int]] = new Function1[Int, Function1[Int, Int]] {
  override def apply(x: Int): Function1[Int, Int] = new Function1[Int, Int] {
    override def apply(y: Int): Int = x + y
  }
}

superAdder(3)(2)
```

## Anonymous Functions (Lambdas λ)

A **lambda λ** is a function that is just defined and saved in a **val**.
They just need parameters and the function definition, return type can be inferred.
If a parameter is only used once, can be replace with underscore _

```scala
val adder: ((Int, Int) => Int) = new Function2[Int, Int, Int] {
  override def apply(a: Int, b: Int): Int = a + b
}

// Anonymize
val adderAnonFunc: ((Int, Int) => Int) = (a: Int, b: Int) => a + b
// Syntactic sugar
val adderAnonFuncSugar: ((Int, Int) => Int) =  _ + _
```


## Higher Order Functions (HOFs) and Curries

### Higher Order Functions (HOFs)

Accepts a function as a parameter

```scala
def nTimes(f: Int => Int, n: Int, x: Int): Int = {
  if (n <= 0) x
  else nTimes(f, n-1, f(x))
}
val plusOne = (x: Int) => x + 1

println(nTimes(plusOne, 10, 1))
```

### Curried functions

Functions with multiple parameter lists

```scala
val superAdder: Int => (Int => Int) = (x: Int) => (y: Int) => x + y
superAdder(2)(5) // = 7
```

### HOF + Curried

In standard Format we set the 1st parameter list and save the result function in standardFormat.
So standardFormat is a function that needs a parameter (the 2nd param list of curried Formatter) to be executed

```scala
def curriedFormatter(c: String)(x: Double): String = c.format(x)
val standardFormat: (Double => String) = curriedFormatter("%4.2f")
println(standardFormat(Math.PI))

def curriedAdder(x: Double)(y: Double) = x + y
```

## map, flatMap, filter and for-comprehensions

Some list utilities:


```scala
val list = List(1, 2, 3)
list.head
list.tail

list.map(_ + 1)
list.map(_ + " is a number")

list.filter(_ % 2 == 0) // gets only even numbers f the list
```

flatMap() makes a **map** over the collection and then a **flatten**

```scala
  val numbers = List(1, 2, 3, 4)
  val chars = List('a', 'b', 'c', 'd')
  val animals = List("🐯", "🦄")

  val combinations = numbers.flatMap(number => chars.flatMap(char => animals.map(animal => s"$animal$number$char")))
  // List(🐯1a, 🦄1a, 🐯1b, 🦄1b, 🐯2a, 🦄2a, 🐯2b, 🦄2b)
```

This can be difficult to read, that's why we have the **for-comprehensions**.
Can also use guards to add conditions

```scala
val forCombinations = for {
  number <- numbers if number % 2 == 0 //guard to filter even numbers
  char <- chars
  animal <- animals
} yield s"$animal$number$char"
```

## Sequences: List, Array, Vector

* C: The operation takes (fast) constant time.
* eC: The operation takes effectively constant time, but this might depend on some assumptions such as maximum length of a vector or distribution of hash keys.
* L: The operation is linear, that is it takes time proportional to the collection size.
* -: The operation is not supported.

|Collection	| head | tail | apply | update | prepend | append | insert|
|-----------|------|------|-------|--------|---------|--------|-------|
|List	      |  C	 | C	  | L	    | L	     | C	     | L	    | -     |
|Array	    |  C	 | L	  | C	    | C	     | -	     | -	    | -     |
|Vector	    |  eC  | eC	  | eC  	| eC	   | eC	     | eC	    | -     |

https://docs.scala-lang.org/overviews/collections/performance-characteristics.html

### Seq

General interface
* Well defined order
* Can be indexed

```scala
val aSequence = Seq(1,3,2,4)

// some operations
aSequence.reverse
aSequence(2)
aSequence ++ Seq(7,5,6)
aSequence.sorted

// Ranges
val aRange: Seq[Int] = 1 until 10
aRange.foreach(println)
(1 to 10).foreach(x => println("Hello"))
```

### List

Immutable linked list
* head, tail, isEmpty are fast O(1)
* most operations are O(n)

```scala  
val aList = List(1,2,3)

// some operations
val prepended1 = 42 :: aList
val prependedAndAppend = 42 +: aList :+ 89)

val apples5 = List.fill(5)("apple")
println(aList.mkString("-|-"))
```

### Array

Equivalent of simple Jav array
* Can be manually constructed with predefined lengths
* Can be mutated (updated)
* Are interoperable with Java's arrays
* Indexing is fast

```scala
val numbers = Array(1,2,3,4)

// predefined type and size initializes all the values for primitives or assign null for others
val threeInts = Array.ofDim[Integer](3) // 0, 0, 0
val threeStrings = Array.ofDim[String](3) // null, null, null

// mutation
numbers(2) = 0  // syntax sugar for numbers.update(2, 0)
println(numbers.mkString(" "))

// arrays and seq
val numbersSeq: Seq[Int] = numbers  // implicit conversion
println(numbersSeq)
```

### Vectors

Default implementation for immutable sequences because is very fast
* Almost constant indexed, read and write: O(log32(n))
* Fast element addition: apprend/prepend
* Good performance for large sizes

```scala

val vector: Vector[Int] = Vector(1,2,3)
// Same opearions as other collections

```

## Tuples and Maps

### Tuples

Are finite ordered lists

```scala
val aTuple = (2, "hello, Scala")  // Tuple2[Int, String] = (Int, String)

// operaitons
println(aTuple._1)  // 2
println(aTuple.copy(_2 = "goodbye Java"))
println(aTuple.swap)  // ("hello, Scala", 2)
```

### Maps

```scala
// Maps - keys -> values
val aMap: Map[String, Int] = Map()

val phonebook = Map(("Jim", 555), "Daniel" -> 789, ("JIM", 9000)).withDefaultValue(-1)
// a -> b is sugar for (a, b)

// map ops
phonebook.contains("Jim")
phonebook("Mary")

// add a pairing
val newPairing = "Mary" -> 678
val newPhonebook = phonebook + newPairing

// functionals on maps
// map, flatMap, filter
phonebook.map(pair => pair._1.toLowerCase -> pair._2)

// filterKeys
phonebook.filterKeys(x => x.startsWith("J"))
// mapValues
phonebook.mapValues(number => "0245-" + number)

// conversions to other collections
phonebook.toList // list of pairs
List(("Daniel", 555)).toMap
val names = List("Bob", "James", "Angela", "Mary", "Daniel", "Jim")
names.groupBy(name => name.charAt(0)) // crate a map where the key is the first char and the value the elements that start by the key char
```

## Options

It's a wrapper for a value that might be present or not (Null)

If has a value is ```Some(value)```, if there's no value is ```None```
```scala
val myFirstOption: Option[Int] = Some(5)
val noOption: Option[Int] = None
```

Is very useful working with APIs that might return **nulls**. Never do ```Some(possibleNull)```, wrap it with ```Option(possibleNull)```.

Use ```orElse``` to return something else if a None is detected in first case
```scala
// WORK with unsafe APIs
def unsafeMethod(): String = null
// val result = Some(unsafeMethod()) // WRONG. Some must have a valid value inside, not a null
// Use Option instead
val result = Option(unsafeMethod())
println(result)

def backupMethod(): String = "A valid result"
val chainedResult = Option(unsafeMethod()).orElse(Option(backupMethod()))

// DESIGN unsafe APIs: return Options where a Null could be found
def betterUnsafeMethod(): Option[String] = None
def betterBackupMethod(): Option[String] = Some("A valid result")
val betterChainedResult = betterUnsafeMethod() orElse betterBackupMethod()
```

For extracting the value inside an Option, never perform ```Option(possibleNull).get``` because this can make the wrapped
null appear. Use ```isEmpty``` to check if there's a value and use ```map```, ```filter``` or ```flatMap``` in order to use 
the wrapped values
```scala
// functions on Options
println(myFirstOption.isEmpty) // isEmpty is good way to test if there's a value or not
println(myFirstOption.get) // UNSAFE - DO NOT USE THIS: could make the Null appear

// map, flatMap, filter
println(myFirstOption.map(_ * 2))
println(myFirstOption.filter(x => x > 10))
println(myFirstOption.flatMap(x => Option(x * 10)))
```

Maps has the ```.get``` function that returns an Option with Some(value) if key is found or None otherwise
```scala
myMap.get("key") // same as Option(myMap("key"))
```

Can use chained calls and for-comprehensions to deal with Options:
```scala
val myMap: Map[String, String] = Map(
  // fetched from elsewhere
  "key1" -> "value1",
  "key2" -> "value2"
)

def optionalMethod(x: String, y: String): Option[String] = if (x.size + y.size >= 2) Some(x.concat(y)) else None

// Chained calls
myMap.get("key1")
  .flatMap(value1 => myMap.get("key2")
    .flatMap(value2 => optionalMethod(value1, value2))
    .map(optionalResult => optionalResult.concat(" returned")))
  .foreach(println)
  
// For-comprehensions
val result = for {
  value1 <- myMap.get("key1")
  value2 <- myMap.get("key2")
  optionalResult <- optionalMethod(value1, value2)
} yield optionalResult.concat(" returned")
result.foreach(println)
```

## Handling Failure

If some code is error-prone, use Try to wrap it. This will return Success or Failure which behaves in a similar logic as Option.

```scala
val potentialFailure = Try(unsafeMethod())

val anotherPotentialFailure = Try {
  // code that might throw
}

// Option behavior:
Try(unsafeMethod()).orElse(Try(backupMethod()))
```

You can create methods that come already wrapped into a Try. If something fails will automatically return a Failure(exception), if not a Success(result):
```scala
def betterUnsafeMethod(): Try[String] = Failure(new RuntimeException)
def betterBackupMethod(): Try[String] = Success("A valid result")
betterUnsafeMethod() orElse betterBackupMethod()
```

Working with Try functions. if Success return result, otherwise return empty:
```scala
// normal use
val possibleConnection = HttpService.getSafeConnection(host, port)
val possibleHTML = possibleConnection.flatMap(connection => connection.getSafe("/home"))
println(possibleHTML.foreach(renderHTML))

// shorthand version
HttpService.getSafeConnection(host, port)
  .flatMap(connection => connection.getSafe("/home"))
  .foreach(renderHTML)

// for-comprehension version
for {
  connection <- HttpService.getSafeConnection(host, port)
  html <- connection.getSafe("/home")
} renderHTML(html)
```

Avoid nested try/catch of Java
```java
try {
  connection = HttpService.getConnection(host, port)
  try {
    page = connection.get("/home")
    renderHTML(page)
  } catch (some other exception) {
  }
} catch (exception) {
}
```
