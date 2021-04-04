package auth

import org.eclipse.jetty.server.session.*
import java.io.File

object Session {



// You can learn more about session handling here: https://javalin.io/tutorials/jetty-session-handling

    // session are stored in a file - this works well on localhost, but consider changing if you're deploying your app
    fun fileSessionHandler() = SessionHandler().apply {
        httpOnly = true
        sessionCache = DefaultSessionCache(this).apply {
            sessionDataStore = FileSessionDataStore().apply {
                val baseDir = File(System.getProperty("java.io.tmpdir"))
                this.storeDir = File(baseDir, "kat-session-store").apply { mkdir() }
            }
        }
    }

    // use this if you need a session database
    fun sqlSessionHandler() = SessionHandler().apply {
        sessionCache = DefaultSessionCache(this).apply { // use a NullSessionCache if you don't have sticky sessions
            sessionDataStore = JDBCSessionDataStoreFactory().apply {
                setDatabaseAdaptor(DatabaseAdaptor().apply {
                    setDriverInfo("org.firebirdsql.jdbc.FBDriver", "jdbc:firebirdsql://localhost:3050/Sessions?encoding=UTF8&user=SYSDBA&password=luisito")
                })
            }.getSessionDataStore(sessionHandler)
        }
        httpOnly = true
      
    }


}