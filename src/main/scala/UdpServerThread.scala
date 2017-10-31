import java.io._
import java.net._

class UdpServerThread(// intention is to allow shutdown (and other admin tasks) from udp
                      val server: Server) extends Runnable {
  val SIZE = 1024
  val PORT = 58285
  override def run(): Unit = {
    val socket = new DatagramSocket (PORT)

    while (! server.shutdownFlag) {
//      try {
//        val in = new BufferedReader(new InputStreamReader(socket.getInputStream))
//        val writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream))

        val buffer = new Array[Byte] (SIZE)
      val packet = new DatagramPacket (buffer, buffer.length)
      socket.receive (packet)
      val message = new String (packet.getData).trim
      val ipAddress = packet.getAddress ().toString
      println ("received from " + ipAddress + ": " + message)

      // respond
      socket.send (packet)
    }
  }

}
