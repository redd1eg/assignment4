package graph.scc;


import java.util.*;


public class TarjanSCC {
    private final int n;
    private final List<int[]> edges;
    private List<List<Integer>> adj;


    public TarjanSCC(int n, List<int[]> edges) {
        this.n = n;
        this.edges = edges;
        buildAdj();
    }


    private void buildAdj() {
        adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) adj.get(e[0]).add(e[1]);
    }


    public SCCResult run() {
        int[] disc = new int[n]; Arrays.fill(disc, -1);
        int[] low = new int[n];
        boolean[] inStack = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        List<List<Integer>> comps = new ArrayList<>();
        int time = 0;


        for (int i = 0; i < n; i++) {
            if (disc[i] == -1) time = dfs(i, time, disc, low, stack, inStack, comps);
        }
        return new SCCResult(comps);
    }


    private int dfs(int u, int time, int[] disc, int[] low, Deque<Integer> stack, boolean[] inStack, List<List<Integer>> comps) {
        disc[u] = low[u] = time++;
        stack.push(u); inStack[u] = true;
        for (int v : adj.get(u)) {
            if (disc[v] == -1) {
                time = dfs(v, time, disc, low, stack, inStack, comps);
                low[u] = Math.min(low[u], low[v]);
            } else if (inStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
        if (low[u] == disc[u]) {
            List<Integer> comp = new ArrayList<>();
            while (true) {
                int w = stack.pop(); inStack[w] = false;
                comp.add(w);
                if (w == u) break;
            }
            comps.add(comp);
        }
        return time;
    }
}