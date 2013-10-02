package com.example.fragmenttab;

import android.content.Context;

public class Eventi implements Comparable<Eventi> {

	private String nome;

	private String inizio;

	private String fine;

	private int ID = 0;

	private String data;

	private static Context context;

	public static void setContext(Context mcontext) {
		if (context == null)
			context = mcontext;
	}

	public Eventi() {
		super();
		this.nome = "";
		this.inizio = "";
		this.fine = "";
		this.data = "";
		this.ID = 0;

	}

	public Eventi(String nome, String inizio, String fine, int ID, String data) {
		super();
		this.nome = nome;
		this.inizio = inizio;
		this.fine = fine;
		this.data = data;
		this.ID = ID;
	}

	public void setID(int a) {

		this.ID = a;
	}

	public int getID() {

		return ID;
	}

	public String getNome() {
		return nome;
	}

	public String getInizio() {
		return inizio;
	}

	public String getFine() {
		return fine;
	}

	public String getData() {
		return data;
	}

	public String toString2() {
		/*
		 * Date d = new Date(); DateFormat df = new
		 * SimpleDateFormat("dd;MM;yyyy;HH;mm;ss"); String fecha = df.format(d);
		 */
		return nome + ";" + inizio + ";" + fine + ";" + ID + ";" + data;
	}

	public String toString() {

		return context.getString(R.string.tipo2) + nome
				+ context.getString(R.string.orainiz) + inizio
				+ context.getString(R.string.orafin) + fine
				+ context.getString(R.string.data) + data;

	}

	public int compareTo(Eventi altro) {

		int ore = Integer.parseInt(altro.getInizio().substring(0, 2));
		int min = Integer.parseInt(altro.getInizio().substring(3));
		int mese = Integer.parseInt(altro.getData().substring(0, 2));
		int giorno = Integer.parseInt(altro.getData().substring(3, 5));
		int anno = Integer.parseInt(altro.getData().substring(6));

		// CONFRONTO ANNI
		if (Integer.parseInt(this.getData().substring(6)) > anno)
			return 1;
		else if (Integer.parseInt(this.getData().substring(6)) < anno)
			return -1;
		else {
			// CONFRONTO MESI
			if (Integer.parseInt(this.getData().substring(0, 2)) > mese)
				return 1;
			else if (Integer.parseInt(this.getData().substring(0, 2)) < mese)
				return -1;
			else {

				// CONFRONTO GIORNI

				if (Integer.parseInt(this.getData().substring(3, 5)) > giorno)
					return 1;
				else if (Integer.parseInt(this.getData().substring(3, 5)) < giorno)
					return -1;
				else {

					// CONFRONTO ORE
					if (Integer.parseInt(this.getInizio().substring(0, 2)) > ore)
						return 1;
					else if (Integer.parseInt(this.getInizio().substring(0, 2)) < ore)
						return -1;

					// CONFRONTO MINUTI
					else if (Integer.parseInt(this.getInizio().substring(3)) > min)
						return 1;
					else if (Integer.parseInt(this.getInizio().substring(3)) < min)
						return -1;
					else
						return 0;
				}

			}
		}
	}

}
