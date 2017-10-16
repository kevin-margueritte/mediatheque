package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.Mediatheque

import scala.concurrent.{ExecutionContext, Future}

/**
  * Comptage du nombre de film par l'annee de production
  *
  * @param cc
  * @param media
  * @param ec
  */
class CountMoviesController @Inject()(cc: ControllerComponents, media : Mediatheque)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def countMovies(year : Integer) = Action.async { implicit request: Request[AnyContent] =>
    year match {
      case y if (y >= 0 && y <= 9999) => {
        media.countMoviesByRealisationYear(y).map { nb =>
          Ok(Json.obj("count" -> nb))
        }
      }
      case _ => Future.successful(BadRequest(Json.obj("message" -> "0 <= year <= 9999")))
    }
  }

}
