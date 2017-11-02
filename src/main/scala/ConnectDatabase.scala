import java.sql.Connection
import java.sql.DriverManager

import java.io.IOException
import java.util.Properties

import org.hsqldb.Server
import org.hsqldb.persist.HsqlProperties
import org.hsqldb.server.ServerAcl


object ConnectDatabase {
  def main(args: Array[String]): Unit = {

    HyperSqlDbServer.start()

    // connect
    try { //Registering the HSQLDB JDBC driver
      Class.forName("org.hsqldb.jdbc.JDBCDriver")
      //Creating the connection with HSQLDB
//      val con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/testdb;ifexists=true", "SA", "")
      val con = DriverManager.getConnection("jdbc:hsqldb:file:intellijData/standalone-test;ifexists=true", "SA", "")
      if (con != null) {
        System.out.println("Connection created successfully")
        val stmt = con.createStatement()

        val result = stmt.executeUpdate("CREATE TABLE tutorials_tbl ( id INT NOT NULL, title VARCHAR(50) NOT NULL,author VARCHAR(20) NOT NULL, submission_date DATE,PRIMARY KEY (id));")
        val result2 = stmt.executeUpdate("INSERT INTO tutorials_tbl VALUES (100,'Learn PHP', 'John Poul', NOW())")
        con.commit
        System.out.println(result2 + " rows effected")
        System.out.println("Rows inserted successfully")

      }
      else System.out.println("Problem with creating connection")
    } catch {
      case e: Exception =>
        e.printStackTrace(System.out)
    }
    HyperSqlDbServer.stop()
  }


  object HyperSqlDbServer  {
    private var server:org.hsqldb.Server = null
    private var running = false

    val tempProperty = new Properties()
    tempProperty.setProperty("server.port", "33555")
    tempProperty.setProperty("server.database.0", "file:intellijData/standalone-test")
    tempProperty.setProperty("server.dbname.0", "testdb")
    tempProperty.setProperty("server.remote_open", "true")
    tempProperty.setProperty("server.silent", "true")
    var properties = new HsqlProperties(tempProperty)


//    def this(props: Properties) {
//      this()
//      properties = new HsqlProperties(props)
//    }

    def isRunning: Boolean = {
      if (server != null) server.checkRunning(running)
      running
    }

    def start(): Unit = {
      if (server == null) {
        server = new org.hsqldb.Server
        try
          server.setProperties(properties)
        catch {
          case e: IOException =>
            e.printStackTrace()
          case e: ServerAcl.AclFormatException =>
            e.printStackTrace()
        }
        server.start
        System.out.println(server.getAddress)
        running = true
      }
    }

    def stop(): Unit = {
      if (server != null) {
        server.stop
        running = false
      }
    }

    def getPhase = 0

    def isAutoStartup = true

    def stop(runnable: Runnable): Unit = {
      stop()
      runnable.run()
    }
  }
}
