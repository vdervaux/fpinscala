package fpinscala.gettingstarted

import MyModule._
import PolymorphicFunctions._

object gettingstarted {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  abs(-42)                                        //> The factorial of 7 is 5040.
                                                  //| res0: Int = 42
   
  fib(1)                                          //> res1: Int = 1
  fib(2)                                          //> res2: Int = 1
  fib(3)                                          //> res3: Int = 2
  fib(4)                                          //> res4: Int = 3
  fib(5)                                          //> res5: Int = 5
  fib(6)                                          //> res6: Int = 8
  fib(7)                                          //> res7: Int = 13
  
 
  val arr = Array(1, 2, 3, 4)                     //> arr  : Array[Int] = Array(1, 2, 3, 4)
  def gt = (x: Int, y: Int) => x > y              //> gt: => (Int, Int) => Boolean
  
  isSorted(arr, gt)                               //> res8: Boolean = true
  
}