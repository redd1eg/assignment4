package graph.scc;

import graph.Graph;
import java.util.*;

public class CondensationGraph {

    public static Graph build(List<List<Integer>> sccs, Graph g) {
        Map<Integer, Integer> nodeToComponent = new HashMap<>();
        for (int i = 0; i < sccs.size(); i++) {
            for (int v : sccs.get(i)) {
                nodeToComponent.put(v, i);
            }
        }

        Graph dag = new Graph(sccs.size());
        for (int u = 0; u < g.size(); u++) {
            for (int v : g.getNeighbors(u)) {
                int compU = nodeToComponent.get(u);
                int compV = nodeToComponent.get(v);
                if (compU != compV) {
                    dag.addEdge(compU, compV, 1.0); // вес можно выбрать 1
                }
            }
        }

        return dag;
    }
}
