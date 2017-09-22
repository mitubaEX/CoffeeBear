package models

import scalikejdbc._

/**
  * Created by mituba on 2017/09/23.
  */
case class User(userId: String, userName: String)

object User extends SQLSyntaxSupport[User] {
  override val tableName = "users"
  // for example 1
  def apply(rs: WrappedResultSet): User = User(rs.string("user_id"), rs.string("user_name"))
  // for example 2 (case classに無いカラム名を使うとコンパイル時に分かる)
  def apply(u: ResultName[User])(rs: WrappedResultSet): User = User(rs.string(u.userId), rs.string(u.userName))
}

