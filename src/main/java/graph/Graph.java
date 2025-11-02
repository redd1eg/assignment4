package graph;

import java.util.*;

public class Graph {
    private final int n;  // количество вершин
    private final Map<Integer, List<Integer>> adj;
    private final Map<String, Double> weights; // ключ "u,v"

    public Graph(int n) {
        this.n = n;
        adj = new HashMap<>();
        weights = new HashMap<>();
        for (int i = 0; i < n; i++) {
            adj.put(i, new ArrayList<>());
        }
    }

    public int size() {
        return n;
    }

    public void addEdge(int from, int to, double weight) {
        adj.get(from).add(to);
        weights.put(from + "," + to, weight);
    }

    public List<Integer> getNeighbors(int u) {
        return adj.get(u);
    }

    public double getWeight(int u, int v) {
        return weights.getOrDefault(u + "," + v, Double.POSITIVE_INFINITY);
    }

    public Map<Integer, List<Integer>> getAdjacencyList() {
        return adj;
    }
}
