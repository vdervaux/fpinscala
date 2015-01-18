package fpinscala.state

import RNG._

object funcstate {
  
  // 6.1
  
  val rng = Simple(12)                            //> rng  : fpinscala.state.RNG.Simple = Simple(12)
  rng.nextInt                                     //> res0: (Int, fpinscala.state.RNG) = (4616986,Simple(302578847015))
  rng.nextInt                                     //> res1: (Int, fpinscala.state.RNG) = (4616986,Simple(302578847015))
  
  val (n1, rng2) = rng.nextInt                    //> n1  : Int = 4616986
                                                  //| rng2  : fpinscala.state.RNG = Simple(302578847015)
  val (n2, rng3) = rng2.nextInt                   //> n2  : Int = -976680786
                                                  //| rng3  : fpinscala.state.RNG = Simple(217467224744870)
  val (n3, rng4) = rng3.nextInt                   //> n3  : Int = 30486735
                                                  //| rng4  : fpinscala.state.RNG = Simple(1997978702265)
  nonNegativeInt1(rng)                            //> res2: (Int, fpinscala.state.RNG) = (4616986,Simple(302578847015))
  
  nonNegativeInt1(rng2)                           //> res3: (Int, fpinscala.state.RNG) = (30486735,Simple(1997978702265))
  nonNegativeInt(rng2)                            //> res4: (Int, fpinscala.state.RNG) = (976680785,Simple(217467224744870))
 
 
  // 6.2
   
  val x: Int = 1                                  //> x  : Int = 1
  x.toDouble                                      //> res5: Double = 1.0
 
  double(rng)                                     //> res6: (Double, fpinscala.state.RNG) = (0.002149951645242959,Simple(302578847
                                                  //| 015))
  
}