package ca.uhn.fhir.jaxrs.server.example

import ca.uhn.fhir.test.utilities.JettyUtil
import ca.uhn.fhir.util.TestUtil
import cn.uhn.fhir.jaxrs.server.example.ExtendedOrganizationResource
import org.apache.commons.lang3.StringUtils
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.hamcrest.CoreMatchers.`is`
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher
import org.junit.AfterClass
import org.junit.Assert.assertThat
import org.junit.BeforeClass
import org.junit.Test
import javax.ws.rs.core.Response

class ExtendedOrganizationResourceTest {

   @Test
   fun makeSureSearchDoesNotThrowOnIncludeParam() {
      val response = ResteasyClientBuilder()
         .build()
         .target("http://localhost:$ourPort/Organization?_id=1")
         .request()
         .method("GET")
      assertThat(
         "This should not explode!",
         response.status,
         `is`(Response.Status.OK.statusCode)
      )
   }

   companion object {
      private var ourPort: Int = 0
      private lateinit var jettyServer: Server

      @JvmStatic
      @AfterClass
      @Throws(Exception::class)
      fun afterClassClearContext() {
         JettyUtil.closeServer(jettyServer)
         TestUtil.clearAllStaticFieldsForUnitTest()
      }

      @JvmStatic
      @BeforeClass
      @Throws(Exception::class)
      fun setUpClass() {
         val context = ServletContextHandler(ServletContextHandler.SESSIONS).also {
            it.contextPath = "/"
         }
         jettyServer = Server(0).also {
            it.handler = context
         }
         val jerseyServlet = context.addServlet(HttpServletDispatcher::class.java, "/*").also {
            it.initOrder = 0
            //@formatter:off
            it.setInitParameter(
               "resteasy.resources",
               StringUtils.join(listOf(ExtendedOrganizationResource::class.java.canonicalName), ",")
            )
            //@formatter:on
         }
         JettyUtil.startServer(jettyServer)
         ourPort = JettyUtil.getPortForStartedServer(jettyServer)
      }
   }
}
