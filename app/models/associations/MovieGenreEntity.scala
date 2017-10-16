package models.associations

import models.genre.Genre
import models.movie.Movie
import slick.jdbc.PostgresProfile.api._


class MovieGenreEntity(tag: Tag) extends Table[MovieGenre](tag, "a_movie_genre")  {

  // Definition des colonnes
  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def genreName = column[String]("genre_name")
  def movieOriginalTitle = column[String]("movie_original_title")

  // Definition des clés étrangeres
  def movies = foreignKey("movie", movieOriginalTitle, Movie.movies)(_.originalTitle)
  def genres = foreignKey("genre", genreName, Genre.genres)(_.name)

  override def * = (id, movieOriginalTitle, genreName) <> ((MovieGenre.apply _).tupled, MovieGenre.unapply)
}
