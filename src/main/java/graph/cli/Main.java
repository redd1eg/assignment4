package graph.cli;

import com.google.gson.*;
import graph.scc.*;
import graph.topo.*;
import graph.dagsp.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    static class Edge {
        int u, v;
        double w;
    }

    static class TaskGraph {
        boolean directed;
        int n;
        List<Edge> edges;
        int source;
        String weight_model;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: java -cp target/classes graph.cli.Main data/tasks.json");
            return;
        }

        String json = Files.readString(Path.of(args[0]));
        Gson gson = new Gson();
        TaskGraph tg = gson.fromJson(json, TaskGraph.class);

        // Build edge list
        List<int[]> edges = new ArrayList<>();
        for (Edge e : tg.edges) edges.add(new int[]{e.u, e.v});

        System.out.println("=== Tarjan SCC ===");
        TarjanSCC tarjan = new TarjanSCC(tg.n, edges);
        SCCResult sccResult = tarjan.run();
        List<List<Integer>> comps = sccResult.getComponents();
        for (int i = 0; i < comps.size(); i++) {
            System.out.println("Component " + i + ": " + comps.get(i));
        }

        // Build condensation DAG
        Map<Integer, Integer> compOf = sccResult.componentOfNode(tg.n);
        int compCount = comps.size();
        List<Set<Integer>> dagSet = new ArrayList<>();
        for (int i = 0; i < compCount; i++) dagSet.add(new HashSet<>());
        for (Edge e : tg.edges) {
            int cu = compOf.get(e.u), cv = compOf.get(e.v);
            if (cu != cv) dagSet.get(cu).add(cv);
        }
        List<List<Integer>> dag = new ArrayList<>();
        for (var s : dagSet) dag.add(new ArrayList<>(s));

        System.out.println("\n=== Condensation DAG ===");
        for (int i = 0; i < dag.size(); i++) {
            System.out.println(i + " -> " + dag.get(i));
        }

        // Topological sort
        System.out.println("\n=== Topological Order ===");
        List<Integer> topo = KahnTopologicalSorter.topoSort(dag.size(), dag);
        System.out.println("Order: " + topo);

        // Shortest & Longest paths (edge weights)
        System.out.println("\n=== DAG Shortest & Longest Paths ===");
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < tg.n; i++) adj.add(new ArrayList<>());
        for (Edge e : tg.edges) adj.get(e.u).add(new int[]{e.v, (int) e.w});

        List<Integer> topoAll = new ArrayList<>();
        boolean[] visited = new boolean[tg.n];
        for (int i = 0; i < tg.n; i++)
            if (!visited[i]) dfsTopo(i, adj, visited, topoAll);
        Collections.reverse(topoAll);

        double[] dist = new double[tg.n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[tg.source] = 0;
        for (int u : topoAll)
            for (int[] vw : adj.get(u))
                if (dist[u] + vw[1] < dist[vw[0]])
                    dist[vw[0]] = dist[u] + vw[1];
        System.out.println("Shortest distances from " + tg.source + ": " + Arrays.toString(dist));

        double[] distLong = new double[tg.n];
        Arrays.fill(distLong, Double.NEGATIVE_INFINITY);
        distLong[tg.source] = 0;
        for (int u : topoAll)
            for (int[] vw : adj.get(u))
                if (distLong[u] + vw[1] > distLong[vw[0]])
                    distLong[vw[0]] = distLong[u] + vw[1];
        System.out.println("Longest path lengths from " + tg.source + ": " + Arrays.toString(distLong));
    }

    private static void dfsTopo(int u, List<List<int[]>> adj, boolean[] vis, List<Integer> topo) {
        vis[u] = true;
        for (int[] vw : adj.get(u))
            if (!vis[vw[0]]) dfsTopo(vw[0], adj, vis, topo);
        topo.add(u);
    }
}
