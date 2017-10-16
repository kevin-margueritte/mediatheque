package controllers

import javax.inject.Inject

import json.ResultJson
import models.movie.Movie
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.Mediatheque

import scala.concurrent.ExecutionContext

/**
  * Controleur permettant de retourner les films par leur genre avec un tri sur
  * l'annee de realisation puis le titre original
  *
  * @param cc
  * @param media
  * @param ec
  */
class GetMoviesByGenreController @Inject()(cc: ControllerComponents, media : Mediatheque)(implicit ec: ExecutionContext)
  extends AbstractController(cc){

  /**
    * Recuperation des films avec filtre par genre, s'il nest pas renseigne tous les film sont retournes
    * @param genre
    * @return
    */
  def getMoviesByGenre(genre : Option[String]) = Action.async { implicit request: Request[AnyContent] =>
    media.filterByGenre(genre).map{
      _  match {
        case m : Seq[(Movie, Seq[String])] => {

          val jsonResult = for {
            (movie, genres) <- m
          } yield ResultJson(movie.title, movie.nationality, movie.year, movie.originalTitle,
            movie.frenchRelease, movie.synopsis, genres, movie.note)

          Ok(Json.obj("movies" -> jsonResult))
        }
      }
    }
  }
}


