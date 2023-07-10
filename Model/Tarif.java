package Model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Tarif {
    
    private String idClient;
    private String idClientHeritage;
    private String codeDepartement;
    private String zone;
    private String montant;

    private static String fileTarifPath = "Files/tarif.xml";

    public Tarif(String idClient, String idClientHeritage, String codeDepartement, String zone, String montant) {
        this.idClient = idClient;
        this.idClientHeritage = idClientHeritage;
        this.codeDepartement = codeDepartement;
        this.zone = zone;
        this.montant = montant;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getIdClientHeritage() {
        return idClientHeritage;
    }

    public void setIdClientHeritage(String idClientHeritage) {
        this.idClientHeritage = idClientHeritage;
    }

    public String getCodeDepartement() {
        return codeDepartement;
    }

    public void setCodeDepartement(String codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

     /**
     * Créer un tarif avec les données du fichier xml
     */
    public static Tarif createTarif(Element tarif) {
        String codeDepartement = tarif.getElementsByTagName("codeDepartement").item(0).getTextContent();
        String idClient = tarif.getElementsByTagName("idClient").item(0).getTextContent();
        String idClientHeritage  = tarif.getElementsByTagName("idClientHeritage").item(0).getTextContent();
        String montant = tarif.getElementsByTagName("montant").item(0).getTextContent();
        String zone = tarif.getElementsByTagName("zone").item(0).getTextContent();

        return new Tarif(idClient, idClientHeritage, codeDepartement , zone, montant);
    }

    /**
     * Récupérer la liste des tarifs
     */
    public static List<Tarif> getTarifs() {

        List<Tarif> tarifs = new ArrayList<>();

        try {
            File tarifFile = new File(fileTarifPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(tarifFile);

            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("ObjectTarif");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element tarifElement = (Element) nodeList.item(i);
                Tarif tarif = createTarif(tarifElement);
                tarifs.add(tarif);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tarifs;
    }
    
}
