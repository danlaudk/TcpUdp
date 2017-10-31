import java.io.Serializable

@SerialVersionUID(101L)
class PersonAccount(val first: String, val last: String, val accnum:Int , val accname: String, val user: String, var pass: String,
                     private var open:Boolean = false) extends Serializable {
  val a = new GameAccount(accname, 0)
  private val hash = first + last
  def getHash: String = hash
  def getAccount: GameAccount = a

  // todo:remove all getters' references
  def getAccNum: Int = accnum

  def getFirst: String = first

  def getLast: String = last




  def getInfo: String = "First: " + first + "\n" + "Last: " + last + "\n" + "Username: " + user + "\n" + "Account Number: " + accnum + "\n" + "Accounts: " + a.name



  def getUser: String = user

  def getPass: String = pass

}