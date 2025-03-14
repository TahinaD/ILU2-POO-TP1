package villagegaulois;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;
	
	public Village(String nom, int nbVillageoisMaximum, int nbEtals) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		this.marche = new Marche(nbEtals);
	}
	
	private static class Marche {
		private Etal[] etals;
		
		private Marche(int nbEtal) {
			etals = new Etal[nbEtal];
			for (int i = 0; i < nbEtal; i++) {
				etals[i] = new Etal();
			}
		}
		
		private void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			if (indiceEtal >= 0 && indiceEtal < etals.length)
				etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
		}
		
		private int trouverEtalLibre() {
			for (int i = 0; i < etals.length; i++) {
				if (!etals[i].isEtalOccupe())
					return i;
			}
			return -1;
		}
		
		private Etal[] trouverEtals(String produit) {
			int nbEtalsCherches = 0;
			for (int i = 0; i < etals.length; i++) {
				if (etals[i].isEtalOccupe() && etals[i].contientProduit(produit))
					nbEtalsCherches++;
			}
			
			Etal[] etalsCherches = null;
			int indiceEtalCherche = 0;
			if (nbEtalsCherches != 0) {
				etalsCherches = new Etal[nbEtalsCherches];
				for (int i = 0; indiceEtalCherche < nbEtalsCherches; i++) {
					if (etals[i].isEtalOccupe() && etals[i].contientProduit(produit)) {
						etalsCherches[indiceEtalCherche] = etals[i];
						indiceEtalCherche++;
					}
				}
			}
			return etalsCherches;
		}
		
		private Etal trouverVendeur(Gaulois gaulois) {
			for (int i = 0; i < etals.length; i++) {
				if (etals[i].getVendeur() == gaulois)
					return etals[i];
			}
			return null;
		}
		
		private String afficherMarche() {
			StringBuilder chaine = new StringBuilder();
			int nbEtalsVides = 0;
			for (int i = 0; i < etals.length; i++) {
				if (etals[i].isEtalOccupe())
					chaine.append(etals[i].afficherEtal());
				else
					nbEtalsVides++;
			}
			chaine.append("Il reste " + nbEtalsVides + " �tals non utilis�s dans le march�.\n");
			return chaine.toString();
		}
	}
	
	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		if (chef == null)
			throw new VillageSansChefException();
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() {
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef "
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}
	
	public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
		StringBuilder chaine = new StringBuilder();
		chaine.append(vendeur.getNom() + " cherche un endroit pour vendre " + nbProduit + " " + produit + ".\n");
		int indiceEtalLibre = marche.trouverEtalLibre();
		
		if (indiceEtalLibre != -1) {
			marche.utiliserEtal(indiceEtalLibre, vendeur, produit, nbProduit);
			chaine.append("Le vendeur " + vendeur.getNom() + " vend des " + produit + " � l'�tal n�" + (indiceEtalLibre + 1) + ".\n");
		} else
			chaine.append(vendeur.getNom() + "ne trouve pas d'�tal libre au march�.\n");
		return chaine.toString();
	}
	
	public String rechercherVendeursProduit(String produit) {
		StringBuilder chaine = new StringBuilder();
		Etal[] etalsCherches = marche.trouverEtals(produit);
		if (etalsCherches == null)
			chaine.append("Il n'y a pas de vendeur qui propose des " + produit + " au march�. \n");
		else {
			Gaulois vendeur = etalsCherches[0].getVendeur();
			if (etalsCherches.length == 1)
				chaine.append("Seul le vendeur " + vendeur.getNom() + " propose des " + produit + " au march�.\n");
			else {
				chaine.append("Les vendeurs qui proposent des " + produit + "sont :\n");
				for (int i = 0; i < etalsCherches.length; i++) {
					vendeur = etalsCherches[i].getVendeur();
					chaine.append("-" + vendeur.getNom() + "\n");
				}
			}
		}
		return chaine.toString();
	}
	
	public Etal rechercherEtal(Gaulois vendeur) {
		return marche.trouverVendeur(vendeur);
	}
	
	public String partirVendeur(Gaulois vendeur) {
		Etal etalQuitte = marche.trouverVendeur(vendeur);
		return etalQuitte.libererEtal();
	}
	
	public String afficherMarche() {
		StringBuilder chaine = new StringBuilder();
		chaine.append("Le march� du village " + nom + " poss�de plusieurs �tals : \n");
		chaine.append(marche.afficherMarche());
		return chaine.toString();
	}
}