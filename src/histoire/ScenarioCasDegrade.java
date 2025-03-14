package histoire;

import villagegaulois.Etal;

public class ScenarioCasDegrade {
	public static void main(String[] args) {
		Etal etal = new Etal();
		etal.libererEtal();
		try {
			etal.acheterProduit(-1, null);
		} catch (IllegalArgumentException e) {
			System.out.println("La quantité de produit n'est pas legit.");
		} catch (IllegalStateException e) {
			System.out.println("Il n'y a pas de vendeur à cet etal.");
		}
		System.out.println("Fin du test");
	}

}
