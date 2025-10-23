import CodeSlice.CodeSliceImp
import org.scalatest.funsuite.AnyFunSuite


class CodeSlicingTest extends AnyFunSuite{
    val codeSlice = new CodeSliceImp("/Users/khoanguyen/Workspace/code-slice/codeslice/src/test/scala/input","/Users/khoanguyen/Workspace/code-slice/codeslice/src/test/scala/output")
    test("test cpg"){
      codeSlice.saveCpgToJsonFile()
    }
}
