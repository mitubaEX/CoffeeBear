package controllers

import java.text.SimpleDateFormat
import java.time.{LocalDate, LocalDateTime}
import java.util.Date

import play.api.mvc.{Action, Controller}
import javax.inject.Inject

import scalikejdbc._
import com.mysql.jdbc.Driver
import models.{CoffeeHistory, User}
import org.joda.time.format.DateTimeFormat

/**
  * Created by mituba on 2017/09/21.
  */

//case class User(userId: String, userName: String)
//
//object User extends SQLSyntaxSupport[User] {
//  override val tableName = "users"
//  // for example 1
//  def apply(rs: WrappedResultSet): User = User(rs.string("user_id"), rs.string("user_name"))
//  // for example 2 (case classに無いカラム名を使うとコンパイル時に分かる)
//  def apply(u: ResultName[User])(rs: WrappedResultSet): User = User(rs.string(u.userId), rs.string(u.userName))
//}

class CoffeeController @Inject() extends Controller{
  Class.forName("com.mysql.jdbc.Driver")
  ConnectionPool.singleton("jdbc:mysql://0.0.0.0/coffee_bear?characterEncoding=UTF-8", "root", "PASSWORD")

  implicit val session = AutoSession

  // ココらへんの操作は別ファイルにしたい
  def selectUserList: List[User] = {
    val u = User.syntax("u")
    withSQL { select.from(User as u) }.map(User(u.resultName)).list.apply()
  }

  def insertUser(userId: String, userName: String): Unit = {
    val uc = User.column
    withSQL { insert.into(User).namedValues(uc.userId -> userId, uc.userName -> userName) }.update.apply()
  }

  def selectCoffeeList(year: Int, month: Int): List[CoffeeHistory] = {
    val u = CoffeeHistory.syntax("u")
    withSQL { select.from(CoffeeHistory as u).where.eq(u.year, year).and.eq(u.month, month) }.map(CoffeeHistory(u.resultName)).list.apply()
  }

  def insertCoffee(userId: String, userName: String, year: Int, month: Int): Unit = {
    val uc = CoffeeHistory.column
    withSQL { insert.into(CoffeeHistory).namedValues(
      uc.userId -> userId,
      uc.userName -> userName,
      uc.year -> year,
      uc.month -> month) }.update.apply()
  }

  /*
  * データが送られて来たらユーザリストからユーザのリストを取り出し，送られてきたユーザが存在するかどうかを確認する．
  * 存在しなかったらユーザリストに追加して，その後支払履歴にユーザ情報と共に日時をぶち込む
  */
  def insertCoffeeAction() = Action(parse.multipartFormData) { request =>
    val userId = request.body.dataParts.get("user_id").get(0)
    val userName = request.body.dataParts.get("user_name").get(0)
    val epoch = request.body.dataParts.get("timestamp").get(0)

    // エポック時間が送られてくるので整形 (1355517523.000005)
    val offsetEpoch = epoch.split("\\.")(0) + "000"
    val convertEpoch = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss").print(offsetEpoch.toLong)
    val dateArray = convertEpoch.split("-")
    val year = dateArray(0).toInt
    val month = dateArray(1).toInt

    if(selectUserList.filter(n => n.userId == userId).size == 0)
      insertUser(userId, userName)

    insertCoffee(userId, userName, year, month)

    Ok("insert")
  }

  /*
  * リクエストが来たらユーザテーブルからユーザのリストを取り出し，支払い依頼を取り出す．
  * 支払い依頼のリストに含まれているユーザをユーザリストから除外し，残ったユーザを表示する．
  */
  def selectCoffeeAction() = Action {
    val users = selectUserList

    // 現在時刻の取得
    val localDateTime = LocalDateTime.now()
    val coffeeHistorys = selectCoffeeList(localDateTime.getYear, localDateTime.getMonthValue)
    val coffeeUserIdList = coffeeHistorys.map(n => n.userId)

    val paymentUserList =
      users.map(n =>
        if(!coffeeUserIdList.contains(n.userId)) n
        else None
        )
        .filter(n => n != None)

    Ok(paymentUserList.mkString(""))
  }

}
