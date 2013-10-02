package com.example.fragmenttab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Environment;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewStyle;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.LineGraphView;

public class CreaGrafoData {

	public void CreaGrafoda(Graficas longOperationContext, List<Eventi> lista,
			List<String> stri, EditText te, int mYear, int mMonth, int mDay) {

		// DATA= DATA IN FORMATO 25-12-2012

		String data = Graficas.pad(mMonth + 1) + "-" + Graficas.pad(mDay) + "-"
				+ mYear;
		// Toast.makeText(longOperationContext, data,
		// Toast.LENGTH_SHORT).show();

		LinearLayout layout = (LinearLayout) longOperationContext
				.findViewById(R.id.graph1);

		// PULISCI ZONA GRAFICI
		layout.removeAllViewsInLayout();

		// LISTA DI ARRAY CON I PUNTI (X,Y) -> È NECESSARIO PERCHÈ CI SONO +
		// GRAFICI
		List<GraphViewData[]> tutti = new ArrayList<GraphViewData[]>();

		List<GraphViewSeries> serie = new ArrayList<GraphViewSeries>();

		// ARRAY CON GLI INT DI 8 COLORI (1 PER OGNI EVENTO). SE EVENTI>8 ->
		// EVENTI%8
		int colori[] = { -65281, -16711936, -3355444, -65536, -1, -256, -7829368, -12303292 };
		
		
		GraphView graphView = null;
		int contr = 0;
		int contra = 0;

		// CONTROLLO SE CI SONO EVENTI CON QUESTA DATA, SE NO ESCO
		for (int j = 0; j < lista.size(); j++) {

			if (data.equals(lista.get(j).getData())) {
				contra = 1;
				break;
			}

		}

		// SE CI SONO EVENTI..
		if (contra == 1) {

			// FOR PER OGNI TIPO DI EVENTO IN STRI
			for (int x = 0; x < stri.size(); x++) {

				// AGGIUNGE ALLA LISTA (IN POSIZIONE X) L'ARRAY CON I PUNTI
				// (X,Y)
				tutti.add(x, new GraphViewData[24 * 60]);

				// LO INIZIALIZZA A 0
				for (int k = 0; k < 24 * 60; k++)
					// ho messo 24 cosi sull'asse delle y si va da 24 a circa
					// 33-34, e non come prima da 0 a 33
					tutti.get(x)[k] = new GraphViewData(k, 28);

				// PER OGNI EVENTO, SE DATA=DATAEVENTO E NOME=NOMEVENTO..
				for (int j = 0; j < lista.size(); j++) {

					if (data.equals(lista.get(j).getData())
							&& lista.get(j).getNome().equals(stri.get(x))) {

						// DATA NEL FORMATO MINUTI
						contr = 1;
						int inizio = 60
								* Integer.parseInt(""
										+ lista.get(j).getInizio().charAt(0)
										+ lista.get(j).getInizio().charAt(1))
								+ Integer.parseInt(""
										+ lista.get(j).getInizio().charAt(3)
										+ lista.get(j).getInizio().charAt(4));
						int fine = 60
								* Integer.parseInt(""
										+ lista.get(j).getFine().charAt(0)
										+ lista.get(j).getFine().charAt(1))
								+ Integer.parseInt(""
										+ lista.get(j).getFine().charAt(3)
										+ lista.get(j).getFine().charAt(4));

						// RIEMPIE ARRAY (INC I VALORI DELL'INTERVALLO)
						if (inizio < fine) {
							for (int i = inizio; i <= fine; i++)
								tutti.get(x)[i] = new GraphViewData(i, 37);
						} else if (inizio == fine)
							tutti.get(x)[inizio] = new GraphViewData(inizio, 37);

						// GESTIONE INIZIO>FINE
						// ES. EVENTO FIESTA INIZIO 22.30 FIN 05.00
						else {
							for (int i = inizio; i < (24 * 60); i++)
								tutti.get(x)[i] = new GraphViewData(i, 37);
							for (int i = 0; i <= fine; i++)
								tutti.get(x)[i] = new GraphViewData(i, 37);

						}

					}

				}

				// CONTROLLO XKE SENNO DISEGNAVA TUTTI GLI EVENTI MA A 0, PERÒ
				// SI VEDEVANO NELLA LEGENDA

				if (contr == 1)
					serie.add(new GraphViewSeries(stri.get(x),
							new GraphViewStyle(colori[x % 8], 3), tutti.get(x)));

				contr = 0;

				// GESTIONE ASSE X CON ORE 08:00 E NON 480
				graphView = new LineGraphView(longOperationContext,
						longOperationContext.getString(R.string.data2) + " "
								+ data) {
					@Override
					protected String formatLabel(double value, boolean isValueX) {
						if (isValueX)
							return Graficas.returnnull2(value);

						else
							return super.formatLabel(value, isValueX); // let
																		// the
																		// y-value
																		// be
																		// normal-formatted
					}
				};

			}

			// CREO L'ULTIMO GRAFICO DELLE TEMPERATURE (DAL FILE TXT) E LO
			// AGGIUNGO NELLA SERIE IN ULTIMA POSIZIONE
			int conte = 0;

			List<Double> ejey = new ArrayList<Double>();
			List<Integer> ejex = new ArrayList<Integer>();

			try {
				File ruta_sd = Environment.getExternalStorageDirectory();

				File f = new File(ruta_sd.getAbsolutePath(),
						"temperatura_solo_datos.txt");

				BufferedReader fin = new BufferedReader(new InputStreamReader(
						new FileInputStream(f)));

				while (true) {
					String due = fin.readLine();
					if (due == null)
						break;
					else {

						String datafile = "" + due.charAt(1) + due.charAt(2)
								+ "-" + due.charAt(4) + due.charAt(5) + "-"
								+ due.charAt(7) + due.charAt(8) + due.charAt(9)
								+ due.charAt(10);

						if (datafile.equals(data)) {

							conte++;
							int ora = 60
									* Integer.parseInt("" + due.charAt(13)
											+ due.charAt(14))
									+ Integer.parseInt("" + due.charAt(16)
											+ due.charAt(17));
							ejex.add(ora);
							double temp = Double.parseDouble(""
									+ due.charAt(21) + due.charAt(22) + "."
									+ due.charAt(24));
							ejey.add(temp);

						}
					}

				}

				fin.close();

			} catch (Exception ex) {
				Toast.makeText(longOperationContext,
						longOperationContext.getString(R.string.nosd),
						Toast.LENGTH_LONG).show();
				conte--;
			}

			if (conte > 0) {
				// QUI INSERISCO I PUNTI DEL GRAFICO TEMPERATURE
				GraphViewData[] temp = new GraphViewData[conte];
				GraphViewData[] mediamovil2 = new GraphViewData[conte];

				// CALCOLO MEDIAMOVIL
				List<Double> ejey3 = new ArrayList<Double>();
				int x1 = Integer.parseInt(te.getText().toString());

				Mediamovil med = new Mediamovil();
				ejey3 = med.returnMediamovil(ejey, x1);

				// QUI INSERISCO I PUNTI DEL GRAFICO TEMPERATURE
				for (int k = 0; k < conte; k++)
					temp[k] = new GraphViewData(ejex.get(k), ejey.get(k));

				for (int k = 0; k < conte; k++)
					mediamovil2[k] = new GraphViewData(ejex.get(k),
							ejey3.get(k));

				serie.add(new GraphViewSeries(longOperationContext
						.getString(R.string.temp), new GraphViewStyle(
						Color.BLUE, 3), temp));
				serie.add(new GraphViewSeries(longOperationContext
						.getString(R.string.tempmm), new GraphViewStyle(
						Color.CYAN, 3), mediamovil2));

			} else if (conte == 0) {
				Toast.makeText(longOperationContext,
						longOperationContext.getString(R.string.notemp),
						Toast.LENGTH_LONG).show();

			}

			// ADD LE SERIE AL GRAFICO SERIE

			for (int i = 0; i < serie.size(); i++)
				graphView.addSeries(serie.get(i));

			// NON VA LO SFONDO, BUG NELLA LIBRERIA: VA SOLO SE VALORI <=145
			// ((LineGraphView) graphView).setDrawBackground(true);

			// PER CANCELLARE LA LINEA DI SOTTO, SOLO CHE AGGIUNGE UN ELEMENTO
			// ALLA LEGENDA!!
			/*
			 * GraphViewData[] zero = new GraphViewData[24*60]; for (int
			 * i=0;i<24*60;i++) zero[i]=new GraphViewData(i, 0);
			 * 
			 * graphView.addSeries(new GraphViewSeries(null, new
			 * GraphViewStyle(Color.BLACK, 3),zero));
			 */

			// SENZA ASSE DELLE Y
			// graphView.setVerticalLabels(new String[] {""});
			graphView.setShowLegend(true);
			graphView.setLegendAlign(LegendAlign.BOTTOM);
			graphView.setLegendWidth(170);
			final CheckBox c2 = (CheckBox) longOperationContext
					.findViewById(R.id.check1);
			if (c2.isChecked() == true) {
				graphView.setScrollable(true);
				graphView.setViewPort(0, 360);
			}
			/*
			 * Scrolling e zooming vanno lentissimi!!!!!!!!!!
			 * graphView.setScrollable(true); graphView.setViewPort(0, 720);
			 * graphView.setScalable(true);
			 */

			// graphView.setViewPort(0, 360);//VALORI SULLE X FISSI (ESSENDO
			// STRING, SONO A DISTANZA FISSA)
			// graphView.setHorizontalLabels(new String[]
			// {"00:00","01:00","02:00","03:00","04:00", "05.00",
			// "06:00","07:00","08:00","09:00","10.00","11:00","12:00","13:00","14:00","15.00","16.00","17.00","18.00","19.00","20.00","21.00","22.00","23.00","24.00",});
			layout.addView(graphView);

		}

		// SE IN QUESTA DATA NON CI SONO EVENTI DISEGNA UN GRAFICO VUOTO E
		// STAMPA UN TOAST
		else if (contra == 0) {

			graphView = new LineGraphView(longOperationContext,
					longOperationContext.getString(R.string.data2) + " " + data);
			graphView.setVerticalLabels(new String[] { "" });
			graphView.setHorizontalLabels(new String[] { "00:00", "12:00",
					"24:00" });
			layout.removeAllViewsInLayout();
			layout.addView(graphView);

			Toast.makeText(
					longOperationContext,
					longOperationContext.getString(R.string.noevfe) + " "
							+ data, Toast.LENGTH_SHORT).show();
		}

	}
}
