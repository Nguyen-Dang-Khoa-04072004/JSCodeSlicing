import CodeSlice.{CodeSlice, CodeSliceImp}
import FileProcessor.{IOFileProcessorImpl, IOFileProcessor}

object Main {

    def main(args: Array[String]): Unit = {

        val ioFileProcessor: IOFileProcessor = IOFileProcessorImpl

        for (packagePath: String <- ioFileProcessor.listPackageDirectories("src/main/resources/input/")) {

            val codeSlice: CodeSlice = new CodeSliceImp(packagePath)

            val sourceMethodGroup    = codeSlice.getSourceMethodGroup
            val sinkMethodGroup      = codeSlice.getSinkMethodGroup
            val pathLine             = codeSlice.getPathLine(sourceMethodGroup,sinkMethodGroup)
            val codeSliced           = codeSlice.extractCode(pathLine)

            ioFileProcessor.saveOutputPackage("src/main/resources/output/" + ioFileProcessor.getPackageName(packagePath) + "_sliced.txt", codeSliced)
            val checkpoint = ioFileProcessor.saveCheckpoint("src/main/resources/checkpoint/checkpoint.txt", ioFileProcessor.getPackageName(packagePath))

        }
    }

}
