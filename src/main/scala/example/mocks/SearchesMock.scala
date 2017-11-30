package example.mocks

import java.time.format.DateTimeFormatter
import java.time.{ZoneId, ZonedDateTime}
import java.util.zip.GZIPInputStream

import scala.io.Source

case class SearchResult(searchId: String,
                        timestampinmillis: Long,
                        origin: String,
                        destination: String,
                        country: String,
                        adults: Int,
                        children: Int,
                        infants: Int,
                        adultTotalFare: Double,
                        maxQuantityInstallments: Int,
                        route: Route) {
  def departureDate = route.segments.head.departure.parsedDateTime.toLocalDate.toString
}

case class Route(segments: List[Segment])

case class Segment(departure: Location,
                   arrival: Location,
                   flightNumber: String,
                   airline: String,
                   remainingSeats: Int
                  ) {
}

case class Location(airport: Airport, dateTime: String) {
  def parsedDateTime = ZonedDateTime.parse(dateTime, Location.zonedDTF)
}

object Location {
  val zonedDTF: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
}

case class Airport(code:String, lat:Double, lon:Double)

class SearchesMock {

  val searches = {
    def string2Search(str: String): SearchResult = {
      import org.json4s._
      import org.json4s.jackson.JsonMethods._
      implicit val formats = DefaultFormats
      parse(str).camelizeKeys.extract[SearchResult]
    }

    val fileStream = this.getClass.getResourceAsStream("/flights_sample.json.gz")
    val gzis = new GZIPInputStream(fileStream)

    val _searches = Source.fromInputStream(gzis).getLines().map(_.trim)//.map{x=> println(x);x}
      .map(string2Search)
      .toList
    fileStream.close()
    _searches
  }

  case class RouteSearchKey(country: String, origin: String, destination: String, departureDate: String, adults: Int)

  protected val searchesByRoute: Map[RouteSearchKey, List[SearchResult]] = searches.groupBy(s => RouteSearchKey(s.country, s.origin, s.destination, s.departureDate, s.adults))
  protected val searchBySearchId: Map[String, SearchResult] = searches.map(s => s.searchId -> s).toMap
  val orderedSearches=searches.sortBy(_.timestampinmillis)

  def getSearchesByRoute(country: String, origin: String, destination: String, departureDate: String, adults: Int) = searchesByRoute.getOrElse(RouteSearchKey(country, origin, destination, departureDate, adults),List())

  def getSearchBySearchId(searchId: String) = searchBySearchId.get(searchId)

  val topSearches = searchesByRoute.mapValues(_.size).toList.sortBy(_._2).reverse.map(s => s._2 -> s._1)

  def getTopSearches(n: Int) = topSearches.take(n)

  println(s"hay ${searches.size} rutas mock")

}
