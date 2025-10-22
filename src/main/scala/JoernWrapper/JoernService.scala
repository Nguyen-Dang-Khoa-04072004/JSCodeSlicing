package JoernWrapper

import io.joern.jssrc2cpg.Config
import io.shiftleft.codepropertygraph.Cpg
import io.shiftleft.codepropertygraph.generated.nodes._

import scala.util.Try

/**
 * Service trait for Joern operations
 * This allows for easier testing and alternative implementations
 */
trait JoernService {
  
  def createCpg(inputPath: String, enableTsTypes: Boolean = true): Try[Cpg]
  
  def createCpgWithConfig(config: Config): Try[Cpg]
  
  def getAllMethods(cpg: Cpg): Iterator[Method]
  
  def getMethodsByName(cpg: Cpg, methodName: String): Iterator[Method]
  
  def getAllCalls(cpg: Cpg): Iterator[Call]
  
  def getCallsByName(cpg: Cpg, methodName: String): Iterator[Call]
  
  def getLineNumber(node: AstNode): Option[Int]
  
  def getFileName(node: AstNode): Option[String]
  
  def closeCpg(cpg: Cpg): Unit
  
  def withCpg[T](inputPath: String, enableTsTypes: Boolean = true)(operation: Cpg => T): Try[T]
}

/**
 * Default implementation of JoernService using JoernWrapper
 */
object JoernServiceImpl extends JoernService {
  
  override def createCpg(inputPath: String, enableTsTypes: Boolean = true): Try[Cpg] =
    JoernWrapper.createCpg(inputPath, enableTsTypes)
  
  override def createCpgWithConfig(config: Config): Try[Cpg] =
    JoernWrapper.createCpgWithConfig(config)
  
  override def getAllMethods(cpg: Cpg): Iterator[Method] =
    JoernWrapper.getAllMethods(cpg)
  
  override def getMethodsByName(cpg: Cpg, methodName: String): Iterator[Method] =
    JoernWrapper.getMethodsByName(cpg, methodName)
  
  override def getAllCalls(cpg: Cpg): Iterator[Call] =
    JoernWrapper.getAllCalls(cpg)
  
  override def getCallsByName(cpg: Cpg, methodName: String): Iterator[Call] =
    JoernWrapper.getCallsByName(cpg, methodName)
  
  override def getLineNumber(node: AstNode): Option[Int] =
    JoernWrapper.getLineNumber(node)
  
  override def getFileName(node: AstNode): Option[String] =
    JoernWrapper.getFileName(node)
  
  override def closeCpg(cpg: Cpg): Unit =
    JoernWrapper.closeCpg(cpg)
  
  override def withCpg[T](inputPath: String, enableTsTypes: Boolean = true)(operation: Cpg => T): Try[T] =
    JoernWrapper.withCpg(inputPath, enableTsTypes)(operation)
}
