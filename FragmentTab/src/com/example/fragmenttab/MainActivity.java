package com.example.fragmenttab;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {

	public int cont = 0;

	private int mHour;
	private int mMinute;
	private int mHour2;
	private int mMinute2;
	private int mYear;
	private int mMonth;
	private int mDay;

	static final int TIME_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID2 = 1;
	static final int CONFIRM_DIALOG = 2;
	static final int CONFIRM_DIALOG2 = 3;
	static final int CONFIRM_DIALOG3 = 4;
	static final int CONFIRM_DIALOG4 = 5;
	static final int DATE_DIALOG_ID = 6;
	AlertDialog alertDialog;
	int selez = 0;
	String items[];

	// Identificatore delle preferenze dell'applicazione
	private final static String MY_PREFERENCES = "MyPref";
	// Costante relativa al nome della particolare preferenza
	private final static String TEXT_DATA_KEY = "textData";
	private final static String TIPI = "Tipi";

	String aa = "";
	String data = "";
	List<String> list;
	Spinner s;
	String pref = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	    final ActionBar ab = getActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

  //      ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        ab.addTab(ab.newTab().setText("Add Event")
                        .setTabListener(new TabListener(new TabContentFragment())));
        ab.addTab(ab.newTab().setText("Event List")
                .setTabListener(new TabListener(new TabContentFragment())));
        ab.addTab(ab.newTab().setText("Graphics")
                .setTabListener(new TabListener(new TabContentFragment())));
        ab.addTab(ab.newTab().setText("Bluetooth")
                .setTabListener(new TabListener(new TabContentFragment())));
        
		SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
				Context.MODE_PRIVATE);
		cont = prefs.getInt(TEXT_DATA_KEY, 0);

		// LISTA CON I TIPI DI EVENTO
		list = new ArrayList<String>();

		// POPOLO LA LISTA CON LE INFO DA SHAREDPREFERENCES (STRING Tipi)
		pref = prefs.getString(TIPI, "");

		String[] splits2 = pref.split("\\.");

		for (int i = 1; i < splits2.length; i++) {
			list.add(splits2[i]);
		}

		if (list.isEmpty()) {
			TextView testo = (TextView) findViewById(R.id.testo);
			testo.setText(getString(R.string.guia));
		}

		aggiornaspin();

		// ////////////////////
		// gestione pickers ore e data
		// /////////////////////

		// capture our View elements
		// TextView mTimeDisplay = (TextView) findViewById(R.id.timeDisplay);
		Button mPickTime = (Button) findViewById(R.id.pickTime);
		Button mPickTime2 = (Button) findViewById(R.id.pickTime2);
		Button mPickDate = (Button) findViewById(R.id.pickDate);
		Button mBluetooth = (Button) findViewById(R.id.bluetooth);
		
		// add a click listener to the button
		mPickTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
			}
		});
		// add a click listener to the button
		mPickTime2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v2) {
				showDialog(TIME_DIALOG_ID2);
			}
		});
		mPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
		mBluetooth.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent openbluetooth = new Intent(MainActivity.this,BluetoothChat.class);
                startActivity(openbluetooth);
			}
		});
	

		// ACQUISISCI DATA E ORA CORRENTI
		final Calendar c = Calendar.getInstance();
		mHour2 = mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute2 = mMinute = c.get(Calendar.MINUTE);
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// VISUALIZZA ORE CORRENTI E DATA CORRENTE
		updateDisplay();
		updateDisplay2();
		updateDisplay3();

		// GESTIONE BOTTONE INSERIMENTO
		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				addevento();

			}
		});
	}

	// GESTIONE MENU A TENDINA
		public class MyOnItemSelectedListener implements OnItemSelectedListener {

			public void onItemSelected(AdapterView<?> parent, View view, int pos,
					long id) {
				aa = parent.getItemAtPosition(pos).toString();

			}

			public void onNothingSelected(AdapterView parent) {
				// Do nothing.
			}
		}

		// updates the time we display in the TextView
		private void updateDisplay() {
			TextView mTimeDisplay = (TextView) findViewById(R.id.timeDisplay);
			mTimeDisplay.setText(getString(R.string.oraat) + pad(mHour) + ":"
					+ pad(mMinute));

		}

		// updates the time we display in the TextView
		private void updateDisplay2() {
			TextView mTimeDisplay2 = (TextView) findViewById(R.id.timeDisplay2);
			mTimeDisplay2.setText(getString(R.string.orafin2) + pad(mHour2) + ":"
					+ pad(mMinute2));

		}

		// visualizza la data
		private void updateDisplay3() {
			TextView mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
			data = pad(mMonth + 1) + "-" + pad(mDay) + "-" + mYear;
			mDateDisplay.setText(getString(R.string.dataat) + data);

		}

		// aggiunge 0 davanti a un numero di 1 sola cifra
		private static String pad(int c) {
			if (c >= 10)
				return String.valueOf(c);
			else
				return "0" + String.valueOf(c);
		}

		// the callback received when the user "sets" the time in the dialog
		private TimePickerDialog.OnTimeSetListener mTimeSetListener2 = new TimePickerDialog.OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

				mHour2 = hourOfDay;
				mMinute2 = minute;
				updateDisplay2();

			}
		};

		// the callback received when the user "sets" the time in the dialog
		private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
			public void onTimeSet(TimePicker view2, int hourOfDay, int minute) {
				mHour = hourOfDay;
				mMinute = minute;
				updateDisplay();
			}
		};

		private DatePickerDialog.OnDateSetListener mDateSetListener3 = new DatePickerDialog.OnDateSetListener() {

			public void onDateSet(DatePicker view3, int year, int monthOfYear,
					int dayOfMonth) {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				updateDisplay3();
			}
		};

		// GESTIONE DELLE DIALOG

		protected Dialog onCreateDialog(int id) {

			switch (id) {

			// DIALOG ORAINIZIO
			// l'ultima opzione true è x mettere il dialog in 24h (senza am,pm)

			case TIME_DIALOG_ID:
				return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
						true);
				// DIALOG ORAFINE

			case TIME_DIALOG_ID2:
				return new TimePickerDialog(this, mTimeSetListener2, mHour2,
						mMinute2, true);

			case DATE_DIALOG_ID:
				return new DatePickerDialog(this, mDateSetListener3, mYear, mMonth,
						mDay);
			}
			return null;
		}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	   
	        switch (item.getItemId()) {
	     // ELIMINARE TIPO
	 		case R.id.eliminatipo:
	 			
	 			items = list.toArray(new String[list.size()]);

	 			AlertDialog.Builder ab = new AlertDialog.Builder(this);
	 			ab.setTitle(getString(R.string.tipoel));
	 			ab.setSingleChoiceItems(items, 0,
	 					new DialogInterface.OnClickListener() {
	 						public void onClick(DialogInterface dialog,
	 								int whichButton) {

	 							selez = whichButton;
	 						}
	 					})
	 					.setPositiveButton("Ok",
	 							new DialogInterface.OnClickListener() {
	 								public void onClick(DialogInterface dialog,
	 										int whichButton) {

	 									removetipoevento();
	 									if (list.isEmpty()) {
	 										TextView testo = (TextView) findViewById(R.id.testo);
	 										testo.setText(getString(R.string.guia));
	 									}

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

	 		// AGGIUNGERE TIPO
	 		case R.id.aggiungitipo: {
	 			
	 			AlertDialog.Builder alert = new AlertDialog.Builder(this);

	 			alert.setTitle(getString(R.string.addev));

	 			// Set an EditText view to get user input
	 			final EditText input = new EditText(this);
	 			alert.setView(input);

	 			alert.setPositiveButton("Ok",
	 					new DialogInterface.OnClickListener() {
	 						public void onClick(DialogInterface dialog,
	 								int whichButton) {

	 							String value = input.getText().toString();

	 							addtipoevento(value);

	 							TextView testo = (TextView) findViewById(R.id.testo);
	 							testo.setText(getString(R.string.eligeevento));

	 						}
	 					});

	 			alert.setNegativeButton("Cancel",
	 					new DialogInterface.OnClickListener() {
	 						public void onClick(DialogInterface dialog,
	 								int whichButton) {
	 							// Canceled.
	 						}
	 					});

	 			alert.show();

	 		}
	    
	 			break;
	 		// DIALOG SELEZIONA NOME FILE DA ESPORTARE (CON SHAREDPREFERENCES)
	 		case R.id.esportadb: {
	 		
	 			AlertDialog.Builder alert = new AlertDialog.Builder(this);

	 			alert.setTitle(getString(R.string.nomexml));

	 			// Set an EditText view to get user input
	 			final EditText input = new EditText(this);
	 			alert.setView(input);

	 			alert.setPositiveButton("Ok",
	 					new DialogInterface.OnClickListener() {
	 						public void onClick(DialogInterface dialog,
	 								int whichButton) {
	 							String value = input.getText().toString();

	 							creaFile(value);
	 						}
	 					});

	 			alert.setNegativeButton("Cancel",
	 					new DialogInterface.OnClickListener() {
	 						public void onClick(DialogInterface dialog,
	 								int whichButton) {
	 							// Canceled.
	 						}
	 					});

	 			alert.show();

	 		}
	    
	 			break;
	 		case R.id.eliminatutto:
	 			 
	 			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);

	 			builder2.setMessage(getString(R.string.vaciali));
	 			builder2.setCancelable(false);
	 			builder2.setPositiveButton(getString(R.string.si),
	 					new DialogInterface.OnClickListener() {
	 						public void onClick(DialogInterface dialog, int id) {

	 							vaciatipi();

	 							TextView testo = (TextView) findViewById(R.id.testo);
	 							testo.setText(getString(R.string.guia));

	 						}
	 					});
	 			builder2.setNegativeButton("No",
	 					new DialogInterface.OnClickListener() {
	 						public void onClick(DialogInterface dialog, int id) {
	 							dialog.dismiss(); // Chiudiamo la finestra di
	 												// dialogo
	 						}
	 					});

	 			builder2.show();

	 			break;
	 			 }
	    
	        return false;
	    }
	 public void onResume() {
			super.onResume();

			aggiornaspin();

		}

		public void removetipoevento() {

			String[] splits = pref.split("\\.");
			pref = "";
			for (int i = 1; i < splits.length; i++) {
				String uno = "" + splits[i];
				String due = "" + list.get(selez).toString();

				if (!(uno.equals(due))) {

					pref = pref + "." + splits[i];
				}
			}
			Toast.makeText(
					getApplicationContext(),
					getString(R.string.tipoe) + " " + list.get(selez).toString()
							+ " " + getString(R.string.eli), Toast.LENGTH_LONG)
					.show();

			final SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
					Context.MODE_WORLD_WRITEABLE);

			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(TIPI, pref);
			editor.commit();

			list.remove(selez);
			aggiornaspin();
			// items = list.toArray(new String[list.size()]);
			aa = "";

		}

		public void addevento() {

			int inizio = 60 * mHour + mMinute;
			int fine = 60 * mHour2 + mMinute2;

			if (!aa.equals("") && (inizio < fine)) {

				SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
						Context.MODE_WORLD_WRITEABLE);
				SharedPreferences.Editor editor = prefs.edit();

				int cont = prefs.getInt(TEXT_DATA_KEY, 0);

				cont++;
				// PROBLEM:INSERT ID=1, INSERT ID=2 (Quindi cont =2)
				// REMOVE ID=1 -> cont=1
				// INSERT NUOVO OGGETTO -> GLI VIENE ASSEGNATO ID=2 CHE GIÀ ESISTE
				// QUINDI LO SOVRASCRIVE!!
				// CHAPUZA: SE ESISTE UN ID = AL MIO, INCREMENTA IL MIO ID E IL CONT
				for (int i = 0; i < SetupActivity.sa.getDB().size(); i++) {
					if (SetupActivity.sa.getEvent(i).getID() == cont)
						cont++;
				}

				editor.putInt(TEXT_DATA_KEY, cont);
				Eventi a = new Eventi(aa, pad(mHour) + ":" + pad(mMinute),
						pad(mHour2) + ":" + pad(mMinute2), cont, data);

				// a.setID(cont);

				SetupActivity.sa.addEvent(a);

				String stampa = getString(R.string.evead) + " " + a.getNome() + " "
						+ getString(R.string.orainiz) + " " + a.getInizio() + " "
						+ getString(R.string.orafin) + " " + a.getFine() + " "
						+ getString(R.string.data) + " " + a.getData();
				Toast.makeText(getApplicationContext(), stampa, Toast.LENGTH_LONG)
						.show();

				// AGGIORNA E STAMPA TEXT_DATA_KEY

				editor.putString("" + cont, a.toString2());
				editor.commit();
			} else if (aa.equals(""))
				Toast.makeText(getApplicationContext(), getString(R.string.guia1),
						Toast.LENGTH_LONG).show();
			else if (inizio == fine)
				Toast.makeText(getApplicationContext(), getString(R.string.guia2),
						Toast.LENGTH_LONG).show();
			else if (inizio > fine)
				Toast.makeText(getApplicationContext(), getString(R.string.guia3),
						Toast.LENGTH_LONG).show();

		}

		public void addtipoevento(String value) {

			boolean boo = true;
			for (int i = 0; i < list.size(); i++) {
				if (value.equals(list.get(i).toString())) {
					boo = false;
					break;
				}
			}

			if (!value.equals("") && !value.contains(";") && boo
					&& !value.contains(".") && !(value.charAt(0) == ' ')) {

				list.add(value);
				aggiornaspin();

				pref = pref + "." + value;

				final SharedPreferences prefs = getSharedPreferences(
						MY_PREFERENCES, Context.MODE_WORLD_WRITEABLE);

				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(TIPI, pref);
				editor.commit();

				Toast.makeText(
						getApplicationContext(),
						getString(R.string.tipoe) + " " + value + " "
								+ getString(R.string.agg), Toast.LENGTH_LONG)
						.show();

			}

			else if (!boo)
				Toast.makeText(getApplicationContext(), getString(R.string.guia4),
						Toast.LENGTH_LONG).show();

			else
				Toast.makeText(getApplicationContext(), getString(R.string.guia5),
						Toast.LENGTH_LONG).show();
		}

		public void vaciatipi() {

			final SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
					Context.MODE_WORLD_WRITEABLE);
			SharedPreferences.Editor editor = prefs.edit();
			Toast.makeText(getApplicationContext(), getString(R.string.tipieli),
					Toast.LENGTH_LONG).show();
			editor.remove("Tipi");
			editor.commit();
			list.clear();
			aggiornaspin();
			pref = "";
			aa = "";

		}

		// AGGIORNARE SPINNER
		public void aggiornaspin() {
			s = (Spinner) findViewById(R.id.spinner);

			s.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_dropdown_item, list));

			s.setOnItemSelectedListener(new MyOnItemSelectedListener());
		}

		// CREA IL FILE COPIANDO DAL FILE CON LE SHAREDPREFERENCES
		public void creaFile(String pNomeFile) {

			File file = new File(Environment.getExternalStorageDirectory() + "/"
					+ pNomeFile + ".xml");

			FileWriter f = null;
			PrintWriter p = null;
			// BufferedWriter b = null;
			try {
				f = new FileWriter(file, false);
				// b = new BufferedWriter(f);
				p = new PrintWriter(f);
				String aBuffer = "";
				try {
					String packageName = getApplicationContext().getPackageName();
					String path = "/data/data/" + packageName
							+ "/shared_prefs/MyPref.xml";
					// Toast.makeText(getBaseContext(),path,Toast.LENGTH_LONG).show();

					File myFile = new File(path);
					FileInputStream fIn = new FileInputStream(myFile);
					BufferedReader myReader = new BufferedReader(
							new InputStreamReader(fIn));
					String aDataRow = "";

					while ((aDataRow = myReader.readLine()) != null) {
						aBuffer += aDataRow + "\n";
					}

					myReader.close();
					// Toast.makeText(getBaseContext(),aBuffer,Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					Toast.makeText(getBaseContext(), e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
				p.println(aBuffer);
				// b.write("ciao sono una riga");
				p.close();
				// b.close();
				f.close();
				Toast.makeText(
						getApplicationContext(),
						getString(R.string.file) + " " + pNomeFile + " "
								+ getString(R.string.insd), Toast.LENGTH_SHORT)
						.show();
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(
						getApplicationContext(),
						getString(R.string.err) + " " + e.toString() + " "
								+ getString(R.string.nolegg), Toast.LENGTH_LONG)
						.show();
			}
		}

		private class TabListener implements ActionBar.TabListener {
            private TabContentFragment mFragment;

            public TabListener(TabContentFragment fragment) {
                    mFragment = fragment;
            }

            public void onTabSelected(Tab tab, FragmentTransaction ft) {               
                    switch (tab.getPosition()) {
                    case 0:                    	
                    	ft.add(R.id.flFragmentContent, mFragment, "New Tab");
                    //	ft.replace(R.id.flFragmentContent, mFragment);
                    	break;
                    case 1:                    	
                    //	ft.add(R.id.flFragmentContent, mFragment, "New Tab");
                    //	ft.replace(R.id.flFragmentContent, mFragment);
                    	Intent opensetup = new Intent(MainActivity.this,SetupActivity.class);
                        startActivity(opensetup);
                    	break;
                    case 2:                    	
                    //	ft.add(R.id.flFragmentContent, mFragment, "New Tab");
                    //	ft.replace(R.id.flFragmentContent, mFragment);
                    	Intent opengraphic = new Intent(MainActivity.this,Graficas.class);
                        startActivity(opengraphic);
                    	break;
                    case 3:                    	
                    //	ft.add(R.id.flFragmentContent, mFragment, "New Tab");
                    //	ft.replace(R.id.flFragmentContent, mFragment);
                    	Intent openbluetooth = new Intent(MainActivity.this,BluetoothChat.class);
                        startActivity(openbluetooth);
                    	break;
                    }
            }

            public void onTabUnselected(Tab tab, FragmentTransaction ft) {
                    ft.remove(mFragment);
            }

            public void onTabReselected(Tab tab, FragmentTransaction ft) {
                    Toast.makeText(MainActivity.this, "Reselected", Toast.LENGTH_SHORT)
                                    .show();
            }

    }
/*
		public static class Tab1Fragment extends Fragment {
		
			    @Override
		
			    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			            Bundle savedInstanceState) {
			    	return inflater.inflate(R.layout.tab1, container, false);
			    }

			}*/

    public static class TabContentFragment extends Fragment {

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
                    View fragView = inflater.inflate(R.layout.tabscontent, container,
                                    false);

                    TextView text = (TextView) fragView.findViewById(R.id.tvTabText);
                    text.setText("Hello Tab");

                    return fragView;
            }

    }
}
