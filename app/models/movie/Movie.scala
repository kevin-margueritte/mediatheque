package models.movie

import java.sql.Timestamp

import play.api.libs.json.{JsString, Json, Reads, Writes}
import slick.lifted.TableQuery

case class Movie(originalTitle : String, synopsis : Option[String], year : Int,
                    title : String, frenchRelease : Option[Timestamp], note : Int, nationality: String)

object Movie {

  lazy val movies = TableQuery[MovieEntity]
  implicit val taskWrites = new Writes[Movie] {
    def writes(m: Movie) = Json.obj(
      "original_title" -> m.originalTitle,
      "synopsis" -> m.synopsis,
      "realisation year" -> m.year,
      "title" -> m.title,
      "french_release" -> m.frenchRelease,
      "nationality" -> m.nationality
    )
  }

  /**
    * Permet de serialiser en JSon TimeStamp
    */
  implicit val tsreads: Reads[Timestamp] = Reads.of[Long] map (new Timestamp(_))
  implicit val tswrites: Writes[Timestamp] = Writes { (ts: Timestamp) => JsString(ts.toString)}
}
