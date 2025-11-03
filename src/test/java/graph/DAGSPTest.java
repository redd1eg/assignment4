package graph.dagsp;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class DAGSPTest {
    @Test
    void testShortestPath() {
        int n = 4;
        List<List<Integer>> adj = List.of(
                List.of(1),
                List.of(2),
                List.of(3),
                List.of()
        );
        long[] duration = {1, 2, 3, 4};
        DAGShortestPaths sp = new DAGShortestPaths(n, adj, duration);
        List<Integer> topo = List.of(0, 1, 2, 3);
        DAGPathResult result = sp.shortestFrom(0, topo);
        assertEquals(10, result.getDistances()[3]); // 1+2+3+4
    }
}
