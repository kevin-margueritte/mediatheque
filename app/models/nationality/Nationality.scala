package models.nationality

import play.api.libs.json.Json
import slick.lifted.TableQuery


case class Nationality(country : String)

object Nationality {
  lazy val nationalities = TableQuery[NationalityEntity]
  implicit val countryFormat = Json.format[Nationality]
}
