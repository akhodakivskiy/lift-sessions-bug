package code
package snippet

import scala.xml.{NodeSeq, Text}
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.http._
import java.util.Date
import code.lib._
import Helpers._

import code.lib._

class UserName {
  def render = {
    ExtSession.currentUser match {
      case Full(user) => "#userName" #> user.name
      case _ => "*" #> ""
    }
  }
}

class UserLogin extends StatefulSnippet with Loggable {
  def dispatch = { case "render" => render }

  private var name = ""

  def render = {
    def process() {
      User.findUser(name) match {
        case Full(user) => ExtSession.logIn(user)
        case _ => ExtSession.logOut
      }
      S.redirectTo("/")
    }

    "#inputName" #> SHtml.text(name, name = _) &
    "type=submit" #> SHtml.onSubmitUnit(process)
  }
}

