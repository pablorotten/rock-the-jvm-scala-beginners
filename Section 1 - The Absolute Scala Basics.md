# Section 1: The Absolute Scala Basics
## 2. Values, Variables and Types
## 3. Expressions

### Instructions vs Expressions

**Instruction** are executed. Is something that you tell the computer to do: change a variable, print to the console.
**Expressions** are evaluated to a value and they have a type:

```scala
val aConditionedValue = if(aCondition) 5 else 3 // IF EXPRESSION
```

The if statement is an **expression** that has a value, which is either 5 or 3. You can print it:

```scala
println(if(aCondition) 5 else 3)
```

Only things like a definition of a val, a class, a package are **Instructions**.

**Everything** in Scala is an **Expression**.

* Mathematical Expressions: + - * / & | ^ << >> >>> (right shift with zero extension)
* Relational Expressions: == != > >= < <=
* Boolean Expressions: ! && ||

```scala
  val x = 1 + 2
  
  println(2 + 3 * 4 - x)
  println(!(1 == x))
  println(1 == x)
```

### Expressions and Side effects

**Side effects**: Expressions that change the value of a variable

* Side effects: -= *= /= 

```scala
  var aVariable = 2
  aVariable += 3 // 
  println(aVariable)
```

For example, re-assigning a value to a var is an **Expression**
 
```scala
  var aVariable = 1
  val assignmentExpression = (aVariable = 3)
```
Re-assigning 3 to the var aVariable is an **Expression** of type Unit which has the value of (). Is like a Java void Function.

This kind of **Expressions** are **Side effects** because they change the value of a variable.

## 4. Functions

If a function has no parameters, can be called without parentheses. The compiler will give us a warning
```scala
  def aParameterlessFunction(): Int = 42
  
  println(aParameterlessFunction())
  println(aParameterlessFunction)
```

## 5. Type Inference
If there's a recursive function, the compiler can't infer the return type, we must specify it
```scala
  def aRepeatedFunction(aString: String, n: Int) = {
    if (n == 1) aString
    else aString + aRepeatedFunction(aString, n-1)
  }
```

## 6. Recursion
### Tail Recursion
A recursive function with big numbers may cause a Stack Overflow error:

```scala
  def factorial(n: Int): Int =
    if (n <= 1) 1
    else {
      val result = n * factorial(n-1) // The recursive call expression is HERE
      result // This is the last expression
    }
    
  println(factorial(5000)) // --> Stack Overflow error.
```

This can be solved using **Tail Recursion**: the last expression is the recursive call:

```scala
  @tailrec
  def factorialTailRecursion(n: Int, accumulator: BigInt): BigInt = {
    if (n <= 1) accumulator
    else factorialTailRecursion(n - 1, n * accumulator) // TAIL RECURSION = use recursive call as the LAST expression
  }
  
  factorialTailRecursion(10000000, 1) // WORKS!
```

#### Fibonacci with tail recursion
Starts from the base case and stops when the limit (n) is reached:
So the base case is fibonacci(3) which is 1 + 1 = 2

If the user enters n = 3 which is the base case, in the first call of fiboTailrec, we already will have i = n and just return the initial last + nextToLast = 1 + 1 = 2
If is a bigger number, just call again fiboTailrec with last + nextToLast in the first parameter and last in the second one
```scala
  def fibonacci(n: Int): Long = {
    // Fibonacci: 1 1 2 3 5 8 13 21
    @tailrec
    def fiboTailrec(i: Int, last: Int, nextToLast: Int): Int = // last is fibonacci(n - 1), nextToLast is fibonacci(n - 2)
    if (i >= n) last
    else fiboTailrec(i + 1, last + nextToLast, last)

    if (n <= 2) 1
    else fiboTailrec(2, 1, 1)
  }
```

## 7. Call-by-Name and Call-by-Value
This is related to how we pass the parameters to a function

### Call by Value
This is the most common one. It **evaluates** the parameter first and passes the value to the function
> function(x: Int)

```scala
  def calledByValue(x: Long): Unit = {
    println("by value: " + x) // 2. prints the same
    println("by value: " + x) // 2. prints the same
  }

calledByValue(System.nanoTime()) // 1. Passes and evaluates the current nanoTime value
```

### Call by Name
Passes the expresion directly to the function without evaluating it. 
It will be evaluated inside the function every time it's used
> function(x: => Int)

```scala
  def calledByName(x: => Long): Unit = {
    println("by name: " + x) // 2. Evaluates System.nanoTime() here and prints it
    println("by name: " + x) // 3. Evaluates again System.nanoTime() here and prints something bigger
  }

calledByName(System.nanoTime()) // 1. Passes the expresion for calculating the current nanoTime
```

## 8. Default and Named Arguments

You can set default values to any of the arguments of a function.
You can use de default values or override them with a different value
Also, can change the order of the arguments using their names
```scala
def savePicture(format: String = "jpg", width: Int = 1920, height: Int = 1080): Unit = ???

savePicture() // Default values
savePicture("bmp") // Override the value of the leading argument (format) and use the default one for the others
savePicture(width = 800) // Override the default value of widh
savePicture(width = 1920, format = "jpg" , height = 1080) // Override all the arguments and change the order
```

## 9. Smart Operations on Strings

**Java** utilities for Strings:

```scala
  val str: String = "Hello, I am learning Scala"

  str.charAt(2) // Char at position 2
  str.substring(7, 11) // Substring between position 7 and 10 (11 - 1)
  str.split(" ").toList // Creates a list with the elements splitting them by the character " "
  str.startsWith("Hello") // Checks if the evaluated String starts with the String "Hello"
  str.replace(" ", "-") // Replaces the character " " with "-" in the whole String
  str.toLowerCase
  str.length
```

**Scala** utilities for Strings

```scala
  val aNumberString = "45"  
  val aNumber = aNumberString.toInt // parses to int
  
  
  val str: String = "Hello, I am learning Scala"
  'a' +: 'a' +: aNumberString :+ 'z' :+ 'z' // = aa45zz +: prepend :+ append
  str.reverse
  str.take(2) // takes the n first chars of the String

  // S-interpolators
  val name = "David"
  val age = 12
  val greeting = s"Hello, my name is $name and I am $age years old"
  val greeting2 = s"Hello, my name is $name and I am $age years old and I will be turning ${age + 1}" // can evaluate complex expressions between brackets ${...}

  // F-interpolators
  val speed = 1.200000056
  val myth = f"$name can eat $speed%2.3f burgers per minute" // 1.200 burgers -->  2.2 means: Min. 2 characters and 3 decimals precision

  // raw-interpoaltors
  println(raw"This is a \n newline") // Takes the \n as a raw chars and doesn't do the new line
  val escaped = "This is a \n newline"
  println(raw"$escaped") // because the \n is inside a val, now this is not raw and make the new line
```
