package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc._
import services.Mediatheque

import scala.concurrent.{ExecutionContext, Future}

/**
  * Controleur permettant d'ajouter un nouveau code pays
  * @param cc
  * @param media
  * @param ec
  */
class AddCountryCodeController  @Inject()(cc: ControllerComponents, media : Mediatheque)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  import forms.AddCountryCodeForm._

  def addCountryCode() = Action.async { implicit request: Request[AnyContent] =>
    form.bindFromRequest().fold(
      formWithErrors => Future.successful(
        BadRequest(Json.toJson(
          formWithErrors.errors.map(error => s"An error occured with the parameter ${error.key} : ${error.message}"))
        )
      ),
      formData => {

        for {
          exist <- media.countryExist(formData.country)
          result <- createCountryCode(exist, formData.country)
        } yield result

      }
    )
  }

  def createCountryCode(countryExist : Boolean, coutryCode : String) : Future[Result]= {
    countryExist match {
      case true => Future.successful(Conflict(Json.obj("messages" -> "The country already exist")))
      case _ => media.addCountryCode(coutryCode).map {
        coutryCode => Created(Json.obj("country" -> coutryCode))
      }
    }
  }
}
