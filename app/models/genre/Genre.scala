package models.genre

import play.api.libs.json.Json
import slick.lifted.TableQuery


case class Genre (name : String)

object Genre {
  lazy val genres = TableQuery[GenreEntity]
  implicit val genreFormat = Json.format[Genre]
}
