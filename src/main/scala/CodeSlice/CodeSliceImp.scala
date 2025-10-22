package CodeSlice

import io.joern.joerncli.{JoernParse, JoernSlice}
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

  def getCpg() : Cpg = this.cpg

  def saveCpgToFile(outputPath: String): Unit = {
      val configure = Config()
        .withTsTypes(true)
        .withInputPath(inputDir)
        .withOutputPath(outputPath)
    JsSrc2Cpg().run(configure)
    val args = Array("data-flow",outputPath,"-o","/Users/khoanguyen/Workspace/code-slice/codeslice/src/test/scala/output/slices.json")
    JoernSlice.main(args)
  }
}
