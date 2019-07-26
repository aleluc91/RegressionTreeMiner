package tree;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.abego.treelayout.util.DefaultTreeForTreeLayout;

import data.Attribute;
import data.ContinuousAttribute;
import data.Data;
import data.DiscreteAttribute;

/**
 * 
 * Modella l'entità dell'intero albero di regressione come insieme di
 * sotto-alberi.
 *
 */
public class RegressionTree implements Serializable {

	private Node root;
	private RegressionTree childTree[];

	RegressionTree() {
	}

	/**
	 * Istanzia un sotto-albero dell'intero albero e avvia l'induzione
	 * dell'albero dagli esempi di training in input.
	 * 
	 * @param trainingSet
	 *            Training set complessivo.
	 */
	public RegressionTree(Data trainingSet) {
		learnTree(trainingSet, 0, trainingSet.getNumberOfExamples() - 1, trainingSet.getNumberOfExamples() * 10 / 100);
	}

	/**
	 * Verifica se il sotto-insieme corrente può essere coperto da un nodo
	 * foglia controllando la cardinalità di tale sotto-insieme.
	 * 
	 * @param trainingSet
	 *            Training set complessivo.
	 * @param begin
	 *            Indice iniziale del sotto-insieme di training.
	 * @param end
	 *            Indice finale del sotto-insieme di training.
	 * @param numberOfExamplesPerLeaf
	 *            Numero minimo che una foglia deve contenere.
	 * @return False se la dimensione del sotto-insieme di training è maggiore
	 *         del numero minimo , True nel caso contrario.
	 */
	boolean isLeaf(Data trainingSet, int begin, int end, int numberOfExamplesPerLeaf) {
		int size = end - begin;
		if (size > numberOfExamplesPerLeaf)
			return false;
		else
			return true;
	}

	/**
	 * Istanzia un DiscreteAttribute su ciascun attributo indipendente e ne
	 * computa la varianza a seguito dello split. Il nodo con varianza minore
	 * tra quelli istanziati viene restituito.
	 * 
	 * @param trainingSet
	 *            Training set complessivo.
	 * @param begin
	 *            Indice iniziale del sotto-insieme di training.
	 * @param end
	 *            Indice finale del sotto-insieme di training.
	 * @return Nodo migliore di split per il sotto-insieme di training corrente.
	 */
	SplitNode determineBestSplitNode(Data trainingSet, int begin, int end) {
		TreeSet<SplitNode> ts = new TreeSet<SplitNode>();
		for (int i = 0; i < trainingSet.getNumberOfExplanatoryAttributes(); i++) {
			SplitNode current = null;
			Attribute a = trainingSet.getExplanatoryAttribute(i);
			if (a instanceof DiscreteAttribute)
				current = new DiscreteNode(trainingSet, begin, end, (DiscreteAttribute) a);
			else if (a instanceof ContinuousAttribute)
				current = new ContinuousNode(trainingSet, begin, end, (ContinuousAttribute) a);
			ts.add(current);
		}
		trainingSet.sort(ts.last().getAttribute(), begin, end);
		return ts.last();
	}

	/**
	 * Genera un sotto-albero con il sotto-insieme di input istanziando un nodo
	 * fogliare o un nodo di split. In tal caso determina il miglior nodo
	 * rispetto al sotto-insieme di input, ed a tale nodo esso associa un
	 * sotto-albero avente radice il nodo medesimo e avente un numero di rami
	 * pari al numero dei figli determinati dallo split. Ricorsivamente ogni
	 * oggetto RegressionTree in childTree[] sarà re-invocato il metodo
	 * learnTree() per l'apprendimento su un insieme del ridotto del
	 * sotto-insieme attuale. Nella condizione in cui il nodo di split non
	 * origina figli , il nodo diventa fogliare.
	 * 
	 * @param trainingSet
	 *            Training set complessivo.
	 * @param begin
	 *            Indice iniziale del sotto-insieme di training.
	 * @param end
	 *            Indice finale del sotto-insieme di training.
	 * @param numberOfExamplesPerLeaf
	 *            Numero massimo che una foglia deve contenere.
	 */
	private void learnTree(Data trainingSet, int begin, int end, int numberOfExamplesPerLeaf) {
		if (isLeaf(trainingSet, begin, end, numberOfExamplesPerLeaf)) {
			// determina la classe che compare più frequentemente nella
			// partizione corrente
			root = new LeafNode(trainingSet, begin, end);
		} else // split node
		{
			root = determineBestSplitNode(trainingSet, begin, end);

			if (root.getNumberOfChildren() > 1) {
				childTree = new RegressionTree[root.getNumberOfChildren()];
				for (int i = 0; i < root.getNumberOfChildren(); i++) {
					childTree[i] = new RegressionTree();
					childTree[i].learnTree(trainingSet, ((SplitNode) root).getSplitInfo(i).beginIndex,
							((SplitNode) root).getSplitInfo(i).endIndex, numberOfExamplesPerLeaf);
				}
			} else
				root = new LeafNode(trainingSet, begin, end);
		}
	}

	/*
	 * public Double predictClass() throws UnknownValueException { Double
	 * predictedValue = 0.0; if (root instanceof LeafNode) predictedValue =
	 * ((LeafNode) root).getPredictedClassValue(); else { int max = ((SplitNode)
	 * root).getNumberOfChildren(); System.out.println(((SplitNode)
	 * root).formulateQuery()); int choice = Keyboard.readInt(); if (choice >
	 * max - 1 || choice < 0) throw new
	 * UnknownValueException("The answer should be an integer between 0 and " +
	 * (max - 1) + "!"); //TODO Aggiornare il metodo per permettere il
	 * funzionamento con la struttura client server predictedValue =
	 * childTree[1].predictClass(); } return predictedValue; }
	 */

	/**
	 * Concatena in una String tutte le informazioni di root e childTree[]
	 * correnti invocando i relativi metodi toString(). Nel caso il root
	 * corrente è di split vengono concatenate anche le informazioni dei rami.
	 */
	public String toString() {
		String tree = root.toString() + "\n";

		if (root instanceof LeafNode) {

		} else // split node
		{
			for (int i = 0; i < childTree.length; i++)
				tree += childTree[i];
		}
		return tree;
	}

	/**
	 * Scandisce ciascun ramo dell'albero completo dalla radice alla foglia
	 * concatenando le informazioni dei nodi di split fino al nodo foglia. Se il
	 * nodo root è di split discende ricorsivamente l'albero per ottenere le
	 * informazioni del nodo sottostante , di ogni ramo-regola , se è di foglia
	 * termina l'attraversamento visualizzando la regola.
	 */
	public String printRules() {
		String rules = new String();
		rules += "********* RULES **********\n";
		String current = new String();
		String rule = new String();
		if (root instanceof LeafNode)
			rules += "Class = " + ((LeafNode) root).getPredictedClassValue();
		else if (root instanceof DiscreteNode) {
			rule += ((SplitNode) root).getAttribute().getName() + "=";
			for (int i = 0; i < ((SplitNode) root).mapSplit.size(); i++) {
				current = rule + ((SplitNode) root).mapSplit.get(i).getSplitValue();
				rules = childTree[i].printRules(current, rules);
			}
		} else if (root instanceof ContinuousNode) {
			rule += ((SplitNode) root).getAttribute().getName();
			for (int i = 0; i < ((SplitNode) root).mapSplit.size(); i++) {
				String comparator = ((SplitNode) root).mapSplit.get(i).getComparator();
				current = rule + comparator + ((SplitNode) root).mapSplit.get(i).getSplitValue();
				childTree[i].printRules(current, rules);
			}
		}
		rules += "*************************\n";
		return rules;
	}

	/**
	 * Supporta il metodo printRules(). Concatena alle informazioni del
	 * precedente nodo , contenute in current , quelle del nodo root del
	 * sotto-albero corrente. Se il nodo corrente è di split il metodo viene
	 * invocato ricorsivamente con current e le informazioni del nodo corrente ,
	 * mentre se è un nodo foglia vengono visualizzate tutte le informazioni
	 * concatenate.
	 * 
	 * @param current
	 *            Informazioni del nodo di split del sotto-albero al livello
	 *            superiore.
	 * @param rules
	 */
	private String printRules(String current, String rules) {
		if (root instanceof LeafNode)
			rules += current + " ==> Class = " + ((LeafNode) root).getPredictedClassValue() + "\n";
		else if (root instanceof DiscreteNode) {
			current += " AND " + ((SplitNode) root).getAttribute().getName() + "=";
			String tmp = null;
			for (int i = 0; i < ((SplitNode) root).mapSplit.size(); i++) {
				tmp = current + ((SplitNode) root).mapSplit.get(i).getSplitValue();
				rules = childTree[i].printRules(tmp, rules);
			}
		} else if (root instanceof ContinuousNode) {
			current += " AND " + ((SplitNode) root).getAttribute().getName();
			String tmp = null;
			for (int i = 0; i < ((SplitNode) root).mapSplit.size(); i++) {
				String comparator = ((SplitNode) root).mapSplit.get(i).getComparator();
				tmp = current + comparator + ((SplitNode) root).mapSplit.get(i).getSplitValue();
				rules = childTree[i].printRules(tmp, rules);
			}
		}
		return rules;
	}

	/**
	 * Invoca il metodo toString() per la visualizzazione dell'albero di
	 * regressione.
	 */
	public void printTree() {
		System.out.println("********* TREE **********\n");
		System.out.println(toString());
		System.out.println("*************************\n");
	}

	public void salva(String nomeFile) throws FileNotFoundException, IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nomeFile));
		out.writeObject(this);
		out.writeObject(root);
		out.writeObject(childTree);
		out.close();
	}

	public static RegressionTree carica(String nomeFile)
			throws FileNotFoundException, ClassNotFoundException, IOException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(nomeFile));
		RegressionTree rTree = (RegressionTree) in.readObject();
		rTree.root = (Node) in.readObject();
		rTree.childTree = (RegressionTree[]) in.readObject();
		in.close();
		return rTree;
	}

	public Node getRoot() {
		return root;
	}

	public RegressionTree getChildTree(int index) {
		return childTree[index];
	}

	public DefaultTreeForTreeLayout<NodeInfo> getTree() {
		DefaultTreeForTreeLayout<NodeInfo> tree;
		NodeInfo treeRoot;
		if(root instanceof LeafNode){
			treeRoot = new NodeInfo(((LeafNode)root).getPredictedClassValue().toString() , 30 , 20);
			tree = new DefaultTreeForTreeLayout<NodeInfo>(treeRoot);
		}else{
			treeRoot = new NodeInfo(((SplitNode)root).getAttribute().getName() , 30 , 20);
			tree = getTree(tree = new DefaultTreeForTreeLayout<NodeInfo>(treeRoot) , treeRoot);
		}
		return tree;
	}

	public DefaultTreeForTreeLayout<NodeInfo> getTree(DefaultTreeForTreeLayout<NodeInfo> tree , NodeInfo current) {
		if(root instanceof LeafNode){
			tree.addChild(current, new NodeInfo(((LeafNode)this.root).getPredictedClassValue().toString() , 60 , 20));
			System.out.println(((LeafNode)this.root).getPredictedClassValue().toString());
		}else{
			for(int i = 0 ; i < ((SplitNode)root).mapSplit.size() ; i++){
				NodeInfo currentNode = null;
				if(root instanceof DiscreteNode){
					currentNode = new NodeInfo(((SplitNode)root).mapSplit.get(i).splitValue.toString().trim() , 30 , 20);
					tree.addChild(current, currentNode);
				}else{
					currentNode = new NodeInfo(((SplitNode)root).mapSplit.get(i).getComparator() + ((SplitNode)root).mapSplit.get(i).splitValue.toString().trim() , 0 , 0);
					tree.addChild(current, currentNode);
				}
				tree = childTree[i].getTree(tree , currentNode);
			}
		}
		return tree;
	}

}
