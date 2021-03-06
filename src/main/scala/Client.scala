import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

object Client {
  val HOSTNAME = "localhost"
  val PORTNUMBER = 8540
  private var clientClose = false
  private var person:PersonAccount = null

  @throws[IOException]
  def main(args: Array[String]): Unit = {
    System.out.println("Client started")
    try {
      val socket = new Socket(HOSTNAME, PORTNUMBER)
      val out = new PrintWriter(socket.getOutputStream, true)
      // InputStream test = echoSocket.getInputStream();
      val in = new BufferedReader(new InputStreamReader(socket.getInputStream))
      import java.io.ObjectInputStream
      //val input = new ObjectInputStream(socket.getInputStream) // this prevents echo
      val stdIn = new BufferedReader(new InputStreamReader(System.in))
      try {
        var userInput = new String()
        while ((userInput = stdIn.readLine) != null && !clientClose) {
          out.println(userInput)
          System.out.println("echo: " + in.readLine)
//          try {
//            val o:Object = input.readObject
//            if (o.isInstanceOf[PersonAccount]) {
//              person = o.asInstanceOf[PersonAccount]
//              System.out.println("person registered ")
//            }
//          } catch {
//            case _: ClassNotFoundException =>
//          }
          // if (userInput.equals("BYE")) {
          // break;
          // }
        }
      } catch {
        case e: java.net.UnknownHostException =>
          System.err.println("Don't know about host " + HOSTNAME)
          System.exit(1)
        case e: IOException =>
          System.err.println("Couldn't get I/O for the connection to " + HOSTNAME)
          System.exit(1)
      } finally {
        if (socket != null) socket.close()
        if (out != null) out.close()
        if (in != null) in.close()
        if (stdIn != null) stdIn.close()
      }
    }
  }

   def closeClient(): Unit = {
    clientClose = true
  }
}
