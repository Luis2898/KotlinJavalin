import auth.Session
import desserts.DessertController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import kotliquery.HikariCP
import io.javalin.plugin.rendering.vue.VueComponent

import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory
import org.eclipse.jetty.http2.HTTP2Cipher
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory
import org.eclipse.jetty.server.*
import org.eclipse.jetty.util.ssl.SslContextFactory

fun main() {
    HikariCP.default("jdbc:firebirdsql://localhost:3050/Kotlin?encoding=UTF8", "SYSDBA", "luisito")
    val app = Javalin.create { config ->
        config.server { createHttp2Server() }
        config.enableWebjars()
        config.sessionHandler { Session.sqlSessionHandler()}
    }.start(8080)


    app.routes {
        get("/", VueComponent("home-page"))
        get("/data-table", VueComponent("data-table"))
        get("/login",VueComponent("sign-in-page"))
    }
    app.routes {
        path("/api") {
            crud("/desserts/:id",DessertController())

        }

    }

}

private fun createHttp2Server(): Server {

    val alpn = ALPNServerConnectionFactory().apply {
        defaultProtocol = "h2"
    }

    val sslContextFactory = SslContextFactory().apply {
        keyStorePath = this::class.java.getResource("/keystore.jks").toExternalForm() // replace with your real keystore
        setKeyStorePassword("password") // replace with your real password
        cipherComparator = HTTP2Cipher.COMPARATOR
        provider = "Conscrypt"
    }

    val ssl = SslConnectionFactory(sslContextFactory, alpn.protocol)

    val httpsConfig = HttpConfiguration().apply {
        sendServerVersion = false
        secureScheme = "https"
        securePort = 8443
        addCustomizer(SecureRequestCustomizer())
    }

    val http2 = HTTP2ServerConnectionFactory(httpsConfig)

    val fallback = HttpConnectionFactory(httpsConfig)

    return Server().apply {
        //HTTP/1.1 Connector
        addConnector(ServerConnector(server).apply {
            port = 8080
        })
        // HTTP/2 Connector
        addConnector(ServerConnector(server, ssl, alpn, http2, fallback).apply {
            port = 8443
        })
    }

}