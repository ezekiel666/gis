package pl.zerocool.gis.tarjan;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

/**
 * Algorithm.
 *
 * @author Cezary Pawlowski
 */
public class Algorithm {
    /**
     * Computes strongly connected components with Tarjan's algoritm.
     *
     * @param graph graph
     * @return strongly connected components
     */
    public static List<Set<Integer>> tarjanSCC(DirectedGraph<Integer, DefaultEdge> graph, boolean time) {
        TarjanSCC impl = new TarjanSCC(graph);

        long start = System.nanoTime();
        List<Set<Integer>> scc = impl.compute();
        long stop = System.nanoTime();

        if(time) {
            long duration = (stop - start) / 1000;
            System.out.println("Elapsed time [ms]: " + duration);
            System.out.println();
        }

        return scc;
    }

    /**
     * Tarjan's strongly connected components algorithm implementation.
     *
     * @author Cezary Pawlowski
     */
    private static class TarjanSCC {
        private final DirectedGraph<Integer, DefaultEdge> graph;

        private Integer index = 0;
        private final Map<Integer, State> stateMap = new HashMap<>();
        private final Stack<Integer> stack = new Stack<>();

        private final List<Set<Integer>> scc = new ArrayList<>();

        public TarjanSCC(DirectedGraph<Integer, DefaultEdge> graph) {
            this.graph = graph;
            for(Integer v : graph.vertexSet()) {
                stateMap.put(v, new State());
            }
        }

        public List<Set<Integer>> compute() {
            for(Integer v : stateMap.keySet()) {
                State vs = stateMap.get(v);

                if(vs.isIndexUndefined()) {
                    strongConnect(v, vs);
                }
            }
            return scc;
        }

        private void strongConnect(Integer v, State vs) {
            vs.setIndex(index);
            vs.setLowlink(index);
            ++index;
            stack.push(v);
            vs.setOnStack(true);

            for(DefaultEdge outEdge : graph.outgoingEdgesOf(v)) {
                Integer w = graph.getEdgeTarget(outEdge);
                State ws = stateMap.get(w);

                if(ws.isIndexUndefined()) {
                    strongConnect(w, ws);
                    vs.setLowlink(Math.min(vs.getLowlink(), ws.getLowlink()));
                } else if(ws.isOnStack()) {
                    vs.setLowlink(Math.min(vs.getLowlink(), ws.getIndex()));
                }
            }

            if(vs.getLowlink().equals(vs.getIndex())) {
                Set<Integer> set = new TreeSet<>();

                Integer w;
                do {
                    w = stack.pop();
                    State ws = stateMap.get(w);

                    ws.setOnStack(false);
                    set.add(w);
                } while(!w.equals(v));

                scc.add(set);
            }
        }

        private static class State {
            private Integer index;
            private Integer lowlink;
            private boolean onStack = false;

            public boolean isIndexUndefined() {
                return index == null;
            }

            public Integer getIndex() {
                return index;
            }

            public void setIndex(Integer index) {
                this.index = index;
            }

            public Integer getLowlink() {
                return lowlink;
            }

            public void setLowlink(Integer lowlink) {
                this.lowlink = lowlink;
            }

            public boolean isOnStack() {
                return onStack;
            }

            public void setOnStack(boolean onStack) {
                this.onStack = onStack;
            }
        }
    }
}
