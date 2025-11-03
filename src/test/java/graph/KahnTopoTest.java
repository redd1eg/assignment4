package graph;

import graph.topo.KahnTopologicalSorter;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class KahnTopoTest {
    @Test
    void testTopoSort() {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < 4; i++) adj.add(new ArrayList<>());
        adj.get(0).add(1);
        adj.get(1).add(2);
        adj.get(2).add(3);
        List<Integer> order = KahnTopologicalSorter.topoSort(4, adj);
        assertEquals(List.of(0, 1, 2, 3), order);
    }
}
