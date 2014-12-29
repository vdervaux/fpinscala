package fpinscala.datastructures

import List._
import Tree._

/*
 * Chapter 3 worksheet
 */
object datastructures {

  // Exercise 2.1
  val x = List(1, 2, 3, 4, 5) match {
    case Cons(x, Cons(2, Cons(4, _))) => x
    case Nil => 42
    case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
    case Cons(h, t) => h + sum(t)
    case _ => 101
  }                                               //> x  : Int = 3
  
  tail(List(1, 2, 3, 4, 5))                       //> res0: fpinscala.datastructures.List[Int] = Cons(2,Cons(3,Cons(4,Cons(5,Nil))
                                                  //| ))

  setHead(List(5, 2, 3, 4), 1)                    //> res1: fpinscala.datastructures.List[Int] = Cons(1,Cons(2,Cons(3,Cons(4,Nil))
                                                  //| ))

  drop(List(1, 2, 3, 4, 5), 2)                    //> res2: fpinscala.datastructures.List[Int] = Cons(3,Cons(4,Cons(5,Nil)))
  drop(List(1, 2, 3, 4, 5), 4)                    //> res3: fpinscala.datastructures.List[Int] = Cons(5,Nil)
  drop(List(1, 2, 3, 4, 5), 5)                    //> res4: fpinscala.datastructures.List[Int] = Nil
  
  dropWhile(List(1, 2, 3, 4, 5), (x: Int) => x < 4)
                                                  //> res5: fpinscala.datastructures.List[Int] = Cons(4,Cons(5,Nil))
  dropWhile(List(1, 2, 3, 4, 5, 4, 3), (x: Int) => x < 4)
                                                  //> res6: fpinscala.datastructures.List[Int] = Cons(4,Cons(5,Cons(4,Cons(3,Nil))
                                                  //| ))
  dropWhile(List(4, 5), (x: Int) => x < 2)        //> res7: fpinscala.datastructures.List[Int] = Cons(4,Cons(5,Nil))


  init(List(1, 2, 3, 4, 5))                       //> res8: fpinscala.datastructures.List[Int] = Cons(1,Cons(2,Cons(3,Cons(4,Nil))
                                                  //| ))
  init(List(1))                                   //> res9: fpinscala.datastructures.List[Int] = Nil
  
  foldRight(List(1, 2, 3), Nil:List[Int])(Cons(_,_))
                                                  //> res10: fpinscala.datastructures.List[Int] = Cons(1,Cons(2,Cons(3,Nil)))
  
  length(List(1, 2, 3, 4))                        //> res11: Int = 4
  length(Nil)                                     //> res12: Int = 0
  length(Cons(1, Nil))                            //> res13: Int = 1

  foldLeft(List(1, 2, 3, 4), 0)(_ + _)            //> res14: Int = 10
  foldLeft(List(1, 2, 3, 4), 1)(_ * _)            //> res15: Int = 24
  
  
  def sumLeft(l: List[Int]) = foldLeft(l, 0)(_ + _)
                                                  //> sumLeft: (l: fpinscala.datastructures.List[Int])Int
  sumLeft(List(1, 2, 3, 4))                       //> res16: Int = 10
  
  def productLeft(l: List[Int]) = foldLeft(l, 1.0)(_ * _)
                                                  //> productLeft: (l: fpinscala.datastructures.List[Int])Double
  
  productLeft(List(1, 2, 3, 4))                   //> res17: Double = 24.0
  
  def lengthLeft[A](l: List[A]) = foldLeft(l, 0)((len, _) => len + 1)
                                                  //> lengthLeft: [A](l: fpinscala.datastructures.List[A])Int

  lengthLeft(List(1, 2, 3, 4))                    //> res18: Int = 4


  // 3.12

  def reverse[A](l: List[A]): List[A] = l match {
    case Nil => Nil
    case Cons(h, Nil) => Cons(h, Nil)
    case Cons(h, t) => append(reverse(t), Cons(h, Nil))
  }                                               //> reverse: [A](l: fpinscala.datastructures.List[A])fpinscala.datastructures.L
                                                  //| ist[A]
  reverse(List(1, 2, 3, 4))                       //> res19: fpinscala.datastructures.List[Int] = Cons(4,Cons(3,Cons(2,Cons(1,Nil
                                                  //| ))))
  
  
  def reverseUsingFold[A](l: List[A]): List[A] =
    foldLeft(l, Nil: List[A])((t,h) => Cons(h,t)) //> reverseUsingFold: [A](l: fpinscala.datastructures.List[A])fpinscala.datastr
                                                  //| uctures.List[A]
  reverseUsingFold(List(1, 2, 3, 4))              //> res20: fpinscala.datastructures.List[Int] = Cons(4,Cons(3,Cons(2,Cons(1,Nil
                                                  //| ))))
  
  
  // 3.13
  
  def foldRightViaFoldLeft[A, B](l: List[A], z: B)(f: (A, B) => B): B =
    foldLeft(reverse(l), z)((x,y) => f(y,x))      //> foldRightViaFoldLeft: [A, B](l: fpinscala.datastructures.List[A], z: B)(f: 
                                                  //| (A, B) => B)B
 
  
  // 3.14
  
  append(List(1, 2, 3), List(4, 5))               //> res21: fpinscala.datastructures.List[Int] = Cons(1,Cons(2,Cons(3,Cons(4,Con
                                                  //| s(5,Nil)))))
  
  def appendUsingFoldRight[A](a1: List[A], a2: List[A]): List[A] =
    foldRight(a1, a2)(Cons(_,_))                  //> appendUsingFoldRight: [A](a1: fpinscala.datastructures.List[A], a2: fpinsca
                                                  //| la.datastructures.List[A])fpinscala.datastructures.List[A]
    
  appendUsingFoldRight(List(1, 2, 3), List(4, 5)) //> res22: fpinscala.datastructures.List[Int] = Cons(1,Cons(2,Cons(3,Cons(4,Con
                                                  //| s(5,Nil)))))
                                                  
  // 3.15
  def concat[A](l: List[List[A]]): List[A] = l match {
    case Nil => Nil
    case Cons(h, Nil) => h
    case Cons(h, t) => append(h, concat(t))
  }                                               //> concat: [A](l: fpinscala.datastructures.List[fpinscala.datastructures.List[
                                                  //| A]])fpinscala.datastructures.List[A]
  concat(List(
      List(1, 2, 3),
      List(4, 5, 6)
  ))                                              //> res23: fpinscala.datastructures.List[Int] = Cons(1,Cons(2,Cons(3,Cons(4,Con
                                                  //| s(5,Cons(6,Nil))))))
  
  def concat2[A](l: List[List[A]]): List[A] =
    foldRight(l, Nil: List[A])(append)            //> concat2: [A](l: fpinscala.datastructures.List[fpinscala.datastructures.List
                                                  //| [A]])fpinscala.datastructures.List[A]
  concat2(List(
      List(1, 2, 3),
      List(4, 5, 6)
  ))                                              //> res24: fpinscala.datastructures.List[Int] = Cons(1,Cons(2,Cons(3,Cons(4,Con
                                                  //| s(5,Cons(6,Nil))))))
  
  // 3.16
  
  def add1(l: List[Int]): List[Int] = l match {
    case Nil => Nil
    case Cons(h, t) => Cons(h+1, add1(t))
  }                                               //> add1: (l: fpinscala.datastructures.List[Int])fpinscala.datastructures.List[
                                                  //| Int]
  add1(List(1, 2, 3))                             //> res25: fpinscala.datastructures.List[Int] = Cons(2,Cons(3,Cons(4,Nil)))
  
  def add1ViaFoldRight(l: List[Int]): List[Int] =
    foldRight(l, Nil: List[Int])((h,t) => Cons(h+1, t))
                                                  //> add1ViaFoldRight: (l: fpinscala.datastructures.List[Int])fpinscala.datastru
                                                  //| ctures.List[Int]
  add1ViaFoldRight(List(1, 2, 3))                 //> res26: fpinscala.datastructures.List[Int] = Cons(2,Cons(3,Cons(4,Nil)))

  
  // 3.17
  
  def doubleToString(l: List[Double]): List[String] =
    foldRight(l, Nil:List[String])((h,t) => Cons(h.toString, t))
                                                  //> doubleToString: (l: fpinscala.datastructures.List[Double])fpinscala.datastr
                                                  //| uctures.List[String]
  
  
  doubleToString(List(1, 2, 3))                   //> res27: fpinscala.datastructures.List[String] = Cons(1.0,Cons(2.0,Cons(3.0,N
                                                  //| il)))
 
  // 3.18
  // use foldRight ViafoldLeft since foldLeft is tail recursive, not foldRight
  
  def map[A,B](as: List[A])(f: A => B): List[B] =
    foldRightViaFoldLeft(as, Nil:List[B])((h, t) => Cons(f(h), t))
                                                  //> map: [A, B](as: fpinscala.datastructures.List[A])(f: A => B)fpinscala.datas
                                                  //| tructures.List[B]
  map(List(1, 2, 3))(_ + 1)                       //> res28: fpinscala.datastructures.List[Int] = Cons(2,Cons(3,Cons(4,Nil)))

  map(List(1.0, 2.0, 3.0))(_.toString)            //> res29: fpinscala.datastructures.List[String] = Cons(1.0,Cons(2.0,Cons(3.0,N
                                                  //| il)))

  // 3.19
  
  def filter1[A](as: List[A])(f: A => Boolean): List[A] =
    as match {
      case Nil => Nil
      case Cons(h, t) => if (f(h)) Cons(h, filter1(t)(f))
                         else filter1(t)(f)
    }                                             //> filter1: [A](as: fpinscala.datastructures.List[A])(f: A => Boolean)fpinscal
                                                  //| a.datastructures.List[A]

  filter1(List(1, 2, 3, 4, 5, 6))(x => (x % 2 == 0))
                                                  //> res30: fpinscala.datastructures.List[Int] = Cons(2,Cons(4,Cons(6,Nil)))

  def filter[A](as: List[A])(f: A => Boolean): List[A] =
    foldRightViaFoldLeft(as, Nil:List[A])((h, t) => if (f(h)) Cons(h, t) else t)
                                                  //> filter: [A](as: fpinscala.datastructures.List[A])(f: A => Boolean)fpinscala
                                                  //| .datastructures.List[A]
  filter(List(1, 2, 3, 4, 5, 6))(x => (x % 2 == 0))
                                                  //> res31: fpinscala.datastructures.List[Int] = Cons(2,Cons(4,Cons(6,Nil)))


  // 3.20
  
  def flatMap[A,B](as: List[A])(f: A => List[B]): List[B] =
    foldRight(as, Nil: List[B])((h,t) => concat( List(f(h), t) ))
                                                  //> flatMap: [A, B](as: fpinscala.datastructures.List[A])(f: A => fpinscala.dat
                                                  //| astructures.List[B])fpinscala.datastructures.List[B]
  flatMap(List(1, 2, 3))(i => List(i, i))         //> res32: fpinscala.datastructures.List[Int] = Cons(1,Cons(1,Cons(2,Cons(2,Con
                                                  //| s(3,Cons(3,Nil))))))

  def flatMap2[A,B](as: List[A])(f: A => List[B]): List[B] =
    concat(map(as)(f))                            //> flatMap2: [A, B](as: fpinscala.datastructures.List[A])(f: A => fpinscala.da
                                                  //| tastructures.List[B])fpinscala.datastructures.List[B]
  flatMap2(List(1, 2, 3))(i => List(i, i))        //> res33: fpinscala.datastructures.List[Int] = Cons(1,Cons(1,Cons(2,Cons(2,Con
                                                  //| s(3,Cons(3,Nil))))))

  // 3.21
  
  def filterViaFlatMap[A](as: List[A])(f: A => Boolean): List[A] =
    flatMap(as)(a => if (f(a)) List(a) else Nil)  //> filterViaFlatMap: [A](as: fpinscala.datastructures.List[A])(f: A => Boolean
                                                  //| )fpinscala.datastructures.List[A]
  filterViaFlatMap(List(1, 2, 3, 4, 5, 6))(x => (x % 2 == 0))
                                                  //> res34: fpinscala.datastructures.List[Int] = Cons(2,Cons(4,Cons(6,Nil)))
                                                  
  // 3.22
  
  def addElements(as: List[Int], bs: List[Int]): List[Int] =
    as match {
      case Nil => bs match {
        case Nil => Nil
        case Cons(_,_) => sys.error("lengths did not match")
      }
      case Cons(ha, ta) => bs match {
        case Nil => sys.error("lengths did not match")
        case Cons(hb, tb) => Cons(ha+hb, addElements(ta, tb))
      }
    }                                             //> addElements: (as: fpinscala.datastructures.List[Int], bs: fpinscala.datastr
                                                  //| uctures.List[Int])fpinscala.datastructures.List[Int]
  addElements(List(1, 2, 3), List(4, 5, 6))       //> res35: fpinscala.datastructures.List[Int] = Cons(5,Cons(7,Cons(9,Nil)))
  
  
  def addElements2(as: List[Int], bs: List[Int]): List[Int] =
    (as, bs) match {
      case (Nil, _) => Nil
      case (_, Nil) => Nil
      case (Cons(ha, ta), Cons(hb, tb)) => Cons(ha + hb, addElements2(ta, tb))
    }                                             //> addElements2: (as: fpinscala.datastructures.List[Int], bs: fpinscala.datast
                                                  //| ructures.List[Int])fpinscala.datastructures.List[Int]
  
  addElements2(List(1, 2, 3), List(4, 5, 6))      //> res36: fpinscala.datastructures.List[Int] = Cons(5,Cons(7,Cons(9,Nil)))
  
  
  // 3.23
  
  def zipWith[A,B](a1: List[A], a2: List[A])(f: (A,A) => B): List[B] =
    (a1, a2) match {
      case (Nil, Nil) => Nil
      case (Nil, _) => sys.error("lengths did not match")
      case (_, Nil) => sys.error("lengths did not match")
      case (Cons(a1, a1s), Cons(a2, a2s)) => Cons(f(a1, a2), zipWith(a1s, a2s)(f))
    }                                             //> zipWith: [A, B](a1: fpinscala.datastructures.List[A], a2: fpinscala.datastr
                                                  //| uctures.List[A])(f: (A, A) => B)fpinscala.datastructures.List[B]

  zipWith(List(1,2,3), List(4,5,6))(_ + _)        //> res37: fpinscala.datastructures.List[Int] = Cons(5,Cons(7,Cons(9,Nil)))


  // 3.24

  def hasSubsequence[A](sup: List[A], sub: List[A]): Boolean = {
    @annotation.tailrec
    def seqHelper[A](sup: List[A], sub: List[A], enteredSequence: Boolean): Boolean =
      (sup, sub) match {
        case (_, Nil) => true
        case (Nil, Cons(_, _)) => false
        case (Cons(h1, t1), Cons(h2, t2)) =>
          if (h1 == h2)
            seqHelper(t1, t2, true)
          else if (enteredSequence) false
          else seqHelper(t1, Cons(h2, t2), false)
      }
    seqHelper(sup, sub, false)
  }                                               //> hasSubsequence: [A](sup: fpinscala.datastructures.List[A], sub: fpinscala.d
                                                  //| atastructures.List[A])Boolean
  
  
  hasSubsequence(Nil, Nil)                        //> res38: Boolean = true
  hasSubsequence(List(1), Nil)                    //> res39: Boolean = true
  hasSubsequence(Nil, List(1))                    //> res40: Boolean = false
 
  hasSubsequence(List(1, 2, 3, 4), List(1, 2))    //> res41: Boolean = true
  hasSubsequence(List(1, 2, 3, 4), List(2, 3))    //> res42: Boolean = true
  hasSubsequence(List(1, 2, 3, 4), List(4))       //> res43: Boolean = true
  
  hasSubsequence(List(1, 2, 3, 4), List(1, 3))    //> res44: Boolean = false
  
  hasSubsequence(List(1, 2, 3, 4, 5), List(4, 5, 1))
                                                  //> res45: Boolean = false
                                                  
  // 3.25
  
  val ab = Branch(Leaf("a"), Leaf("b"))           //> ab  : fpinscala.datastructures.Branch[String] = Branch(Leaf(a),Leaf(b))
  val cd = Branch(Leaf("c"), Leaf("d"))           //> cd  : fpinscala.datastructures.Branch[String] = Branch(Leaf(c),Leaf(d))
  val abcd = Branch(ab, cd)                       //> abcd  : fpinscala.datastructures.Branch[String] = Branch(Branch(Leaf(a),Lea
                                                  //| f(b)),Branch(Leaf(c),Leaf(d)))
  
  def size[A](t: Tree[A]): Int = t match {
    case Leaf(_) => 1
    case Branch(l, r) => 1 + size(l) + size(r)
  }                                               //> size: [A](t: fpinscala.datastructures.Tree[A])Int
  
  size(Leaf(1))                                   //> res46: Int = 1
  size(ab)                                        //> res47: Int = 3
  size(abcd)                                      //> res48: Int = 7


  // 3.26
  
  def maximum(t: Tree[Int]): Int = t match {
    case Leaf(x) => x
    case Branch(l, r) => maximum(l) max maximum(r)
  }                                               //> maximum: (t: fpinscala.datastructures.Tree[Int])Int
  
  maximum(Branch(Branch(Leaf(1), Leaf(2)), Branch(Leaf(3), Leaf(4))))
                                                  //> res49: Int = 4

  // 3.27
  
  def depth[A](t: Tree[A]): Int = t match {
    case Leaf(x) => 1
    case Branch(l, r) => 1 + (depth(l)) max (depth(r))
  }                                               //> depth: [A](t: fpinscala.datastructures.Tree[A])Int
  
  depth(abcd)                                     //> res50: Int = 3
  
  
  // 3.28
  
  def mapt[A,B](t: Tree[A])(f: A => B): Tree[B] = t match {
    case Leaf(x) => Leaf(f(x))
    case Branch(l, r) => Branch(mapt(l)(f), mapt(r)(f))
  }                                               //> mapt: [A, B](t: fpinscala.datastructures.Tree[A])(f: A => B)fpinscala.datas
                                                  //| tructures.Tree[B]
  
  mapt(Branch(Branch(Leaf(1), Leaf(2)), Branch(Leaf(3), Leaf(4))))(x => x+1)
                                                  //> res51: fpinscala.datastructures.Tree[Int] = Branch(Branch(Leaf(2),Leaf(3)),
                                                  //| Branch(Leaf(4),Leaf(5)))
  
  // 3.29
  
 
  def fold[A,B](t: Tree[A])(f: A => B)(g: (B,B) => B): B = t match {
    case Leaf(x) => f(x)
    case Branch(l, r) => g(fold(l)(f)(g), fold(r)(f)(g))
  }                                               //> fold: [A, B](t: fpinscala.datastructures.Tree[A])(f: A => B)(g: (B, B) => B
                                                  //| )B
  /*
     From answers: Like `foldRight` for lists, `fold` receives a "handler" for each of the data constructors of the type,
     and recursively accumulates some value using these handlers.
     As with `foldRight`, `fold(t)(Leaf(_))(Branch(_,_)) == t`,
     and we can use this function to implement just about any recursive function that would otherwise be defined by pattern matching.
     
     1st function: what to do for a Leaf
     2nd function: what to do for a Branch
  */

  def sizeViaFold[A](t: Tree[A]): Int =
    fold(t)(t => 1)(1 + _ + _)                    //> sizeViaFold: [A](t: fpinscala.datastructures.Tree[A])Int
    
  sizeViaFold(abcd)                               //> res52: Int = 7
  
  
  def maxViaFold(t: Tree[Int]): Int =
    fold(t)(a => a)(_ max _)                      //> maxViaFold: (t: fpinscala.datastructures.Tree[Int])Int
  
  maxViaFold(Branch(Branch(Leaf(1), Leaf(2)), Branch(Leaf(3), Leaf(4))))
                                                  //> res53: Int = 4
                                                  
  def depthViaFold[A](t: Tree[A]): Int =
    fold(t)(a => 1)((a,b) => 1 + (a max b))       //> depthViaFold: [A](t: fpinscala.datastructures.Tree[A])Int

  depthViaFold(abcd)                              //> res54: Int = 3
  
  
  def mapViaFold[A,B](t: Tree[A])(f: A => B): Tree[B] =
    fold(t)(a => Leaf(f(a)): Tree[B])((a,b) => Branch(a,b))
                                                  //> mapViaFold: [A, B](t: fpinscala.datastructures.Tree[A])(f: A => B)fpinscala
                                                  //| .datastructures.Tree[B]
  mapViaFold(Branch(Branch(Leaf(1), Leaf(2)), Branch(Leaf(3), Leaf(4))))(x => x+1)
                                                  //> res55: fpinscala.datastructures.Tree[Int] = Branch(Branch(Leaf(2),Leaf(3)),
                                                  //| Branch(Leaf(4),Leaf(5)))
}