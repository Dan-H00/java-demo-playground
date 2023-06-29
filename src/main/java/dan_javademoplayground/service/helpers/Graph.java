package dan_javademoplayground.service.helpers;

import dan_javademoplayground.persistence.model.ExchangePool;

import java.util.*;

public class Graph {
    public Map<Vertex, List<Vertex>> adjVertices = new HashMap<>();

    public Graph() {
    }

    public void addVertex(ExchangePool exchangePool) {
        adjVertices.putIfAbsent(new Vertex(exchangePool), new ArrayList<Vertex>());
    }

    public void addEdge(ExchangePool exchangePool1, ExchangePool exchangePool2) {
        Vertex v1 = new Vertex(exchangePool1);
        Vertex v2 = new Vertex(exchangePool2);

        adjVertices.get(v1).add(v2);
        adjVertices.get(v2).add(v1);
    }

    public List<Vertex> shortestPath(Vertex source, Vertex destination) {
        Map<Vertex, Double> distance = new HashMap<>();
        Map<Vertex, Vertex> previous = new HashMap<>();
        PriorityQueue<Vertex> queue = new PriorityQueue<>(Comparator.comparingDouble(distance::get));

        for (Vertex vertex : adjVertices.keySet()) {
            distance.put(vertex, Double.POSITIVE_INFINITY);
            previous.put(vertex, null);
        }

        distance.put(source, 0.0);
        queue.add(source);

        while (!queue.isEmpty()) {
            Vertex current = queue.poll();

            if (current.equals(destination)) {
                break;
            }

            List<Vertex> neighbors = adjVertices.get(current);
            for (Vertex neighbor : neighbors) {
                double weight = 1.0;
                double totalDistance = distance.get(current) + weight;

                if (totalDistance < distance.get(neighbor)) {
                    distance.put(neighbor, totalDistance);
                    previous.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        List<Vertex> shortestPath = new ArrayList<>();
        Vertex current = destination;
        while (current != null) {
            shortestPath.add(current);
            current = previous.get(current);
        }
        Collections.reverse(shortestPath);

        return shortestPath;
    }

    public class Vertex {
        public ExchangePool exchangePool;

        public Vertex(ExchangePool exchangePool) {
            this.exchangePool = exchangePool;
        }

        public Vertex() {
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((exchangePool == null) ? 0 : exchangePool.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Vertex other = (Vertex) obj;
            if (!getOuterType().equals(other.getOuterType())) return false;
            if (exchangePool == null) {
                if (other.exchangePool != null) return false;
            } else if (!exchangePool.equals(other.exchangePool)) return false;
            return true;
        }

        private Graph getOuterType() {
            return Graph.this;
        }
    }
}
