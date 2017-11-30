package example

import example.mocks.SearchesMock

import scalaj.http.{Http, HttpResponse}

object ClientMockMain extends App {
  val seachesMock = new SearchesMock

  while (true) {
    seachesMock.orderedSearches.foreach { search =>
      import search._
      val url = s"http://localhost:8080/itineraries/$country/$origin/$destination/$departureDate/$adults"
      val response: HttpResponse[String] = Http(url).asString
      if (response.isSuccess)
        println(s"Success $url")
      else
        println(s"Error $url")
    }
  }

}