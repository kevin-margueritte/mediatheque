package repositories

import java.sql.Timestamp
import javax.inject.Inject

import models.associations.MovieGenre
import models.movie.Movie
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}


class MovieRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import models.movie.Movie._
  import profile.api._

  /**
    * Cette methode permet de créer un film est de le retourner ensuite
    *
    * @param originalTitle
    * @param synopsis
    * @param year
    * @param title
    * @param frenchRelease
    * @param note
    * @param nationality
    * @return
    */
  def insert(originalTitle : String, synopsis : Option[String], year : Int,
             title : String, frenchRelease : Option[Timestamp], note : Int, nationality: String) : Future[Movie] = {

    // Retourne la ligne inseree
    val insertQuery = movies returning movies.map(_.originalTitle) into ((movie, title) => movie.copy(originalTitle = title))

    // Ajout en base du film
    val action = insertQuery += Movie(originalTitle, synopsis, year, title, frenchRelease, note, nationality)

    db.run(action)
  }

  /**
    * Methode permetant de recherher des films par genre et filtre des films par annee puis par le titre original
    * @param genre
    * @return
    */
  def filterByGenreSortByYearByAndOriginalTitle(genre : Option[String]) : Future[Seq[(Movie, String)]] = {

    val filterGenre = genre match {
      case Some(g) => MovieGenre.associations.filter(_.genreName === g)
      case _ => MovieGenre.associations
    }

    // Création de la jointure
    val join = for {
      (movie, movieGenre) <- movies join filterGenre on (_.originalTitle === _.movieOriginalTitle)
    } yield (movie, movieGenre.genreName)

    db.run(join.result)
  }

  /**
    * Retourne l'existance d'un film par son titre original
    * @param originalTitle
    * @return
    */
  def exist(originalTitle : String) : Future[Boolean] = {
    val query = movies.filter(_.originalTitle === originalTitle)

    db.run(query.exists.result)
  }

  /**
    * Compte le nombre de films par année de réalisation
    *
    * @param year
    * @return
    */
  def countMoviesByRealisationYear(year : Int) : Future[Int] = {
    val filter = movies.filter(_.year === year)

    db.run(filter.size.result)
  }

}
