package fpinscala.errorhandling

import Option._

object errorhandling {

  // map
  
  Some(1)                                         //> res0: fpinscala.errorhandling.Some[Int] = Some(1)
  Some(1).map(x => x + 1)                         //> res1: fpinscala.errorhandling.Option[Int] = Some(2)
  Some(1) map (x => x + 2)                        //> res2: fpinscala.errorhandling.Option[Int] = Some(3)
  
  val noneInt = (None: Option[Int])               //> noneInt  : fpinscala.errorhandling.Option[Int] = None
  
  noneInt.map(x => x + 1)                         //> res3: fpinscala.errorhandling.Option[Int] = None

  (None: Option[Int]) map (x => x + 2)            //> res4: fpinscala.errorhandling.Option[Int] = None
  
  // getOrElse
  
  None.getOrElse(1)                               //> res5: Int = 1
  Some(2).getOrElse(1)                            //> res6: Int = 2
  
  // flatMap
  
  def divide100(x: Int): Option[Int] = x match {
    case 0 => None
    case a => Some(100 / a)
  }                                               //> divide100: (x: Int)fpinscala.errorhandling.Option[Int]
  
  Some(2).map(divide100)                          //> res7: fpinscala.errorhandling.Option[fpinscala.errorhandling.Option[Int]] = 
                                                  //| Some(Some(50))
  Some(0).map(divide100)                          //> res8: fpinscala.errorhandling.Option[fpinscala.errorhandling.Option[Int]] = 
                                                  //| Some(None)
  
  Some(2).flatMap(divide100)                      //> res9: fpinscala.errorhandling.Option[Int] = Some(50)
  Some(0).flatMap(divide100)                      //> res10: fpinscala.errorhandling.Option[Int] = None
 
  // orElse
  
  None.orElse(Some(0))                            //> res11: fpinscala.errorhandling.Option[Int] = Some(0)
  Some(1).orElse(Some(0))                         //> res12: fpinscala.errorhandling.Option[Int] = Some(1)
 
  Some(1).getOrElse(Some(0))                      //> res13: Any = 1
  
  // filter
  
  Some(1) filter(x => x > 0)                      //> res14: fpinscala.errorhandling.Option[Int] = Some(1)
  Some(-1) filter(x => x > 0)                     //> res15: fpinscala.errorhandling.Option[Int] = None
  (None: Option[Int]) filter(x => x > 0)          //> res16: fpinscala.errorhandling.Option[Int] = None
  
  
  // traverse test
  
  val l1 = List(1, 2, 3, 4, 5)                    //> l1  : List[Int] = List(1, 2, 3, 4, 5)
  val l2 = List(1, 2, 3, 0, 5)                    //> l2  : List[Int] = List(1, 2, 3, 0, 5)
  
  def plusOne(x: Int): Option[Int] = {
    Some(x + 1)
  }                                               //> plusOne: (x: Int)fpinscala.errorhandling.Option[Int]
  
  traverse(l1)(plusOne)                           //> res17: fpinscala.errorhandling.Option[List[Int]] = Some(List(2, 3, 4, 5, 6))
                                                  //| 
  traverse(l1)(divide100)                         //> res18: fpinscala.errorhandling.Option[List[Int]] = Some(List(100, 50, 33, 25
                                                  //| , 20))
  
  traverse(l2)(plusOne)                           //> res19: fpinscala.errorhandling.Option[List[Int]] = Some(List(2, 3, 4, 1, 6)
                                                  //| )
  traverse(l2)(divide100)                         //> res20: fpinscala.errorhandling.Option[List[Int]] = None


import Either._

  def safeDiv100By(x: Int): Either[Exception, Int] =
    try Right(100 / x)
    catch { case e: Exception => Left(e) }        //> safeDiv100By: (x: Int)fpinscala.errorhandling.Either[Exception,Int]
  
// Either traverse test
  
  traverse(l1)(safeDiv100By)                      //> res21: fpinscala.errorhandling.Either[Exception,List[Int]] = Right(List(100
                                                  //| , 50, 33, 25, 20))
  
  traverse(l2)(safeDiv100By)                      //> res22: fpinscala.errorhandling.Either[Exception,List[Int]] = Left(java.lang
                                                  //| .ArithmeticException: / by zero)
// Either sequence test

  l1 map safeDiv100By                             //> res23: List[fpinscala.errorhandling.Either[Exception,Int]] = List(Right(100
                                                  //| ), Right(50), Right(33), Right(25), Right(20))
  l2 map safeDiv100By                             //> res24: List[fpinscala.errorhandling.Either[Exception,Int]] = List(Right(100
                                                  //| ), Right(50), Right(33), Left(java.lang.ArithmeticException: / by zero), Ri
                                                  //| ght(20))

  sequence(l1 map safeDiv100By)                   //> res25: fpinscala.errorhandling.Either[Exception,List[Int]] = Right(List(100
                                                  //| , 50, 33, 25, 20))
  sequence(l2 map safeDiv100By)                   //> res26: fpinscala.errorhandling.Either[Exception,List[Int]] = Left(java.lang
                                                  //| .ArithmeticException: / by zero)
}