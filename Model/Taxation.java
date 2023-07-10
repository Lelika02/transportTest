package Model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Taxation {
    
    private String idClient;
    private boolean useTaxePortDuGenerale;
    private String taxePortDu;
    private boolean useTaxePortPayeGenerale;
    private String taxePortPaye;

    private static String fileTaxationPath = "Files/conditiontaxation.xml";

    public Taxation(String idClient, boolean useTaxePortDuGenerale, String taxePortDu,
            boolean useTaxePortPayeGenerale, String taxePortPaye) {
        this.idClient = idClient;
        this.useTaxePortDuGenerale = useTaxePortDuGenerale;
        this.taxePortDu = taxePortDu;
        this.useTaxePortPayeGenerale = useTaxePortPayeGenerale;
        this.taxePortPaye = taxePortPaye;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public boolean isUseTaxePortDuGenerale() {
        return useTaxePortDuGenerale;
    }

    public void setUseTaxePortDuGenerale(boolean useTaxePortDuGenerale) {
        this.useTaxePortDuGenerale = useTaxePortDuGenerale;
    }

    public String getTaxePortDu() {
        return taxePortDu;
    }

    public void setTaxePortDu(String taxePortDu) {
        this.taxePortDu = taxePortDu;
    }

    public boolean isUseTaxePortPayeGenerale() {
        return useTaxePortPayeGenerale;
    }

    public void setUseTaxePortPayeGenerale(boolean useTaxePortPayeGenerale) {
        this.useTaxePortPayeGenerale = useTaxePortPayeGenerale;
    }

    public String getTaxePortPaye() {
        return taxePortPaye;
    }

    public void setTaxePortPaye(String taxePortPaye) {
        this.taxePortPaye = taxePortPaye;
    }

     /**
     * Créer une taxation avec les données du fichier xml
     */
    public static Taxation createTaxation(Element taxation) {
        String idClient = taxation.getElementsByTagName("idClient").item(0).getTextContent();
        String taxePortDu  = taxation.getElementsByTagName("taxePortDu").item(0).getTextContent();
        String taxePortPaye  = taxation.getElementsByTagName("taxePortPaye").item(0).getTextContent();
        String useTaxePortDuGenerale  = taxation.getElementsByTagName("useTaxePortDuGenerale").item(0).getTextContent();
        String useTaxePortPayeGenerale  = taxation.getElementsByTagName("useTaxePortPayeGenerale").item(0).getTextContent();

        return new Taxation(idClient, Boolean.parseBoolean(useTaxePortDuGenerale), taxePortDu, Boolean.parseBoolean(useTaxePortPayeGenerale), taxePortPaye);
    }
    

    /**
     * Récupérer la liste des taxations
     */
    public static List<Taxation> getTaxations() {

        List<Taxation> taxations = new ArrayList<>();

        try {
            File taxationFile = new File(fileTaxationPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(taxationFile);

            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("ObjectConditionTaxation");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element taxationElement = (Element) nodeList.item(i);
                Taxation taxation = createTaxation(taxationElement);
                taxations.add(taxation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return taxations;
    }
    
}
