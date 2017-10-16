package json

import java.sql.Timestamp

import play.api.libs.json.{JsString, Json, Reads, Writes}

/**
  * Classe permettant de renvoyer un film sous forme Json
  *
  * @param title
  * @param country
  * @param year
  * @param originalTitle
  * @param frenchRelease
  * @param synopsis
  * @param genres
  * @param note
  */
case class ResultJson(title : String, country : String, year : Int, originalTitle : String,
                             frenchRelease : Option[Timestamp], synopsis : Option[String], genres : Seq[String], note : Int)

object ResultJson {

  implicit val taskWrites = new Writes[ResultJson] {
    def writes(m: ResultJson) = Json.obj(
      "original_title" -> m.originalTitle,
      "synopsis" -> m.synopsis,
      "realisation year" -> m.year,
      "title" -> m.title,
      "french_release" -> m.frenchRelease,
      "nationality" -> m.country,
      "genres" -> m.genres
    )
  }

  implicit val tsreads: Reads[Timestamp] = Reads.of[Long] map (new Timestamp(_))
  implicit val tswrites: Writes[Timestamp] = Writes { (ts: Timestamp) => JsString(ts.toString)}
}
