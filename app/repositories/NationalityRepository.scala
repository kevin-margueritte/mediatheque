package repositories

import java.sql.Date
import javax.inject.Inject

import models.nationality.{Nationality, NationalityEntity}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.lifted.TableQuery

import scala.concurrent.{ExecutionContext, Future}


class NationalityRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import models.nationality.Nationality._
  import dbConfig._
  import profile.api._

  /**
    * Insere une nouvelle nationalite en base et la retourne
    *
    * @param country
    * @return
    */
  def insert(country : String) : Future[Nationality] = {

    // Selection de la nationalite pour la retourner
    val selectQuery = nationalities returning nationalities.map(_.country) into ((nationality, country) => nationality.copy(country = country))

    val action = selectQuery += Nationality(country)

    db.run(action)
  }

  /**
    * Retourne true si la region existe, false sinon
    *
    * @param country
    * @return
    */
  def exist(country : String) : Future[Boolean] = {
    val query = nationalities.filter(_.country === country)

    db.run(query.exists.result)
  }

  /**
    * Retourne une nationalité si elle existe
    *
    * @param country
    * @return
    */
  def get(country : String) : Future[Option[Nationality]] = {
    db.run (nationalities.filter (_.country === country).result.headOption)
  }

  /**
    * Selectionne tous les code pays
    *
    * @return
    */
  def getAll() : Future[Seq[Nationality]] = db.run(nationalities.result)
}
