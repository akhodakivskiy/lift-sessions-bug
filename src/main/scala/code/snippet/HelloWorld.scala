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

class SessionData {
  def render =
    "#sessionVar *" #> ExtSession.currentData &
    "#cookie *" #> ExtSession.currentCookie
}

class ChangeForm extends StatefulSnippet with Loggable {
  def dispatch = { case "render" => render }

  private var data = ""

  def render = {
    def process() {
      ExtSession.setData(data)
      S.redirectTo("/")
    }

    "#input" #> SHtml.text(data, data = _) &
    "type=submit" #> SHtml.onSubmitUnit(process)
  }
}

