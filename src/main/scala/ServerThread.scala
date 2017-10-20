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
                        val server: MulServer_v1) extends Runnable {
  private var closeFlag = false

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
          serverResponse = processInput(userInput)
          System.out.println("Received message from " + Thread.currentThread.getName + " : " + userInput)
          writer.write("Sever Response : " + serverResponse)
          writer.newLine()
          writer.flush()
          if (closeFlag) {
            Client_v1.closeClient
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
