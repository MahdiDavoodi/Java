package Mahdi.Davoodi;

/*
 * My Graph - created with Hash Map.
 * Thanks to Michael T. Goodrich!
 * You can find my past classes in These links{
 *      Position, PositionalList, LinkedPositionalList:
 *                           --->   https://github.com/MahdiDavoodi/Java/tree/master/Exercises/2021/February/Positional%20List
 *      Entry, Map, AbstractMap, AbstractHashMap, ProbeHashMap:
 *                           --->   https://github.com/MahdiDavoodi/Java/tree/master/Exercises/2021/March/Map
 * }
 * Note That my PositionalList doesn't have any iterator, so i put Michael T. Goodrich's PositionalList in this directory.
 * */
public class AdjacencyMapGraph<V, E> implements Graph<V, E> {

    private boolean isDirected;
    private PositionalList<Vertex<V>> vertices = new LinkedPositionalList<>();
    private PositionalList<Edge<E>> edges = new LinkedPositionalList<>();

    public AdjacencyMapGraph(boolean isDirected) {
        this.isDirected = isDirected;
    }

    @Override
    public int numberOfVertices() {
        return vertices.size();
    }

    @Override
    public int numberOfEdges() {
        return edges.size();
    }

    @Override
    public Iterable<Vertex<V>> vertices() {
        return vertices;
    }

    @Override
    public Iterable<Edge<E>> edges() {
        return edges;
    }

    @Override
    public int outDegree(Vertex<V> vertex) throws IllegalArgumentException {
        InnerVertex<V> v = validate(vertex);
        return v.getOutgoing().size();
    }

    @Override
    public int inDegree(Vertex<V> vertex) throws IllegalArgumentException {
        InnerVertex<V> v = validate(vertex);
        return v.getIncoming().size();
    }

    @Override
    public Iterable<Edge<E>> outgoingEdges(Vertex<V> vertex) throws IllegalArgumentException {
        InnerVertex<V> v = validate(vertex);
        return v.getOutgoing().values();
    }

    @Override
    public Iterable<Edge<E>> incomingEdges(Vertex<V> vertex) throws IllegalArgumentException {
        InnerVertex<V> v = validate(vertex);
        return v.getIncoming().values();
    }

    @Override
    public Edge<E> getEdge(Vertex<V> first, Vertex<V> second) throws IllegalArgumentException {
        InnerVertex<V> origin = validate(first);
        return origin.getOutgoing().get(second);
    }

    @Override
    public Vertex<V>[] endVertices(Edge<E> edge) throws IllegalArgumentException {
        InnerEdge<E> e = validate(edge);
        return e.getEndpoints();
    }

    @Override
    public Vertex<V> opposite(Vertex<V> vertex, Edge<E> edge) throws IllegalArgumentException {
        InnerEdge<E> e = validate(edge);
        Vertex<V>[] endpoints = e.getEndpoints();
        if (endpoints[0] == vertex)
            return endpoints[1];
        else if (endpoints[1] == vertex)
            return endpoints[0];
        else
            throw new IllegalArgumentException("Vertex is not belong to this edge!");
    }

    @Override
    public Vertex<V> insertVertex(V element) {
        InnerVertex<V> vertex = new InnerVertex<>(element, isDirected);
        vertex.setPosition(vertices.addLast(vertex));
        return vertex;
    }

    @Override
    public Edge<E> insertEdge(Vertex<V> first, Vertex<V> second, E element) throws IllegalArgumentException {
        if (getEdge(first, second) == null) {
            InnerEdge<E> edge = new InnerEdge<>(first, second, element);
            edge.setPosition(edges.addLast(edge));
            InnerVertex<V> origin = validate(first);
            InnerVertex<V> dest = validate(second);
            origin.getOutgoing().put(second, edge);
            dest.getIncoming().put(first, edge);
            return edge;
        } else
            throw new IllegalArgumentException("Edge from first to second already exists!");
    }

    @Override
    public void removeVertex(Vertex<V> vertex) throws IllegalArgumentException {
        InnerVertex<V> v = validate(vertex);
        for (Edge<E> edge : v.getOutgoing().values())
            removeEdge(edge);
        for (Edge<E> edge : v.getIncoming().values())
            removeEdge(edge);
        vertices.remove(v.getPosition());
        v.setPosition(null);
    }

    @Override
    public void removeEdge(Edge<E> edge) throws IllegalArgumentException {
        InnerEdge<E> e = validate(edge);
        InnerVertex<V>[] vertices = (InnerVertex<V>[]) e.getEndpoints();
        vertices[0].getOutgoing().remove(vertices[1]);
        vertices[1].getIncoming().remove(vertices[0]);
        edges.remove(e.getPosition());
        e.setPosition(null);
    }


    private class InnerVertex<V> implements Vertex<V> {
        private V element;
        private Position<Vertex<V>> position;
        private Map<Vertex<V>, Edge<E>> outgoing, incoming;

        public InnerVertex(V element, boolean graphIsDirected) {
            this.element = element;
            outgoing = new ProbeHashMap<>();
            if (graphIsDirected)
                incoming = new ProbeHashMap<>();
            else
                incoming = outgoing;
        }

        public Position<Vertex<V>> getPosition() {
            return position;
        }

        public void setPosition(Position<Vertex<V>> position) {
            this.position = position;
        }

        public Map<Vertex<V>, Edge<E>> getOutgoing() {
            return outgoing;
        }

        public Map<Vertex<V>, Edge<E>> getIncoming() {
            return incoming;
        }

        @Override
        public V getElement() {
            return element;
        }
    }

    private class InnerEdge<E> implements Edge<E> {
        private E element;
        private Position<Edge<E>> position;
        private InnerVertex<V>[] endpoints;

        public InnerEdge(Vertex<V> first, Vertex<V> second, E element) {
            InnerVertex<V> v1 = validate(first);
            InnerVertex<V> v2 = validate(second);
            this.element = element;
            endpoints = (InnerVertex<V>[]) new InnerVertex[]{v1, v2};
        }

        public Position<Edge<E>> getPosition() {
            return position;
        }

        public Vertex<V>[] getEndpoints() {
            return endpoints;
        }

        public void setPosition(Position<Edge<E>> position) {
            this.position = position;
        }

        @Override
        public E getElement() {
            return element;
        }
    }

    private InnerVertex<V> validate(Vertex<V> vertex) {
        if (!(vertex instanceof InnerVertex)) throw new IllegalArgumentException("Invalid vertex");
        return (InnerVertex<V>) vertex;
    }

    private InnerEdge<E> validate(Edge<E> edge) {
        if (!(edge instanceof InnerEdge)) throw new IllegalArgumentException("Invalid edge");
        return (InnerEdge<E>) edge;
    }
}