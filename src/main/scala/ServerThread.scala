import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.UnsupportedEncodingException
import java.net.Socket
import java.util.regex.Pattern


class ServerThread(// for shutdown password = "password";
                        val client: Socket,
                        val server: Server,
                   val base: Database) extends Runnable {
  private var closeFlag = false

  import java.io.ObjectInputStream
  import java.io.ObjectOutputStream
  import java.net.Socket
  import java.util



  private var newAccounts = null
  private var person:PersonAccount = null

  override def run(): Unit = {
    try {
      val in = new BufferedReader(new InputStreamReader(client.getInputStream))
      val writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream))

      try {
        System.out.println("Thread started with name:" + Thread.currentThread.getName)
        var userInput = new String()
        var serverResponse  = new String()
        while ( {
          (userInput = in.readLine) != null
        }) {
          serverResponse = processInput2(userInput)
          System.out.println("Received message from " + Thread.currentThread.getName + " : " + userInput)
          writer.write("Sever Response : " + serverResponse)
          writer.newLine()
          writer.flush()
          if (closeFlag) {
            Client.closeClient
            server.socketList.-=(client)
            client.close()
            server.shutdownAndAwaitTermination()
          }
        }
      } catch {
        case e: IOException =>
          System.out.println("I/O exception: " + e)
        case ex: Exception =>
          System.out.println("Exception in Thread Run. Exception : " + ex)
      } finally {
        if (in != null) in.close()
        if (writer != null) writer.close()
      }
    }
  }

  import scala.util.control.Exception.allCatch
  def processInput2(input: String):String = input match {
    case input if input == "person" => {

      receive(Array("sdf", "adsf", "xcv", "dfe", "vde", "useless"))
      System.out.println("komme ich hierhin processinput2?")
      "person received"
    }
    case input if input == "update" => {
      if (base.hasPerson(input)) {
        sendPerson(base.getPerson(input))
        person = base.getPerson(input)
        return "display person"
      } else return "no such person"
    }
    case input => allCatch.opt(input.toInt) match {
      case Some(i) => {
        receive(i)
        "int deposited or received"
      }
      case None => {
        input match {
          case _@("false" | "true") => {
            Client.closeClient
            server.socketList.-=(client)
            client.close()
            "connection closed"
          }: String

        }
      }
    }
  }


  import java.io.IOException

  @throws[IOException]
  @throws[ClassNotFoundException]
  def receive(info: Array[String]): Unit = {
    System.out.println("komme ich hierhin db receive?")

    base.addPerson(info(0), info(1), info(2), info(3), info(4))
  }


  @throws[IOException]
  @throws[ClassNotFoundException]
  def receive(s: String): Unit = {
    if (base.hasPerson(s)) {
      sendPerson(base.getPerson(s))
      person = base.getPerson(s)
    }
  }

  import java.io.IOException

  @throws[IOException]
  def receive(n: Int): Unit = {
    if (n > 0) {
      person.a.deposit(n)
      sendPerson(person)
    }
    else if (n < 0) {
      person.getAccount.withdraw(n * -1)
      sendPerson(person)
    }
    else {
    }
  }

  private def sendPerson(p: PersonAccount): Unit = {
    try {
      val output = new ObjectOutputStream(client.getOutputStream()) // testing
      output.writeObject(p)
      output.flush
    } catch {
      case ioException: IOException =>

    }

  }

  def processInput(input: String): String = {
    var commandFound = false
    var output = ""
    try
        if (input.getBytes("UTF-8").length > 255) output = "Max string length exceeded"
    catch {
      case e: UnsupportedEncodingException =>
        // TODO Auto-generated catch block
        e.printStackTrace()
    }
    val allPattern = Pattern.compile("(?<lower>^LOWERCASE\\s.+)|(?<upper>^UPPERCASE\\s.+)|(?<reverse>^REVERSE\\s.+)|(?<bye>^BYE)|(?<shutdown>^SHUTDOWN password)")
    val allMatcher = allPattern.matcher(input)
    if (allMatcher.find) {
      val lower = allMatcher.group("lower")
      val upper = allMatcher.group("upper")
      val reverse = allMatcher.group("reverse")
      val bye = allMatcher.group("bye")
      val shutdown = allMatcher.group("shutdown")
      commandFound = true
      if (lower != null) output = lower.substring(10).toLowerCase
      if (upper != null) output = upper.substring(10).toUpperCase
      if (reverse != null) output = new StringBuilder(reverse.substring(8)).reverse.toString
      if (bye != null) {
        output = "BYE"
        closeFlag = true
      }
      if (shutdown != null) {
        output = "SHUTDOWN"
        server.shutdownFlag = true
        closeFlag = true
      }
    }
    else {
      commandFound = false
      output = "UNKNOWN COMMAND"
    }
    if (commandFound) output = "OK ".concat(output)
    else output = "ERROR ".concat(output)
    output
  }
}
