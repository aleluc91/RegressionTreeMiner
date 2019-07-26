package data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Estende la classe Attribute e rappresenta un attributo discreto.
 * 
 * @author Alessio Lucarella
 *
 */
public class DiscreteAttribute extends Attribute implements Iterable<String>, Serializable {
	private Set<String> values = new TreeSet<>(); // order by asc

	/**
	 * Invoca il costruttore della superclasse per avvalorare gli attributi name
	 * ed index , e in seguito assegna al TreeSet values l'insieme di valori
	 * ricevuto in input.
	 * 
	 * @param name
	 *            Nome simbolico dell'attributo.
	 * @param index
	 *            Identidicativo numerico dell'attributo.
	 * @param values
	 *            Insieme dei valori discreti che l'attributo può assumere.
	 */
	public DiscreteAttribute(String name, int index, Set<String> values) {
		super(name, index);
		this.values = values;
	}

	/**
	 * Restituisce la cardinalità dell'insieme values.
	 * 
	 * @return Numero di valori discreti dell'attributo.
	 */
	public int getNumberOfDistinctValues() {
		return values.size();
	}

	/**
	 * Restituisce l'iteratore per l'insieme di valori.
	 */
	@Override
	public Iterator<String> iterator() {
		return values.iterator();
	}

}
