import CodeSlice.{CodeSlice, CodeSliceImp}
import FileProcessor.*

object Main {

  def main(args: Array[String]): Unit = {

    print("\u001b[H\u001b[2J")

    val ioFileProcessor: IOFileProcessor = IOFileProcessorImpl

    for (
      packagePath: String <- ioFileProcessor.listPackageDirectories(
        "src/main/resources/input/"
      )
    ) {
      val codeSlice: CodeSlice = new CodeSliceImp(
        packagePath,
        ioFileProcessor.createOutputDirectoryIfNotExists(
          "src/main/resources/output/" + ioFileProcessor.getPackageName(
            packagePath
          ) + "/"
        )
      )

      val sourceMethod = codeSlice.getSourceMethodGroup
      print("\n====================\n")
      val sinkMethod = codeSlice.getSinkMethodGroup
      print("\n====================\n")
      val pathLine = codeSlice.getPathLine(sourceMethod, sinkMethod)
      print("\n====================\n")
      // Export code slice to file
      val outputDir = "src/main/resources/output/" + ioFileProcessor.getPackageName(packagePath) + "/"
      val codeSliceOutputPath = ioFileProcessor.saveOutputPackage(
        outputDir + "code_slice.txt",
        pathLine.exportCodeSlice()
      )

      if (codeSliceOutputPath) {
        println(
          s"Code slice saved successfully for package ${ioFileProcessor.getPackageName(packagePath)}"
        )
      } else {
        println(
          s"Failed to save code slice for package ${ioFileProcessor.getPackageName(packagePath)}"
        )
      }

      codeSlice.close()
    }
  }

}
