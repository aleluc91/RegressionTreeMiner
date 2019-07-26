package tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import data.Attribute;
import data.ContinuousAttribute;
import data.Data;

/**
 * 
 * Modella l'entità per un nodo corrispondente ad un attributo continuo.
 *
 */
public class ContinuousNode extends SplitNode implements Serializable {

	/**
	 * Istanzia un oggetto invocando il costruttore della superclasse SplitNode
	 * con il parametro continuo ContinuousAttribute.
	 * 
	 * @param trainingSet
	 *            Training set corrente.
	 * @param beginExampleIndex
	 *            Indice iniziale del sotto-insieme di training.
	 * @param endExampleIndex
	 *            Indice finale del sotto-insieme di training.
	 * @param attribute
	 *            Attributo continuo sul quale si definisce lo split.
	 */
	ContinuousNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, ContinuousAttribute attribute) {
		super(trainingSet, beginExampleIndex, endExampleIndex, attribute);
	}

	/**
	 * {@inheritDoc} Istanzia gli oggetti SplitInfo con ciascuno dei valori
	 * continui relativamente al sotto-insieme di training corrente , quindi
	 * popola la lista mapSplit con gli oggetti creati.
	 * 
	 */
	void setSplitInfo(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute) {
		// Update mapSplit defined in SplitNode -- contiene gli indici del
		// partizionamento
		Double currentSplitValue = (Double) trainingSet.getExplanatoryValue(beginExampleIndex, attribute.getIndex());
		double bestInfoVariance = 0;
		List<SplitInfo> bestMapSplit = null;

		for (int i = beginExampleIndex + 1; i <= endExampleIndex; i++) {
			Double value = (Double) trainingSet.getExplanatoryValue(i, attribute.getIndex());
			if (value.doubleValue() != currentSplitValue.doubleValue()) {
				// System.out.print(currentSplitValue +" var ");
				double localVariance = new LeafNode(trainingSet, beginExampleIndex, i - 1).getVariance();
				double candidateSplitVariance = localVariance;
				localVariance = new LeafNode(trainingSet, i, endExampleIndex).getVariance();
				candidateSplitVariance += localVariance;
				// System.out.println(candidateSplitVariance);
				if (bestMapSplit == null) {
					bestMapSplit = new ArrayList<SplitInfo>();
					bestMapSplit.add(new SplitInfo(currentSplitValue, beginExampleIndex, i - 1, 0, "<="));
					bestMapSplit.add(new SplitInfo(currentSplitValue, i, endExampleIndex, 1, ">"));
					bestInfoVariance = candidateSplitVariance;
				} else {

					if (candidateSplitVariance < bestInfoVariance) {
						bestInfoVariance = candidateSplitVariance;
						bestMapSplit.set(0, new SplitInfo(currentSplitValue, beginExampleIndex, i - 1, 0, "<="));
						bestMapSplit.set(1, new SplitInfo(currentSplitValue, i, endExampleIndex, 1, ">"));
					}
				}
				currentSplitValue = value;
			}
		}
		mapSplit = bestMapSplit;
		// rimuovo split inutili (che includono tutti gli esempi nella stessa
		// partizione)
		if ((mapSplit.get(1).beginIndex == mapSplit.get(1).getEndIndex()))
			mapSplit.remove(1);

	}

	@Override
	int testCondition(Object value) {
		// TODO Controllare il corretto funzionamento del metodo
		return 0;
	}

	/**
	 * Invoca il metodo toString() della superclasse specializzandolo per i
	 * continui.
	 */
	@Override
	public String toString() {
		return "CONTINUOUS " + super.toString();
	}

}
