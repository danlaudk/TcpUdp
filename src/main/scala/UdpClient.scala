import java.io._
import java.net._

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

object UdpClient {

  val SIZE = 1024
  val PORT = 58285

  @throws[Exception]
  def main(args: Array[String]): Unit = {
    val stdIn = new BufferedReader(new InputStreamReader(System.in))
    val clientSocket = new DatagramSocket
    val IPAddress = InetAddress.getByName("localhost")
    var sendData = new Array[Byte](SIZE)
    val receiveData = new Array[Byte](SIZE)
    try {
      var userInput = new String()
      while ((userInput = stdIn.readLine) != null) {
        sendData = userInput.getBytes
        val sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT)
        clientSocket.send(sendPacket)
        val receivePacket = new DatagramPacket(receiveData, receiveData.length)
        clientSocket.receive(receivePacket)
        val modifiedSentence = new String(receivePacket.getData).trim
        System.out.println("FROM SERVER:" + modifiedSentence)
      }
      clientSocket.close()
    }
  }
}
