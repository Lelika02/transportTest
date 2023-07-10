package Model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Localite {
    
    private String codePostal;
    private String ville;
    private String zone;

    private static String fileLocalitePath = "Files/localite.xml";

    private Localite(String codePostal, String ville, String zone) {
        this.codePostal = codePostal;
        this.ville =  ville;
        this.zone = zone;
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

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    /**
     * Créer une localité avec les données du fichier xml
     */
    public static Localite createLocalite(Element localite) {
        String codePostal = localite.getElementsByTagName("codePostal").item(0).getTextContent();
        String ville  = localite.getElementsByTagName("ville").item(0).getTextContent();
        String zone   = localite.getElementsByTagName("zone").item(0).getTextContent();

        return new Localite(codePostal, ville, zone);
    }

    /**
     * Récupérer la liste des localités
     */
    public static List<Localite> getLocalites() {

        List<Localite> localites = new ArrayList<>();

        try {
            File localiteFile = new File(fileLocalitePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(localiteFile);

            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("ObjectLocalite");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element localiteElement = (Element) nodeList.item(i);
                Localite localite = createLocalite(localiteElement);
                localites.add(localite);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return localites;
    }
}