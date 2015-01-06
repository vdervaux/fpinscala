package fpinscala.laziness

import Stream._

object laziness {

  // Stream 1, 2, 3, 4, 5
  val s12345 = cons(1, cons(2, cons(3, cons (4, cons(5, Empty)))))
                                                  //> s12345  : fpinscala.laziness.Stream[Int] = Cons(<function0>,<function0>)
  
  // 5.1
  
  s12345.toListNotTailRec                         //> res0: List[Int] = List(1, 2, 3, 4, 5)

  s12345.toList                                   //> res1: List[Int] = List(1, 2, 3, 4, 5)

  // 5.2

  s12345.take(1).toList                           //> res2: List[Int] = List(1)
  s12345.take(3).toList                           //> res3: List[Int] = List(1, 2, 3)
  s12345.takeTailRecButReversed(1).toList         //> res4: List[Int] = List(1)
  s12345.takeTailRecButReversed(3).toList         //> res5: List[Int] = List(3, 2, 1)

  s12345.dropNotTailRec(-1).toList                //> res6: List[Int] = List(1, 2, 3, 4, 5)
  s12345.dropNotTailRec(1).toList                 //> res7: List[Int] = List(2, 3, 4, 5)
  s12345.dropNotTailRec(3).toList                 //> res8: List[Int] = List(4, 5)

  s12345.dropTailRec1(-1).toList                  //> res9: List[Int] = List(1, 2, 3, 4, 5)
  s12345.dropTailRec1(1).toList                   //> res10: List[Int] = List(2, 3, 4, 5)
  s12345.dropTailRec1(3).toList                   //> res11: List[Int] = List(4, 5)

  s12345.drop(-1).toList                          //> res12: List[Int] = List(1, 2, 3, 4, 5)
  s12345.drop(1).toList                           //> res13: List[Int] = List(2, 3, 4, 5)
  s12345.drop(3).toList                           //> res14: List[Int] = List(4, 5)

}