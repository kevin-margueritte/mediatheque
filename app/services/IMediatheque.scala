package services

import models.associations.MovieGenre
import models.genre.Genre
import models.movie.Movie
import models.nationality.Nationality

import scala.concurrent.Future

trait IMediatheque {

  /**
    * Retourne une nationalité
    *
    * @param country
    * @return
    */
  def getCountry(country : String) : Future[Option[Nationality]]

  /**
    * Retourne l'existance d'un film
    *
    * @param originalTitle
    * @return
    */
  def movieExist(originalTitle : String) : Future[Boolean]

  /**
    * Creation d'un film
    *
    * @param title
    * @param country
    * @param year
    * @param originalTitle
    * @param frenchRelease
    * @param synopsis
    * @param note
    * @return
    */
  def createMovie(title : String, country : String, year : Int, originalTitle : String,
    frenchRelease : Option[String], synopsis : Option[String], note : Int) : Future[Movie]

  /**
    * Retourne l'existance d'un pays
    *
    * @param country
    * @return
    */
  def countryExist(country : String) : Future[Boolean]

  /**
    * Creation des associations entre un film et ses genres
    * @param originalTitle
    * @param listGenre
    * @return
    */
  def createAssociations(originalTitle : String, listGenre : Seq[String]) : Future[Seq[MovieGenre]]

  /**
    * Création d'une multitude de genres
    *
    * @param genres
    * @return
    */
  def createGenre(genres : Seq[String]) : Future[Seq[Genre]]

  /**
    * Selection des films en fonction du genre et sort par l'annee de realisation et le titre original
    *
    * @param genre
    * @return
    */
   def filterByGenre(genre : Option[String]) : Future[Seq[(Movie, Seq[String])]]

  /**
    * Ajout d'un code pays
    *
    * @param country
    * @return
    */
   def addCountryCode(country : String) : Future[Nationality]

  /**
    * Selection de tout les codes pays
    *
    * @return
    */
   def getAllCountryCode() : Future[Seq[Nationality]]

  /**
    * Retourne le nombre de film par annee de realisation
    *
    * @param year
    * @return
    */
   def countMoviesByRealisationYear(year : Int) : Future[Int]
}
