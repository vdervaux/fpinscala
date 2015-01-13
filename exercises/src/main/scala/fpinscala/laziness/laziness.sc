package fpinscala.laziness

import Stream._

object laziness {

  // Stream 1, 2, 3, 4, 5
  val s12345 = cons(1, cons(2, cons(3, cons (4, cons(5, Empty)))))
                                                  //> Evaluating: 0 + 1
                                                  //| s12345  : fpinscala.laziness.Stream[Int] = Cons(<function0>,<function0>)
  Stream(10, 11, 12, 13, 14, 15).toList           //> res0: List[Int] = List(10, 11, 12, 13, 14, 15)
  
  // 5.1
  
  s12345.toListNotTailRec                         //> res1: List[Int] = List(1, 2, 3, 4, 5)

  s12345.toList                                   //> res2: List[Int] = List(1, 2, 3, 4, 5)

  // 5.2

  s12345.take(1).toList                           //> res3: List[Int] = List(1)
  s12345.take(3).toList                           //> res4: List[Int] = List(1, 2, 3)
  s12345.takeTailRecButReversed(1).toList         //> res5: List[Int] = List(1)
  s12345.takeTailRecButReversed(3).toList         //> res6: List[Int] = List(3, 2, 1)

  s12345.dropNotTailRec(-1).toList                //> res7: List[Int] = List(1, 2, 3, 4, 5)
  s12345.dropNotTailRec(1).toList                 //> res8: List[Int] = List(2, 3, 4, 5)
  s12345.dropNotTailRec(3).toList                 //> res9: List[Int] = List(4, 5)

  s12345.dropTailRec1(-1).toList                  //> res10: List[Int] = List(1, 2, 3, 4, 5)
  s12345.dropTailRec1(1).toList                   //> res11: List[Int] = List(2, 3, 4, 5)
  s12345.dropTailRec1(3).toList                   //> res12: List[Int] = List(4, 5)

  s12345.drop(-1).toList                          //> res13: List[Int] = List(1, 2, 3, 4, 5)
  s12345.drop(1).toList                           //> res14: List[Int] = List(2, 3, 4, 5)
  s12345.drop(3).toList                           //> res15: List[Int] = List(4, 5)

  // 5.3
  
  Stream(2, 4, 6, 7, 8, 9).takeWhile(_ < 5).toList//> res16: List[Int] = List(2, 4)
  
  // 5.4
 
  Stream(2, 4, 6, 8).forAll(_ % 2 == 0)           //> res17: Boolean = true
  Stream(2, 4, 5, 6, 8).forAll(_ % 2 == 0)        //> res18: Boolean = false
  Stream(2, 4, 6, 8, 9).forAll(_ % 2 == 0)        //> res19: Boolean = false
  
  // 5.5
  
  Stream(2, 4, 6, 7, 8, 9).takeWhileUsingFoldRight(_ < 5).toList
                                                  //> res20: List[Int] = List(2, 4)
                                                  
  // 5.6
  
  Stream(1, 2).headOption                         //> res21: Option[Int] = Some(1)
  Stream(1).headOption                            //> res22: Option[Int] = Some(1)
  Empty.headOption                                //> res23: Option[Nothing] = None
  
  
  // 5.7
  
  // map
  Stream(1, 2, 3).map(x => x *2).toList           //> res24: List[Int] = List(2, 4, 6)
  
  // filter
  Stream(1, 2, 3, 4, 5, 6).filter(_ % 2 == 0).toList
                                                  //> res25: List[Int] = List(2, 4, 6)
  // append
  Stream(1, 2, 3).append(Stream(4, 5, 6)).toList  //> res26: List[Int] = List(1, 2, 3, 4, 5, 6)
  
  // flatMap
  def threeOf(x: Int) = Stream(x, x, x)           //> threeOf: (x: Int)fpinscala.laziness.Stream[Int]
  Stream(1, 2, 3).flatMap(threeOf).toList         //> res27: List[Int] = List(1, 1, 1, 2, 2, 2, 3, 3, 3)

  
  // infinite streams
  
  ones.take(5).toList                             //> res28: List[Int] = List(1, 1, 1, 1, 1)
  ones.exists(_ % 2 != 0)                         //> res29: Boolean = true
  ones.map(_ + 1).exists(_ % 2 == 0)              //> res30: Boolean = true
  ones.takeWhile(_ != 1)                          //> res31: fpinscala.laziness.Stream[Int] = Empty
  ones.forAll(_ != 1)                             //> res32: Boolean = false

  // 5.8
  
  constant(3).take(5).toList                      //> res33: List[Int] = List(3, 3, 3, 3, 3)
  constant1(3).take(5).toList                     //> res34: List[Int] = List(3, 3, 3, 3, 3)
  
  // 5.9
 
  from(10).take(10).toList                        //> res35: List[Int] = List(10, 11, 12, 13, 14, 15, 16, 17, 18, 19)
 
  // 5.10
  
  fibs.take(5).toList                             //> Evaluating: 1 + 1
                                                  //| Evaluating: 1 + 2
                                                  //| Evaluating: 2 + 3
                                                  //| Evaluating: 3 + 5
                                                  //| Evaluating: 5 + 8
                                                  //| res36: List[Int] = List(0, 1, 1, 2, 3)
  fibs.take(10).toList                            //> Evaluating: 8 + 13
                                                  //| Evaluating: 13 + 21
                                                  //| Evaluating: 21 + 34
                                                  //| Evaluating: 34 + 55
                                                  //| Evaluating: 55 + 89
                                                  //| res37: List[Int] = List(0, 1, 1, 2, 3, 5, 8, 13, 21, 34)
}