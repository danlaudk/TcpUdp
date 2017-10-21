@SerialVersionUID(100L)
class GameAccount(val name: String, var balance:Double) extends Serializable {
  def deposit(cash: Double): Unit = {
    balance += cash
  }

  def withdraw(cash: Double) =
    if (balance >= cash) {
      balance -= cash
      cash
    }
    else .0


}