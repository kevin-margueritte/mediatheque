package models.genre

import slick.jdbc.PostgresProfile.api._

class GenreEntity(tag: Tag) extends Table[Genre](tag, "genre") {

  def name = column[String]("name", O.PrimaryKey)

  override def * = (name) <> ((Genre.apply _), Genre.unapply)
}
