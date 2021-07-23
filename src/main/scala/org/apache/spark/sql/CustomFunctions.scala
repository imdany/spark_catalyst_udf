package org.apache.spark.sql

import org.apache.spark.sql.catalyst.expressions.{Expression, Uuid}

object CustomFunctions {

  private def withExpr(expr: Expression): Column = Column(expr)

  def UUID_CUSTOM(): Column = withExpr {
    Uuid()
  }

}
