package lectures.part2oop

import scala.language.postfixOps

object MethodNotations extends App{

  class Person(val name: String, favoriteMovie: String) { // created inside the object to avoid conflicts with the other Person in the same package
    def likes(movie: String): Boolean = movie == favoriteMovie
    def hangOutWith(person: Person): String = s"${this.name} is hanging out with ${person.name}"
    // since hangOutWith acts like an operator, lets define it with the operator '+'
    def +(person: Person): String = s"${this.name} is hanging out with ${person.name}"
    // Prefix notation
    // Defining the unary operator '!' for Person
    def unary_! : String = s"$name!!!!!"
    // Postfix notation
    def isAlive: Boolean = true
    // the apply method can be called: 'object.apply()' or just 'object()'
    def apply(): String = s"Hi, my name is $name and I like $favoriteMovie"
  }

  val mary = new Person("Mary", "Inception")
  // infix notation / operator notation (syntactic sugar) => can only be used with methods with 1 parameter
  println(mary.likes("Inception"))
  // this is the infix notation: More natural
  println(mary likes "Inception") // using method 'like' with parameter '"Inception"'
  println(mary.name) // getting a field
  println(mary.isAlive) // using method 'isAlive' with no parameters

  // "Operators" in Scala
  // and operator is something that is between 2 things and yields a 3rd thing
  val tom = new Person("Tom", "Fight club")
  // 'hangOutWith' acts like an operator between 'mary' and 'tom' yielding a String.
  // Is like 1 + 4: '+' is an operator that yields the number 5
  println(mary hangOutWith tom)
  println(mary + tom) // using the method + between 2 persons
  print(mary.+(tom)) // equivalent but not so nice

  // In fact, the + operator is a method between numbers and can be used like a method:
  println(1 + 2)
  println(1.+(2)) // = 1 + 2

  // ALL OPERATORS ARE METHODS
  // Akka actors have ! ?

  // Prefix notation and unary operators
  // An unary operator is an operator that is applied to 1 thing and yields a 2nd thing
  val x = -1 // The unary operator '-' is applied to '1' and returns '-1'
  val x1 = !true // '!' is applied to 'true' and returns 'false'
  val y = 1.unary_- // = -1
  val y1 = true.unary_! // = !true

  // Use the ! definition for Person in the 2 ways
  println(!mary)
  println(mary.unary_!)
  // Postfix notation
  println(mary.isAlive)
  println(mary isAlive) // this is the postfix notation. Only with methods with no arguments

  // apply
  println(mary.apply())
  println(mary()) // equivalent
  /*

    EXERCISES

    1.  Overload the + operator
        mary + "the rockstar" => new person "Mary (the rockstar)"

    2.  Add an age to the Person class
        Add a unary + operator => new person with the age + 1
        +mary => mary with the age incrementer

    3.  Add a "learns" method in the Person class => "Mary learns Scala"
        Add a learnsScala method, calls learns method with "Scala".
        Use it in postfix notation.

    4.  Overload the apply method
        mary.apply(2) => "Mary watched Inception 2 times"
   */
  class PersonExercise(val name: String, val age: Int = 0, favoriteMovie: String) {
    def +(person: Person): String = s"${this.name} is hanging out with ${person.name}"
    def +(nickname: String): PersonExercise = new PersonExercise(s"$name ($nickname)", age, favoriteMovie)
    def unary_+ : PersonExercise = new PersonExercise(name, age + 1, favoriteMovie)
    def learns(subject: String): String = s"$name is learning $subject"
    def learnsScala: String = learns("Scala")
    def apply(): String = s"Hi, my name is $name, I am $age years old and I like $favoriteMovie"
    def apply(times: Int): String = s"$name watched $favoriteMovie $times times"
  }

  val elizabeth = new PersonExercise("Elizabeth", 15, "They live")
  println((elizabeth + "the rockstar")())
  println((+elizabeth)())
  println((elizabeth.learns("English")))
  println(elizabeth learnsScala)
  println(elizabeth(2))


}
