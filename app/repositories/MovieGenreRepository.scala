package repositories

import javax.inject.Inject

import models.associations.MovieGenre
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}


class MovieGenreRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import models.associations.MovieGenre._
  import profile.api._

  def insert(originalTitle : String, genreName : String) : Future[MovieGenre] = {

    // Select movie by id
    val insertQuery = associations returning associations.map(_.id) into ((movieGenre, id) => movieGenre.copy(id = id))

    // Create movie and return it
    val action = insertQuery += MovieGenre(0, originalTitle, genreName)

    db.run(action)
  }

  /**
    * Ajout des associations films <-> genres
    *
    * @param ass
    * @return
    */
  def add(ass : Seq[MovieGenre]) = db.run(associations ++= ass).map{_ => ass}

}
