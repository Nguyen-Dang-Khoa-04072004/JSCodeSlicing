package CodeSlice.Group

import io.shiftleft.codepropertygraph.generated.Cpg
import io.shiftleft.codepropertygraph.generated.nodes.*
import scala.collection.mutable.{Set, Map}
import CodeSlice.Group.CustomNode

class SourceMethodGroup {
  private val nodes = scala.collection.mutable.Map[Long, StoredNode]()

  def appendNode(newNode: StoredNode): Unit = {
    val nodeId: Long = newNode.id()
    nodes.getOrElseUpdate(nodeId, newNode)
  }

  def dumpNodeInfo(): Unit = {
    for ((_, node) <- nodes) {
      val customNode = new CustomNode(node)
      customNode.dumpNodeInfo()
    }
  }

}
