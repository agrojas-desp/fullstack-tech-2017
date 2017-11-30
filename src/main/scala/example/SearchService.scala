package example

import java.time.LocalDate

import example.mocks.{SearchesMock, Segment}


case class Itinerary(
                      adultTotalFare: Double,
                      maxQuantityInstallments: Int,
                      segments: List[Segment]
                    )

class SearchService {

  val searchesMock = new SearchesMock

  def search(country: String, origin: String, destination: String, departureDate: LocalDate, adults: Int): List[Itinerary] = {
    sleep(country)
    searchesMock.getSearchesByRoute(country, origin, destination, departureDate.toString, adults)
      .filter(_.route.segments.map(_.remainingSeats).min >= adults)
      .map { searchResult =>
        Itinerary(searchResult.adultTotalFare, searchResult.maxQuantityInstallments, searchResult.route.segments)
      }
  }

  val r = scala.util.Random

  def sleep(country: String): Unit = {
    def timeInMillies() = Iterator.continually {
      val currentGaussian = r.nextGaussian()
      if (country == "BR")
        currentGaussian * 50 + 200
      else
        currentGaussian * 25 + 100
    }.takeWhile(_ > 0).take(1).toList.head
    Thread.sleep(timeInMillies().toLong)
  }

}
