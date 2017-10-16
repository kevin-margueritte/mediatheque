package forms

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._


case class AddCountryCodeForm(country : String)

object AddCountryCodeForm {
  val form = Form(
    mapping(
      "country" -> nonEmptyText.verifying(countryCheck _)
    )(AddCountryCodeForm.apply)(AddCountryCodeForm.unapply)
  )

  /**
    * Check Format ISO 3166-1 alpha-3
    *
    * @param country
    * @return
    */
  def countryCheck(country : String) : Boolean = country.matches("^[a-zA-Z]{3}")
}
