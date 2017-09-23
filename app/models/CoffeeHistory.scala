package models

import scalikejdbc._

/**
  * Created by mituba on 2017/09/23.
  */
case class CoffeeHistory(userId: String, userName: String, year: Int, month: Int)

object CoffeeHistory extends SQLSyntaxSupport[CoffeeHistory] {
  override val tableName = "coffee_history"

  def apply(rs: WrappedResultSet): CoffeeHistory =
    CoffeeHistory(rs.string("user_id"), rs.string("user_name"), rs.int("year"), rs.int("month"))

  def apply(u: ResultName[CoffeeHistory])(rs: WrappedResultSet): CoffeeHistory =
    CoffeeHistory(rs.string(u.userId), rs.string(u.userName), rs.int(u.year), rs.int(u.month))
}

