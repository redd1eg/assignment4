package graph.scc;


import java.util.*;


public class SCCResult {
    private final List<List<Integer>> components;


    public SCCResult(List<List<Integer>> components) {
        this.components = components;
    }


    public List<List<Integer>> getComponents() { return components; }


    public int getCount() { return components.size(); }


    public Map<Integer,Integer> componentOfNode(int n) {
        Map<Integer,Integer> map = new HashMap<>();
        for (int i = 0; i < components.size(); i++) {
            for (int v : components.get(i)) map.put(v, i);
        }
        return map;
    }
}