import java.util.Random
import java.util
// //todo write as functional


class RandomNums() {
  list = new Array[Int](1000)

  def getNew: Int = {
    var n = 0
    var flag = true
    while ( {
      flag
    }) {
      flag = false
      n = new Random().nextInt(8999) + 1000
      var i = 0
      while ( {
        i < list.length
      }) {
        if (list(i) == n) flag = true

        {
          i += 1; i - 1
        }
      }
    }
    if (empty != 0) {
      list(empty) = n
      empty = 0
    }
    else list(accs) = n
    n
  }

  def remove(n: Int): Unit = {
    var i = 0
    while ( {
      i < list.length
    }) {
      if (list(i) == n) {
        list(i) = 0
        empty = i
        //break //todo: break is not supported
      }

      {
        i += 1; i - 1
      }
    }
  }

  var list: Array[Int] = null
  val accs = 0
  var empty = 0
}