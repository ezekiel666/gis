package pl.zerocool.gis.tarjan;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Cezary Pawlowski
 */
public class AlgorithmTest {
    @Test
    public void testTarjanSCC() throws Exception {
        String path = getClass().getResource("/s100.gv").getPath();
        DirectedGraph<Integer, DefaultEdge> graph = Main.read(path);

        List<Set<Integer>> actual = Algorithm.tarjanSCC(graph, false);
        //Main.printSCC(scc);

        StrongConnectivityInspector sci = new StrongConnectivityInspector(graph);
        List expected = sci.stronglyConnectedSubgraphs();

        assertEquals(expected.size(), actual.size());
    }
}