package Projet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import Model.Client;
import Model.Localite;
import Model.Tarif;
import Model.Taxation;

public class Main {

    private static Client expediteur = null;
    private static Client destinataire = null;
    
     public static void main(String[] args) {

        //Afficher la liste de tous les clients dans la console
        System.out.println("Voici la liste des clients : ");
        for (Client client : Client.getClients()) {
            System.out.println("Client " + client.getIdClient() + " raison sociale : " + client.getRaisonSociale());
        }

        //Le scanner pour la saisie dans notre console
        Scanner scanner = new Scanner(System.in);

        //L'utilisateur saisie la raison sociale du destinataire
        System.out.println("Veuillez saisir la raison sociale du destinataire(la liste ci-dessus)");
        String raisonSocialeDest = scanner.nextLine();
        if(raisonSocialeDest != null && (!raisonSocialeDest.isEmpty())) {
            destinataire = Client.getClient(raisonSocialeDest);
            if(destinataire == null) {
                System.out.println("Veuillez saisir une raison sociale valide pour le destinataire");
            }
        }

        //L'utilisateur saisie la raison sociale de l'éxpéditeur
        System.out.println("Veuillez saisir la raison sociale de l'expéditeur(la liste ci-dessus)");
        String raisonSocialeEx = scanner.nextLine();
        if(raisonSocialeEx != null && (!raisonSocialeEx.isEmpty())) {
            expediteur = Client.getClient(raisonSocialeEx);
            if(expediteur == null) {
                System.out.println("Veuillez saisir une raison sociale valide");
            }
        }

        //L'utilisateur saisit le nombre de colis
        System.out.println("Veuillez saisir le nombre de colis");
        int nbColis = scanner.nextInt();
        if(nbColis != 0) {
            //System.out.println("Nombre de colis saisit : " + texte);
        }

        //L'utilisateur saisit le poids de l'expédition
        System.out.println("Veuillez saisir le poids de l'expédition");
        int poids = scanner.nextInt();
        if(poids != 0) {
           // System.out.println("Le poids saisit : " + texte);
        }
        scanner.nextLine();

        //L'utilisateur saisit qui entre l'expéditeur et le destinatire paie le transport
        System.out.println("Veuillez saisir qui doit payer le transport. Soit le destinataire ou l'expéditeur");
        String payeClient = scanner.nextLine();
        if(payeClient != null && (!payeClient.isEmpty())) {
            Client client = Client.getClient(payeClient);
            if(client == destinataire) {
                calcul(destinataire, nbColis, poids, true);
            } else if(client == expediteur) {
                calcul(expediteur, nbColis, poids, false);
            }
        }

        scanner.close();
    }

    /**
     * Effectuer le calcul
     * @param raisonSociale
     * @param nbColis
     * @param poids
     */
    public static void calcul(Client client, int nbColis, int poids, boolean isDestinatairePaye) {
        double tarif = getTarif(client);
        double taxe = getTaxation(client, isDestinatairePaye);
        double resultat = tarif + taxe;
        System.out.println("Voici le détail du calcul : \n Montant ht tarif : " + tarif + " \n Taxe à appliquer " + taxe + " \n Montant à payer " + resultat);
    }

    /**
     * Méthode pour définir le montant hors tarif à appliquer
     * @param client
     */
    public static double getTarif(Client client) {
        Localite localiteClient = null;
        Tarif tarifClient = null;

        //Récupérer la localité correspondant à la ville du client
        for(Localite localite : Localite.getLocalites()) {
            if(localite.getVille().equals(client.getVille()) && localite.getCodePostal().equals(client.getCodePostal())) {
                localiteClient = localite;
            }
        }

        if(localiteClient != null) {
            //Récupérer le tarif correspondant(avec l'id du client, la zone et le code département)
            for(Tarif tarif : Tarif.getTarifs()) {
                if(client.getIdClient().equals(tarif.getIdClient()) 
                    && tarif.getZone().equals(localiteClient.getZone()) 
                    && tarif.getCodeDepartement().equals(localiteClient.getCodePostal())) {
                    tarifClient = tarif;
                }
            }
            //Si pas de tarif trouvé, on recherche le tarif de la zone z-1
            if(tarifClient == null) {
                Integer zone = Integer.parseInt(localiteClient.getZone()) - 1;
                String zoneString = zone.toString();
                if(zoneString != null || !zoneString.isEmpty()) {
                    for(Tarif tarif : Tarif.getTarifs()) {
                        if(client.getIdClient().equals(tarif.getIdClient()) 
                            && tarif.getZone().equals(zoneString) 
                            && tarif.getCodeDepartement().equals(localiteClient.getCodePostal())) {
                            tarifClient = tarif;
                        }
                    }
                }
               
            }
            //Si pas de tarif, on utilise le tarif générale ou un tarif hérité
            if(tarifClient == null) {
                for(Tarif tarif : Tarif.getTarifs()) {
                    if(tarif.getIdClient().equals("0")
                        && tarif.getZone().equals(localiteClient.getZone()) 
                        && tarif.getCodeDepartement().equals(localiteClient.getCodePostal())) {
                            //Tarif hérité ou générale
                            if(tarif.getIdClientHeritage() != null || !tarif.getIdClient().isEmpty()) {
                                tarifClient = Tarif.getTarifs().stream()
                                    .filter(t -> t.getIdClient().equals(tarif.getIdClientHeritage()))
                                    .findFirst().orElseThrow(() -> new RuntimeException("Tarif non trouvé !"));
                            } else {
                                tarifClient = tarif;
                            }
                    }
                }
            }
        }
        return Double.parseDouble(tarifClient.getMontant());    
    }

    /**
     * Méthode pour récupérer la taxe à appliquer
     */
    public static double getTaxation(Client client, boolean isDestinatairePaye) {
        double montantTaxe = 0;

        //Taxation en fonction de l'id du client
        for(Taxation t : Taxation.getTaxations()) {
            if(t.getIdClient().equals(client.getIdClient())) {
                //Si c'est le destinataire ou l'expéditeur qui paye
                if(isDestinatairePaye) {
                    montantTaxe = Double.parseDouble(t.getTaxePortDu());
                } else {
                    montantTaxe = Double.parseDouble(t.getTaxePortPaye());
                }
            }
        }
        //Taxation générale dont l'id du client est égal à 0
        //Dans notre fichier conditionsTaxation.xml il n'y a pas d'idClient égal à 0. On prend donc celui dont l'id est vide)
        if(montantTaxe == 0) {
            Taxation taxe = Taxation.getTaxations().stream()
                                .filter(t -> t.getIdClient().isBlank() || t.getIdClient().isEmpty())
                                .findFirst().orElseThrow(() -> new RuntimeException("Taxation non trouvée !"));
            if(taxe != null) {
                montantTaxe = setMontant(taxe, montantTaxe);
            }
        }
        return montantTaxe;
    }

    /**
     * Set le montant de la taxe
     * Soit utiliser le taxePortDu ou taxePortPaye
     * @param taxe
     * @param montantTaxe
     * @return
     */
    public static double setMontant(Taxation taxe, double montantTaxe) {
        if(taxe.isUseTaxePortDuGenerale()) {
            montantTaxe = Double.parseDouble(taxe.getTaxePortDu());
        }
        
        if(taxe.isUseTaxePortPayeGenerale()) {
            montantTaxe = Double.parseDouble(taxe.getTaxePortPaye());
        }
        return montantTaxe;
    }
}
