package models.movie

import java.sql.{Date, Timestamp}

import models.nationality.Nationality
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape


class MovieEntity(tag: Tag) extends Table[Movie](tag, "movie") {

  // Declration des colonnes
  def originalTitle = column[String]("original_title", O.PrimaryKey)
  def synopsis = column[Option[String]]("synopsis")
  def year = column[Int]("realisation_year")
  def title = column[String]("title")
  def frenchRelease = column[Option[Timestamp]]("french_release")
  def note = column[Int]("note")
  def nationality = column[String]("nationality")

  // Declaration des cles etrangeres
  def nationalities = foreignKey("nationality", nationality, Nationality.nationalities)(_.country)

  override def * = {
    (originalTitle, synopsis, year, title, frenchRelease, note,
      nationality) <> ((Movie.apply _).tupled, Movie.unapply)
  }
}
