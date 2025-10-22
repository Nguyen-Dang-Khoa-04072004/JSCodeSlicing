import CodeSlice.{CodeSlice, CodeSliceImp}
import FileProcessor.{IOFileProcessorImpl, IOFileProcessor}

object Main {

    def main(args: Array[String]): Unit = {

        val ioFileProcessor: IOFileProcessor = IOFileProcessorImpl
        
        for (packagePath: String <- ioFileProcessor.listPackageDirectories("src/main/resources/input/")) {
            val codeSlice: CodeSlice = new CodeSliceImp(packagePath, "src/main/resources/output/")
            // Execute the slicing process (when methods are implemented)
            // codeSlice.asInstanceOf[CodeSliceImp].executeSlicing()
        }
    }

}
