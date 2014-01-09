package csvtordf;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import au.com.bytecode.opencsv.CSVReader;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public class Converter {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		Model m = ModelFactory.createDefaultModel();

		CSVReader reader = new CSVReader(new FileReader("Book2.csv"));
		String[] nextLine;
		String[] properties = reader.readNext();
		
		String ns = "http://aksw.org/ns/drugs/";

		// for each property
		for (String property : properties)
			// create property
			m.createProperty(property);

		int i = 1;
		
		HashMap<String, String> created = new HashMap<>(); 

		while ((nextLine = reader.readNext()) != null) {

			Resource cl;
			
			if(!created.containsKey(nextLine[0])) {
			
				created.put(nextLine[0], ns+"Class" + i);
				
				// create classes
		cl = m.createResource(ns+"Class" + i,m.getResource("http://www.w3.org/2000/01/rdf-schema#Class"));
                                
	m.add(cl,m.getProperty("http://www.w3.org/2000/01/rdf-schema#label"),nextLine[0]);
       
        
        
			} else {
				cl = m.getResource(created.get(nextLine[0]));
                                
			}

			// create instance
			Resource r = m.createResource(ns+"Drug" + i, cl);

			// for each column
			for (int j = 1; j < nextLine.length; j++)
				// create the triple
				m.add(r, m.getProperty(properties[j]), nextLine[j]);

			i++;
		}
        // print turtle file
        m.write(System.out, "N-TRIPLES");
      //  m.write(System.out, "N-TRIPLE");
        
        // uncomment these to print all statements
//        Iterator<Statement> it = m.listStatements();
//        while (it.hasNext())
//                System.out.println(it.next());
        
        reader.close();
        
        // save to file
        try {
                FileOutputStream fout = new FileOutputStream(
                                "output.nt");
                m.write(fout, "N-TRIPLES");
        } catch (IOException e) {
                System.out.println("Exception caught" + e.getMessage());
                e.printStackTrace();
        }
        

}

}