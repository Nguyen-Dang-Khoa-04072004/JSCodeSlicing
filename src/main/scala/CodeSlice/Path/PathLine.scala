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
    // Danh sách các node labels và code patterns không mang ngữ nghĩa cần loại bỏ
    val nonSemanticLabels = Set("FILE", "NAMESPACE_BLOCK", "TYPE_DECL", "BINDING")
    val nonSemanticCodePatterns = Set("<empty>", ":program", "window", "this", "undefined", "null", "true", "false")
    
    // Lọc các node có ý nghĩa
    val meaningfulFlows = flows.filter { node =>
      // Loại bỏ node có label không mang ngữ nghĩa
      !nonSemanticLabels.contains(node.label) &&
      // Loại bỏ node có code trống hoặc chỉ chứa whitespace
      node.code.trim.nonEmpty &&
      // Loại bỏ node có code pattern không mang ngữ nghĩa
      !nonSemanticCodePatterns.contains(node.code.trim) &&
      // Loại bỏ các node chỉ chứa các ký tự đặc biệt
      node.code.trim.exists(_.isLetterOrDigit) &&
      // Loại bỏ sink methods (CreateObject, Run, etc.) khỏi output
      !isSinkMethod(node.code)
    }
    
    // Loại bỏ trùng lặp dựa trên (fileName, lineNumber, code)
    val uniqueFlows = meaningfulFlows
      .groupBy(node => (node.fileName, node.lineNumber, node.code))
      .values
      .map(_.head)
      .toSeq
      .sortBy(node => (node.fileName, node.lineNumber))
    
    // Build code slice
    val codeSliceBuilder = new StringBuilder
    var currentFile: String = ""
    
    for (node <- uniqueFlows) {
      if (node.fileName != currentFile) {
        if (currentFile.nonEmpty) {
          codeSliceBuilder.append("\n")
        }
        currentFile = node.fileName
        codeSliceBuilder.append(s"// File: ${currentFile}\n")
      }
      codeSliceBuilder.append(s"${node.code}\n")
    }
    
    codeSliceBuilder.toString()
  }
  
  // Helper method để kiểm tra xem có phải sink method không
  private def isSinkMethod(code: String): Boolean = {
    val sinkPatterns = List(
      "CreateObject",
      "WScript.Shell",
      ".Run\\(",
      ".Exec\\(",
      ".ShellExecute",
      "eval\\(",
      "Function\\(",
      "ActiveXObject",
      "GetObject",
      "document.write"
    )
    
    sinkPatterns.exists(pattern => code.matches(s".*$pattern.*"))
  }
}
