import CodeSlice.CodeSliceImp
import org.scalatest.funsuite.AnyFunSuite


class CodeSlicingTest extends AnyFunSuite{
    val codeSlice = new CodeSliceImp("/Users/khoanguyen/Workspace/code-slice/codeslice/src/test/scala/input")
    test("test cpg"){
      codeSlice.saveCpgToFile("/Users/khoanguyen/Workspace/code-slice/codeslice/src/test/scala/output/cpg.bin")
    }
}
