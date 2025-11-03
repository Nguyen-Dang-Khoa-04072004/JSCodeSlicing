package CodeSlice.Path

import io.shiftleft.codepropertygraph.generated.nodes.*

import scala.collection.mutable.{Map, Set}
import flatgraph.Edge
import CodeSlice.Group.CustomNode

class PathLine {
  val flows: Set[CustomNode] = Set()
  val potentialPaths: Map[CustomNode, Set[CustomNode]] = Map()
  
  def addToSlice(node: CustomNode): Unit = {
    flows.add(node)
  }

  def addPotentialPaths(source: CustomNode, sinks: Set[CustomNode]): Unit = {
    if (potentialPaths.contains(source)) {
      potentialPaths(source) ++= sinks
    } else {
      potentialPaths(source) = sinks
    }
  }

  def exportCodeSlice(): String = {
    val sortedFlows = flows.toSeq.sortBy(node => (node.fileName, node.lineNumber))
    val codeSliceBuilder = new StringBuilder
    var currentFile: String = ""
    for (node <- sortedFlows) {
      if (node.fileName != currentFile) {
        currentFile = node.fileName
        codeSliceBuilder.append(s"\n// File: ${currentFile}\n")
      }
      codeSliceBuilder.append(s"${node.code}\n")
    }
    codeSliceBuilder.toString()
  }
}
