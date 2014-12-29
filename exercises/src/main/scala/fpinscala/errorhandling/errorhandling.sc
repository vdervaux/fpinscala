package fpinscala.errorhandling

import Option._

object errorhandling {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
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
}