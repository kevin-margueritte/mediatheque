package models.nationality

import slick.jdbc.PostgresProfile.api._

class NationalityEntity(tag: Tag) extends Table[Nationality](tag, "nationality") {

  def country = column[String]("country", O.PrimaryKey)

  override def * = (country) <> ((Nationality.apply _), Nationality.unapply)

}
