class Main {
    val codeSlice : CodeSlice = new CodeSliceImp("../../input", "../../output/slice.txt")
    val sourceGroup : SourceGroup = new SourceGroup
    val sinkGroup : SinkGroup = new SinkGroup
    def main(args: Array[String]): Unit = {
        val sourceMethodGroup = codeSlice.getSourceMethodGroup(sourceGroup)
        val sinkMethodGroup = codeSlice.getSinkMethodGroup(sinkGroup)
        val pathLine = codeSlice.getPathLine(sourceMethodGroup,sinkMethodGroup)
        codeSlice.extractCode(pathLine)
    }
}
