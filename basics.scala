// ─── VALUES & VARIABLES ───────────────────────────────────────────────────────
val x: Int = 42          // cant be changed
var y: Int = 10
y = 20

// ─── BASIC TYPES ──────────────────────────────────────────────────────────────
val b: Boolean = true
val c: Char    = 'A'
val s: String  = "hello"
val d: Double  = 3.14
val l: Long    = 100L

// ─── STRING INTERPOLATION ─────────────────────────────────────────────────────
val name = "Scala"
println(s"Hello, $name!")          // s-interpolator
println(f"Pi is ${math.Pi}%.2f")   // f-interpolator (formatted)
println(raw"no \n escape")         // raw-interpolator

// ─── IF / ELSE  (expression, returns a value) ─────────────────────────────────
val max = if x > y then x else y

// ─── WHILE LOOP ───────────────────────────────────────────────────────────────
var i = 0
while i < 3 do
  println(i)
  i += 1

// ─── FOR LOOP ─────────────────────────────────────────────────────────────────
for n <- 1 to 5 do println(n)       // inclusive range
for n <- 1 until 5 do println(n)    // exclusive range

// for with guard (filter)
for n <- 1 to 10 if n % 2 == 0 do println(n)

// for comprehension (yields a collection)
val squares = for n <- 1 to 5 yield n * n

// ─── FUNCTIONS ────────────────────────────────────────────────────────────────
def add(a: Int, b: Int): Int = a + b          // single-expression, return inferred
def greet(n: String = "World"): String =       // default parameter
  s"Hello, $n!"

// higher-order function
def applyTwice(f: Int => Int, x: Int): Int = f(f(x))
applyTwice(_ + 1, 5)   // 7

// ─── ANONYMOUS FUNCTIONS / LAMBDAS ────────────────────────────────────────────
val double = (n: Int) => n * 2
val triple: Int => Int = _ * 3     // underscore shorthand

// ─── COLLECTIONS ──────────────────────────────────────────────────────────────
val list   = List(1, 2, 3)         // immutable linked list
val vector = Vector(1, 2, 3)       // immutable indexed sequence
val set    = Set(1, 2, 2, 3)       // no duplicates → Set(1,2,3)
val map    = Map("a" -> 1, "b" -> 2)

// common operations
list.map(_ * 2)              // List(2, 4, 6)
list.filter(_ > 1)           // List(2, 3)
list.foldLeft(0)(_ + _)      // 6
list.head                    // 1
list.tail                    // List(2, 3)
list :+ 4                    // append  → List(1,2,3,4)
0 :: list                    // prepend → List(0,1,2,3)

// ─── TUPLES ───────────────────────────────────────────────────────────────────
val pair = (1, "one")
val (n, w) = pair            // destructuring
pair._1                      // access by position (1-indexed)

// ─── OPTION (null-safety) ─────────────────────────────────────────────────────
val some: Option[Int] = Some(42)
val none: Option[Int] = None
some.getOrElse(0)            // 42
none.getOrElse(0)            // 0
some.map(_ * 2)              // Some(84)

// ─── PATTERN MATCHING ─────────────────────────────────────────────────────────
val result = x match
  case 1       => "one"
  case 2 | 3   => "two or three"
  case n if n > 10 => s"big: $n"   // guard
  case _       => "other"          // wildcard

// match on type
def describe(a: Any): String = a match
  case i: Int    => s"int $i"
  case s: String => s"str $s"
  case _         => "unknown"

// ─── CASE CLASSES ─────────────────────────────────────────────────────────────
// immutable data classes with equals, hashCode, toString, copy for free
case class Point(x: Double, y: Double)
val p1 = Point(1.0, 2.0)
val p2 = p1.copy(y = 5.0)   // copy with modified field

// destructure in match
p1 match
  case Point(0, 0) => "origin"
  case Point(x, y) => s"($x, $y)"

// ─── SEALED TRAITS / ADTs ─────────────────────────────────────────────────────
// compiler warns on non-exhaustive matches
sealed trait Shape
case class Circle(r: Double)          extends Shape
case class Rectangle(w: Double, h: Double) extends Shape

def area(s: Shape): Double = s match
  case Circle(r)        => math.Pi * r * r
  case Rectangle(w, h)  => w * h

// ─── TRAITS ───────────────────────────────────────────────────────────────────
trait Greeter:
  def greet(): String          // abstract
  def shout(): String = greet().toUpperCase   // concrete with default impl

class EnglishGreeter extends Greeter:
  def greet() = "Hello"

// mixin multiple traits
trait Logger:
  def log(msg: String): Unit = println(s"[LOG] $msg")

class App extends Greeter with Logger:
  def greet() = "Hi"

// ─── CLASSES ──────────────────────────────────────────────────────────────────
class Animal(val name: String, private var age: Int):
  def birthday(): Unit = age += 1
  override def toString = s"Animal($name, $age)"

class Dog(name: String, age: Int) extends Animal(name, age):
  def bark() = "Woof!"

// ─── OBJECTS (singletons) ─────────────────────────────────────────────────────
object MathUtils:
  def square(n: Int): Int = n * n

MathUtils.square(4)    // 16

// companion object shares name with a class; can access private members
object Point:
  def origin: Point = Point(0, 0)

// ─── TYPE ALIASES ─────────────────────────────────────────────────────────────
type Name = String
type Predicate[A] = A => Boolean

// ─── GENERICS ─────────────────────────────────────────────────────────────────
def identity[A](a: A): A = a
case class Box[A](value: A)

// ─── IMPLICIT / GIVEN (Scala 3 type-class style) ──────────────────────────────
trait Show[A]:
  def show(a: A): String

given Show[Int] with
  def show(a: Int) = a.toString

def printIt[A](a: A)(using s: Show[A]): Unit = println(s.show(a))
printIt(42)

// ─── EXTENSION METHODS ────────────────────────────────────────────────────────
extension (s: String)
  def shout: String = s.toUpperCase + "!"
  def whisper: String = s.toLowerCase

"hello".shout    // "HELLO!"

// ─── HIGHER-KINDED / FUNCTIONAL PATTERNS ──────────────────────────────────────
// map / flatMap on Option chain (for-comprehension desugars to these)
val result2: Option[Int] =
  for
    a <- Some(3)
    b <- Some(4)
  yield a + b    // Some(7)

// ─── ERROR HANDLING ───────────────────────────────────────────────────────────
import scala.util.{Try, Success, Failure}
val t: Try[Int] = Try("42".toInt)   // Success(42)
val bad         = Try("x".toInt)    // Failure(NumberFormatException)
t.getOrElse(-1)
bad.recover { case _: NumberFormatException => 0 }

// Either: Left = error, Right = success (by convention)
def parse(s: String): Either[String, Int] =
  s.toIntOption.toRight(s"'$s' is not an int")

parse("10")   // Right(10)
parse("hi")   // Left("'hi' is not an int")
