package data;

import java.io.Serializable;

/**
 * La classe modella un generico attributo discreto o continuo.
 * 
 * @author Alessio Lucarella.
 *
 */
public abstract class Attribute implements Serializable {

	protected String name;
	protected int index;

	/**
	 * Inizializza i valori dei membri name ed index.
	 * 
	 * @param name
	 *            Nome simbolico dell'attributo.
	 * @param index
	 *            Identificativo numerico dell'attributo.
	 */
	Attribute(String name, int index) {
		this.name = name;
		this.index = index;
	}

	/**
	 * Restituisce il valore del membro main.
	 * 
	 * @return name Nome simbolico dell'attributo.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Restituisve l'identificativo numerico dell'attributo.
	 * 
	 * @return index Identificativo numerico dell'attributo.
	 */
	public int getIndex() {
		return index;
	}

}
