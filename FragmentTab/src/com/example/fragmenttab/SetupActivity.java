package com.example.fragmenttab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SetupActivity extends Activity {

	ListView mylistview;
	private List<Eventi> DB;
	ArrayAdapter<Eventi> listAdapter;
	static final int CONFIRM_DIALOG = 0;
	static final int CONFIRM_DIALOG1 = 1;

	int posizione = 0;
	AlertDialog alertDialog;
	public static SetupActivity sa;

	// Identificatore delle preferenze dell'applicazione
	private final static String MY_PREFERENCES = "MyPref";
	// Costante relativa al nome della particolare preferenza
	private final static String TEXT_DATA_KEY = "textData";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setupactivity);
		
	/*	 final ActionBar ab = getActionBar();
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
	                .setTabListener(new TabListener(new TabContentFragment())));*/
		Eventi.setContext(getApplicationContext());
		sa = new SetupActivity();
		mylistview = (ListView) findViewById(R.id.list);

		// resetta();
		riempilista();

		// ORDINO LISTA
		Collections.sort(sa.getDB());

		listAdapter = new ArrayAdapter<Eventi>(SetupActivity.this,
				android.R.layout.simple_list_item_1, sa.getDB());

		mylistview.setAdapter(listAdapter);

		// GESTIONE DEL CLICK SU UN EVENTPO -> SI CHIAMA LA FINESTRA DI CONFERMA
		mylistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				showDialog(CONFIRM_DIALOG);

				posizione = position;
			}
		});
	}

	// PER AGGIORNARE LA LISTA - NECESSARIO X L'INSERIMENTO
	public void onResume() {
		super.onResume();

		if (sa.getDB().size() == 0) {
			final SharedPreferences prefs = getSharedPreferences(
					MY_PREFERENCES, Context.MODE_WORLD_WRITEABLE);

			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt(TEXT_DATA_KEY, 0);
		}

		Collections.sort(sa.getDB());

		listAdapter = new ArrayAdapter<Eventi>(SetupActivity.this,
				android.R.layout.simple_list_item_1, sa.getDB());
		mylistview.setAdapter(listAdapter);

	}

	// FINESTRA DI DIALOGO PER CONFERMA ELIMINAZIONE EVENTO
	protected Dialog onCreateDialog(int id) {
		final SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
				Context.MODE_WORLD_WRITEABLE);

		Dialog dialog;
		switch (id) {
		case CONFIRM_DIALOG:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getString(R.string.eliminaev));
			builder.setCancelable(false);
			builder.setPositiveButton(getString(R.string.si),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							// ELIMINO EVENTO
							String stampa = getString(R.string.evelim) + " "
									+ sa.getEvent(posizione).getNome() + " "
									+ getString(R.string.orainiz) + " "
									+ sa.getEvent(posizione).getInizio() + " "
									+ getString(R.string.orafin) + " "
									+ sa.getEvent(posizione).getFine() + " "
									+ getString(R.string.data) + " "
									+ sa.getEvent(posizione).getData();
							Toast.makeText(getApplicationContext(), stampa,
									4000).show();

							// RIMUOVI PREFERENCE, AGGIORNA CONT E RIMUOVI
							// OGGETTO DA ARRAYLIST
							SharedPreferences.Editor editor = prefs.edit();
							int cont = prefs.getInt(TEXT_DATA_KEY, 0);

							int b = sa.getEvent(posizione).getID();

							editor.remove("" + b);
							cont--;
							editor.putInt(TEXT_DATA_KEY, cont);

							editor.commit();
							sa.removeEvent(posizione);

							listAdapter = new ArrayAdapter<Eventi>(
									SetupActivity.this,
									android.R.layout.simple_list_item_1, sa
											.getDB());
							mylistview.setAdapter(listAdapter);

						}
					});
			builder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss(); // Chiudiamo la finestra di
												// dialogo
						}
					});

			dialog = builder.create();
			break;
		case CONFIRM_DIALOG1:
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			builder2.setMessage(getString(R.string.vaciali));
			builder2.setCancelable(false);
			builder2.setPositiveButton(getString(R.string.si),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							resetta();

						}
					});
			builder2.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss(); // Chiudiamo la finestra di
												// dialogo
						}
					});

			dialog = builder2.create();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	// SVUOTARE DB CON EVENTI
	public void resetta() {
		final SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
				Context.MODE_WORLD_WRITEABLE);

		SharedPreferences.Editor editor = prefs.edit();
		String prefer = prefs.getString("Tipi", "");

		editor.clear();
		editor.putString("Tipi", prefer);

		editor.commit();
		sa.getDB().clear();

		listAdapter = new ArrayAdapter<Eventi>(SetupActivity.this,
				android.R.layout.simple_list_item_1, sa.getDB());
		mylistview.setAdapter(listAdapter);

		// sa.getDB().removeAll(sa.getDB());
		Toast.makeText(getApplicationContext(), getString(R.string.bdvacia),
				Toast.LENGTH_SHORT).show();

	}

	// RIEMPIRE DB CON EVENTI
	public void riempilista() {

		final SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
				Context.MODE_WORLD_WRITEABLE);

		// int cont=prefs.getInt(TEXT_DATA_KEY, 0);

		Map<String, ?> items = prefs.getAll();
		for (String s : items.keySet()) {

			String nome = "";
			String inizio = "";
			String fine = "";
			String data = "";
			int id = 0;

			String str = items.get(s).toString();

			// CHAPUZA PER NON FAR PRENDERE IL CONT (mettiam caso che non supera
			// le 3 cifre)!!!
			if (str.length() > 2 && !(str.charAt(0) == '.')) {

				String[] splits2 = str.split(";");

				nome = splits2[0];
				inizio = splits2[1];
				fine = splits2[2];
				id = Integer.parseInt(splits2[3]);
				data = splits2[4];

				sa.addEvent(new Eventi(nome, inizio, fine, id, data));

			}
		}

	}

	// GESTIONE MENU

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(getString(R.string.vaciabd)).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {

						if (!sa.getDB().isEmpty())
							showDialog(CONFIRM_DIALOG1);
						else
							Toast.makeText(getApplicationContext(),
									getString(R.string.listaya),
									Toast.LENGTH_LONG).show();

						return true;

					}
				});
		;
		/*
		 * menu.add("Comando2").setOnMenuItemClickListener(new
		 * OnMenuItemClickListener() { public boolean onMenuItemClick(MenuItem
		 * item) { Toast.makeText(getApplicationContext(), item.getTitle(),
		 * Toast.LENGTH_SHORT).show(); return true; } });;
		 * menu.add("Comando3").setOnMenuItemClickListener(new
		 * OnMenuItemClickListener() { public boolean onMenuItemClick(MenuItem
		 * item) { Toast.makeText(getApplicationContext(), item.getTitle(),
		 * Toast.LENGTH_SHORT).show(); return true; } });;
		 */
		return true;
	}

	public SetupActivity() {
		DB = new ArrayList<Eventi>();
	}

	public void addEvent(Eventi element) {
		DB.add(element);
	}

	public void removeEvent(int element) {
		DB.remove(element);

	}

	public List<Eventi> getDB() {
		return DB;
	}

	public Eventi getEvent(int position) {
		return DB.get(position);
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
                	ft.add(R.id.flFragmentContent, mFragment, "New Tab");
                //	ft.replace(R.id.flFragmentContent, mFragment);
                	break;
                case 2:                    	
                //	ft.add(R.id.flFragmentContent, mFragment, "New Tab");
                //	ft.replace(R.id.flFragmentContent, mFragment);
                	Intent opensetup = new Intent(SetupActivity.this,SetupActivity.class);
                    startActivity(opensetup);
                	break;
                case 3:                    	
                //	ft.add(R.id.flFragmentContent, mFragment, "New Tab");
                //	ft.replace(R.id.flFragmentContent, mFragment);
                	Intent openbluetooth = new Intent(SetupActivity.this,BluetoothChat.class);
                    startActivity(openbluetooth);
                	break;
                }
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
                ft.remove(mFragment);
        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
                Toast.makeText(SetupActivity.this, "Reselected", Toast.LENGTH_SHORT)
                                .show();
        }

}

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
