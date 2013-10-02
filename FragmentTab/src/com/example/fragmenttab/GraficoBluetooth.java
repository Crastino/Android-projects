package com.example.fragmenttab;

import java.util.ArrayList;
import java.util.List;

import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.graphics.Color;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries.GraphViewStyle;
import com.jjoe64.graphview.GraphViewSeries;

public class GraficoBluetooth {
	
	Graficas longOperationContext;
	
	public GraficoBluetooth() {}

	final int EVENT_NUMBER = 9;
	int inizio = 0, fine = 0, counter = 0, number = 0;
	boolean start = false, end = false, done = false;
	
	String eventTipe[] = new String[] {"Wake up", "Breakfast", "Lunch", "Sport", "Dinner", "Rest", 
			"Go to bed", "Sleep"};
	
	public void creagrafico(Graficas longOperationContext, ArrayList<String> date, ArrayList<String> hour, 
							ArrayList<String> event, ArrayList<String> extTemp, ArrayList<String> bodyTemp,
							ArrayList<String> light, boolean bevent, boolean btemp, boolean blight ) {
		
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
		if(bevent == true) {
			// FOR PER OGNI TIPO DI EVENTO
			for (int x = 0; x < EVENT_NUMBER; x++) {

				// AGGIUNGE ALLA LISTA (IN POSIZIONE X) L'ARRAY CON I PUNTI
				// (X,Y)
				tutti.add(x, new GraphViewData[24 * 60]);
				
				// LO INIZIALIZZA AL VALORE MINORE
				for (int k = 0; k < 24 * 60; k++)
					tutti.get(x)[k] = new GraphViewData(k, 0);
				//cicla tutta la lista con gli eventi e se è uguale all'evento x entra nell'if e se non è in riposo
				for (int j = 0; j < (event.size()-1); j++) {					
					if(x == StringToInt(event.get(j)) && x != 5) {
						if(start == false) {
							inizio = 60
									* Integer.parseInt(""
											+ hour.get(j).charAt(0)
											+ hour.get(j).charAt(1))
									+ Integer.parseInt(""
											+ hour.get(j).charAt(3)
											+ hour.get(j).charAt(4));
							start = true;
						}	
				//entra se l'evento dopo è diverso e devo quindi salvare l'ora di fine		
						if(StringToInt(event.get(j+1)) != StringToInt(event.get(j)) && start == true) {
							fine = 60
									* Integer.parseInt(""
											+ hour.get(j).charAt(0)
											+ hour.get(j).charAt(1))
									+ Integer.parseInt(""
											+ hour.get(j).charAt(3)
											+ hour.get(j).charAt(4));
							end = true;
							start = false;
							
						}
						//serve se l'ultimo evento è = al penultimo, se l'ultimo evento è singolo non funziona 
						//e non viene visualizzato. Da migliorare
						if(j == (event.size()-2) && end == false) {
							fine = 60
									* Integer.parseInt(""
											+ hour.get(j).charAt(0)
											+ hour.get(j).charAt(1))
									+ Integer.parseInt(""
											+ hour.get(j).charAt(3)
											+ hour.get(j).charAt(4));
							end = true;
							
						}
						if(end == true) {
							// RIEMPIE ARRAY (INC I VALORI DELL'INTERVALLO)
							if (inizio < fine) {
								for (int i = inizio; i <= fine; i++)
									tutti.get(x)[i] = new GraphViewData(i, 35);
							} else if (inizio == fine)
								tutti.get(x)[inizio] = new GraphViewData(inizio, 35);
						 
							end = false;
							done = true;
						}						
					}			
				}	
					if(done == true) {
					serie.add(new GraphViewSeries(" " + x + " " + eventTipe[x],
							new GraphViewStyle(colori[x % 8], 3), tutti.get(x)));
					done = false;
					}

			}	
	}
			// GESTIONE ASSE X CON ORE 08:00 E NON 480
				graphView = new LineGraphView(longOperationContext,
						longOperationContext.getString(R.string.data2) + " "
								+ date.get(0)) {
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

//  ============================================================================	
//	INIZIO CALCOLO TEMPERATURE
//=============================================================================  
	final int LISTLENGTH = hour.size();
			
	if(btemp == true) {
		String ext;
		String body;
		Double dext = 0.0, dbody = 0.0;
		
		// CREA ARRAY DI DATI (OGNI GraphViewData È UN PUNTO (X,Y)
		GraphViewData[] grafi3 = new GraphViewData[LISTLENGTH];
		GraphViewData[] grafi2 = new GraphViewData[LISTLENGTH];
		
		// ciclo per tutta la lunghezza della lista e trasformo le strighe in double
		for(int i=0; i < LISTLENGTH; i++) {
			if(extTemp.isEmpty()) {
				
				Toast.makeText(longOperationContext,
						longOperationContext.getString(R.string.notemp),
						Toast.LENGTH_LONG).show();
	
			}
			else {
				ext = extTemp.get(i);
				body = bodyTemp.get(i);
				dext = StringToDouble(ext);
				dbody = StringToDouble(body);
				int hourtemp = 60
						* Integer.parseInt(""
								+ hour.get(i).charAt(0)
								+ hour.get(i).charAt(1))
						+ Integer.parseInt(""
								+ hour.get(i).charAt(3)
								+ hour.get(i).charAt(4));
				grafi3[i] = new GraphViewData(hourtemp, dext);
				grafi2[i] = new GraphViewData(hourtemp, dbody);			
			}
		}
		
		GraphViewSeries exttemp = new GraphViewSeries("Exterior Temperature", new GraphViewStyle(Color.BLUE, 3),grafi3);
		GraphViewSeries bodytemp = new GraphViewSeries("Body Temperature", new GraphViewStyle(Color.CYAN, 3),grafi2);
		graphView.addSeries(exttemp); 
		graphView.addSeries(bodytemp);
	}
//  ============================================================================	
//	INIZIO CALCOLO SENSORE LUCE
//=============================================================================
    if(blight == true) {
		String sLight;
		int iLight = 0; 
		double lightlog = 0.0, lightfinal = 0.0;
		
		// CREA ARRAY DI DATI (OGNI GraphViewData È UN PUNTO (X,Y)
		GraphViewData[] grafilight = new GraphViewData[LISTLENGTH];
		
		// ciclo per tutta la lunghezza della lista e trasformo le strighe in double
		for(int i=0; i < LISTLENGTH; i++) {
			if(light.isEmpty()) {
				
				Toast.makeText(longOperationContext,
						longOperationContext.getString(R.string.notemp),
						Toast.LENGTH_LONG).show();
	
			}
			else {
				sLight = light.get(i);			
				iLight = StringToIntLight(sLight);			
				lightlog = Math.log10(iLight);
				lightfinal = lightlog * 7;
				int hourtemp = 60
						* Integer.parseInt(""
								+ hour.get(i).charAt(0)
								+ hour.get(i).charAt(1))
						+ Integer.parseInt(""
								+ hour.get(i).charAt(3)
								+ hour.get(i).charAt(4));
				grafilight[i] = new GraphViewData(hourtemp, lightfinal);			
			}
		}
		
		GraphViewSeries lightsensor = new GraphViewSeries("Illuminance (lux)", new GraphViewStyle(Color.YELLOW, 3),grafilight);
		graphView.addSeries(lightsensor);
	}
    
	// ADD I DATI AL GRAFICO	
	for (int i = 0; i < serie.size(); i++)
		graphView.addSeries(serie.get(i));
//	((LineGraphView) graphView).setDrawBackground(true);
	graphView.setShowLegend(true);
	
	//checkbox scrollable
	final CheckBox c2 = (CheckBox) longOperationContext
			.findViewById(R.id.check1);
	if (c2.isChecked() == true) {
		graphView.setScrollable(true);
		graphView.setViewPort(0, 360);
	}
	//checkbox scalable
	final CheckBox c3 = (CheckBox) longOperationContext
			.findViewById(R.id.check2);
	if (c3.isChecked() == true) {
		graphView.setScalable(true);
	}
	
	graphView.setVerticalLabels(new String[] {"35°/5.5loglux", "28°/4.4loglux", "21°/3.3loglux","14°/2.2loglux",
												"7°/1.1loglux","0°/0loglux"});
	// STAMPA GRAFICO
	layout.addView(graphView);	 
}
		
	// Funzione per trasformare una stringa in double; usato per le temperature
	double StringToDouble(String s) {
		
		double result = Double.parseDouble(""
				+ s.charAt(0) + s.charAt(1) + "."
				+ s.charAt(3));

		return result;
		
	}
	int StringToInt(String s) {
		
		int result = 0;
		
		result = Integer.parseInt(""+s.charAt(0));
		
		return result;
	}
	// Trasforma la stringa relativa ai lux in intero, va da 0 a 100000
	int StringToIntLight(String s) {
		
		char semicolon = ';';
		int result = 0;
		if(s.charAt(1) == semicolon) {
			result = Integer.parseInt(""+s.charAt(0));			
		}
		else if(s.charAt(2) == semicolon) {
			result = Integer.parseInt(""+s.charAt(0) + s.charAt(1));			
		}
		else if(s.charAt(3) == semicolon) {
			result = Integer.parseInt(""+s.charAt(0) + s.charAt(1) + s.charAt(2));
		}
		else if(s.charAt(4) == semicolon) {
			result = Integer.parseInt(""+s.charAt(0) + s.charAt(1) + s.charAt(2)+ s.charAt(3));
		}
		else if(s.charAt(5) == semicolon){
			result = Integer.parseInt(""+s.charAt(0) + s.charAt(1) + s.charAt(2)+ s.charAt(3)
					+ s.charAt(4) + s.charAt(5));
		}
		else {
			result = Integer.parseInt(""+s.charAt(0) + s.charAt(1) + s.charAt(2)+ s.charAt(3)
					+ s.charAt(4) + s.charAt(5) + s.charAt(6));
		}
		return result;
	}
}
