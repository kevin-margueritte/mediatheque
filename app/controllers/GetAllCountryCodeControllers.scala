package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.Mediatheque

import scala.concurrent.ExecutionContext

/**
  * Controleur permettant de selectionner tous les codes pays possibles
  *
  * @param cc
  * @param media
  * @param ec
  */
class GetAllCountryCodeControllers @Inject()(cc: ControllerComponents, media : Mediatheque)(implicit ec: ExecutionContext)
  extends AbstractController(cc){

  def getCountries() = Action.async { implicit request: Request[AnyContent] =>
    media.getAllCountryCode().map {
      countries => Ok(Json.obj("countries" -> countries))
    }
  }

}
