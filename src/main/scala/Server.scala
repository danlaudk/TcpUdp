import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import java.util
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

import scala.collection.mutable.ArrayBuffer


object Server {
  protected var portNumber = 8540
  protected var max_Clients = 3

  def main(args: Array[String]): Unit = {
    val server = new Server(portNumber, max_Clients)
    server.runServer()
  }
}

class Server(val portNumber: Int, val poolSize: Int) {
    private val base = new Database
   var shutdownFlag = false
   var serverSocket: ServerSocket = null
  //  protected val executor: ExecutorService
   val socketList = ArrayBuffer[Socket]()
   var  executor: ExecutorService = null
  def runServer(): Unit = {
    try {
      this.serverSocket = new ServerSocket(this.portNumber)
      executor = Executors.newFixedThreadPool(Server.max_Clients)
    } catch {
      case e: IOException =>
        System.out.println("Could not create server on specific port")
        e.printStackTrace()
    }
    while ( {
      !this.shutdownFlag
    }) try {
      val clientSocket = this.serverSocket.accept
      this.socketList.:+(clientSocket)
      this.executor.submit(new ServerThread(clientSocket, this, base))
    } catch {
      case e: IOException =>
        System.out.println("Couldn't accept on the Socket")
        this.executor.shutdown()
        e.printStackTrace()
    }
    this.shutdownAndAwaitTermination()
  }

   def shutdownAndAwaitTermination(): Unit = {
    System.out.println("Shutting down..")
    executor.shutdown() // Disable new tasks from being submitted

    try // Wait a while for existing tasks to terminate
    if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
      for (s:Socket <- socketList) {
        try
          s.close()
        catch {
          case e: IOException =>
            System.out.println("Couldn't close the socket")
            e.printStackTrace()
        }
      }
      executor.shutdownNow
      // Cancel currently executing tasks
      // Wait a while for tasks to respond to being cancelled
      if (!executor.awaitTermination(10, TimeUnit.SECONDS)) System.err.println("Pool did not terminate")
    }
    catch {
      case ie: InterruptedException =>
        // (Re-)Cancel if current thread also interrupted
        executor.shutdownNow
        // Preserve interrupt status
        Thread.currentThread.interrupt()
    }
    try
      serverSocket.close()
    catch {
      case e: IOException =>
        System.out.println("Serversocket konnte nicht geschlossen werden")
        e.printStackTrace()
    }
    System.out.println("I got here!")
  }
}