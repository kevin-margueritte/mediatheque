package repositories

import javax.inject.Inject

import models.genre.Genre
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class GenreRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import models.genre.Genre._
  import profile.api._

  /**
    * Cette methode permet de creer un Genre est de l'inserer en base
    *
    * @param genreName
    * @return
    */
  def insert(genreName : String) : Future[Genre] = {

    val action = genres.filter(_.name === genreName).exists.result.flatMap {
      case true => DBIO.successful(Genre(genreName))
      case _ => {
        val returnQuery = genres returning genres.map(_.name) into ((genres, genreName) => genres.copy(name = genreName))
        returnQuery += Genre(genreName)
      }
    }

    db.run(action)

  }

  /**
    * Retoure l'existance d'un genre a partie de son nom
    * @param genre
    * @return
    */
  def exist(genre : String) : Future[Boolean] = {
    val query = genres.filter(_.name === genre)

    db.run(query.exists.result)
  }

}
