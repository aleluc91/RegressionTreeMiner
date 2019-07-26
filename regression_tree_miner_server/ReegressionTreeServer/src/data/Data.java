package data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import database.Column;
import database.DatabaseConnectionException;
import database.DbAccess;
import database.EmptySetException;
import database.Example;
import database.TableData;
import database.TableSchema;

/**
 * 
 * Modella l'insieme di esempi di training.
 *
 */
public class Data {

	private List<Example> data = new ArrayList<Example>();
	private int numberOfExamples;
	private List<Attribute> explanatorySet = new LinkedList<Attribute>();
	private ContinuousAttribute classAttribute;

	// TODO Java doc per la lettura da database
	/**
	 * Costruttore di classe che avvia una connessione con il database , da dove andrà
	 * a recuperare i dati con cui avvalorare il training set e i vari attributi di classe.
	 * 
	 * @param tableName Nome della tabella del database MapDb dalla quale avvalorare il training set.
	 * @throws TrainingDataException Errore nell'apprendimento del training set.
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException Errore durante la connessione al database o durante la lettura dei valori.
	 */
	public Data(String tableName) throws TrainingDataException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, SQLException {
		DbAccess db = new DbAccess();
		try {
			db.initConnection();
		} catch (DatabaseConnectionException e) {
			throw new TrainingDataException(e.getMessage());
		}
		TableSchema ts = new TableSchema(db, tableName);
		if (ts.getNumberOfAttributes() < 2)
			throw new TrainingDataException("The table has less than 2 columns.");
		TableData td = new TableData(db);
		Iterator<Column> it = ts.iterator();
		int i = 0;
		while (it.hasNext()) {
			Column column = (Column) it.next();
			if (!column.isNumber() && it.hasNext()) {
				Set<Object> distinctValues = (TreeSet<Object>) td.getDistinctValues(tableName, column);
				Set<String> discreteValues = new TreeSet<String>();
				for (Object o : distinctValues)
					discreteValues.add((String) o);
				explanatorySet.add(new DiscreteAttribute(column.getColumnName(), i, discreteValues));
				i++;
			} else if (it.hasNext() && column.isNumber()) {
				explanatorySet.add(new ContinuousAttribute(column.getColumnName(), i));
				i++;
			}

			else if (!it.hasNext() && column.isNumber())
				classAttribute = new ContinuousAttribute(column.getColumnName(), i);
			else
				throw new TrainingDataException("The last column attribute is not numeric.");
		}
		try {
			data = td.getTransazioni(tableName);
		} catch (EmptySetException e) {
			throw new TrainingDataException(e.getMessage());
		}
		numberOfExamples = data.size();
		db.closeConnection();
	}

	/**
	 * Restistuisce il valore numerico del membro numberOfExamples.
	 * 
	 * @return Cardinalità dell'insieme di esempi.
	 */
	public int getNumberOfExamples() {
		return numberOfExamples;
	}

	/**
	 * Restituisce la dimensione della lista di attributi explanatorySet.
	 * 
	 * @return Cardinalità dell'insieme degli attributi indipendenti.
	 */
	public int getNumberOfExplanatoryAttributes() {
		return explanatorySet.size();
	}

	/**
	 * Restituisce il valore dell'attributo di classe per l'esempio exampleIndex
	 * 
	 * @param exampleIndex
	 *            Indice di posizione all'interno della lista.
	 * @return Valore dell'attributo di classe per l'esempio exampleIndex.
	 */
	public Double getClassValue(int exampleIndex) {
		return (Double) data.get(exampleIndex).get(classAttribute.getIndex());
	}

	/**
	 * Restituisce il valore dell'attributo indicizzato da attributeIndex per
	 * l'esempio exampleIndex all'interno della lista.
	 * 
	 * @param exampleIndex
	 *            Indice di posizone all'interno della lista.
	 * @param attributeIndex
	 *            Indice di posizione dell'attributo contenuto all'interno della
	 *            lista explanatorySet.
	 * @return Oggetto associato all'attributo indipendente per l'esempio
	 *         indicizzato in input.
	 */
	public Object getExplanatoryValue(int exampleIndex, int attributeIndex) {
		// return
		// data[exampleIndex][explanatorySet.get(attributeIndex).getIndex()];
		return data.get(exampleIndex).get(explanatorySet.get(attributeIndex).getIndex());
	}

	/**
	 * Restituisce l'attributo indicizzato da index all'interno della lista
	 * explanatorySet.
	 * 
	 * @param index
	 *            Indice nella lista explanatorySet per uno specifico attributo
	 *            indipendente.
	 * @return Oggetto di tipo Attribute indicizzato da index.
	 */
	public Attribute getExplanatoryAttribute(int index) {
		return explanatorySet.get(index);
	}

	/**
	 * Restituisce l'oggetto corrispondente all'attributo di classe.
	 * 
	 * @return Oggetto ContinuousAttribute associato al membro classAttribute.
	 */
	ContinuousAttribute getClassAttribute() {
		return classAttribute;
	}

	/**
	 * Legge i valori di tutti gli attributi per ogni singolo esempio contenuto
	 * nella lista di esempi data , e li concatena in un ogetto String che viene
	 * restituito come risultato finale in forma di sequenza di testi.
	 */
	public String toString() {
		String value = "";
		for (int i = 0; i < numberOfExamples; i++) {
			value += "[" + i + "]\t";
			for (int j = 0; j < explanatorySet.size(); j++)
				// value += data[i][j] + ",";
				value += data.get(i).get(j);

			// value += data[i][explanatorySet.size()] + "\n";
			value += data.get(i).get(explanatorySet.size());
		}
		return value;
	}

	/**
	 * Ordina il sottoinsieme di esempi compresi nell'intervallo [inf,sup]
	 * all'interno della lista di esempi data rispetto allo specifico attributo
	 * Attribute. Fa uso dell'algoritmo quicksort per l'ordinamento di un array
	 * di interi usando come relazione d'ordine "<=". L'array , in questo caso ,
	 * è dato dai valori assunti dall'attributo passato in input.
	 * 
	 * @param attribute
	 *            Attributo i cui valori devono essere ordinati.
	 * @param beginExampleIndex
	 *            Indice iniziale degli esempi esaminati.
	 * @param endExampleIndex
	 *            Indicefinale degli esempi esaminati.
	 */
	public void sort(Attribute attribute, int beginExampleIndex, int endExampleIndex) {

		quicksort(attribute, beginExampleIndex, endExampleIndex);
	}

	// scambio esempio i con esempi oj
	private void swap(int i, int j) {
		Collections.swap(data, i, j);
	}

	/*
	 * Partiziona il vettore rispetto all'elemento x e restiutisce il punto di
	 * separazione
	 */
	private int partition(DiscreteAttribute attribute, int inf, int sup) {
		int i, j;

		i = inf;
		j = sup;
		int med = (inf + sup) / 2;
		String x = (String) getExplanatoryValue(med, attribute.getIndex());
		swap(inf, med);

		while (true) {

			while (i <= sup && ((String) getExplanatoryValue(i, attribute.getIndex())).compareTo(x) <= 0) {
				i++;

			}

			while (((String) getExplanatoryValue(j, attribute.getIndex())).compareTo(x) > 0) {
				j--;

			}

			if (i < j) {
				swap(i, j);
			} else
				break;
		}
		swap(inf, j);
		return j;

	}

	/*
	 * Partiziona il vettore rispetto all'elemento x e restiutisce il punto di
	 * separazione
	 */
	private int partition(ContinuousAttribute attribute, int inf, int sup) {
		int i, j;

		i = inf;
		j = sup;
		int med = (inf + sup) / 2;
		Double x = (Double) getExplanatoryValue(med, attribute.getIndex());
		swap(inf, med);

		while (true) {

			while (i <= sup && ((Double) getExplanatoryValue(i, attribute.getIndex())).compareTo(x) <= 0) {
				i++;

			}

			while (((Double) getExplanatoryValue(j, attribute.getIndex())).compareTo(x) > 0) {
				j--;

			}

			if (i < j) {
				swap(i, j);
			} else
				break;
		}
		swap(inf, j);
		return j;

	}

	/*
	 * Algoritmo quicksort per l'ordinamento di un array di interi A usando come
	 * relazione d'ordine totale "<="
	 * 
	 * @param A
	 */
	private void quicksort(Attribute attribute, int inf, int sup) {

		if (sup >= inf) {

			int pos;
			if (attribute instanceof DiscreteAttribute)
				pos = partition((DiscreteAttribute) attribute, inf, sup);
			else
				pos = partition((ContinuousAttribute) attribute, inf, sup);

			if ((pos - inf) < (sup - pos + 1)) {
				quicksort(attribute, inf, pos - 1);
				quicksort(attribute, pos + 1, sup);
			} else {
				quicksort(attribute, pos + 1, sup);
				quicksort(attribute, inf, pos - 1);
			}

		}

	}

}
