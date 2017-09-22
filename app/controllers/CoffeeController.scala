package controllers

import play.api.mvc.{Action, Controller}
import javax.inject.Inject

import scalikejdbc._
import com.mysql.jdbc.Driver

/**
  * Created by mituba on 2017/09/21.
  */

case class User(userId: String, userName: String)

object User extends SQLSyntaxSupport[User] {
  override val tableName = "users"
  // for example 1
  def apply(rs: WrappedResultSet): User = User(rs.string("user_id"), rs.string("user_name"))
  // for example 2 (case classに無いカラム名を使うとコンパイル時に分かる)
  def apply(u: ResultName[User])(rs: WrappedResultSet): User = User(rs.string(u.userId), rs.string(u.userName))
}

class CoffeeController @Inject() extends Controller{
  Class.forName("com.mysql.jdbc.Driver")
  ConnectionPool.singleton("jdbc:mysql://0.0.0.0/coffee_bear?characterEncoding=UTF-8", "root", "PASSWORD")

  implicit val session = AutoSession

  def insertCoffee() = Action(parse.multipartFormData) { request =>
    println(request.body.dataParts.get("user_id").get(0))

    val userId = request.body.dataParts.get("user_id").get(0)
    val userName = request.body.dataParts.get("user_name").get(0)
    println(userId)
    println(userName)
    // insert example 2: DSLを使った書き方 (case classに無いカラム名を使うとコンパイル時に分かる)
    val uc = User.column
    withSQL { insert.into(User).namedValues(uc.userId -> userId, uc.userName -> userName) }.update.apply()
    Ok("insert")
  }

  def selectCoffee() = Action {
    // select example 2: DSLを使った書き方
    val u = User.syntax("u")
    val users = withSQL { select.from(User as u) }.map(User(u.resultName)).list.apply()
    println(users)
    Ok(users.mkString(""))
  }

}
