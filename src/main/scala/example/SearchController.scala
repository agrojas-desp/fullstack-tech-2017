package example

import java.net.InetAddress
import java.time.{Instant, LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter

import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.TransportAddress
import org.elasticsearch.common.xcontent.XContentType
import org.elasticsearch.transport.client.PreBuiltTransportClient
import org.json4s.jackson.Serialization._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

case class URLExample(count: Int, url: String)


case class SearchTracking(country:String,
                          origin:String,
                          destination:String,
                          departureDate:String,
                          adults:Int,
                          itineraries: List[Itinerary],
                          elapsedMillis:Long,
                          timestamp:String = Instant.now().toString
                          )


class SearchController extends ScalatraServlet with JacksonJsonSupport {
  override protected implicit def jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  protected val searchService = new SearchService
  protected val formatter = DateTimeFormatter.ISO_DATE


  val esClient: TransportClient = new PreBuiltTransportClient(Settings.EMPTY)
    .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300))

  get("/itineraries/:country/:origin/:destination/:departure_date/:adults") {
    val startTimeMillis=System.currentTimeMillis()
    val itineraries=searchService.search(
      params("country"),
      params("origin"),
      params("destination"),
      LocalDate.parse(params("departure_date"), formatter),
      params("adults").toInt
    )
    val endTimeMillis=System.currentTimeMillis()

    val searchTracking=SearchTracking(params("country"), params("origin"),params("destination"),params("departure_date"),params("adults").toInt, itineraries, endTimeMillis-startTimeMillis)
    esClient.prepareIndex("searches","searches").setSource(write(searchTracking),XContentType.JSON).execute()
    itineraries
  }


  get("/url-examples") {
    searchService.searchesMock.topSearches
      .filter { case (_, search) => params.get("country").map(_ == search.country).getOrElse(true) }
      .take(params.getOrElse("limit", "10").toInt)
      .map { case (count, search) =>
        import search._
        URLExample(count, s"http://localhost:8080/itineraries/$country/$origin/$destination/$departureDate/$adults")
      }
  }
}

