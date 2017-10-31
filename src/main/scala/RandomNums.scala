import java.util.Random
import java.util

import scala.collection.mutable.ArrayBuffer
// //todo write as functional


class RandomNums() {
  val r = scala.util.Random
  var list = new Array[Int](1000)

  def getNew: Int = {
    // initialize  if not initialized (empty=true false), and then get new
    System.out.println("komme ich hierhin random line1 ?")

    if (empty) list = list.map { case _ => r.nextInt(8999) + 1000 }
//set breakpoint HERE
    var toReturn = list(accs)
    accs += 1
    if (accs == 1000) {
      empty = true
    } else {
      System.out.println("komme ich hierhin not empty randomnums?")

      empty = false
    }
    return toReturn
  }
//    var n = 0
//    var flag = true
//    while ( {
//      flag
//    }) {
//      System.out.println("komme ich hierhin start random?")
//
//      flag = false
//      n = new Random().nextInt(8999) + 1000
//      System.out.println("komme ich hierhin db mid random?")
//
//      var i = 0
//      while ( {
//        i < list.length
//      }) {
//        if (list(i) == n) flag = true
//
//        {
//          i += 1; i - 1
//        }
//      }
//    }
//    if (empty != 0) {
//      list(empty) = n
//      empty = 0
//    }
//    else list(accs) = n
//    System.out.println("komme ich hierhin db end random?")
//
//    n
//  }

//  def remove(n: Int): Unit = {
//    var i = 0
//    while ( {
//      i < list.length
//    }) {
//      if (list(i) == n) {
//        list(i) = 0
//        empty = i
//        //break //todo: break is not supported. method  remove is not used anyway
//      }
//
//      {
//        i += 1; i - 1
//      }
//    }
//  }

  var accs = 0
  var empty = true
}