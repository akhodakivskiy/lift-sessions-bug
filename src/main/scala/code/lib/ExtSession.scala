package code.lib

import net.liftweb.util._
import net.liftweb.util.Helpers._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.provider._
import net.liftweb.sitemap._

case class User(val name: String)

object User {
  val UserNames = List("Anton", "Antonio", "Trepi")

  def findUser(name: String): Box[User] = {
    if (UserNames.contains(name))
      Full(User(name))
    else
      Empty
  }
}

object ExtSession extends Loggable {
  val CookieName = "EXT_SESSION_TEST"

  private object _currentUser extends SessionVar[Box[User]](Empty)

  def currentUser = _currentUser.get

  def logIn(user: User) {
    logger.info(user.name)
    _currentUser(Full(user))
    S.addCookie(
      HTTPCookie(CookieName, user.name)
        .setMaxAge((1.days / 1000L).toInt))
  }

  def logOut = {
    _currentUser(Empty)
    S.deleteCookie(CookieName)
  }

  def testCookieEarlyInStateful: Box[Req] => Unit = {
    ignoredReq => {
      (currentUser, S.findCookie(CookieName)) match {
        case (Empty, Full(c)) => {
          c.value.map { cookieId =>
            User.findUser(cookieId) match {
              case Full(user) => logIn(User("-----------"))
              case _ => S.deleteCookie(CookieName)
            }
          }
        }
        case _ =>
      }
    }
  }
}
