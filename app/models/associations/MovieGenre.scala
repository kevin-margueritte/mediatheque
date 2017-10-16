package models.associations

import play.api.libs.json.Json
import slick.lifted.TableQuery

case class MovieGenre(id : Long = 0, movieOriginalTitle : String, genreName : String)

object MovieGenre {
  lazy val associations = TableQuery[MovieGenreEntity]
  implicit val movieGenreFormat = Json.format[MovieGenre]
}
