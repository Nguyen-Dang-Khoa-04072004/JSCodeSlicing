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
    // Lọc các node có ý nghĩa
    val meaningfulFlows = flows.filter { node =>
      val trimmedCode = node.code.trim
      isMeaningfulNode(node, trimmedCode)
    }
    
    // Loại bỏ trùng lặp và subsumption (code nhỏ nằm trong code lớn)
    val uniqueFlows = removeSubsumedAndDuplicates(meaningfulFlows)
    
    // Sắp xếp theo file và line number
    val sortedFlows = uniqueFlows.toSeq.sortBy(node => (node.fileName, node.lineNumber))
    
    // Build code slice với format rõ ràng
    buildCodeSliceOutput(sortedFlows)
  }
  
  // Kiểm tra node có ý nghĩa không
  private def isMeaningfulNode(node: CustomNode, trimmedCode: String): Boolean = {
    // Label không có ý nghĩa
    val nonSemanticLabels = Set("FILE", "NAMESPACE_BLOCK", "TYPE_DECL", "BINDING")
    if (nonSemanticLabels.contains(node.label)) return false
    
    // Code rỗng hoặc pattern không có ý nghĩa
    val nonSemanticPatterns = Set("<empty>", ":program", "window", "this")
    if (trimmedCode.isEmpty || nonSemanticPatterns.contains(trimmedCode)) return false
    
    // Phải có ít nhất 1 chữ cái hoặc số
    if (!trimmedCode.exists(_.isLetterOrDigit)) return false
    
    // Loại bỏ IDENTIFIER đơn lẻ (không có context)
    if (node.label == "IDENTIFIER" && isSingleIdentifier(trimmedCode)) return false
    
    // Loại bỏ LITERAL đơn lẻ trừ khi là string quan trọng
    if (node.label == "LITERAL" && !isImportantLiteral(trimmedCode)) return false
    
    // Giữ lại các statement/expression quan trọng
    true
  }
  
  // Kiểm tra identifier đơn (không có context)
  private def isSingleIdentifier(code: String): Boolean = {
    // Nếu có operator, dấu ngoặc, dấu chấm => không phải identifier đơn
    val meaningfulChars = Set('(', ')', '[', ']', '{', '}', '.', '=', '+', '-', '*', '/', 
                               '<', '>', '!', '&', '|', '?', ':', ';', ',', '"', '\'', '`', ' ')
    
    if (code.exists(meaningfulChars.contains)) return false
    
    // Identifier đơn: chỉ gồm chữ, số, underscore và không phải keyword quan trọng
    !isImportantKeyword(code)
  }
  
  // Kiểm tra literal có quan trọng không
  private def isImportantLiteral(code: String): Boolean = {
    // Giữ lại string literal dài hoặc có pattern đặc biệt
    if (code.length > 20) return true
    
    // Pattern URL, command, path
    val importantPatterns = List(
      "http", "https", "ftp", "://",
      ".exe", ".dll", ".dat", ".php",
      "cmd", "powershell", "wscript",
      "$env:", "Invoke", "Download", "CreateObject"
    )
    
    importantPatterns.exists(pattern => code.toLowerCase.contains(pattern.toLowerCase))
  }
  
  // Kiểm tra keyword quan trọng
  private def isImportantKeyword(code: String): Boolean = {
    val keywords = Set(
      "eval", "function", "return", "switch", "case", "break",
      "if", "else", "for", "while", "typeof", "new"
    )
    keywords.contains(code.toLowerCase)
  }
  
  // Loại bỏ code bị subsume và trùng lặp
  private def removeSubsumedAndDuplicates(flows: Set[CustomNode]): scala.collection.immutable.Set[CustomNode] = {
    val flowsList = flows.toList
    val normalizedMap = scala.collection.mutable.Map[String, CustomNode]()
    
    for (node <- flowsList) {
      val normalized = normalizeCode(node.code)
      
      // Nếu chưa có code này, thêm vào
      if (!normalizedMap.contains(normalized)) {
        normalizedMap(normalized) = node
      } else {
        // Nếu đã có, giữ lại node có code dài hơn (chi tiết hơn)
        val existing = normalizedMap(normalized)
        if (node.code.length > existing.code.length) {
          normalizedMap(normalized) = node
        }
      }
    }
    
    // Loại bỏ code nhỏ bị subsume bởi code lớn hơn
    val result = scala.collection.mutable.Set[CustomNode]()
    val codes = normalizedMap.values.toList.sortBy(-_.code.length) // Sort by length descending
    
    for (node <- codes) {
      val nodeCode = normalizeCode(node.code)
      // Kiểm tra xem code này có bị subsume bởi code nào lớn hơn không
      val isSubsumed = result.exists { existingNode =>
        val existingCode = normalizeCode(existingNode.code)
        existingCode.length > nodeCode.length && existingCode.contains(nodeCode)
      }
      
      if (!isSubsumed) {
        result.add(node)
      }
    }
    
    result.toSet // Convert mutable.Set to immutable Set
  }
  
  // Normalize code để so sánh
  private def normalizeCode(code: String): String = {
    code.replaceAll("\\s+", " ").trim
  }
  
  // Build output với format rõ ràng
  private def buildCodeSliceOutput(sortedFlows: Seq[CustomNode]): String = {
    val builder = new StringBuilder
    var currentFile = ""
    
    for (node <- sortedFlows) {
      // Header cho mỗi file
      if (node.fileName != currentFile) {
        if (currentFile.nonEmpty) builder.append("\n")
        currentFile = node.fileName
        builder.append(s"// File: $currentFile\n")
        builder.append("// " + "=" * 70 + "\n")
      }
      
      // Thêm comment với thông tin node type nếu quan trọng
      val nodeType = node.label match {
        case "CALL" => " [CALL]"
        case "CONTROL_STRUCTURE" => " [CONTROL]"
        case "BLOCK" => " [BLOCK]"
        case "METHOD" => " [METHOD]"
        case _ => ""
      }
      
      // Xuất code
      builder.append(s"${node.code}$nodeType\n")
    }
    
    builder.toString()
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
