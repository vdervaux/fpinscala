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


  // 5.3
  
  //
  // takeWhile
  //
  
  def takeWhile(p: A => Boolean): Stream[A] =
    this match {
      case Cons(h, t) if p(h()) => cons(h(), t() takeWhile p)
      case _ => Empty
    }
  
  //
  // 5.4
  //
  
  def forAll(p: A => Boolean): Boolean = 
    this match {
    case Cons(h, t) => p(h()) && (t() forAll p)
    case _ => true
  }
  
  //
  // 5.5
  //
  
  def takeWhileUsingFoldRight(p: A => Boolean): Stream[A] =
    // simplified template foldRight(z: B)((a: A, b: B) => B)
    // so B is Stream[A]
    foldRight(empty: Stream[A])( (a, b) => if (p(a)) cons(a, b) else empty)
    
  //
  // 5.6
  //
    
  def headOption: Option[A] =
    foldRight(None: Option[A])((a, b) => Some(a))
    
  //
  // 5.7
  //
    
  def map[B](f: A => B): Stream[B] =
    foldRight(empty[B])( (a, b) => cons(f(a), b) )
  
  def filter(p: A => Boolean): Stream[A] =
    foldRight(empty[A])((a, b) => if (p(a)) cons(a, b) else b)
  
  def append[B>:A](s: => Stream[B]): Stream[B] =
    foldRight(s)((a, b) => cons(a, b))
  
  def flatMap[B](f: A => Stream[B]): Stream[B] = 
    foldRight(empty[B])((a, b) => f(a) append b)
    

  // 5.13

  def mapViaUnfold[B](f: A => B): Stream[B] =
    unfold(this) { // initial state is the stream itself: this
      case Cons(h, t) => Some((f(h()), t())) // next value is f(h) and next state is t
      case _ => None
    }

  def takeViaUnfold(n: Int): Stream[A] =
    // the state if a pair containing a stream and the number of elements to take from it
    unfold((this, n)) {
      case (Cons(h, t), n) if n > 0 => Some((h(), (t(), n - 1)))
      case _ => None
    }
    
  def takeWhileViaUnfold(p: A => Boolean): Stream[A] =
    unfold(this) {
    case Cons(h, t) if p(h()) => Some((h(), t())) 
    case _ => None
  }
    
  // Reminder: zipWith for List is zipWith[A,B](a1: List[A], a2: List[A])(f: (A,A) => B): List[B]
  
  def zipWith[B,C](s2: Stream[B])(f: (A,B) => C): Stream[C] =
    unfold(this, s2) {
    case (Empty, Empty) => None
    case (_, Empty) => None
    case (Empty, _) => None
    case (Cons(h1, t1), Cons(h2, t2)) => Some(f(h1(), h2()), (t1(), t2()))
  }
  
  def zipAll[B](s2: Stream[B]): Stream[(Option[A],Option[B])] =
    unfold(this, s2) {
    case (Empty, Empty) => None
    case (Cons(h,t), Empty) => Some( (Some(h()), None), (t(), Empty) )
    case (Empty, Cons(h,t)) => Some( (None, Some(h())), (Empty, t()) )
    case (Cons(h1,t1), Cons(h2,t2)) => Some( (Some(h1()), Some(h2())), (t1(), t2()) )
  }
    
    
  // 5.14

  def startsWith[B](s: Stream[B]): Boolean =
    this.zipWith(s)(_ == _).forAll(_ == true)
    
  // solution:
  def startsWith2[A](s: Stream[A]): Boolean = 
    zipAll(s).takeWhile(!_._2.isEmpty) forAll {
      case (h,h2) => h == h2
    }

  // 5.15

  def tails1: Stream[Stream[A]] =
    unfold(this) {
      case Empty => None
      case Cons(h, t) => Some(Cons(h, t), t())
    }

  // solution:
  def tails: Stream[Stream[A]] =
    unfold(this) {
      case Empty => None
      case s => Some((s, s drop 1))
    } append (Stream(empty))

  def hasSubsequence[A](s: Stream[A]): Boolean =
    tails exists (_ startsWith s)


  // 5.16
  def scanRight[B](z: B)(f: (A, => B) => B): Stream[B] =
    // initial value is (B, Stream[B]) which is the current result and the stream of previous results
    foldRight(z, Stream(z))((a, p) => {
      // a is the value read from 'this' stream
      // p is the pair (previous result, stream of previous results)
      // p is passed by name and then sent by name to two functions, so use lazy to evaluate it only once
      lazy val lp = p
      val result = f(a, lp._1)  // compute new result
      (result, cons(result, lp._2)) // add new result to stream of results
    })._2 // return the stream of results
    
    
  // def foldRight[B](z: => B)(f: (A, => B) => B): B  
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
  
  // 5.8
  
  def constant1[A](a: A): Stream[A] =
    cons(a, constant1(a))
    
  def constant[A](a: A): Stream[A] = {
    lazy val tail: Stream[A] = Cons(() => a, () => tail)
    tail
  }
    
  // 5.9
  
  def from(n: Int): Stream[Int] = 
    cons(n, from(n + 1))

  // 5.10

  val fibs = {
    def go(x0: Int, x1: Int): Stream[Int] = {
      println("Evaluating: %s + %s".format(x0, x1))
      cons(x0, go(x1, x0 + x1))
    }
    go(0, 1)
  } 
  
  // 5.11
  
  // my version
  /*
  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] = { 
    lazy val (value, nextState) = f(z).get
    cons(value, unfold(nextState)(f))
  }
  */
  // solution
   def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] =
    f(z) match {
      case Some((h,s)) => cons(h, unfold(s)(f))
      case None => empty
    }

   
  // 5.12   
  
  // A -> Int
  // S -> pair for n-2 and n-1 -> (Int, Int)
  // f: (Int, Int) => Option[(Int, (Int, Int))]
  //     n-2, n-1               n, (n-1, n-2)
  def fibsViaUnfold_skipsFirstTwo = 
    unfold(0,1)(prev => Some((prev._1 + prev._2 , (prev._2 , prev._1 + prev._2 )) ))

  // A -> Int
  // S -> seed: pair for current and next values -> (Int, Int)
  // f: (Int, Int) => Option[(Int, (Int, Int))]
  //     curr, next           curr, (next, curr+next)
  def fibsViaUnfold = 
    unfold(0,1)(p => Some((p._1, (p._2 , p._1 + p._2 )) ))
  
  def fibsViaUnfoldWithDebug = {
    def add(a: Int, b: Int) = {
      println("Evaluating: %s + %s".format(a, b))
      a + b
    }
    unfold(0,1)(p => Some((p._1, (p._2 , add(p._1, p._2) )) ))
  }
  
  // using a case for the function literal
  def fibsViaUnfold2 = 
    unfold(0,1){ case (x0, x1) => Some((x0, (x1 , x0 + x1)) )}
      
  // from via unfold
  // initial state: n
  // next value: n
  // next state: n + 1
  def fromViaUnfold(n: Int) = 
    unfold(n)(n => Some((n, n+1)) )    
  
  // constant via unfold
  def constantViaUnfold[A](a: A) = 
    unfold(a)(a => Some((a,a)))
    
  // ones via unfold  
  val onesViaUnfold = unfold(1)(_ => Some((1, 1)))  
  
  
}
