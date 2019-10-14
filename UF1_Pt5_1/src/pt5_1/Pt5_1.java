package pt5_1;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Pt5_1 implements Serializable {

	public static void main(String[] args) {
		crearFichero();
		leerFichero_crearXML();
		leerXML();
	}

	public static void crearFichero() {
		FileOutputStream fos = null;
		ObjectOutputStream salida = null;
		Persona p;
		try {
			// creamos el fichero
			fos = new FileOutputStream("..\\UF1_Pt5_1\\src\\pt5_1\\myPeople.dat");
			salida = new ObjectOutputStream(fos);
			// creamos los objetos Persona
			p = new Persona("Maria Lopez", 36);
			salida.writeObject(p);
			p = new Persona("Gustavo Gomez", 1);
			salida.writeObject(p);
			p = new Persona("Irene Salas", 36);
			salida.writeObject(p);
			p = new Persona("Roberto Morgade", 63);
			salida.writeObject(p);
			p = new Persona("Graciela Iglesias", 60);
			salida.writeObject(p);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (fos != null)
					fos.close();
				if (salida != null)
					salida.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public static void leerFichero_crearXML() {
		Persona p;
		FileInputStream fis = null;
		ObjectInputStream entrada = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();
			// definimos el elemento raíz del documento
			Element raiz = doc.createElement("personas");
			doc.appendChild(raiz);
			try {
				// lectura del fichero dat
				fis = new FileInputStream("..\\UF1_Pt5_1\\src\\pt5_1\\myPeople.dat");
				entrada = new ObjectInputStream(fis);
				while (true) {
					// definimos que cada objeto que lee sea de tipo persona y
					// lo mostramos
					p = (Persona) entrada.readObject();
					System.out.println(p);
					// elemento persona
					Element persona = doc.createElement("persona");
					raiz.appendChild(persona);
					// elemento nombre
					Element nombre = doc.createElement("nombre");
					nombre.appendChild(doc.createTextNode(p.getNombre()));
					persona.appendChild(nombre);
					// elemento edad (lo pasamos a string)
					String anys = String.valueOf(p.getEdad());
					Element edad = doc.createElement("edad");
					edad.appendChild(doc.createTextNode(anys));
					persona.appendChild(edad);
				}
			} catch (EOFException e) {
				System.out.println("\n---Lectura del fichero dat finalizada---");
			}
			// clases necesarias para finalizar la creación del archivo XML
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("..\\UF1_Pt5_1\\src\\pt5_1\\myPeople.xml"));
			transformer.transform(source, result);
			// StreamResult consoleResult = new StreamResult(System.out);
			// transformer.transform(source, consoleResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void leerXML() {
		File file = new File("..\\UF1_Pt5_1\\src\\pt5_1\\myPeople.xml");
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			// estos métodos podemos usarlos combinados para normalizar el
			// archivo XML
			doc.getDocumentElement().normalize();
			// almacenamos los nodos para luego mostrar la
			// cantidad de ellos con el método getLength()
			NodeList nList = doc.getElementsByTagName("persona");
			System.out.println("\nNumero de personas: " + nList.getLength() + "\n");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					System.out.println("Nombre: " + eElement.getElementsByTagName("nombre").item(0).getTextContent());
					System.out.println("Edad: " + eElement.getElementsByTagName("edad").item(0).getTextContent());
					System.out.println();
				}
			}
			System.out.println("---Lectura del fichero xml finalizada---");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
