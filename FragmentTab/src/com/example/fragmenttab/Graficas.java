package com.example.fragmenttab;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class Graficas extends Activity {

	Spinner s;
	List<String> stri;
	int aselez = 0;
	String selez = "";
	List<Eventi> lista;
	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID = 0;
	static final int CONFIRM_DIALOG = 1;
	ProgressDialog progressDialog;
	EditText te;

	boolean bevent = false, btemp = false, blight = false;
	
	
	ArrayList<String> date = new ArrayList<String>();
	ArrayList<String> hour = new ArrayList<String>();
	ArrayList<String> event = new ArrayList<String>();
	ArrayList<String> extTemp = new ArrayList<String>();
	ArrayList<String> bodyTemp = new ArrayList<String>();
	ArrayList<String> light = new ArrayList<String>();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graficas);

		riempiStri();

		
		//bottone grafico evento
		Button jBtnCrea = (Button) this.findViewById(R.id.button1);

		jBtnCrea.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!stri.isEmpty())
					showDialog(CONFIRM_DIALOG);
				else
					Toast.makeText(getApplicationContext(),
							getString(R.string.noev), Toast.LENGTH_LONG).show();
				//
			}
		});
	
		final CheckBox checkBox = (CheckBox) findViewById(R.id.check_panel);
		
		checkBox.setOnClickListener(new View.OnClickListener() {		 
			  @Override
			  public void onClick(View v) {
		        if (checkBox.isChecked()) {
		        	 LinearLayout rl1 = (LinearLayout) findViewById(R.id.buttons1);
			            rl1.setVisibility(View.GONE);
			            LinearLayout rl3 = (LinearLayout) findViewById(R.id.buttons3);
			            rl3.setVisibility(View.GONE);
		        }
		       else {
		       	 LinearLayout rl1 = (LinearLayout) findViewById(R.id.buttons1);
			            rl1.setVisibility(View.VISIBLE);
			            LinearLayout rl3 = (LinearLayout) findViewById(R.id.buttons3);
			            rl3.setVisibility(View.VISIBLE);
		       }
		        
			  }
		});

		//GESTIONE DEI TRE CHECKBOX PER DISEGNARE DATI BLUETOOTH
		
		final CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkevent);
		
		checkBox1.setOnClickListener(new View.OnClickListener() {		 
			  @Override
			  public void onClick(View v) {
		        if (checkBox1.isChecked()) {
		        	bevent = true;
		        }
		        else {
		        	bevent = false;
		        }
			  }
		});
		
		final CheckBox checkBox2 = (CheckBox) findViewById(R.id.checktemp);
		
		checkBox2.setOnClickListener(new View.OnClickListener() {		 
			  @Override
			  public void onClick(View v) {
		        if (checkBox2.isChecked()) {
		        	btemp = true;
		        }
		        else {
		        	btemp = false;
		        }
			  }
		});
		
		final CheckBox checkBox3 = (CheckBox) findViewById(R.id.checklight);
		
		checkBox3.setOnClickListener(new View.OnClickListener() {		 
			  @Override
			  public void onClick(View v) {
		        if (checkBox3.isChecked()) {
		        	blight = true;
		        }
		        else {
		        	blight = false;
		        }
			  }
		});
		
		// Bottone grafico bluetooth 
				Button btdata = (Button) this.findViewById(R.id.bluetoothdata);

				btdata.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						
						GraficoBluetooth gb = new GraficoBluetooth();
						Intent ricevidati=getIntent();
						date=(ArrayList<String>) ricevidati.getSerializableExtra("date");
						hour=(ArrayList<String>) ricevidati.getSerializableExtra("time");
						event=(ArrayList<String>) ricevidati.getSerializableExtra("event");	
						extTemp=(ArrayList<String>) ricevidati.getSerializableExtra("exttemp");
						bodyTemp=(ArrayList<String>) ricevidati.getSerializableExtra("bodytemp");	
						light=(ArrayList<String>) ricevidati.getSerializableExtra("light");	
						gb.creagrafico(Graficas.this, date, hour, event, extTemp, bodyTemp, light,
													  bevent, btemp, blight );
						
					}
				});
				// Bottone statistiche dati ricevuti
				Button stats = (Button) this.findViewById(R.id.statistics);

				stats.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						
						Intent ricevidati=getIntent();
						date=(ArrayList<String>) ricevidati.getSerializableExtra("date");						
						hour=(ArrayList<String>) ricevidati.getSerializableExtra("time");
						event=(ArrayList<String>) ricevidati.getSerializableExtra("event");
						extTemp=(ArrayList<String>) ricevidati.getSerializableExtra("exttemp");
						bodyTemp=(ArrayList<String>) ricevidati.getSerializableExtra("bodytemp");	
						light=(ArrayList<String>) ricevidati.getSerializableExtra("light");
						
						Intent inviadati = new Intent(getApplicationContext(), BluetoothStatistics.class);
		            	inviadati.putExtra("date", (ArrayList<String>)date);
		            	inviadati.putExtra("time", (ArrayList<String>)hour);
		            	inviadati.putExtra("event", (ArrayList<String>)event);
		            	inviadati.putExtra("exttemp", (ArrayList<String>)extTemp);
		            	inviadati.putExtra("bodytemp", (ArrayList<String>)bodyTemp);
		            	inviadati.putExtra("light", (ArrayList<String>)light);
		                startActivity(inviadati);
					}
				});
				
		// GESTIONE BOTTONI + e - E EDITVIEW DEL NUMBERPICKER

		te = (EditText) this.findViewById(R.id.edit_text);
		Button piu = (Button) this.findViewById(R.id.btn_plus);
		Button meno = (Button) this.findViewById(R.id.btn_minus);
		piu.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				int x = Integer.parseInt(te.getText().toString());
				x += 2;
				te.setText("" + x);

			}
		});
		meno.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				int x = Integer.parseInt(te.getText().toString());
				x -= 2;
				if (x > 2)
					te.setText(String.valueOf(x));
				else
					te.setText("3");

			}
		});

		//bottone grafico per data
		Button jBtnCrea2 = (Button) this.findViewById(R.id.button2);
		jBtnCrea2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (!lista.isEmpty()) {

					showDialog(DATE_DIALOG_ID);
				} else
					Toast.makeText(getApplicationContext(),
							getString(R.string.noev), Toast.LENGTH_LONG).show();
			}
		});

		// get the current date
		final Calendar c = Calendar.getInstance();

		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		//bottone grafico tuttedate e eventi
		Button jBtnCrea3 = (Button) this.findViewById(R.id.button3);
		jBtnCrea3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// creo e avvio asynctask
				GraficoTutteDate task = new GraficoTutteDate(Graficas.this,
						lista, stri, te);
				task.execute();

			}
		});
	}


	

	

	// GESTIONE FORM X IMPOSTARE DATA
	private DatePickerDialog.OnDateSetListener mDateSetListener3 = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view3, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			CreaGrafoData c = new CreaGrafoData();
			c.CreaGrafoda(Graficas.this, lista, stri, te, mYear, mMonth, mDay);

		}
	};

	protected Dialog onCreateDialog(int id) {

		switch (id) {

		case DATE_DIALOG_ID: {

			DatePickerDialog b = new DatePickerDialog(this, mDateSetListener3,
					mYear, mMonth, mDay);

			// progressDialog.dismiss();
			return b;

		}
		// SELEZIONE TIPO PER CREAZIONE GRAFO EVENTO
		case CONFIRM_DIALOG:

			CharSequence[] items = stri.toArray(new String[stri.size()]);
			// items = list.toArray(new String[list.size()]);

			AlertDialog.Builder ab = new AlertDialog.Builder(this);
			ab.setTitle(getString(R.string.tipo));
			ab.setSingleChoiceItems(items, 0,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// onClick Action

							aselez = whichButton;
						}
					})
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									selez = stri.get(aselez);
									CreaGrafoEvento c = new CreaGrafoEvento();
									c.CreaGrafoev(Graficas.this, lista, stri,
											selez);

								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// on cancel button action

								}
							});
			ab.show();

			break;

		}

		return null;

	}

	public static String returnnull2(double value) {

		double numero, decimal, entero;

		numero = value / 60;
		numero = Math.rint(numero * 100) / 100;
		entero = (long) numero;
		decimal = ((numero - entero) * 100) * 0.6;
		decimal = Math.rint(decimal * 100) / 100;

		int a = (int) entero;
		int b = (int) Math.rint(decimal);
		String num = pad(a % 24) + ":" + pad(b % 60);

		return num;
	}
	// PER TRASFORMARE 8:00 IN 08:00
		public static String pad(int c) {
			if (c >= 10)
				return String.valueOf(c);
			else
				return "0" + String.valueOf(c);
		}
		
		// RIMPE LA LISTA STRI CON I TIPI DI EVENTO CORRENTI
		public void riempiStri() {
			SetupActivity set = new SetupActivity();
			lista = set.sa.getDB();

			stri = new ArrayList<String>();

			for (int i = 0; i < lista.size(); i++) {

				String a = lista.get(i).getNome();
				boolean cont = false;
				for (int j = 0; j < stri.size(); j++) {
					if (a.equals(stri.get(j))) {
						cont = true;
						break;
					}
				}
				if (cont == false)
					stri.add(a);

			}

		}

		public void onResume() {
			super.onResume();
			//nuova modifica
			LinearLayout layout = (LinearLayout) Graficas.this
					.findViewById(R.id.graph1);
			layout.removeAllViewsInLayout();
			//fine
			riempiStri();

		}

}
