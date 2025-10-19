import io.joern.jssrc2cpg.{Config, JsSrc2Cpg}
import io.shiftleft.codepropertygraph.Cpg

class CodeSliceImp(inputDir : String, codeSliceFilePath : String) extends CodeSlice {
  val config : Config = Config()
    .withTsTypes(true)
    .withInputPath(inputDir)
  val cpg: Cpg  = JsSrc2Cpg().createCpg(config).get
  // TODO: thang
  override def getSinkMethodGroup(sinkGroup: SinkGroup): SinkMethodGroup = ???
  // TODO: thang
  override def getSourceMethodGroup(sourceGroup: SourceGroup): SourceMethodGroup = ???
  // TODO: khoa
  override def getPathLine(sourceMethodGroup: SourceMethodGroup, sinkMethodGroup: SinkMethodGroup): PathLine = ???
  // TODO: thang
  override def extractCode(pathLine: PathLine): Unit = ???
}
