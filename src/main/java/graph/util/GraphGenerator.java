package graph.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.util.*;

public class GraphGenerator {

    static class Edge {
        int u, v;
        double w;
        Edge(int u, int v, double w) { this.u = u; this.v = v; this.w = w; }
    }

    static class TaskGraph {
        boolean directed = true;
        int n;
        List<Edge> edges;
        int source = 0;
        String weight_model = "edge";
    }

    public static void generate(String filename, int n, double density, boolean allowCycles) throws Exception {
        TaskGraph g = new TaskGraph();
        g.n = n;
        g.edges = new ArrayList<>();
        Random rnd = new Random();

        int maxEdges = n * (n - 1);
        int numEdges = (int) (maxEdges * density);

        Set<String> used = new HashSet<>();
        while (g.edges.size() < numEdges) {
            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);
            if (u == v) continue; // без петель
            String key = u + "," + v;
            if (used.contains(key)) continue;
            if (!allowCycles && u > v) continue; // DAG: только "вниз"
            double w = 1 + rnd.nextInt(10); // вес 1–10
            g.edges.add(new Edge(u, v, w));
            used.add(key);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter fw = new FileWriter(filename)) {
            gson.toJson(g, fw);
        }
        System.out.println("Generated " + filename + " with " + n + " nodes and " + g.edges.size() + " edges");
    }

    public static void main(String[] args) throws Exception {
        // Small graphs: 6–10 nodes
        for (int i = 0; i < 3; i++) generate("data/small_" + i + ".json", 6 + i, 0.3, true);

        // Medium graphs: 10–20 nodes
        for (int i = 0; i < 3; i++) generate("data/medium_" + i + ".json", 10 + i * 3, 0.4, true);

        // Large graphs: 20–50 nodes
        for (int i = 0; i < 3; i++) generate("data/large_" + i + ".json", 20 + i * 10, 0.5, true);
    }
}
