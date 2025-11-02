package graph.topo;


import java.util.*;


public class KahnTopologicalSorter {
    public static List<Integer> topoSort(int n, List<List<Integer>> adj) {
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++) for (int v : adj.get(u)) indeg[v]++;
        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) q.add(i);
        List<Integer> order = new ArrayList<>();
        int pops = 0, pushes = 0;
        while (!q.isEmpty()) {
            int u = q.remove(); pops++;
            order.add(u);
            for (int v : adj.get(u)) {
                indeg[v]--;
                if (indeg[v] == 0) { q.add(v); pushes++; }
            }
        }
        if (order.size() != n) throw new IllegalStateException("Graph has cycle (not a DAG)");
        return order;
    }
}