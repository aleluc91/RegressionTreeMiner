package tree;

import java.io.Serializable;

import data.Attribute;
import data.Data;
import data.DiscreteAttribute;
import tree.SplitNode.SplitInfo;

/**
 * Modella l'entità per un nodo di split relativo ad un attributo indipendente
 * discreto.
 * 
 * @author Alessio Lucarella
 *
 */
class DiscreteNode extends SplitNode implements Serializable {

	/**
	 * Istanzia un oggetto invocando il costruttore della superclasse SplitNode
	 * con il parametro discreto DiscreteAttribute.
	 * 
	 * @param trainingSet
	 *            Training set complessivo.
	 * @param beginExampleIndex
	 *            Indice iniziale del sotto-insieme di training.
	 * @param endExampleIndex
	 *            Indice finale del sotto-insieme di training.
	 * @param attribute
	 *            Attributo indipendente sul quale si definiscce lo split.
	 */
	DiscreteNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, DiscreteAttribute attribute) {
		super(trainingSet, beginExampleIndex, endExampleIndex, attribute);
	}

	/**
	 * {@inheritDoc} Istanzia gli oggetti SplitInfo con ciascuno dei valori
	 * discreti relativamente al sotto-insieme di training corrente , quindi
	 * popola la lista mapSplit con gli oggetti creati.
	 */
	@Override
	void setSplitInfo(Data trainingSet, int beginExampelIndex, int endExampleIndex, Attribute attribute) {
		// TODO Controllare il corretto funzionamento del metodo
		Object currentSplitValue = trainingSet.getExplanatoryValue(beginExampelIndex, attribute.getIndex());
		// SplitInfo tempMap[] = new SplitInfo[endExampleIndex -
		// beginExampleIndex];
		int beginSplit = beginExampleIndex;
		int child = 0;
		for (int i = beginExampleIndex + 1; i <= endExampleIndex; i++) {
			if (currentSplitValue.equals(trainingSet.getExplanatoryValue(i, attribute.getIndex())) == false) {
				mapSplit.add(new SplitInfo(currentSplitValue, beginSplit, i - 1, child));
				currentSplitValue = trainingSet.getExplanatoryValue(i, attribute.getIndex());
				beginSplit = i;
				child++;
			}
		}
		mapSplit.add(new SplitInfo(currentSplitValue, beginSplit, endExampleIndex, child));
		/*
		 * for(int i = 0 ; i <= child ; i++) System.out.println("TEMPMAP " + i +
		 * " " +tempMap[i].beginIndex + " " + tempMap[i].endIndex);
		 */
		// mapSplit = new SplitInfo[child + 1];
		// System.arraycopy(tempMap, 0, mapSplit, 0, child + 1);
		// System.out.println("Map split length = " + mapSplit.length);
		/*
		 * for(int i = 0 ; i <= child ; i++) System.out.println("MAPSPLIT " + i
		 * + " " + mapSplit[i].beginIndex + " " + mapSplit[i].endIndex);
		 */
	}

	/**
	 * {@inheritDoc} Effettua il confronto del valore in input rispetto tutti a
	 * tutti gli split contenuti all'interno della lista mapSplit e restituisce
	 * l'identificativo dello split con cui il test è positivo.
	 */
	@Override
	int testCondition(Object value) {
		// TODO Controllare il corretto funzionamento del metodo
		int i;
		for (i = 0; i < mapSplit.size(); i++) {
			if (mapSplit.get(i).splitValue.equals(value))
				break;
		}
		return mapSplit.get(i).numberChild;
	}

	/**
	 * Invoca il metodo toString() della superclasse specializzandolo per i
	 * discreti.
	 */
	@Override
	public String toString() {
		return "DISCRETE " + super.toString();
	}

}
