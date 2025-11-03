Assignment 4 – Smart City / Smart Campus Scheduling
🎯 Goal

Integrate two major graph-theory topics in one applied case study:

Strongly Connected Components (SCC) and Topological Ordering

Shortest and Longest Paths in Directed Acyclic Graphs (DAGs)

The context is a Smart City / Smart Campus scheduling system that manages tasks such as:

Street cleaning, repair jobs, camera/sensor maintenance

Internal analytics subtasks with dependency relations

Some task dependencies are cyclic (require SCC compression), while others are acyclic (allow optimal planning).

🧩 Project Structure
assignment4-smart-scheduling/
├─ pom.xml
├─ README.md
├─ data/
│  ├─ small_1.json
│  ├─ medium_1.json
│  ├─ large_1.json
│  └─ tasks.json
├─ src/
│  ├─ main/java/graph/
│  │  ├─ scc/      → TarjanSCC.java, SCCResult.java
│  │  ├─ topo/     → KahnTopologicalSorter.java
│  │  ├─ dagsp/    → DAGShortestPaths.java, DAGPathResult.java
│  │  └─ cli/      → Main.java
│  └─ test/java/graph/
│     ├─ scc/TarjanSCCTest.java
│     ├─ topo/KahnTopoTest.java
│     └─ dagsp/DAGSPTest.java

⚙️ Build & Run
Compile and package (with dependencies)
mvn clean package

Run with any dataset
java -jar target/assignment4-smart-scheduling-1.0-SNAPSHOT-jar-with-dependencies.jar data/tasks.json


Example output:

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

📚 Algorithms Implemented
Category	Algorithm	Class	Complexity
SCC	Tarjan’s algorithm	TarjanSCC.java	O(V + E)
Condensation	Build component graph	SCCResult.java	O(V + E)
Topological Sort	Kahn’s algorithm	KahnTopologicalSorter.java	O(V + E)
Shortest Paths	Dynamic Programming over Topo Order	DAGShortestPaths.java	O(V + E)
Longest Path	Max-DP over Topo Order	DAGShortestPaths.java	O(V + E)

Weight model: edge weights (from JSON file).
Each edge has a numerical duration w.

🧪 Testing and Metrics

JUnit 5 tests are located under src/test/java.
Metrics collected for each algorithm include:

DFS calls and edge visits (SCC)

Queue push/pop operations (Topo)

Relaxation operations (DAG SP)

Timing measured via System.nanoTime().

Run all tests:

mvn test


Expected output:

Tests run: 3, Failures: 0, Errors: 0, Skipped: 0

📊 Datasets Summary (/data)
File	Nodes	Edges	Type	Cycles	Description
small_1.json	6	7	Mixed	1	Simple SCC + DAG structure
small_2.json	8	10	DAG	0	Linear task chain
medium_1.json	15	25	Mixed	2	Two independent SCCs
medium_2.json	18	30	DAG	0	Dense acyclic graph
large_1.json	25	60	Mixed	3	Stress test for performance
tasks.json	8	7	Mixed	1	Provided example file

All datasets follow the format:

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

🧠 Analysis & Results
1. SCC Performance
   Dataset	Vertices	Edges	SCCs	Time (ns)	DFS Calls
   small_1	6	7	2	~25 000	6
   medium_1	15	25	3	~50 000	15
   large_1	25	60	4	~90 000	25

Tarjan SCC scales linearly with graph size.

2. Topological Ordering

Kahn’s algorithm produces a valid ordering of the condensation DAG in O(V + E).
Push/pop counts are proportional to edges.
For dense graphs, Kahn requires more queue operations.

3. Shortest and Longest Paths in DAG
   Dataset	Source	Longest Path	Length	Critical Nodes
   tasks.json	4	4 → 5 → 6 → 7	8	{4, 5, 6, 7}
   small_1.json	0	0 → 1 → 2 → 3	6	{0–3}

Shortest paths computed via standard DP relaxation over topological order.
Longest path found using sign inversion / max-DP.

🧾 Conclusions

SCC + Condensation effectively reduce cyclic dependencies into manageable DAGs.

Topological ordering enables efficient task scheduling and dependency resolution.

DAG shortest paths identify optimal execution times;
longest paths highlight critical chains of dependent tasks.

For dense graphs, SCC and Topo overhead remains manageable (O(V + E)).

Practical recommendation:
Use SCC compression first for cyclic task graphs, then apply DAG-based dynamic programming for optimal scheduling.