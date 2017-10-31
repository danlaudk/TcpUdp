import java.util

import scala.collection.mutable.ArrayBuffer


class Database {

  val hlist = new util.Hashtable[String, PersonAccount]
  val alist = new ArrayBuffer[PersonAccount]()
  val inplay = new RandomNums()
  var grandTotal:Double = 0
  def admin(): Unit = {
  }

  def addPerson(first: String, last: String ,accname: String, user: String, pass: String): Unit = {

    val p = new PersonAccount(first, last, accnum = inplay.getNew, accname, user, pass, open=false)

    hlist.put(p.getUser + p.getPass, p)
    p +: alist

  }

  def addPerson(p: PersonAccount): Unit = {
    hlist.put(p.getUser + p.getPass, p)
    p +: alist
  }

  def hasPerson(userpass: String): Boolean = if (hlist.containsKey(userpass)) true
  else false

  def getPerson(userpassword: String): PersonAccount = hlist.get(userpassword)

  def getPerson(i: Int): PersonAccount = null

  def find(s: String): Option[PersonAccount] = {
    alist.filter(p => p.user == s) match {
      case al if al.length==0 => None
      case al => Some(al.head)
    }
  }

  def update(p: PersonAccount): Unit = {
    hlist.replace(p.getHash, p)
    alist.map{ //update
      case personA if p.getHash == personA.getHash => p
      case personA => personA
    }
  }

  def getTotal: Double = alist.foldLeft(0.0)( (acc:Double, p) => acc + p.getAccount.balance)

  def getSize: Int = alist.size

  def getNum: Int = inplay.getNew

  def getUsernames: ArrayBuffer[String] = alist.map(p => p.user)

  def sumUp(): Unit = {
  }

}