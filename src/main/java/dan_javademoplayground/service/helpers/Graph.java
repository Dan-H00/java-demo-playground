package dan_javademoplayground.service.helpers;

import dan_javademoplayground.persistence.model.ExchangePool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Graph {
    public Map<Vertex, List<Vertex>> adjVertices;

    public Graph(Map<Vertex, List<Vertex>> adjVertices) {
        this.adjVertices = adjVertices;
    }

    public Graph() {
    }

    public List<Vertex> getAdjVertices(ExchangePool exchangePool) {
        return adjVertices.get(new Vertex(exchangePool));
    }

    public void addVertex(ExchangePool exchangePool) {
        adjVertices.putIfAbsent(new Vertex(exchangePool), new ArrayList<>());
    }

    public void addEdge(ExchangePool exchangePool1, ExchangePool exchangePool2) {
        Vertex v1 = new Vertex(exchangePool1);
        Vertex v2 = new Vertex(exchangePool2);

        adjVertices.get(v1).add(v2);
        adjVertices.get(v2).add(v1);
    }

    public class Vertex {
        public ExchangePool exchangePool;
        public Vertex(ExchangePool exchangePool) {
            this.exchangePool = exchangePool;
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
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Vertex other = (Vertex) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (exchangePool == null) {
                if (other.exchangePool != null)
                    return false;
            } else if (!exchangePool.equals(other.exchangePool))
                return false;
            return true;
        }

        private Graph getOuterType() {
            return Graph.this;
        }
    }
}
