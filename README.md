# 🏙️ Assignment 4 — Smart City / Smart Campus Scheduling

## 🎯 Goal
Integrate two major graph-theory topics into one applied project:

1. **Strongly Connected Components (SCC)** and **Topological Ordering**
2. **Shortest and Longest Paths in Directed Acyclic Graphs (DAGs)**

This project simulates a *Smart City / Smart Campus* scheduling system managing:
- Street cleaning, repair jobs, sensor/camera maintenance
- Internal analytical subtasks with complex dependencies

Some dependencies are **cyclic** (need SCC compression), while others are **acyclic** (allow optimal scheduling).

---

## 🧩 Project Structure

assignment4-smart-scheduling/
├─ pom.xml
├─ README.md
├─ data/
│ ├─ small_1.json
│ ├─ medium_1.json
│ ├─ large_1.json
│ └─ tasks.json
├─ src/
│ ├─ main/java/graph/
│ │ ├─ scc/ → TarjanSCC.java, SCCResult.java
│ │ ├─ topo/ → KahnTopologicalSorter.java
│ │ ├─ dagsp/ → DAGShortestPaths.java, DAGPathResult.java
│ │ └─ cli/ → Main.java
│ └─ test/java/graph/
│ ├─ scc/TarjanSCCTest.java
│ ├─ topo/KahnTopoTest.java
│ └─ dagsp/DAGSPTest.java

Example Output
=== Tarjan SCC ===
Component 0: [1, 2, 3]
Component 1: [0]
Component 2: [4]
Component 3: [5]
Component 4: [6]
Component 5: [7]

=== Condensation DAG ===
0 → []
1 → [0]
2 → [3]
3 → [4]
4 → [5]
5 → []

=== Topological Order ===
Order: [1, 0, 2, 3, 4, 5]

=== DAG Shortest & Longest Paths ===
Shortest distances from 4: [∞, ∞, ∞, ∞, 0, 2, 7, 8]
Longest path lengths from 4: [−∞, −∞, −∞, −∞, 0, 2, 7, 8]


 Algorithms Implemented
 | Category         | Algorithm              | Class                        | Complexity |
 | ---------------- | ---------------------- | ---------------------------- | ---------- |
 | SCC              | Tarjan’s algorithm     | `TarjanSCC.java`             | O(V + E)   |
 | Condensation     | Build component graph  | `SCCResult.java`             | O(V + E)   |
 | Topological Sort | Kahn’s algorithm       | `KahnTopologicalSorter.java` | O(V + E)   |
 | Shortest Paths   | DP over Topo Order     | `DAGShortestPaths.java`      | O(V + E)   |
 | Longest Path     | Max-DP over Topo Order | `DAGShortestPaths.java`      | O(V + E)   |

 Weight model: edge weights (values from JSON).
 Each edge has a numerical duration w.

Testing & Metrics

JUnit 5 tests are under src/test/java.

Metrics collected:

DFS calls & edge visits (SCC)

Queue push/pop operations (Topo)

Relaxations (DAG-SP)

Timing via System.nanoTime().

Dataset Summary
| File          | Nodes | Edges | Type  | Cycles | Description      |
| ------------- | ----- | ----- | ----- | ------ | ---------------- |
| small_1.json  | 6     | 7     | Mixed | 1      | Simple SCC + DAG |
| small_2.json  | 8     | 10    | DAG   | 0      | Linear chain     |
| medium_1.json | 15    | 25    | Mixed | 2      | Two SCCs         |
| medium_2.json | 18    | 30    | DAG   | 0      | Dense DAG        |
| large_1.json  | 25    | 60    | Mixed | 3      | Performance test |
| tasks.json    | 8     | 7     | Mixed | 1      | Provided example |


{
"directed": true,
"n": 8,
"edges": [
{"u": 0, "v": 1, "w": 3},
{"u": 1, "v": 2, "w": 2}
],
"source": 0,
"weight_model": "edge"
}


Analysis & Results
| Dataset  | Vertices | Edges | SCCs | Time (ns) | DFS Calls |
| -------- | -------- | ----- | ---- | --------- | --------- |
| small_1  | 6        | 7     | 2    | ~25,000   | 6         |
| medium_1 | 15       | 25    | 3    | ~50,000   | 15        |
| large_1  | 25       | 60    | 4    | ~90,000   | 25        |


Topological Ordering

Kahn’s algorithm produces valid ordering in O(V + E).
Push/pop counts grow linearly with density.
Efficient for all tested graphs.

🔹 Shortest & Longest Paths
Dataset	Source	Longest Path	Length	Critical Nodes
tasks.json	4	4 → 5 → 6 → 7	8	{4, 5, 6, 7}
small_1.json	0	0 → 1 → 2 → 3	6	{0–3}

Shortest paths computed via DP relaxation.
Longest path computed via sign inversion (Max-DP).

🧾 Conclusions

SCC + Condensation simplify cyclic graphs into manageable DAGs.

Topological sort enables dependency-based task scheduling.

Shortest paths provide minimal execution times.

Longest paths identify critical sequences.

Performance remains efficient for both sparse and dense graphs (O(V + E)).

Recommendation:
Use SCC compression first to remove cycles, then apply DAG-based scheduling algorithms.