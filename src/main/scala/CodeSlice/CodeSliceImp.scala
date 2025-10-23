package CodeSlice

import io.joern.jssrc2cpg.{Config, JsSrc2Cpg}
import io.joern.x2cpg.X2Cpg
import io.shiftleft.codepropertygraph.generated.Cpg

import scala.util.{Failure, Success}
import Type.Source.SourceGroups
import Type.Sink.SinkGroups
import Type.{CallType, CodeType, RegexType}
import io.joern.joerncli.{JoernParse, JoernSlice}

import java.nio.file.Paths

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
        // TODO: Handle CallType sources - process function calls
        // Example: find calls to eval, exec, fs.writeFile, etc.

        case CodeType(value) =>
        // TODO: Handle CodeType sources - process code/variable references
        // Example: find references to innerHTML, location.href, etc.

        case RegexType(value) =>
        // TODO: Handle RegexType sources - process pattern matches
        // Example: find patterns matching dangerous operations
      }
    }

    sourceMethodGroup
  }
  // TODO: khoa
  override def getPathLine(
      sourceMethodGroup: SourceMethodGroup,
      sinkMethodGroup: SinkMethodGroup
  ): PathLine = ???
  // TODO: thang
  override def extractCode(pathLine: PathLine): String = ???

  def saveCpgToJsonFile(): Unit = {
      JsSrc2Cpg().run(Config()
      .withInputPath(inputDir)
      .withOutputPath(outputDir + "/cpg.bin"))
//      val args = Array("data-flow",outputDir + "/cpg.bin", "-o", outputDir + "/slices.json")
//      args.foreach(arg => println(arg))
//      JoernSlice.main(args)
  }
}
