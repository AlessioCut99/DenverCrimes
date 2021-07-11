package it.polito.tdp.crimes.model;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private SimpleWeightedGraph<String, DefaultWeightedEdge> grafo;
	private EventsDao dao;
	
	public Model() {
		dao = new EventsDao();
	}
	
	public void creaGrafo(String categoria, int mese) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiunta vertici
		Graphs.addAllVertices(grafo, dao.getVertici(categoria, mese));
		
		//aggiunta archi
		for(Adiacenza a : dao.getAdiacenze(categoria, mese)) {
			if(this.grafo.getEdge(a.getV1(), a.getV2()) == null) {
				Graphs.addEdgeWithVertices(grafo, a.getV1(), a.getV2(), a.getPeso());
				
			}
		}
		
		System.out.println("# Vertici: " + this.grafo.vertexSet().size());
		System.out.println("# Archi: " + this.grafo.edgeSet().size());
	}
	
	public List<Adiacenza> getArchi(){
		//calcolo il peso medio degli archi presenti nel grafo
		double pesoMedio = 0.0;
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			pesoMedio += this.grafo.getEdgeWeight(e);
		}
		pesoMedio = pesoMedio/this.grafo.edgeSet().size();
		
		//"filtro" gli archi, tenendo solo quelli che hanno peso maggiore del peso medio
		List<Adiacenza> result = new LinkedList<>();
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e) > pesoMedio)
				result.add(new Adiacenza(this.grafo.getEdgeSource(e), 
						this.grafo.getEdgeTarget(e),this.grafo.getEdgeWeight(e)));
		}
		
		return result;
	}
}
