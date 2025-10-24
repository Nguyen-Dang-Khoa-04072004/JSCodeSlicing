package CodeSlice.Group

import io.shiftleft.codepropertygraph.generated.nodes.*
import io.shiftleft.codepropertygraph.generated.language.*

class CustomNode(
    node: StoredNode
) {
  val nodeId: Long = node.id()
  val code: String = node.propertyOption[String]("CODE").getOrElse("")
  val lineNumber: Int = node.propertyOption[Int]("LINE_NUMBER").getOrElse(0)
  val columnNumber: Int = node.propertyOption[Int]("COLUMN_NUMBER").getOrElse(0)
  val label: String = node.label
  val fileName: String =
    node._sourceFileIn
      .toSeq
      .headOption
      .map(_.propertyOption[String]("NAME").getOrElse("UNKNOWN"))
      .getOrElse("UNKNOWN")


  def dumpNodeInfo(): Unit = {
    println(
      s"CustomNode - ID: $nodeId, Label: $label, Code: $code, Line: $lineNumber, Column: $columnNumber, File: $fileName"
    )
  }

  override def equals(obj: Any): Boolean = obj match {
    case other: CustomNode => this.nodeId == other.nodeId
    case _                 => false
  }

  override def hashCode(): Int = nodeId.hashCode()
}
