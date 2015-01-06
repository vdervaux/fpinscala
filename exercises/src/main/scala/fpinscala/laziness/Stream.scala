package fpinscala.laziness

import Stream._

trait Stream[+A] {

  // 5.1
  
  // transforms a Stream into a List. Non tail recursive version.
  def toListNotTailRec: List[A] =
    this match {
      case Empty => Nil
      case Cons(h, t) => h() :: t().toListNotTailRec
    }
  
  // transforms a Stream into a List using a tail recursive helper function.
  def toList: List[A] = {
    @annotation.tailrec
    def go(s: Stream[A], acc: List[A]): List[A] = 
      s match {
      case Empty => acc
      case Cons(h,t) => go(t(), h()::acc)  // h is added at the beginning of acc
    }
    go(this, Nil).reverse                  // so a reverse is needed at the end
  }

  

  def foldRight[B](z: => B)(f: (A, => B) => B): B = // The arrow `=>` in front of the argument type `B` means that the function `f` takes its second argument by name and may choose not to evaluate it.
    this match {
      case Cons(h,t) => f(h(), t().foldRight(z)(f)) // If `f` doesn't evaluate its second argument, the recursion never occurs.
      case _ => z
    }

  def exists(p: A => Boolean): Boolean = 
    foldRight(false)((a, b) => p(a) || b) // Here `b` is the unevaluated recursive step that folds the tail of the stream. If `p(a)` returns `true`, `b` will never be evaluated and the computation terminates early.

  @annotation.tailrec
  final def find(f: A => Boolean): Option[A] = this match {
    case Empty => None
    case Cons(h, t) => if (f(h())) Some(h()) else t().find(f)
  }

  // 5.2

  //
  // Take
  //
  
  def take(n: Int): Stream[A] =
    if (n <= 0) Empty
    else this match {
      case Empty => sys.error("take on empty stream")
      case Cons(h, t) => cons(h(), t().take(n - 1))
    }

  def takeTailRecButReversed(n: Int): Stream[A] = {
    @annotation.tailrec
    def go(s: Stream[A], n: Int, acc: Stream[A]): Stream[A] =
      if (n <= 0) acc
      else s match {
        case Empty => sys.error("take on empty list")
        case Cons(h, t) => go(t(), n - 1, cons(h(), acc))
      }
    go(this, n, Empty)
  }
  
  //
  // Drop
  //

  // Non tail recursive version
  def dropNotTailRec(n: Int): Stream[A] = 
    if (n <= 0) this
    else this match {
      case Empty => sys.error("drop on empty stream")
      case Cons(h, t) => t().dropNotTailRec(n - 1)
    }

  // Tail recursive version using a helper method
  def dropTailRec1(n: Int): Stream[A] = {
    @annotation.tailrec
    def go(s: Stream[A], n: Int): Stream[A] = {
      if (n <= 0) s
      else s match {
        case Empty => sys.error("drop on empty stream")
        case Cons(h, t) => go(t(), n - 1)
      }
    }
    go(this, n)
  } 
  
  // To allow the method to be tail recursive: make it final
  // no real need for a helper function
  @annotation.tailrec
  final def drop(n: Int): Stream[A] = 
    if (n <= 0) this
    else this match {
      case Empty => sys.error("drop on empty stream")
      case Cons(h, t) => t().drop(n - 1)
    }


  
  
  

  def takeWhile(p: A => Boolean): Stream[A] = sys.error("todo")

  def forAll(p: A => Boolean): Boolean = sys.error("todo")

  def startsWith[B](s: Stream[B]): Boolean = sys.error("todo")
}

case object Empty extends Stream[Nothing]
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }

  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty 
    else cons(as.head, apply(as.tail: _*))

  val ones: Stream[Int] = Stream.cons(1, ones)
  def from(n: Int): Stream[Int] = sys.error("todo")

  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] = sys.error("todo")
}