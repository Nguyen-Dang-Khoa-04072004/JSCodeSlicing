package JoernWrapper

import io.joern.jssrc2cpg.{Config, JsSrc2Cpg}
import io.shiftleft.codepropertygraph.Cpg
import io.shiftleft.codepropertygraph.generated.nodes._
import io.shiftleft.semanticcpg.language._
import better.files.{File => BFile}

import scala.util.{Try, Success, Failure}
import java.io.{File => JFile}

/**
 * JoernWrapper provides a convenient interface to work with Joern tool
 * for code property graph (CPG) generation and analysis.
 */
object JoernWrapper {

  /**
   * Creates a Code Property Graph from the given input directory
   * 
   * @param inputPath Path to the JavaScript/TypeScript source code
   * @param enableTsTypes Enable TypeScript type information (default: true)
   * @return Try[Cpg] containing the generated CPG or error
   */
  def createCpg(inputPath: String, enableTsTypes: Boolean = true): Try[Cpg] = {
    Try {
      // Set astgen binary location if not already set
      if (System.getenv("ASTGEN_BIN") == null) {
        val homeDir = System.getProperty("user.home")
        val astgenPath = s"$homeDir\\.local\\share\\joern\\astgen\\astgen.exe"
        if (new JFile(astgenPath).exists()) {
          System.setProperty("ASTGEN_BIN", astgenPath)
        }
      }
      
      val absolutePath = new JFile(inputPath).getAbsolutePath
      val outputDir = BFile.newTemporaryDirectory("joern-cpg-")
      val outputPath = outputDir / "cpg.bin"
      
      val config = Config()
        .withTsTypes(enableTsTypes)
        .withInputPath(absolutePath)
        .withOutputPath(outputPath.pathAsString)
      
      val jsSrc2Cpg = new JsSrc2Cpg()
      val cpgTry = jsSrc2Cpg.createCpgWithOverlays(config)
      
      cpgTry match {
        case Success(cpg) => cpg
        case Failure(exception) =>
          println(s"Error creating CPG: ${exception.getMessage}")
          throw exception
      }
    }
  }

  /**
   * Creates a CPG with custom configuration
   * 
   * @param config Custom Joern configuration
   * @return Try[Cpg] containing the generated CPG or error
   */
  def createCpgWithConfig(config: Config): Try[Cpg] = {
    Try {
      JsSrc2Cpg().createCpg(config).get
    }
  }

  /**
   * Get all method nodes from the CPG
   * 
   * @param cpg Code Property Graph
   * @return Iterator of Method nodes
   */
  def getAllMethods(cpg: Cpg): Iterator[Method] = {
    cpg.method.iterator
  }

  /**
   * Get methods by name
   * 
   * @param cpg Code Property Graph
   * @param methodName Name of the method to search
   * @return Iterator of Method nodes matching the name
   */
  def getMethodsByName(cpg: Cpg, methodName: String): Iterator[Method] = {
    cpg.method.name(methodName).iterator
  }

  /**
   * Get all call nodes from the CPG
   * 
   * @param cpg Code Property Graph
   * @return Iterator of Call nodes
   */
  def getAllCalls(cpg: Cpg): Iterator[Call] = {
    cpg.call.iterator
  }

  /**
   * Get calls by method name
   * 
   * @param cpg Code Property Graph
   * @param methodName Name of the called method
   * @return Iterator of Call nodes
   */
  def getCallsByName(cpg: Cpg, methodName: String): Iterator[Call] = {
    cpg.call.name(methodName).iterator
  }

  /**
   * Get all identifiers from the CPG
   * 
   * @param cpg Code Property Graph
   * @return Iterator of Identifier nodes
   */
  def getAllIdentifiers(cpg: Cpg): Iterator[Identifier] = {
    cpg.identifier.iterator
  }

  /**
   * Get all literals from the CPG
   * 
   * @param cpg Code Property Graph
   * @return Iterator of Literal nodes
   */
  def getAllLiterals(cpg: Cpg): Iterator[Literal] = {
    cpg.literal.iterator
  }

  /**
   * Get all file nodes from the CPG
   * 
   * @param cpg Code Property Graph
   * @return Iterator of File nodes
   */
  def getAllFiles(cpg: Cpg): Iterator[File] = {
    cpg.file.iterator
  }

  /**
   * Get data flow paths from source to sink
   * 
   * @param cpg Code Property Graph
   * @param source Starting node
   * @param sink Ending node
   * @return List of paths
   */
  def getDataFlowPaths(cpg: Cpg, source: Iterator[?], sink: Iterator[?]): List[List[String]] = {
    Try {
      // This is a placeholder - actual implementation depends on specific requirements
      // and Joern's data flow tracking capabilities
      List.empty[List[String]]
    }.getOrElse(List.empty)
  }

  /**
   * Get control flow information for a method
   * 
   * @param method Method node
   * @return Control flow information
   */
  def getControlFlow(method: Method): Iterator[CfgNode] = {
    method.cfgNode.iterator
  }

  /**
   * Get line number for a node
   * 
   * @param node Any CPG node with tracking information
   * @return Option containing the line number
   */
  def getLineNumber(node: AstNode): Option[Int] = {
    node.lineNumber
  }

  /**
   * Get file location for a node
   * 
   * @param node Any CPG node
   * @return Option containing the file name
   */
  def getFileName(node: AstNode): Option[String] = {
    node.file.name.headOption
  }

  /**
   * Close and cleanup CPG resources
   * 
   * @param cpg Code Property Graph to close
   */
  def closeCpg(cpg: Cpg): Unit = {
    Try {
      cpg.close()
    } match {
      case Success(_) => println("CPG closed successfully")
      case Failure(exception) => println(s"Error closing CPG: ${exception.getMessage}")
    }
  }

  /**
   * Execute a custom query on the CPG
   * 
   * @param cpg Code Property Graph
   * @param queryFn Function that takes a CPG and returns results
   * @return Try containing the query results
   */
  def executeQuery[T](cpg: Cpg)(queryFn: Cpg => T): Try[T] = {
    Try {
      queryFn(cpg)
    }
  }

  /**
   * Get method parameters
   * 
   * @param method Method node
   * @return Iterator of MethodParameterIn nodes
   */
  def getMethodParameters(method: Method): Iterator[MethodParameterIn] = {
    method.parameter.iterator
  }

  /**
   * Get method return information
   * 
   * @param method Method node
   * @return Iterator of MethodReturn nodes
   */
  def getMethodReturn(method: Method): Iterator[MethodReturn] = {
    method.methodReturn.iterator
  }

  /**
   * Get all local variables in a method
   * 
   * @param method Method node
   * @return Iterator of Local nodes
   */
  def getLocalVariables(method: Method): Iterator[Local] = {
    method.local.iterator
  }

  /**
   * Create a CPG and execute operations on it, ensuring proper cleanup
   * 
   * @param inputPath Path to source code
   * @param operation Function to execute on the CPG
   * @return Try containing the operation result
   */
  def withCpg[T](inputPath: String, enableTsTypes: Boolean = true)(operation: Cpg => T): Try[T] = {
    createCpg(inputPath, enableTsTypes).flatMap { cpg =>
      try {
        Success(operation(cpg))
      } catch {
        case e: Exception => Failure(e)
      } finally {
        closeCpg(cpg)
      }
    }
  }
}
