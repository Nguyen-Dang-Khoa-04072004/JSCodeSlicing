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

  // TODO: thang
  override def getSinkMethodGroup: SinkMethodGroup = {
    val sinkMethodGroup = new SinkMethodGroup()

    for (sink <- SinkGroups.getAllSinks) {
      sink match {
        case CallType(value) =>
        // TODO: Handle CallType sinks - process function calls
        // Example: find calls to eval, exec, fs.writeFile, etc.

        case CodeType(value) =>
        // TODO: Handle CodeType sinks - process code/variable references
        // Example: find references to innerHTML, location.href, etc.

        case RegexType(value) =>
        // TODO: Handle RegexType sinks - process pattern matches
        // Example: find patterns matching dangerous operations
      }
    }

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
            val storeNode = new CustomNode(call)
            sourceMethodGroup.appendNode(storeNode)
          }

        case CodeType(value) =>
          val codes = cpg.identifier
            .where(_.name(value))
            .toSet

          for (code <- codes) {
            val storeNode = new CustomNode(code)
            sourceMethodGroup.appendNode(storeNode)
          }

        case RegexType(value) =>
          val pattern = value.r
          val patternMatches = cpg.call
            .where(_.name(pattern.regex))
            .toSet

          for (patternMatch <- patternMatches) {
            val storeNode = new CustomNode(patternMatch)
            sourceMethodGroup.appendNode(storeNode)
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
  ): PathLine = ???
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
