package data;

import java.io.Serializable;

/**
 * Estende la classe Attribute e rappresenta un attributo Continuo.
 * 
 * @author Alessio Lucarella
 *
 */
public class ContinuousAttribute extends Attribute implements Serializable {

	/**
	 * Invoca il costruttore della super-classe per avvalorare gli attributi
	 * name ed index.
	 * 
	 * @param name
	 *            Nome simbolico dell'attributo.
	 * @param index
	 *            Identificativo numerico dell'attributo.
	 */
	public ContinuousAttribute(String name, int index) {
		super(name, index);
	}

}
