package code.lib

import net.liftweb.util._
import net.liftweb.util.Helpers._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.provider._
import net.liftweb.sitemap._

object ExtSession extends Loggable {
  val CookieName = "EXT_SESSION_TEST"

  private object _currentData extends SessionVar[Box[String]](Empty)

  def currentData: Box[String] = _currentData.get

  def currentCookie: Box[String] = S.findCookie(CookieName).flatMap(_.value)

  def setData(value: String) {
    _currentData(Full(value))
    S.addCookie(
      HTTPCookie(CookieName, value)
        .setMaxAge((1.days / 1000L).toInt))
  }

  def testCookieEarlyInStateful: Box[Req] => Unit = {
    ignoredReq => {
      (currentData, currentCookie) match {
        case (Empty, Full(c)) => setData("---Reset by earlyInStateful---")
        case _ =>
      }
    }
  }
}
