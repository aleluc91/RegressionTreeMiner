package database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * Modella una transazione letta dalla base di dati.
 *
 */
public class Example implements Comparable<Example>, Iterable<Object> {

	private List<Object> example = new ArrayList<Object>();

	/**
	 * Aggiunge una transazione alla lista degli esempi.
	 * 
	 * @param o
	 *            Oggetto che rappresenta una transazione letta dal database.
	 */
	public void add(Object o) {
		example.add(o);
	}

	/**
	 * Restituisce la transazione indicizzata da i all'interno della lista
	 * example.
	 * 
	 * @param i
	 *            Indice della transazione da restituire.
	 * @return Oggetto indicizzato da index.
	 */
	public Object get(int i) {
		return example.get(i);
	}

	public int compareTo(Example ex) {
		int i = 0;
		for (Object o : ex.example) {
			if (!o.equals(this.example.get(i)))
				return ((Comparable) o).compareTo(example.get(i));
			i++;
		}
		return 0;
	}

	public String toString() {
		String str = "";
		for (Object o : example)
			str += o.toString() + " ";
		return str;
	}

	@Override
	public Iterator<Object> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}