package tree;

import java.io.Serializable;

import data.Data;
import utility.Statistics;

/**
 * Modella l'entità nodo fogliare.
 * 
 * @author Alessio Lucarella
 *
 */
public class LeafNode extends Node implements Serializable {

	private Double predictedClassValue;

	/**
	 * Istanzia un oggetto invocando il costruttore della superclasse e avvalora
	 * l'attributo predictedClassValue , effettuando la media dei valori
	 * dell'attributo di classe che ricadono nella partizione. Fa utilizzo della
	 * classe Statistics per il calcolo della media.
	 * 
	 * @param trainingSet
	 *            Training set complessivo.
	 * @param beginExampleIndex
	 *            Indice iniziale del sotto-insieme di training.
	 * @param endExampleIndex
	 *            Indice finale del sotto-insieme di training.
	 */
	LeafNode(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
		super(trainingSet, beginExampleIndex, endExampleIndex);
		predictedClassValue = Statistics.getMean(trainingSet, beginExampleIndex, endExampleIndex);
	}

	/**
	 * Restituisce il valore del membro predictedClassValue.
	 * 
	 * @return Media dei valori dell'attributo di classe che ricadono nella
	 *         partizione.
	 */
	public Double getPredictedClassValue() {
		return predictedClassValue;
	}

	/**
	 * {@inheritDoc} Restituisce il numero di split generati dal nodo foglia ,
	 * ovvero 0.
	 */
	@Override
	int getNumberOfChildren() {
		return 0;
	}

	/**
	 * Invoca il metodo della superclasse assegnando anche il valore di classe
	 * della foglia.
	 */
	@Override
	public String toString() {
		return "LEAF class=" + predictedClassValue + " " + super.toString();
	}

}
