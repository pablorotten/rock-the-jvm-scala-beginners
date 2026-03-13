# Section 2: Object-Oriented Programming in Scala
## 10. 11. Object-Oriented Basics
### Creating a class
* The constructor **parameters** can be **fields** if they have **val/var**
* Can create a **block {...}** that will be evaluated in every instantiation
* Access to params, fields and methods:
    * Can access to **methods** and **fields** using their names
        * Fields don't need getter/setter methods
        * Methods without parameters can be accessed without parenthesis()
    * Can't access to the **parameters** if they are not **fields**
    * Internally, we can still access to the **parameters** (use **this.parameter** if necessary)
    
```scala
class Class(parameter: String, val field1: Int = 0) {
  val field2 = field1 + 1
  var field3 = parameter
  // this.parameter is not a class field but is still available inside the class
  def method(parameter: String): Unit = println(s"Class parameter: ${this.parameter} Method param: $parameter")
  // this,parameter needs a getter to be accessed.
  // It would be better to just make parameter a field: val parameter
  def getConstructorParam = parameter 
}
// Using the Class
val clazz = new Class("param", 31)
clazz.field1 //accessing to constructor field
clazz.field2 //accessing to class field 
clazz.parameter // Error: parameter is class parameter not field
clazz.getConstructorParam //accessing to class parameter through getter
clazz.method("other param")
```

### Overriding
* Can override methods and the constructor
* The constructor can only be overriden by another constructor, a different code is not allowed

```scala
class Class(parameter: String, val field1: Int = 0) {
  var field2 = parameter
  // Overriding constructor
  def this(otherParameter: String) = this(parameter, 1) // calls main constructor with 'otherParameter' and '1'
  def this() = this("Second constructor") // calls secondary constructor
  
  // Overriding methods
  def method(): Unit = println(s"Class parameter: ${parameter}")
  def method(parameter: String): Unit = println(s"Class parameter: ${this.parameter} Method param: $parameter")
}
  val clazz1 = new Class("Using second constructor")// using second constructor
  val clazz2 = new Class()// using third constructor
  clazz2.method("Using overridden method")
```
### Good practices: Immutability and DRY
* **Immutability** is when, instead of changing the instance, return a new instance with the new values
* **DRY** in this case means, that if you override any method, try to use recursion instead repeating the same as the overridden one

```scala
class Person(val name: String, var age: Int) {
  // Gets 1 year older
  def getOlderMutable = age = age + 1 // Side effect: changes the field 'age' of the Instance
  def getOlder = new Person(name, age + 1) // Immutable: creates a new Instance with the new 'age'
  // Gets 'age' years older
  // DRY: Overrides getOlder to get 'yearsOld' older but using the overridden function => DRY
  def getOlder(yearsOld: Int): Person = {
    if (yearsOld <= 0) this
    // 1st 'getOlder' calls the overridden method and returns 1 years older Person
    // 2nd 'getOlder' makes the recursive call but with 1 year less
    else getOlder.getOlder(yearsOld - 1) 
  }
  // Doesn't respect DRY: Creates a new Person again like the overridden method
  // Note: yearsOld should be Int
  def getOlder(yearsOld: Double): Person = new Person(name, age + yearsOld.toInt)
}
```

## 12. 13. Syntactic Sugar: Method Notations
### "Operators" in Scala
In Scala, we can use use spaces instead of dots and parenthesis for calling fields or methods with 0 or 1 parameters.
```scala
val object = new Class(...)
object field // = object.field
object method // = object.method
object method parameter // = object.method(parameter)
```

#### Infix notation
In the case of methods with 1 parameter, the method acts like an operator because it's between 2 things and yields a 3rd thing.
```scala
val result = object method parameter // = object.method(parameter)
```

In a sum we have thing1 + thing2 = result. So the operator '+' is between 'thing1' and 'thing2' and yields 'resut'
In fact, the operator '+' is and can be used like a method:
```scala
1 + 2
1.+(2)
```
ALL OPERATORS ARE METHODS

We can also define the operator '+' between to objects
```scala
class Person(val name: String, val age: Int) {
  def +(person: Person): String = s"${this.name} is with ${person.name}"
}

val joe = new Person("Joe", 15)
val anne = new Person("Anne", 16)
joe + anne // returns "Joe is with Anne"
```

#### Postfix notation
In the case of fields and methods with 0 parameters, they act like postfix operators
```scala
val joe = new Person("Joe", 15)
joe name // = joe.name
joe learnsScala // calls the joe.learnsScala method
"45" toInt // = "45".toInt()
```
Not very used because may cause confusion reading code!!

#### Prefix notation and unary operators
An unary operator is an operator that is applied to 1 thing and yields a 2nd thing
The only ones we have in Scala are: - + ~ !
Those are also methods, and can be called using **.unary_** and the symbol
```scala
val x = -1 // The unary operator '-' is applied to '1' and returns '-1'
val x1 = !true // '!' is applied to 'true' and returns 'false'
val y = 1.unary_- // = -1
val y1 = true.unary_! // = !true
```

We can also override them in a Class
```scala
class Person(val name: String, val age: Int) {
  def unary_! = s"$name is not a Person"
}

val joe = new Person("Joe", 15)
!joe // returns "Joe is not a Person"
joe.unary_! // = !joe
```

### Method apply()
If we define in a class the method apply, in can be called adding parenthesis to the object: **object()**
```scala
class Class(val field: String) {
  def apply(): String = s"This is the apply method of $field"
}
val object1 = new Class("Object 1")
object1.apply() // = "This is the apply method of Object 1"
object1 apply()
object1() // Just the object with parenthesis
```
This is very helpful because allows us to treat object in a functional way!

## 14. Scala Objects
### Class level functionality
Methods and attributes that belong to the class not to the Oject. 
The **'static'** in Java, we access to the Class not to the Instance (or Object)

In java:
```java
class Person { 
  public static final int NUMBER_OF_EYES = 2;
}
Person.NUMBER_OF_EYES // Returns 2, no need to instantiate the class
```

Scala **DOES NOT HAVE** Class-Level Functionality --> NO CONCEPT OF 'static'.
The equivalent is using **OBJECTS**
```scala
object Person {
  val NUMBER_OF_EYES = 2
  def canFly: Boolean = false
}
Person.NUMBER_OF_EYES // = 2
Person.canFly // = false
```

### Singleton
Objects in scala are also **Singletons** by definition. There's only 1 instance per **Object**
```scala
val mary = Person // Doesn't create a new Instance, just makes the val mary point to the instance/object Person
val john = Person
println(mary == john) // true, the same instance/object

val eli = new Person // Error: Cannot resolve symbol Person
```

### Companions
The Companion Pattern in Scala is an object and a class with same **scope** and same **name**.
The purpose of this is to have the static/class-level functionality in the **Object** and the instance-level functionality in the **Class**

This is a **Companion**:
```scala
object Person {
  // 'static'/'class'-level functionality
  val NUMBER_OF_EYES = 2
  def canFly: Boolean = false
}

class Person(val name: String) {
  // instance-level functionality
  def getName: String = name
}

val mary = Person // Points to the Object Instance Person
val eli = new Person("Elisabeth") // Creates a new Instance from the class Person 

mary.canFly // = 2
mary.getName // Error: The Object cannot access to a instance-level Method

eli.canFly // Error: The Instance cannot access to a class-level Method
eli.getName // = 0
```

### Factory Method
This pattern is implemented with a method whose purpose is **generate instances** of a class using the given parameters
In Scala we do it using the **apply()** method in the **Object Companion** and generating instances of the **Class Companion**:

```scala
object Person {
  // Generates new Instances of the Class Person
  def apply(mother: Person, father: Person): Person = new Person("Bobbie")
}

class Person(val name: String) {
  def getName: String = name
}

val mary = new Person("Mary")
val john = new Person("John")

// Using factory method of Object Person
val bobbie = Person(mary, john)
```

### Main method
The JVM needs the main function as entry point.
In java we have the `'static void main(String args[])'` method:
```java
public class JavaApp {
  public static void main(String args[]) {
    // your code here
  }
}
```
If we translate this to Scala, the main function would be like this:
```scala
object ScalaApp{
  def main(args: Array[String]): Unit = {
    // your code here
  }
}
```
* object = static
* Unit = void

We can also just extend App and then the object will have implicit the main method
```scala
object ScalaApp extends App {
  // your code here
}
```

## 15. Inheritance
```scala
class Animal { 
  def eat = println("nom")
}

class Cat extends Animal {
  override def eat = println("slurp slurp slurp")
}
```
### Access modifiers
* **def** method: Can be accessed everywhere
* **private def** method: Can only accessed by the Class in Class level
* **protected def** method: Can be accessed by the Class and Subclasses in Class level
* **final def/class**: Can't be overridden
* **sealed def/class**: Can be **only** overridden in the current file

### Constructor
If the SuperClass has a constructor, we need to call it the SubClass definition
```scala
class Person(name: String, age: Int) { // Person class with Constructor
  def this(name: String) = this(name, 0) // Secondary constructor
}
// Adult needs to call the Person constructor
class Adult(name: String, age: Int, idCard: String) extends Person(name, age)
// Adult1 is using the secondary constructor 
class Adult1(name: String, age: Int, idCard: String) extends Person(name) 
```
### Overriding
We can override public and protected methods and attributes of the SuperClass.
We can also call the public and protected methods of the SuperClass with **super**
```scala
class Animal { 
  val creatureType = "wild"
  
  protected def eat1 = println("nom") 
  def eat2 = println("nomnom")
}

// Overriding Animal 'creatureType' attribute in the constructor
class Dog(override val creatureType: String = "domestic") extends Animal { 
  // Overriding public and protected methods of Animal
  override def eat1 = println("crunch crunch")
  override def eat2 = {
    super.eat1 // calling the superClass method, in this case Animal eat3
    println("crunch crunch crunch")
  }
}
// Overriding Animal attribute in the class definition using a parameter
class Dog1(dogType: String = "domestic") extends Animal {
  override val creatureType = dogType  
}
```
### Polymorphism
Declare a SuperClass type but save a SubClass inside.
This is correct because a SubClass will always be compatible the SuperClass type.
We can only call the available SuperClass methods and attributes, but we are going to use the overridden implementations in the SubClass (if any)

```scala
val unknownAnimal: Animal = new Dog("K9")
val unknownAnimal1: Animal = new Cat()
// unknownAnimal is an Animal and a Dog. So we can call the Animal method 'eat' but it will use the overridden Dog implementation
unknownAnimal.eat 
// this unknownAnimal is a Cat, will call the Cat 'eat' implementation
unknownAnimal1.eat 
```

## 16. Inheritance, Continued: Abstract Classes and Traits
A **Class** can extend only one **SuperClass**, but can extend multiple **Traits**.
Scala is **Single-Class inheritance** but **Multiple-Traits inheritance**

If a class overrides an attribute or a method, don't need to use the **'override'** keyword unless the overridden
attribute/method was already defined in the SuperClass or Trait
```scala
// abstract class
abstract class AbstractClass {
  val attribute1: String
  val attribute2: String = "Attribute 2"
  def method: Unit
}

// traits
trait Trait1 {
  def method(animal: AbstractClass): Unit
  val traitAttribute: String = "Trait Attribute"
}

trait Trait2

class Class extends AbstractClass with Trait1 with Trait2 {
  val attribute1: String = "Overridden Attribute 1"
  // NEEDS the 'override' because attribute2 is already defined in AbstractClass
  override val attribute2: String = "Overridden Attribute 2" 
  def method: Unit = println("Overridden AbstractClass Method")
  def method(animal: AbstractClass): Unit = println(s"Overridden Trait1 Method")
}
```

### Difference between Traits and Abstract Class
* Traits do not have constructor parameters
* Multiple Traits may be inherited by the same Class, but only can extend 1 Abstract class
* Traits = Behaviors, Abstract = "Thing"

### Scala's Type Hierarchy

* **scala.Any**: The mother of all types
  * **scala.AnyRef** (java.lang.Object): String, List, Set... also the Plain Classes we use like Person
    * **scala.Null**: Extends everything of scala.AnyRef. You can replace any AnyRef wit Null
  * **scala.AnyVal**: Int, Unit, Boolean... All the Primitive values of Scala
* **scala.Nothing**: Is a subtype of **EVERYTHING** in Scala. Nothing can replace Everything. Means no Instance of anything at all
   
## 18. Generics
Sometimes you want to create a new class or function that uses a parameter. But are not really
interested with the **parameter type** just use a generic one, or even, be able to define 
rules about it.
For that are the **Generics**

A class with two Generics parameters **Key** and **Value**, you can name them whatever you want
```scala
class MyMap[Key, Value]
class GenericClass[qwerty, asdfg]
```
With functions:
```scala
object MyListExample {
  def empty[A]: MyListExample[A] = ???
}
val emptyListOfIntegers = MyListExample.empty[Int]
```
### Variance types
We can define some rules about those **Generic Parameteres**
#### Covariance
For two types A and B where A is a subtype of B, then Class[A] is a subtype of Class[B]

(+) Class[SuperClass] accepts Class[Subclass]
```scala
class CovariantList[+A]
val animal: Animal = new Cat
val animalList: CovariantList[Animal] = new CovariantList[Cat]
```
The CovariantList of Animals accepts Cats, because a CovariantList of Cats is a CovariantList of Animals
#### Invariance
Only accepts the class A
```scala
class InvariantList[A]
val invariantAnimalList: InvariantList[Animal] = new InvariantList[Animal]
```
#### Contravariance
For two types A and B where A is a subtype of B, Class[B] is a subtype of Class[A]

(-) Class[Subclass] accepts Class[SuperClass]
```scala
class ContravariantList[-A]
val contravariantList: ContravariantList[Cat] = new ContravariantList[Animal]
```
The ContravariantList of Cats accepts Animals, because a ContravariantList of Animals is a ContravariantList of Cats
#### Bounded types
Restrict only **Subtypes** or **Supertypes** of certain class
```scala
class Clase
class BoundedSub[A <: Clase](instance: A) // only accepts subtypes of Clase
class BoundedSuper[A >: Clase](instance: A) // supertypes of Clase

val bounded = new BoundedSub(new Clase)
```

#### Covariance + Bounded types
Poblem: Can we add Dogs in a Covariant list of Cats? 
```scala
animalList.add(new Dog)
```
In other words: How to define a covariant list of A that accepts different subclasses of 
A at the same time.

If A is subclass of B:
* **Covariant List class of A** 
* Method add that accepts **B** which is Superclass of **A**
  * If adds **A**, return MyListCovariance[A] 
  * If adds **B** or any other subclass of **B** like **C**: return MyListCovariance[B]
    Since added something different than A, will always return MyListCovariance[B]
```scala
class MyListCovariance[+A] {
  def add[B >: A](element: B): MyListCovariance[B] = ???
}

val michu: Cat = new Cat
val fray: Dog = new Dog
val monchito: Cat = new Cat

val catList = new MyListCovariance[Cat].add(michu) // List of cat
val animalList1 = catList.add(fray) // List of cat + dog = List of Animals
val animalList2 = animalList1.add(monchito) // List of cat + dog + cat = still List of Animals
```

## 19. Anonymous Classes
We can instantiate abstract classes, traits or normal classes on the fly overriding and adding their behaviours.
We always have to respect the rules:
* pass the required constructor arguments if needed
* implement all abstract fields/methods

```scala
abstract class AbstractClass(parameter: String) {
  val attribute: Any
  def method: Unit
}

trait Trait {
  val attribute: Any
  def method: Unit
}

class NormalClass(parameter: String) {
  val attribute: Any = ???
  def method: Unit = ???
}

val anonymousClass1: AbstractClass = new AbstractClass(parameter = "Parameter") {
  override val attribute: Any = "Attribute"
  override def method: Unit = println("Method")
}
val anonymousClass2: Trait = new Trait {
  override val attribute: Any = "Attribute"
  override def method: Unit = println("Method")
}
val anonymousClass3: NormalClass = new NormalClass(parameter = "Parameter") {
  override val attribute: Any = "Attribute"
  override def method: Unit = println("Method")
}
```

## 21. Case Classes
* Quick lightweight data structures with little boilerplate
```scala
case class Person(name: String, age: Int)
```
* Companions already implemented 
```scala
val bob = Person("Bob", 26)
```
* Sensible equals, hashCode, toString
* Auto-promoted params to fields
* Cloning
* case objects

## 22. Exceptions

### Typical structure:

```scala
try {
  // code that might fail
} catch {
  case e: NullPointerException =>
  case e: RuntimeException =>
  } finally {
    // code that will get executed NO MATTER
    // does not influence the return type of this expression
    // use finally only for side effects like logging the error
    println("finally")
  }
```

The try-catch-finally value is determined by the try and catch block. The finally block doesn't return anything.

### Custom Exceptions

Can create and throw custom exceptions

```scala
class MyException extends Exception
class OverflowException extends RuntimeException
class UnderflowException extends RuntimeException
class MathCalculationException extends RuntimeException("Division by 0")

val exception = new MyException
throw exception
```

### Provoke exceptions

Out of Memory
```scala
val array = Array.ofDim(Int.MaxValue)
```

Stack Overflow
```scala
def infinite: Int = 1 + infinite
val noLimit = infinite
```

## 23. Packaging and imports

Package: A group of definition under the same name
Best practice: mirror the file structure

Can import whole package but the best practice is to import the desired classes
```scala
import playground._ 
import playground.{PrinceCharming, Cinderella}
```

### Avoid import conflicts

Fully Qualified name: Use the complete package + class name
```scala
val princess = new playground.Cinderella
```

Aliasing: Rename the imported classes
```scala
import java.sql.{Date => SqlDate}
val sqlDate2 = new SqlDate(2020, 6, 17)
```
