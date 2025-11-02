package graph;

import com.google.gson.*;
import java.io.FileReader;
import java.util.List;

public class GraphLoader {

    public static Graph loadGraph(String filename) throws Exception {
        Gson gson = new Gson();
        JsonObject obj = gson.fromJson(new FileReader(filename), JsonObject.class);

        int nodes = obj.get("nodes").getAsInt();
        Graph graph = new Graph(nodes);

        JsonArray edges = obj.getAsJsonArray("edges");
        for (JsonElement e : edges) {
            JsonObject edge = e.getAsJsonObject();
            int from = edge.get("from").getAsInt();
            int to = edge.get("to").getAsInt();
            double weight = edge.has("weight") ? edge.get("weight").getAsDouble() : 1.0;
            graph.addEdge(from, to, weight);
        }

        return graph;
    }
}
