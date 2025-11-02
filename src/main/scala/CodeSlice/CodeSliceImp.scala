package CodeSlice

import io.joern.jssrc2cpg.{Config, JsSrc2Cpg}
import io.shiftleft.codepropertygraph.generated.Cpg

import scala.util.{Failure, Success}
import Type.Source.SourceGroups
import Type.Sink.SinkGroups
import Type.{CallType, CodeType, RegexType}
import io.joern.joerncli.{JoernParse, JoernSlice}
import CodeSlice.Group.{SinkMethodGroup, SourceMethodGroup}
import CodeSlice.Path.PathLine
import io.shiftleft.semanticcpg.language.*
import io.joern.dataflowengineoss.language.*

import scala.collection.mutable.{Set, Queue}
import java.nio.file.Paths
import CodeSlice.Group.CustomNode

import flatgraph.Edge
import io.joern.dataflowengineoss.DefaultSemantics

import scala.util.matching.Regex
import io.joern.dataflowengineoss.language.*
import io.joern.dataflowengineoss.layers.dataflows.{OssDataFlow, OssDataFlowOptions}
import io.shiftleft.codepropertygraph.Cpg
import io.shiftleft.semanticcpg.layers.*
import io.joern.dataflowengineoss.queryengine.EngineContext
import io.joern.dataflowengineoss.semanticsloader.{FlowSemantic, NoCrossTaintSemantics, Semantics}
import io.joern.dataflowengineoss.slicing.{DataFlowConfig, DataFlowSlicing}
import io.shiftleft.codepropertygraph.generated.nodes.{Call, Method, StoredNode}
import io.shiftleft.semanticcpg.Overlays
import scala.collection.mutable.ListBuffer

// 30064771073 - 30064771076 - 30064771078
class CodeSliceImp(inputDir: String, outputDir: String) extends CodeSlice {

    println(inputDir)
    println(outputDir)

    private val cpg: Cpg = {
        println("ASTGEN_BIN = " + sys.env.get("ASTGEN_BIN"))
        val config = Config()
          .withInputPath(inputDir)
          .withOutputPath(s"$outputDir/cpg.bin")

        val jsSrc2Cpg = new JsSrc2Cpg()
        jsSrc2Cpg.createCpg(config) match {
            case Success(cpg) => cpg
            case Failure(exception) =>
                throw new RuntimeException(
                    s"Failed to create CPG from source code at $inputDir",
                    exception
                )
        }
    }

    private val edges = cpg.graph.allEdges.toSet
    private val nodes = cpg.graph.allNodes.toSet
    private val files = cpg.file.toSet
    private val engineContext = new EngineContext()

    // Print all edges for debugging
    //    edges.foreach(edge =>
    //        println(
    //            f"label=${edge.label}, src=${edge.src.id}, dst=${edge.dst.id}"
    //        )
    //    )
    //
    // TODO: thang
    override def getSinkMethodGroup: SinkMethodGroup = {
        val sinkMethodGroup = new SinkMethodGroup()

        for (sink <- SinkGroups.getAllSinks) {
            sink match {
                case CallType(value) =>
                    val calls = cpg.call
                      .where(_.name(value))
                      .toSet
                    for (call <- calls) {
                        // val storeNode = new CustomNode(call)
                        sinkMethodGroup.appendNode(call)
                    }

                case CodeType(value) =>
                    val codes = cpg.identifier
                      .where(_.name(value))
                      .toSet
                    for (code <- codes) {
                        // val storeNode = new CustomNode(code)
                        sinkMethodGroup.appendNode(code)
                    }

                case RegexType(value) =>
                    val pattern = value.r
                    val patternMatches = cpg.call
                      .where(_.name(pattern.regex))
                      .toSet
                    for (patternMatch <- patternMatches) {
                        // val storeNode = new CustomNode(patternMatch)
                        sinkMethodGroup.appendNode(patternMatch)
                    }
            }
        }
        sinkMethodGroup.dumpNodeInfo()
        sinkMethodGroup
    }

    // TODO: thang
    override def getSourceMethodGroup: SourceMethodGroup = {

        val sourceMethodGroup = new SourceMethodGroup()

        for (source <- SourceGroups.getAllSources) {
            source match {
                case CallType(value) =>
                    val calls = cpg.call
                      .where(_.name(value))
                      .toSet

                    for (call <- calls) {
                        // val storeNode = new CustomNode(call)
                        sourceMethodGroup.appendNode(call)
                    }

                case CodeType(value) =>
                    val codes = cpg.identifier
                      .where(_.name(value))
                      .toSet

                    for (code <- codes) {
                        // val storeNode = new CustomNode(code)
                        sourceMethodGroup.appendNode(code)
                    }

                case RegexType(value) =>
                    val pattern = value.r
                    val patternMatches = cpg.call
                      .where(_.name(pattern.regex))
                      .toSet

                    for (patternMatch <- patternMatches) {
                        // val storeNode = new CustomNode(patternMatch)
                        sourceMethodGroup.appendNode(patternMatch)
                    }
            }
        }

        sourceMethodGroup.dumpNodeInfo()
        sourceMethodGroup
    }

    override def close(): Unit = {
        cpg.close()
        println(s"Cleaned up resources for $inputDir")
    }

    // TODO: khoa
    override def getPathLine(
                              sourceMethodGroup: SourceMethodGroup,
                              sinkMethodGroup: SinkMethodGroup
                            ): PathLine = {
        for (sourceMethod <- sourceMethodGroup.getAllNodes) {
            for (sinkMethod <- sinkMethodGroup.getAllNodes) {
                if (sourceMethod != sinkMethod) {
                    val flows = reachableByFlow(sourceMethod,sinkMethod)

                    println("======== Start Flow ==========")

                    val nodesWithLine = flows.toList.map { id =>

                        val node = cpg.graph.node(id)
                        val code = node.propertyOption[String]("CODE")
                          .orElse(node.propertyOption[String]("code"))
                          .getOrElse("")

                        val lineNumber: Long = node.propertyOption[Any]("LINE_NUMBER")
                          .orElse(node.propertyOption[Any]("lineNumber"))
                          .map {
                              case l: Long => l
                              case i: Int => i.toLong
                              case s: Short => s.toLong
                              case _ => Long.MaxValue
                          }
                          .getOrElse(Long.MaxValue)

                        (lineNumber, code)
                    }.filter(_._2.nonEmpty) // keep only nodes with code

                    // Sort by line number
                    val sortedNodes = nodesWithLine.sortBy(_._1)

                    // Build code slice
                    val codeSlice = new StringBuilder
                    for ((line, code) <- sortedNodes) {
                        codeSlice.append(code + "\n")
                    }

                    if (codeSlice.nonEmpty) {
                        println(codeSlice.toString())
                    }

                    println("======== End Flow ==========")

                }
            }
        }


        PathLine()
    }

    // TODO: thang
    override def extractCode(pathLine: PathLine): String = ???

    /**
     * get a flow from source to sink if the flow is empty return node in between source and sink
     * use reverse breadth first search
     *
     * @param sourceMethod
     * @param sinkMethod
     * @return set of long numbers that is a node identity
     */
    def reachableByFlow(sourceMethod: StoredNode, sinkMethod: StoredNode): Set[Long] = {
        val slices = Set[Long]()
        val queue = Queue[Long]()
        val sourceId = sourceMethod.id()
        var reachable : Boolean = false
        queue.enqueue(sinkMethod.id())
        while (!queue.isEmpty) {
            val edgeId = queue.front
            slices.add(edgeId)
            queue.dequeue()
            val reachableEdges = edges.filter(edge => edge.dst.id == edgeId)
            reachableEdges.foreach(edge => {
                if (!slices.contains(edge.src.id )
                  && isLineNumberGreaterThan(sourceMethod,cpg.all.id(edge.src.id()).next())
                && isLineNumberGreaterThan(cpg.all.id(edge.src.id()).next(), sinkMethod) ){
                    queue.enqueue(edge.src.id)
                }
            })
        }
        slices.add(sourceMethod.id())
        slices
    }

    /**
     * get a line number of the node
     *
     * @param node
     * @return a line number of node in the code
     */
    def extractLineNumber(node : StoredNode) : Long = {
        node.propertyOption[Any]("LINE_NUMBER")
          .orElse(node.propertyOption[Any]("lineNumber"))
          .map {
              case l: Long => l
              case i: Int => i.toLong
              case s: Short => s.toLong
              case _ => Long.MaxValue
          }
          .getOrElse(Long.MaxValue)
    }

    /**
     * check a line number of current node whether is greater than source node
     *
     * @param sourceNode
     * @param currentNode
     * @return result of comparing source node's line number and current node's line number
     */
    def isLineNumberGreaterThan(sourceNode : StoredNode, currentNode : StoredNode): Boolean = {
       extractLineNumber(sourceNode) <= extractLineNumber(currentNode)
    }
}