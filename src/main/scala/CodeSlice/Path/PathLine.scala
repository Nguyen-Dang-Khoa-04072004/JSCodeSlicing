package CodeSlice.Path

import io.shiftleft.codepropertygraph.generated.nodes.*

import scala.collection.mutable.{Map, Set}
import flatgraph.Edge
import CodeSlice.Group.CustomNode

class PathLine {
  val flows: Set[CustomNode] = Set()
  val potentialPaths: Map[CustomNode, Set[CustomNode]] = Map()
  
  def addToSlice(node: CustomNode): Unit = {
    val existingNodeOpt = flows.find(n =>
      n.fileName == node.fileName && 
      n.lineNumber == node.lineNumber
    )
    
    existingNodeOpt match {
      case Some(existingNode) =>
        if (node.code.length > existingNode.code.length) {
          flows.remove(existingNode)
          flows.add(node)
        }
      case None =>
        flows.add(node)
    }
  }

  def addPotentialPaths(source: CustomNode, sinks: Set[CustomNode]): Unit = {
    if (potentialPaths.contains(source)) {
      potentialPaths(source) ++= sinks
    } else {
      potentialPaths(source) = sinks
    }
  }

  def exportCodeSlice(): String = {
    // Sắp xếp theo file và line number
    val sortedFlows = this.flows.toSeq.sortBy(node => (node.fileName, node.lineNumber))
    
    // Build code slice với format rõ ràng
    buildCodeSliceOutput(sortedFlows)
  }
  
  // Build output với format rõ ràng
  private def buildCodeSliceOutput(sortedFlows: Seq[CustomNode]): String = {
    val builder = new StringBuilder
    var currentFile = ""
    
    for (node <- sortedFlows) {
      if (!node.fileName.isEmpty()) {
        if (node.fileName != currentFile) {
          if (currentFile.nonEmpty) builder.append("\n")
          currentFile = node.fileName
          builder.append(s"// File: $currentFile\n")
          builder.append("// " + "=" * 70 + "\n")
        }
        
        builder.append(node.code + "\n")
      
      }
    }
    
    builder.toString()
  }

}
