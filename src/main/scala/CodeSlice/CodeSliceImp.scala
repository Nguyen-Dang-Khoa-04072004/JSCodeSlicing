package CodeSlice

import io.joern.jssrc2cpg.{Config, JsSrc2Cpg}
import scala.util.{Success, Failure}
import Type.Source.SourceGroups
import Type.Sink.SinkGroups
import Type.{CallType, CodeType, RegexType}

class CodeSliceImp(inputDir: String, outputDir: String) extends CodeSlice {

  println(inputDir)
  println(outputDir)

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
}
