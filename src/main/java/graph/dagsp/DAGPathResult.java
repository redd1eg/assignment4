package graph.dagsp;


import java.util.*;


public class DAGPathResult {
    private final long[] dist;
    private final int[] parent;
    private final int sourceOrTarget;
    public DAGPathResult(long[] dist, int[] parent, int sourceOrTarget) {
        this.dist = dist; this.parent = parent; this.sourceOrTarget = sourceOrTarget;
    }
    public long[] getDistances() { return dist; }
    public int[] getParents() { return parent; }
    public List<Integer> reconstructPathTo(int target) {
        if (dist[target] == Long.MAX_VALUE/4 || dist[target] == Long.MIN_VALUE/4) return Collections.emptyList();
        List<Integer> path = new ArrayList<>();
        for (int cur = target; cur != -1; cur = parent[cur]) path.add(cur);
        Collections.reverse(path); return path;
    }
}