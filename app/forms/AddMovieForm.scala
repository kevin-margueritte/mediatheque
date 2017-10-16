package forms

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._


/**
 *  Formulaire permettant de binder le json envoyé par le client
  *
  * @param title
  * @param country
  * @param year
  * @param originalTitle
  * @param frenchRelease
  * @param synopsis
  * @param genres
  * @param note
  */
case class AddMovieForm(title : String, country : String, year : Int, originalTitle : Option[String],
                          frenchRelease : Option[String], synopsis : Option[String], genres : Seq[String], note : Int)

object AddMovieForm {

  /**
  * Apply est deja definie avec la case class, cependant on veut ajouter la regle de gestion suivante
  *
  * @param title
  * @param country
  * @param year
  * @param originalTitle
  * @param frenchRelease
  * @param synopsis
  * @param genres
  * @param note
  */
  def apply(title : String, country : String, year : Int, originalTitle : Option[String],
            frenchRelease : Option[String], synopsis : Option[String], genres : Seq[String], note : Int): AddMovieForm = {

    // Passage des genres en minuscule
    val lowerGenres = genres.map(genre => genre.toLowerCase)

    // Si l'origine du film est FRA alors son titre original est son titre
    country match {
      case "FRA" => new AddMovieForm(title, country, year, Some(title), frenchRelease, synopsis, lowerGenres, note)
      case _ => new AddMovieForm(title, country, year, originalTitle, frenchRelease, synopsis, lowerGenres, note)
    }

  }

  val form = Form(
    mapping(
    "title" -> nonEmptyText.verifying(maxLength(250)),
    "country" -> nonEmptyText.verifying(countryCheck _),
    "year" -> number.verifying(min(0), max(9999)),
    "originalTitle" -> optional(text.verifying(maxLength(250))),
    "frenchRelease" -> optional(nonEmptyText.verifying(frenchReleaseCheck _)),
    "synopsis" -> optional(text.verifying(maxLength(1000))),
    "genres" -> seq(text.verifying(maxLength(50))),
    "ranking" -> number.verifying(min(0), max(10))
   )(AddMovieForm.apply)(AddMovieForm.unapply).verifying("You need to specify an original title and genre", addMovieCheck _)
  )

  /**
  * Vérifie que frenchRelease est au format de date YYYY/MM/DD
  *
  * @param frenchRelease
  * @return
  */
  def frenchReleaseCheck(frenchRelease : String) : Boolean
      = frenchRelease.matches("^[0-9]{4}\\/(0[1-9]|1[0-2])\\/(0[1-9]|[1-2][0-9]|3[0-1])$")

  /**
    * Check Format ISO 3166-1 alpha-3
    *
    * @param country
    * @return
    */
  def countryCheck(country : String) : Boolean = country.matches("^[a-zA-Z]{3}")

  /**
  * Verifie la regle de gestion suivante
  *   si country != FRA alors le titre original est obligatoire
  * @param movie
  * @return
  */
  def addMovieCheck(movie : AddMovieForm) : Boolean = {
    (movie.country match {
      case "FRA" => true
      case _ => movie.originalTitle.isDefined
    } ) match {
      case true => !movie.genres.isEmpty
      case false => false
    }
  }

}


