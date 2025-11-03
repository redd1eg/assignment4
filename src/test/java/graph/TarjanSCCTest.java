package graph;

import graph.scc.SCCResult;
import graph.scc.TarjanSCC;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TarjanSCCTest {
    @Test
    void testSimpleSCC() {
        List<int[]> edges = List.of(
                new int[]{0, 1},
                new int[]{1, 2},
                new int[]{2, 0},
                new int[]{2, 3}
        );
        TarjanSCC tarjan = new TarjanSCC(4, edges);
        SCCResult result = tarjan.run();
        assertEquals(2, result.getCount());
    }
}
