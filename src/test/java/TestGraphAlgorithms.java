import graph.Graph;
import graph.scc.TarjanSCC;
import graph.topo.KahnTopologicalSorter;
import graph.dagsp.DAGShortestPaths;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestGraphAlgorithms {

    @Test
    public void testSmallGraph() {
        // Граф: 5 вершин, 1 цикл
        Graph g = new Graph(5);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1); // цикл
        g.addEdge(2, 3, 1);
        g.addEdge(3, 4, 1);

        // SCC
        TarjanSCC tarjan = new TarjanSCC();
        List<List<Integer>> sccs = tarjan.findSCC(g);
        assertEquals(3, sccs.size(), "Должны быть 3 компоненты");

        // Конденсация DAG
        Map<Integer, Integer> compOf = new HashMap<>();
        for (int i = 0; i < sccs.size(); i++)
            for (int v : sccs.get(i)) compOf.put(v, i);

        List<Set<Integer>> dagSet = new ArrayList<>();
        for (int i = 0; i < sccs.size(); i++) dagSet.add(new HashSet<>());
        for (int u = 0; u < g.size(); u++)
            for (int v : g.getNeighbors(u)) {
                int cu = compOf.get(u), cv = compOf.get(v);
                if (cu != cv) dagSet.get(cu).add(cv);
            }

        List<List<Integer>> dag = new ArrayList<>();
        for (Set<Integer> s : dagSet) dag.add(new ArrayList<>(s));

        // Topological sort
        List<Integer> topo = KahnTopologicalSorter.topoSort(dag.size(), dag);
        assertEquals(dag.size(), topo.size(), "Топологический порядок должен покрывать все компоненты");

        // DAG Shortest/Longest Paths (node durations = 1)
        long[] duration = new long[g.size()];
        Arrays.fill(duration, 1);
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < g.size(); i++) adj.add(new ArrayList<>(g.getNeighbors(i)));
        DAGShortestPaths dagSP = new DAGShortestPaths(g.size(), adj, duration);

        DAGShortestPaths.DAGPathResult sp = dagSP.shortestFrom(0, Arrays.asList(0,1,2,3,4));
        DAGShortestPaths.DAGPathResult lp = dagSP.longest(Arrays.asList(0,1,2,3,4));

        assertEquals(1, sp.dist[0], "Shortest distance to self = 1");
        assertTrue(lp.dist[4] >= 1, "Longest path to 4 >= 1");
    }
}
