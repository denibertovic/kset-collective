package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._

object Application extends Controller {
  // -- Actions

  /**
   * Home page
   */
def index = Action { request =>
  val name = request.queryString.get("name").flatMap(_.headOption)
  Ok("Hello2 " + name.getOrElse("Guest"))
}

}
