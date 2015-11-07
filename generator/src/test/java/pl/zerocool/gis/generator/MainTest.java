package pl.zerocool.gis.generator;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DigraphGenerator;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Cezary Pawlowski
 */
public class MainTest {
    @Test
    public void testConvert() throws Exception {
        Digraph graph = DigraphGenerator.simple(10, 0.25);
        DirectedGraph jgraph = Main.convert(graph);
        assertEquals(graph.V(), jgraph.vertexSet().size());
        assertEquals(graph.E(), jgraph.edgeSet().size());

        //print(graph);
        //print(jgraph);
    }

    private void print(Digraph graph) {
        System.out.println("graph:");
        for(Integer v1 = 0; v1 < graph.V(); ++v1) {
            for(Integer v2 : graph.adj(v1)) {
                System.out.println(v1 + " -> " + v2);
            }
        }
    }

    private void print(DirectedGraph graph) {
        System.out.println("graph:");
        for(DefaultEdge edge : (Set<DefaultEdge>) graph.edgeSet()) {
            System.out.println(graph.getEdgeSource(edge) + " -> " + graph.getEdgeTarget(edge));
        }
    }
}