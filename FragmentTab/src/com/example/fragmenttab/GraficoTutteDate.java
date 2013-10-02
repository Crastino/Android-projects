package com.example.fragmenttab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewStyle;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.LineGraphView;

public class GraficoTutteDate extends AsyncTask<String, String, String> {
	List<String> stri;
	List<Eventi> lista;
	Graficas longOperationContext;
	LineGraphView graphView;
	EditText te;
	private ProgressDialog pd;
	boolean cont=true;

	public GraficoTutteDate(Graficas context, List<Eventi> listas,
			List<String> strin, EditText tes) {

		stri = strin;
		lista = listas;
		longOperationContext = context;
		te = tes;

	}

	@Override
	protected String doInBackground(String... params) {

		// //////////////////////////////////////////////////////////////////////////////////////
		// CREAZIONE GRAFICO CON TUTTE LE TEMPERATURE DEL FILE
		// //////////////////////////////////////////////////////////////////////////////////////
		List<Double> ejey1 = new ArrayList<Double>();
		List<Integer> ejex1 = new ArrayList<Integer>();
		List<Integer> supp = new ArrayList<Integer>();
		List<String> date = new ArrayList<String>();

		List<GraphViewSeries> serie = new ArrayList<GraphViewSeries>();

		int colori[] = { -65281, -16711936, -3355444, -65536, -1, -256, -7829368, -12303292 };

		int dimetem = 0;
		String pre = "";

		// analizzo il file: prendo le date e le salvo in date (senza doppioni)
		// e riempio le liste x
		// i grafici sulla temperatura
		try {
			File ruta_sd = Environment.getExternalStorageDirectory();

			File f = new File(ruta_sd.getAbsolutePath(),
					"temperatura_solo_datos.txt");

			BufferedReader fin = new BufferedReader(new InputStreamReader(
					new FileInputStream(f)));
			int dia = 0;
			while (true) {
				String due = fin.readLine();
				if (due == null)
					break;
				else {

					String datafile = "" + due.charAt(1) + due.charAt(2) + "-"
							+ due.charAt(4) + due.charAt(5) + "-"
							+ due.charAt(7) + due.charAt(8) + due.charAt(9)
							+ due.charAt(10);

					if (dimetem == 0)
						date.add(datafile);
					else if (!pre.equals(datafile) && dimetem != 0) {
						date.add(datafile);
						dia += 24 * 60;
					}

					int ora = dia
							+ (60 * Integer.parseInt("" + due.charAt(13)
									+ due.charAt(14)) + Integer.parseInt(""
									+ due.charAt(16) + due.charAt(17)));
					ejex1.add(ora);
					double temp = Double.parseDouble("" + due.charAt(21)
							+ due.charAt(22) + "." + due.charAt(24));
					ejey1.add(temp);

					pre = datafile;
					dimetem++;

				}

			}

			fin.close();
		}

		catch (Exception ex) {

			/*Toast.makeText(longOperationContext,
					longOperationContext.getString(R.string.nosd),
					Toast.LENGTH_LONG).show();*/
			cont=false;

		}

		// ///INIZIO CREAZIONE GRAFICI EVENTI
		if(cont){
		for (int x = 0; x < stri.size(); x++) {

			publishProgress(longOperationContext.getString(R.string.caltip)
					+ stri.get(x).toString() + "\"....");

			int giorno = 0;
			int contr = 0;

			supp.clear();

			for (int i = 0; i < (24 * 60) * (date.size()); i++)
				supp.add(i, 26);

			for (int q = 0; q < date.size(); q++) {

				String datafile = date.get(q);

				if (q > 0)
					giorno += 60 * 24;

				for (int j = 0; j < lista.size(); j++) {

					if (datafile.equals(lista.get(j).getData())
							&& lista.get(j).getNome().equals(stri.get(x))) {

						// DATA NEL FORMATO MINUTI
						contr = 1;
						int inizio = giorno
								+ (60 * Integer.parseInt(""
										+ lista.get(j).getInizio().charAt(0)
										+ lista.get(j).getInizio().charAt(1)) + Integer
											.parseInt(""
													+ lista.get(j).getInizio()
															.charAt(3)
													+ lista.get(j).getInizio()
															.charAt(4)));
						int fine = giorno
								+ (60 * Integer.parseInt(""
										+ lista.get(j).getFine().charAt(0)
										+ lista.get(j).getFine().charAt(1)) + Integer
											.parseInt(""
													+ lista.get(j).getFine()
															.charAt(3)
													+ lista.get(j).getFine()
															.charAt(4)));

						for (int i = inizio; i < fine; i++)
							supp.add(i, 40);

					}
				}

			}

			if (contr != 0) {

				GraphViewData[] dati = new GraphViewData[supp.size()];
				for (int i = 0; i < dati.length; i++)
					dati[i] = new GraphViewData(i, supp.get(i));

				serie.add(new GraphViewSeries(stri.get(x), new GraphViewStyle(
						colori[x % 8], 3), dati));

			}

		}

		// ///FINE CREAZIONE GRAFICI EVENTI

		// SE CI SONO DATI...
		if (!date.isEmpty()) {

			final CheckBox c1 = (CheckBox) longOperationContext
					.findViewById(R.id.check1);
			if (c1.isChecked() == true)
				publishProgress(longOperationContext.getString(R.string.dise));
			else
				publishProgress(longOperationContext.getString(R.string.disescro));

			dimetem--;
			GraphViewData[] memo = new GraphViewData[dimetem];
			GraphViewData[] memomovil = new GraphViewData[dimetem];

			// CALCOLO MEDIA MOVIL!!!!!!!!!
			List<Double> ejey2 = new ArrayList<Double>();
			int x1 = Integer.parseInt(te.getText().toString());

			Mediamovil med = new Mediamovil();
			ejey2 = med.returnMediamovil(ejey1, x1);

			// QUI INSERISCO I PUNTI DEL GRAFICO TEMPERATURE
			for (int k = 0; k < dimetem; k++)
				memo[k] = new GraphViewData(ejex1.get(k), ejey1.get(k));

			for (int k = 0; k < dimetem; k++)
				memomovil[k] = new GraphViewData(ejex1.get(k), ejey2.get(k));

			// GESTIONE ASSE DELLE X CON ORARI 04:30 INVECE CHE NUMERI
			// 270

			graphView = new LineGraphView(longOperationContext,
					longOperationContext.getString(R.string.tempda) + " "
							+ date.get(0) + " "
							+ longOperationContext.getString(R.string.tempal)
							+ " " + date.get(date.size() - 1)) {
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

			GraphViewSeries exampleSeries1 = new GraphViewSeries(
					longOperationContext.getString(R.string.temp),
					new GraphViewStyle(Color.BLUE, 3), memo);
			GraphViewSeries exampleSeries2 = new GraphViewSeries(
					longOperationContext.getString(R.string.tempmm),
					new GraphViewStyle(Color.CYAN, 3), memomovil);

			graphView.addSeries(exampleSeries1);
			graphView.addSeries(exampleSeries2);

			for (int i = 0; i < serie.size(); i++)
				graphView.addSeries(serie.get(i));

			if (c1.isChecked() == true) {
				graphView.setScrollable(true);
				graphView.setViewPort(0, 360);
			}

			graphView.setShowLegend(true);
			graphView.setLegendAlign(LegendAlign.BOTTOM);
			graphView.setLegendWidth(170);

			// VALORI SULLE X FISSI (ESSENDO STRING, SONO A DISTANZA
			// FISSA)
			// graphView.setHorizontalLabels(new String[]
			// {"00:00","01:00","02:00","03:00","04:00", "05.00",
			// "06:00","07:00","08:00","09:00","10.00","11:00","12:00","13:00","14:00","15.00","16.00","17.00","18.00","19.00","20.00","21.00","22.00","23.00","24.00",});

			// return graphView;
			// layout.addView(graphView);

		} else {
			Toast.makeText(longOperationContext,
					longOperationContext.getString(R.string.notemp),
					Toast.LENGTH_LONG).show();

		}
		}
		return null;

	}

	@Override
	protected void onProgressUpdate(String... values) {
		// aggiorno la progress dialog
		pd.setMessage(values[0]);
	}

	@Override
	protected void onPreExecute() {
		pd = ProgressDialog.show(longOperationContext, longOperationContext.getString(R.string.cregra),
				longOperationContext.getString(R.string.caltem), true, false);

	}

	@Override
	protected void onPostExecute(String result) {
		// chiudo la progress dialog
		if(cont){
		LinearLayout layout = (LinearLayout) longOperationContext
				.findViewById(R.id.graph1);

		// PULISCI ZONA GRAFICI
		layout.removeAllViewsInLayout();

		layout.addView(graphView);
		}
		else
			Toast.makeText(longOperationContext,
			longOperationContext.getString(R.string.nosd),
			Toast.LENGTH_LONG).show();
		try {
			pd.dismiss();
		} catch (Exception e) {
		}

	}
}
