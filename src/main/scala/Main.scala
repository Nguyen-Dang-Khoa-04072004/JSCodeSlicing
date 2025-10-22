import CodeSlice.{CodeSlice, CodeSliceImp}
import FileProcessor.{IOFileProcessorImpl, IOFileProcessor}

object Main {

  def main(args: Array[String]): Unit = {

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
    }
  }

}
