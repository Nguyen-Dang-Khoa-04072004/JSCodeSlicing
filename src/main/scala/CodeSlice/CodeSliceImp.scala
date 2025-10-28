package CodeSlice

import io.joern.jssrc2cpg.{Config, JsSrc2Cpg}
import io.shiftleft.codepropertygraph.generated.Cpg
import scala.util.{Failure, Success}
import Type.Source.SourceGroups
import Type.Sink.SinkGroups
import Type.{CallType, CodeType, RegexType}
import io.joern.joerncli.{JoernParse, JoernSlice}
import CodeSlice.Group.{SourceMethodGroup, SinkMethodGroup}
import CodeSlice.Path.PathLine
import io.shiftleft.semanticcpg.language._
import io.joern.dataflowengineoss.language._
import scala.collection.mutable.Set
import java.nio.file.Paths
import CodeSlice.Group.CustomNode
import scala.util.matching.Regex
import io.joern.dataflowengineoss.language._

class CodeSliceImp(inputDir: String, outputDir: String) extends CodeSlice {

  println(inputDir)
  println(outputDir)

  private val cpg: Cpg = {
    println("ASTGEN_BIN = " + sys.env.get("ASTGEN_BIN"))
    val config = Config()
      .withInputPath(inputDir)
      .withOutputPath(s"$outputDir/cpg.bin")

    val jsSrc2Cpg = new JsSrc2Cpg()
    jsSrc2Cpg.createCpg(config) match {
      case Success(cpg) => cpg
      case Failure(exception) =>
        throw new RuntimeException(
          s"Failed to create CPG from source code at $inputDir",
          exception
        )
    }
  }

  private val edges = cpg.graph.allEdges.toSet
  private val files = cpg.file.toSet

  // Print all edges for debugging
  edges.foreach(edge =>
    println(
      f"label=${edge.label}, src=${edge.src.id}, dst=${edge.dst.id}"
    )
  )

  // TODO: thang
  override def getSinkMethodGroup: SinkMethodGroup = {
    val sinkMethodGroup = new SinkMethodGroup()

    for (sink <- SinkGroups.getAllSinks) {
      sink match {
        case CallType(value) =>
          val calls = cpg.call
            .where(_.name(value))
            .toSet
          for (call <- calls) {
            // val storeNode = new CustomNode(call)
            sinkMethodGroup.appendNode(call)
          }

        case CodeType(value) =>
          val codes = cpg.identifier
            .where(_.name(value))
            .toSet
          for (code <- codes) {
            // val storeNode = new CustomNode(code)
            sinkMethodGroup.appendNode(code)
          }

        case RegexType(value) =>
          val pattern = value.r
          val patternMatches = cpg.call
            .where(_.name(pattern.regex))
            .toSet
          for (patternMatch <- patternMatches) {
            // val storeNode = new CustomNode(patternMatch)
            sinkMethodGroup.appendNode(patternMatch)
          }
      }
    }
    sinkMethodGroup.dumpNodeInfo()
    sinkMethodGroup
  }

  // TODO: thang
  override def getSourceMethodGroup: SourceMethodGroup = {

    val sourceMethodGroup = new SourceMethodGroup()

    for (source <- SourceGroups.getAllSources) {
      source match {
        case CallType(value) =>
          val calls = cpg.call
            .where(_.name(value))
            .toSet

          for (call <- calls) {
            // val storeNode = new CustomNode(call)
            sourceMethodGroup.appendNode(call)
          }

        case CodeType(value) =>
          val codes = cpg.identifier
            .where(_.name(value))
            .toSet

          for (code <- codes) {
            // val storeNode = new CustomNode(code)
            sourceMethodGroup.appendNode(code)
          }

        case RegexType(value) =>
          val pattern = value.r
          val patternMatches = cpg.call
            .where(_.name(pattern.regex))
            .toSet

          for (patternMatch <- patternMatches) {
            // val storeNode = new CustomNode(patternMatch)
            sourceMethodGroup.appendNode(patternMatch)
          }
      }
    }

    sourceMethodGroup.dumpNodeInfo()
    sourceMethodGroup
  }

  override def close(): Unit = {
    cpg.close()
    println(s"Cleaned up resources for $inputDir")
  }

  // TODO: khoa
  override def getPathLine(
      sourceMethodGroup: SourceMethodGroup,
      sinkMethodGroup: SinkMethodGroup
  ): PathLine = {
    var pathLine = new PathLine()
    for (sourceMethod <- sourceMethodGroup.getAllNodes) {
      for (sinkMethod <- sinkMethodGroup.getAllNodes) {
        for (edge <- edges) {
          if (
            edge.src.id
              .equals(sourceMethod.id) && (edge.dst.id.equals(sinkMethod.id))
          ) {
            // Có đường trực tiếp từ source -> sink
            pathLine.addEdge(edge)
          } else {
            // Lưu lại đường tiềm năng từ source -> sink
            pathLine.addPotentialPaths(sourceMethod, sinkMethod)
          }
        }
      }
    }

    pathLine
  }

  // TODO: thang
  override def extractCode(pathLine: PathLine): String = ???

  def saveCpgToJsonFile(): Unit = {
    JsSrc2Cpg().run(
      Config()
        .withInputPath(inputDir)
        .withOutputPath(outputDir + "/cpg.bin")
    )
//      val args = Array("data-flow",outputDir + "/cpg.bin", "-o", outputDir + "/slices.json")
//      args.foreach(arg => println(arg))
//      JoernSlice.main(args)
  }
}
