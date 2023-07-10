package Model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Client {

    private String idClient;
    private String raisonSociale;
    private String codePostal;
    private String ville;
    private static List<Client> clients = new ArrayList<>();
    private static String fileClientPath = "Files/client.xml";

    public Client(String idClient, String raisonSociale, String codePostal, String ville) {
        this.idClient = idClient;
        this.raisonSociale = raisonSociale;
        this.codePostal = codePostal;
        this.ville = ville;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }
    
    /**
     * Créer un client avec les données du fichier xml
     */
    public static Client createClient(Element client) {
        String codePostal = client.getElementsByTagName("codePostal").item(0).getTextContent();
        String idClient = client.getElementsByTagName("idClient").item(0).getTextContent();
        String raisonSociale = client.getElementsByTagName("raisonSociale").item(0).getTextContent();
        String ville = client.getElementsByTagName("ville").item(0).getTextContent();

        return new Client(idClient, raisonSociale, codePostal, ville);
    }
    

    /**
     * Récupérer la liste des clients
     */
    public static List<Client> getClients() {

        try {
            File clientFile = new File(fileClientPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(clientFile);

            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("ObjectClient");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element clientElement = (Element) nodeList.item(i);
                Client client = createClient(clientElement);
                clients.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clients;
    }

    public static Client getClient(String raisonSociale) {
        Client client = null;
        for(Client c : clients) {
            if(c.getRaisonSociale().equals(raisonSociale)) {
                client = c;
            }
        }
        return client;
    }
}
