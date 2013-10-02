package com.example.fragmenttab;

import java.util.List;

import android.widget.LinearLayout;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;

public class CreaGrafoEvento {
	
	List<String> stri;
	List<Eventi> lista;
	Graficas longOperationContext;
	
	
	public CreaGrafoEvento(){
		
		
	}
	
	public void CreaGrafoev(Graficas longOperationContext, List<Eventi> lista, List<String> stri,String selez){
	
		int ejex[]=new int[24 * 60];
		LinearLayout layout = (LinearLayout) longOperationContext.findViewById(R.id.graph1);

	for (int i = 0; i < ejex.length; i++)
		ejex[i] = 0;

	// PULISCI GRAFICO
	layout.removeAllViewsInLayout();

	// SELEZIONA SOLO GLI EVENTI CON NOME= TIPO SELEZIONATO
	for (int j = 0; j < lista.size(); j++) {
		if (selez.equals(lista.get(j).getNome())) {

			// TRADUCE ORA DI INIZIO E FINE IN NUMEROMINUTI (ES 01:30 = 90)
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

			// INCREMENTA EJEX NELL'INTERVALLO DI TEMPO FINE-INIZIO
			if (inizio < fine) {
				for (int i = inizio; i <= fine; i++)
					ejex[i]++;
			} else if (inizio == fine)
				ejex[inizio]++;

			// GESTIONE INIZIO>FINE
			// ES. EVENTO FIESTA INIZIO 22.30 FIN 05.00
			else {
				for (int i = inizio; i < (24 * 60); i++)
					ejex[i]++;
				for (int i = inizio = 0; i <= fine; i++)
					ejex[i]++;

			}

		}

	}

	// CREA ARRAY DI DATI (OGNI GraphViewData È UN PUNTO (X,Y)
	GraphViewData[] grafi = new GraphViewData[24 * 60];

	// CREA OGGETTI DELL'ARRAY
	for (int i = 0; i < 24 * 60; i++) {
		grafi[i] = new GraphViewData(i, ejex[i]);
	}

	GraphViewSeries exampleSeries = new GraphViewSeries(grafi);

	// GESTIONE ASSE DELLE X CON ORARI 04:30 INVECE CHE NUMERI 270
	BarGraphView graphView = new BarGraphView(longOperationContext,
			longOperationContext.getString(R.string.tipo2) + " " + selez) {
		@Override
		protected String formatLabel(double value, boolean isValueX) {
			if (isValueX){
				
					return Graficas.returnnull2(value);}

			else
				return super.formatLabel(value, isValueX); // let the
															// y-value be
															// normal-formatted
		}
	};

	// graphView.setScrollable(true);
	// graphView.setViewPort(0, 360);
	// VALORI SULLE X FISSI (ESSENDO STRING, SONO A DISTANZA FISSA)
	// graphView.setHorizontalLabels(new String[]
	// {"00:00","01:00","02:00","03:00","04:00", "05.00",
	// "06:00","07:00","08:00","09:00","10.00","11:00","12:00","13:00","14:00","15.00","16.00","17.00","18.00","19.00","20.00","21.00","22.00","23.00","24.00",});

	// ADD I DATI AL GRAFICO
	graphView.addSeries(exampleSeries); // data

	// optional - activate scaling / zooming
	// graphView.setScalable(true);
	// STAMPA GRAFICO
	layout.addView(graphView);

}
}
