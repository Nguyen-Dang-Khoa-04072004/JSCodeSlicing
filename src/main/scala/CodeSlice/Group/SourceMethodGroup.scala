package CodeSlice.Group

import io.shiftleft.codepropertygraph.generated.Cpg

import scala.collection.mutable.{Set, Map}
import CodeSlice.Group.CustomNode

class SourceMethodGroup {
  private val nodes = scala.collection.mutable.Map[Long, CustomNode]()

  def appendNode(newNode: CustomNode): Unit = {

    if (!this.nodes.contains(newNode.nodeId)) {
      this.nodes(newNode.nodeId) = newNode
    }
  }

  def dumpNodeInfo(): Unit = {
    println(s"Dumping info for ${nodes.size} nodes in SourceMethodGroup:")
    for ((_, node) <- nodes) {
      node.dumpNodeInfo()
    }
  }

}
