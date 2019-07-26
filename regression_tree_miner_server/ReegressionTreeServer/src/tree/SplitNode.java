package tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import data.Attribute;
import data.Data;

public abstract class SplitNode extends Node implements Comparable<SplitNode>, Serializable {
	// Classe che colelzione informazioni descrittive dello split
	class SplitInfo implements Serializable {
		Object splitValue;
		int beginIndex;
		int endIndex;
		int numberChild;
		String comparator = "=";

		SplitInfo(Object splitValue, int beginIndex, int endIndex, int numberChild) {
			this.splitValue = splitValue;
			this.beginIndex = beginIndex;
			this.endIndex = endIndex;
			this.numberChild = numberChild;
		}

		SplitInfo(Object splitValue, int beginIndex, int endIndex, int numberChild, String comparator) {
			this.splitValue = splitValue;
			this.beginIndex = beginIndex;
			this.endIndex = endIndex;
			this.numberChild = numberChild;
			this.comparator = comparator;
		}

		int getBeginindex() {
			return beginIndex;
		}

		int getEndIndex() {
			return endIndex;
		}

		Object getSplitValue() {
			return splitValue;
		}

		public String toString() {
			return "child " + numberChild + " split value" + comparator + splitValue + "[Examples:" + beginIndex + "-"
					+ endIndex + "]";
		}

		String getComparator() {
			return comparator;
		}

	}

	private Attribute attribute;

	protected List<SplitInfo> mapSplit = new ArrayList<SplitInfo>();

	protected double splitVariance;

	/**
	 * Metodo abstract per generare le informazioni necessarie per ciascuno
	 * degli split candidati.
	 * 
	 * @param trainingSet
	 *            Training set complessivo.
	 * @param beginExampelIndex
	 *            Indice iniziale del sotto-insieme di training.
	 * @param endExampleIndex
	 *            Indice finale del sotto-insieme di training.
	 * @param attribute
	 *            Attributo sul quale si definisce lo split.
	 */
	abstract void setSplitInfo(Data trainingSet, int beginExampelIndex, int endExampleIndex, Attribute attribute);

	// TODO controllare il java doc
	/**
	 * Metodo abstract per modellare la condizione di test.
	 * 
	 * @param value
	 *            Valore dell'attributo che si vuole testare rispetto a tutti
	 *            gli split.
	 * @return
	 */
	abstract int testCondition(Object value);

	/**
	 * Invoca il costruttore della super-classe , ordina i valori dell'attributo
	 * di input per gli esempi contenuti nel sotto-insieme di training compreso
	 * tra beginExampleIndex e endExampleIndex , e sfrutta questo ordinamento
	 * per determinare i possibili split e popolare la lista mapSplit. Computa
	 * la varianza per l'attributo usato nello split sulla base del
	 * partizionamento indotto dallo split.
	 * 
	 * @param trainingSet
	 *            Training set complessivo.
	 * @param beginExampleIndex
	 *            Indice iniziale del sotto-insieme di training.
	 * @param endExampleIndex
	 *            Indice finale del sotto-insieme di training.
	 * @param attribute
	 *            Attributo indipendente sul quale definire lo split.
	 */
	SplitNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute) {
		super(trainingSet, beginExampleIndex, endExampleIndex);
		this.attribute = attribute;
		trainingSet.sort(attribute, beginExampleIndex, endExampleIndex); // order
																			// by
																			// attribute
		setSplitInfo(trainingSet, beginExampleIndex, endExampleIndex, attribute);

		// compute variance
		splitVariance = 0;
		for (int i = 0; i < mapSplit.size(); i++) {
			double localVariance = new LeafNode(trainingSet, mapSplit.get(i).getBeginindex(),
					mapSplit.get(i).getEndIndex()).getVariance();
			splitVariance += (localVariance);
		}
	}

	/**
	 * Restituisce l'oggetto per l'attributo usato per lo split.
	 * 
	 * @return Attributo usato per lo split.
	 * @return 0 splitVariance uguali , -1 splitVariance minore , 1
	 *         splitVariance minore.
	 */
	Attribute getAttribute() {
		return attribute;
	}

	/**
	 * Restituisce l'information gain per lo split corrente.
	 */
	double getVariance() {
		return splitVariance;
	}

	/**
	 * {@inheritDoc} Restistuisce il numero dei rami generati dal nodo corrente.
	 */
	public int getNumberOfChildren() {
		return mapSplit.size();
	}

	/**
	 * Restituisce le informazioni per il ramo all'interno della lista mapSplit
	 * indicizzato da child.
	 * 
	 * @param child
	 *            Indice per la lista mapSplit.
	 * @return Informazioni riguardanti il ramo indicizzato da child.
	 */
	SplitInfo getSplitInfo(int child) {
		return mapSplit.get(child);
	}

	/**
	 * Concatena le informazioni di ciascun test(attributo , operatore e valore)
	 * in un oggetto String. La stringa restituita è necessaria per la
	 * predizione di nuovi esempi.
	 * 
	 * @return Stringa contenente le informazioni di ciascun test.
	 */
	public String formulateQuery() {
		String query = "";
		for (int i = 0; i < mapSplit.size(); i++)
			query += (i + ":" + attribute.getName() + mapSplit.get(i).getComparator() + mapSplit.get(i).getSplitValue())
					+ "\n";
		return query;
	}

	// TODO JavaDoc per il metodo compareTo
	/**
	 * Confronta i valori di splitVariance dei due nodi e restituisce l'esito.
	 * 
	 * @param o
	 *            Nodo di split da confrontare con nodo DiscreteNode.
	 * 
	 */
	@Override
	public int compareTo(SplitNode o) {
		if (o.getVariance() == this.splitVariance)
			return 0;
		else if (o.getVariance() < this.splitVariance)
			return -1;
		else
			return 1;
	}

	public String toString() {
		String v = "SPLIT : attribute=" + attribute.getName() + " " + super.toString() + " Split Variance: "
				+ getVariance() + "\n";

		for (int i = 0; i < mapSplit.size(); i++) {
			v += "\t" + mapSplit.get(i) + "\n";
		}

		return v;
	}
}
