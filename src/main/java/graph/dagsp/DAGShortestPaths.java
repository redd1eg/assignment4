package graph.dagsp;


import java.util.*;


/**
 * Shortest and longest paths in DAG.
 * Weight model: **node durations** (each node has duration). Edges have zero cost.
 * Implementation notes: convert to edge weights by placing node duration on outgoing edges or
 * treat distances as sum of durations along path including start node.
 */
public class DAGShortestPaths {
    private final int n;
    private final List<List<Integer>> adj;
    private final long[] duration; // node durations


    public DAGShortestPaths(int n, List<List<Integer>> adj, long[] duration) {
        this.n = n; this.adj = adj; this.duration = duration;
    }


    public DAGPathResult shortestFrom(int src, List<Integer> topo) {
        final long INF = Long.MAX_VALUE / 4;
        long[] dist = new long[n]; Arrays.fill(dist, INF);
        int[] parent = new int[n]; Arrays.fill(parent, -1);
        dist[src] = duration[src];
        for (int u : topo) {
            if (dist[u] == INF) continue;
            for (int v : adj.get(u)) {
                long nd = dist[u] + duration[v];
                if (nd < dist[v]) { dist[v] = nd; parent[v] = u; }
            }
        }
        return new DAGPathResult(dist, parent, src);
    }


    public DAGPathResult longest(List<Integer> topo) {
        final long NEG = Long.MIN_VALUE / 4;
        long[] dist = new long[n]; Arrays.fill(dist, NEG);
        int[] parent = new int[n]; Arrays.fill(parent, -1);
// initialize: any node could be a start; set dist[u] = duration[u]
        for (int i = 0; i < n; i++) dist[i] = duration[i];
        for (int u : topo) {
            if (dist[u] == NEG) continue;
            for (int v : adj.get(u)) {
                long nd = dist[u] + duration[v];
                if (nd > dist[v]) { dist[v] = nd; parent[v] = u; }
            }
        }
// find max
        long max = NEG; int at = -1;
        for (int i = 0; i < n; i++) if (dist[i] > max) { max = dist[i]; at = i; }
        return new DAGPathResult(dist, parent, at);
    }
}