package CodeSlice.Path

import io.shiftleft.codepropertygraph.generated.nodes.*

import scala.collection.mutable.{Map, Set}
import flatgraph.Edge

class PathLine {
  var edges: Set[Edge] = Set()
  var potentialPaths: Map[StoredNode, StoredNode] = Map()

  def addEdge(edge: Edge): Unit = {
    edges.add(edge)
  }

  def addPotentialPaths(sourceNode: StoredNode, sinkNode: StoredNode): Unit = {
    potentialPaths.addOne(sourceNode, sinkNode)
  }

}
