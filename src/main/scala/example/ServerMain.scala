package example

import java.net.InetAddress
import java.time.Instant
import java.util.concurrent.{Executors, TimeUnit}
import javax.servlet.ServletContext

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.eclipse.jetty.webapp.WebAppContext
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.TransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient
import org.scalatra.LifeCycle
import org.scalatra.servlet.ScalatraListener

object ServerMain extends App {
  val port = 8080
  val server = new Server(port)
  val serverThreadPool = server.getThreadPool.asInstanceOf[QueuedThreadPool]
  serverThreadPool.setMaxThreads(10)
  //ThreadPoolMetricsNotifier.register(serverThreadPool)

  val context = new WebAppContext()
  context.setContextPath("/")
  context.setResourceBase(".")
  context.setInitParameter(ScalatraListener.LifeCycleKey, classOf[ScalatraBootstrap].getCanonicalName)
  context.setEventListeners(Array(new ScalatraListener))

  server.setHandler(context)
  server.start

  server.join
}


class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context mount(new SearchController, "/*")
  }
}


/*
object ThreadPoolMetricsNotifier {
  val esClient: TransportClient = new PreBuiltTransportClient(Settings.EMPTY)
    .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300))

  val tp=Executors.newSingleThreadScheduledExecutor()
  def register(queuedThreadPool: QueuedThreadPool) = {
    tp.scheduleWithFixedDelay(new Runnable {
      override def run(): Unit = try {
        import scala.collection.JavaConverters._
        esClient
          .prepareIndex("threadpool", "threadpool")
          .setSource(Map(
            "name" -> queuedThreadPool.getName,
            "idle_threads" -> queuedThreadPool.getIdleThreads,
            "queue_size" -> queuedThreadPool.getQueueSize,
            "threads" -> queuedThreadPool.getThreads,
            "max_threads" -> queuedThreadPool.getMaxThreads,
            "timestamp" -> Instant.now().toString
          ).asJava)
          .execute()
      } catch {
        case e => println(e)
      }
    }, 10, 10, TimeUnit.SECONDS)
  }
} */