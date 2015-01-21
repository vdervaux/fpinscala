package fpinscala.state

trait RNG {
  def nextInt: (Int, RNG) // Should generate a random `Int`. We'll later define other functions in terms of `nextInt`.
}

object RNG {
  // NB - this was called SimpleRNG in the book text

  case class Simple(seed: Long) extends RNG {
    def nextInt: (Int, RNG) = {
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL // `&` is bitwise AND. We use the current seed to generate a new seed.
      val nextRNG = Simple(newSeed) // The next state, which is an `RNG` instance created from the new seed.
      val n = (newSeed >>> 16).toInt // `>>>` is right binary shift with zero fill. The value `n` is our new pseudo-random integer.
      (n, nextRNG) // The return value is a tuple containing both a pseudo-random integer and the next `RNG` state.
    }
  }

  type Rand[+A] = RNG => (A, RNG)

  val int: Rand[Int] = _.nextInt

  def unit[A](a: A): Rand[A] =
    rng => (a, rng)

  def map[A,B](s: Rand[A])(f: A => B): Rand[B] =
    rng => {
      val (a, rng2) = s(rng)
      (f(a), rng2)
    }

    
  // 6.1
    
  // generate integer between 0 and Int.maxValue (inclusive)  
    
  def nonNegativeInt1(rng: RNG): (Int, RNG) = {
    rng.nextInt match {
      case (n, rng2) if n > 0 => (n, rng2)
      case (_, rng2) => nonNegativeInt(rng2)
    }
  }

  // We need to be quite careful not to skew the generator.
  // Since `Int.Minvalue` is 1 smaller than `-(Int.MaxValue)`,
  // it suffices to increment the negative numbers by 1 and make them positive.
  // This maps Int.MinValue to Int.MaxValue and -1 to 0.
  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val (i, r) = rng.nextInt
    (if (i < 0) -(i + 1) else i, r)
  }


  // 6.2
  
  // generate double between 0 and 1 (1 excluded)
  
  def double(rng: RNG): (Double, RNG) = { 
    val (n, rng2) = nonNegativeInt(rng)
    // 0 <= n <= Int.maxValue
    val d = n.toDouble / (Int.MaxValue.toDouble + 1) 
    (d, rng2) 
  }


  // 6.3

  def intDouble(rng: RNG): ((Int, Double), RNG) = {
    val (i, rng2) = rng.nextInt
    val (d, rng3) = double(rng)
    ((i, d), rng3)
  }

  def doubleInt(rng: RNG): ((Double, Int), RNG) = {
    val ((i, d), rng2) = intDouble(rng)
    ((d, i), rng2)
  }

  def double3(rng: RNG): ((Double, Double, Double), RNG) = {
    val (d1, rng1) = double(rng)
    val (d2, rng2) = double(rng1)
    val (d3, rng3) = double(rng2)
    ((d1, d2, d3), rng3)
  }


  // 6.4

  def intsNotTailRec(count: Int)(rng: RNG): (List[Int], RNG) = count match {
    case 0 => (Nil, rng)
    case n => {
      val (i, rng2) = rng.nextInt
      val (list, rng3) = intsNotTailRec(n - 1)(rng2)
      (i :: list, rng3)
    }
  }

  def ints(count: Int)(rng: RNG): (List[Int], RNG) = {
    @annotation.tailrec
    def go(count: Int, xs: List[Int], rng: RNG): (List[Int], RNG) =
      count match {
        case 0 => (xs, rng)
        case n => {
          val (x, rng2) = rng.nextInt
          go(n - 1, x :: xs, rng2)
        }
      }
    go(count, Nil, rng)
    // or we can reverse to compare to the non-recursive version
    // val (xs, rng2) = go(count, Nil, rng)
    // (xs.reverse, rng2) 
  }

  // 6.5

  def doubleViaMap: Rand[Double] =
    map(nonNegativeInt)(_.toDouble / (Int.MaxValue.toDouble + 1))

  // 6.6

  // Reminder: Rand[+X] = RNG => (X, RNG)
  // so map2 has type RNG => (C, RNG)  
  def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] =
    rng => {
      val (a, rng2) = ra(rng)
      val (b, rng3) = rb(rng2)
      (f(a, b), rng3)
    }

  def both[A, B](ra: Rand[A], rb: Rand[B]): Rand[(A, B)] =
    map2(ra, rb)((_, _))

  val randIntDouble: Rand[(Int, Double)] =
    both(int, double)

  val randDoubleInt: Rand[(Double, Int)] =
    both(double, int)

  // 6.7

  // fs: List[ RNG => (A, RNG) ] 
  def sequence[A](fs: List[Rand[A]]): Rand[List[A]] =
    fs match {
      case Nil => unit(List()) // rng => (List(), rng)
      case r :: rs =>
        // r: RNG => (A, RNG)
        // rs: List[Rand[A]] or List[ RNG => (A, RNG) ]
        // return type: Rand[List[A]] or RNG => (List[A], RNG)
        { rng =>
          {
            val (a, rng2) = r(rng)
            val (as, rng3) = sequence(rs)(rng2)
            (a :: as, rng3)
          }
        }
    }

  // since we have map2, we can use foldRight
  // reminder:  foldRight[B](z: B)(op: (A, B) ⇒ B): B
  //               z: start value, type B
  //               B: result of the binary operator. Here B is Rand[List[A]]
  def sequenceViaFoldRight[A](fs: List[Rand[A]]): Rand[List[A]] =
    fs.foldRight(unit(List[A]()))((ra, rb) => map2(ra, rb)(_ :: _))

  def intsViaSequence(n: Int) = sequenceViaFoldRight(List.fill(n)(int))
  
  
  // 6.8

  def flatMap[A, B](f: Rand[A])(g: A => Rand[B]): Rand[B] = 
    rng => {
      val (a, rng2) = f(rng)
      g(a)(rng2)
    }

  // nonNegativeInt: Rand[Int] or RNG => (Int, RNG) 
  def nonNegativeLessThan(n: Int): Rand[Int] = {
    // helper: Int => Rand[Int]
    def helper(i: Int) = {
      rng: RNG =>
        {
          val mod = i % n
          if (i + (n - 1) - mod >= 0) (mod, rng)
          else nonNegativeInt(rng)
        }
    }
    flatMap(nonNegativeInt)(helper)
  }

  def nonNegativeLessThan2(n: Int): Rand[Int] = {
    // helper: Int => Rand[Int]
    def helper(i: Int) = {
      val mod = i % n
      if (i + (n - 1) - mod >= 0) unit(mod)
      else nonNegativeLessThan2(n)
    }
    flatMap(nonNegativeInt)(helper)
  }

  
  // 6.9

  def mapViaflatMap[A, B](s: Rand[A])(f: A => B): Rand[B] = {
    // need function g: A => Rand[B]
    // have f: A => B
    // have unit(s: B) => Rand[B]
    def g = { a: A => unit(f(a)) }
    flatMap(s)(g)
  }

  def map2ViaflatMap[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] =
    {
      // helper: A => Rand[C]
      // map[B,C](s: Rand[B])(f: B => C): Rand[C] 
      def helper = { a: A => map(rb) { b: B => f(a, b) } }
      flatMap(ra)(helper)
    }

}

import State._

case class State[S, +A](run: S => (A, S)) {
  
  // 6.10
  
  def mapv1[B](f: A => B): State[S, B] =
    State(s => {
      val (a, s2) = run(s)
      (f(a), s2)
    })

  def map[B](f: A => B): State[S, B] =
    flatMap { a: A => unit(f(a)) }

  def map2[B, C](sb: State[S, B])(f: (A, B) => C): State[S, C] = {
    flatMap(a => sb.map { b => f(a, b) })
  }

  def flatMap[B](f: A => State[S, B]): State[S, B] =
    State(s => {
      val (a, s2) = run(s)
      // f(a) has type State[S, B] so f(a).run(s2) has type (B, S)
      f(a).run(s2)
    })
}

sealed trait Input
case object Coin extends Input
case object Turn extends Input

case class Machine(locked: Boolean, candies: Int, coins: Int)

object State {
  type Rand[A] = State[RNG, A]

  def unit[S, A](a: A): State[S, A] =
    State(s => (a, s))

  def sequenceViaFoldRight[S, A](sas: List[State[S, A]]): State[S, List[A]] =
    sas.foldRight(unit[S, List[A]](List()))((f, acc) => f.map2(acc)(_ :: _))

  def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] = ???
}
