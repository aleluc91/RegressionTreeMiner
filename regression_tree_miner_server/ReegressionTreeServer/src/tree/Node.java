package tree;

import java.io.Serializable;

import data.Data;
import utility.Statistics;

/**
 * Classe astratat node che modella l'astrazione dell'entità nodo dell'albero di
 * regressione.
 * 
 * @author Alessio Lucarella
 *
 */
abstract class Node implements Serializable {

	static int idNodeCount = 0;
	int idNode;
	int beginExampleIndex;
	int endExampleIndex;
	double variance;

	/**
	 * Avvolara gli attributi primitivi di classe , inclusa la varianza che
	 * viene calcolata rispetto all'attributo di classe nel sotto-insieme di
	 * training coperto dal nodo. Per il calcolo della varianza si fa uso dei
	 * metodi della classe Statistics.
	 * 
	 * @param trainingSet
	 *            Oggetto di classe Data contenente il training set completo.
	 * @param beginExampleIndex
	 *            Indice iniziale del sotto-insieme di training coperto dal nodo
	 *            corrente.
	 * @param endExampleIndex
	 *            Indice finale del sotto-insieme di training coperto dal nodo
	 *            corrente.
	 */
	Node(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
		idNode = idNodeCount++;
		this.beginExampleIndex = beginExampleIndex;
		this.endExampleIndex = endExampleIndex;
		variance = Statistics.getVariance(trainingSet, beginExampleIndex, endExampleIndex);
	}

	/**
	 * Restituisce il valode del membro idNode.
	 * 
	 * @return Identificativo numerico del nodo.
	 */
	int getIdNode() {
		return idNode;
	}

	/**
	 * Restistuisce il valore del membro beginExampleIndex.
	 * 
	 * @return Indice del primo esempio nel sotto-insieme rispetto al training
	 *         set complessivo.
	 */
	int getBeginExampleIndex() {
		return beginExampleIndex;
	}

	/**
	 * Restituisve il valore del membro endExampleIndex.
	 * 
	 * @return Indice dell'ultimo esempio nel sotto-insieme rispetto al training
	 *         set complessivo.
	 */
	int getEndExampleIndex() {
		return endExampleIndex;
	}

	/**
	 * Restituise il valore del membro variance.
	 * 
	 * @return Valore della varianza rispetto al nodo corrente.
	 */
	double getVariance() {
		return variance;
	}

	/**
	 * E' un metodo astratto la cui implementazione riguarda i nodi di tipo test
	 * (split node) dai quali si possono generare figli , uno per ogni split
	 * prodotto. Restituisce il numero di tali nodi figli.
	 * 
	 * @return Valore del numero di nodi sottostanti.
	 */
	abstract int getNumberOfChildren();

	/**
	 * Concatena in un oggetto String i valori beginExampleIndex ,
	 * endExampleIndex e variance e restituisce la stringa finale.
	 */
	@Override
	public String toString() {
		return "Nodo : [Examples:" + beginExampleIndex + "-" + endExampleIndex + "]" + " variance:" + variance;
	}

}
