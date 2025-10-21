package CodeSlice

import io.joern.jssrc2cpg.{Config, JsSrc2Cpg}
import io.shiftleft.codepropertygraph.Cpg

class CodeSliceImp(inputDir : String) extends CodeSlice {

  private val config : Config = Config()
    .withTsTypes(true)
    .withInputPath(inputDir)
  private val cpg: Cpg  = JsSrc2Cpg().createCpg(config).get

  // TODO: thang
  override def getSinkMethodGroup: SinkMethodGroup = ???
  // TODO: thang
  override def getSourceMethodGroup: SourceMethodGroup = ???
  // TODO: khoa
  override def getPathLine(sourceMethodGroup: SourceMethodGroup, sinkMethodGroup: SinkMethodGroup): PathLine = ???
  // TODO: thang
  override def extractCode(pathLine: PathLine): String = ???
}
