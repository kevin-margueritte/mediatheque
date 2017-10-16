package controllers

import javax.inject.Inject

import json.ResultJson
import models.movie.Movie
import play.api.libs.json.Json
import play.api.mvc._
import services.Mediatheque

import scala.concurrent.{ExecutionContext, Future}

/**
  * Controleur d'ajout d'un film
  * @param cc
  * @param media
  * @param ec
  */
class AddMovieController @Inject()(cc: ControllerComponents, media : Mediatheque)(implicit ec: ExecutionContext)
  extends AbstractController(cc){

  import forms.AddMovieForm._

  def addMovie() = Action.async { implicit request: Request[AnyContent] =>
    form.bindFromRequest().fold(
      formWithErrors => Future.successful(
        BadRequest(Json.toJson(
          formWithErrors.errors.map(error => s"An error occured with the parameter ${error.key} : ${error.message}"))
        )
      ),
      formData => {

        for {
          countryExist <- media.countryExist(formData.country)
          movieExist <- media.movieExist(formData.originalTitle.get) // Le titre est forcement present
          result <- createMovie(countryExist, movieExist, formData.title, formData.country, formData.year,
            formData.originalTitle.get, formData.frenchRelease, formData.synopsis, formData.note, formData.genres)
        } yield result

      }
    )
  }

  /**
    * Creation d'un film avec les genres associes
    *
    * @param countryExist
    * @param movieExist
    * @param title
    * @param country
    * @param year
    * @param originalTitle
    * @param frenchRelease
    * @param synopsis
    * @param note
    * @param genres
    * @return
    */
  def createMovie(countryExist :Boolean, movieExist : Boolean, title : String, country : String, year : Int, originalTitle : String,
                  frenchRelease : Option[String], synopsis : Option[String], note : Int, genres : Seq[String]) : Future[Result]= {
    countryExist match {
      case false => Future.successful(
        NotFound(Json.obj("messages" -> "The country doesn't exist"))
      )
      case _ => movieExist match {
        case true => Future.successful(
          Conflict(Json.obj("messages" -> "A movie exist with the same original_title"))
        )
        case _ => {

          // Resolution des Futures
          val action = for {
            movie <- media.createMovie(originalTitle, country, year, title, frenchRelease, synopsis, note)
            genres <- media.createGenre(genres)
            result <- media.createAssociations(movie.originalTitle, genres.map(genre => genre.name))
          } yield movie

          action.map(
            _ match {
              case m: Movie => {
                val resJson = ResultJson(m.title, m.nationality, m.year, m.originalTitle,
                  m.frenchRelease, m.synopsis, genres, m.note)
                Created(Json.obj("movies" -> resJson))
              }
            }
          )
        }
      }
    }
  }

}
