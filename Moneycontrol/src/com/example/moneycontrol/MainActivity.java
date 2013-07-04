package com.example.moneycontrol;

import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import com.androidplot.xy.*;

import com.example.moneycontrol.RegistersTableService.LocalBinder;

public class MainActivity extends Activity {

	RegistersTableService mService;
	boolean mBound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// context the class
		final Context context = this;
		final Button saveButton = (Button) findViewById(R.id.save);
		final TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
		final EditText dateText = (EditText) findViewById(R.id.date);

		createTap();
		dateText.setText(getDate());

		// Implement “click” event
		saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				createRegister();
				clearForm();

			}
		});

		tabs.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if (tabId == "registers") {

					Intent service = new Intent(context,
							RegistersTableService.class);
					context.startService(service);

					if (mBound) {
						// Call a method from the LocalService.
						// However, if this call were something that might hang,
						// then this request should
						// occur in a separate thread to avoid slowing down the
						// activity performance.
						ArrayAdapter<String> adaptador = mService
								.buldingTable();

						// put the data in the table
						GridView grdOpciones = (GridView) findViewById(R.id.list);

						grdOpciones.setAdapter(adaptador);
					}
				}
				if (tabId == "graph") {

					// Creamos dos arrays de prueba. En el caso real debemos
					// reemplazar
					// estos datos por los que realmente queremos mostrar
					Number[] data = { 1, 8, 5, 2, 7, 4 };
					createGraph(data);

				}

			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
		// Bind to LocalService
		Intent intent = new Intent(this, RegistersTableService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		// Unbind from the service
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
	}

	/*
	 * Method private
	 */

	// *** clean the element of form
	private void clearForm() {
		final EditText moneyText = (EditText) findViewById(R.id.money);
		final EditText dateText = (EditText) findViewById(R.id.date);
		final EditText descriptionText = (EditText) findViewById(R.id.description);

		dateText.setText("");
		moneyText.setText("");
		descriptionText.setText("");
	}

	private String castDate(String dateString) {

		String[] dateArray = dateString.split(("/"));
		if (dateArray.length < 2) {
			// notify fecha invalida
			return "";
		}

		// year, month, day ej (2008, 01, 01);
		Calendar cal1 = new GregorianCalendar(new Integer(dateArray[2]),
				new Integer(dateArray[1]) + 1, new Integer(dateArray[0]));

		// Format the date
		SimpleDateFormat date_format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		return date_format.format(cal1.getTime());

	}

	// return the current date
	private String getDate() {

		Calendar now = GregorianCalendar.getInstance();
		SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy");
		return date_format.format(now.getTime());

	}

	// create a new register with the values form
	private void createRegister() {

		final EditText moneyText = (EditText) findViewById(R.id.money);
		final EditText dateText = (EditText) findViewById(R.id.date);
		final EditText descriptionText = (EditText) findViewById(R.id.description);
		final Spinner typeSpinner = (Spinner) findViewById(R.id.type);

		// get the date
		String dateString = dateText.getText().toString();
		// get the amount
		float money = new Float(moneyText.getText().toString());
		// get the description
		String description = descriptionText.getText().toString();
		// get the type spend egress entry
		String type = typeSpinner.getSelectedItem().toString();

		dateString = castDate(dateString);

		if (dateString.equals("")) {
			// notyfy date invalid
			return;
		}

		// create a new register
		insert(dateString, money, description, type);
	}

	// create the tap
	private void createTap() {

		final TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);

		Resources res = getResources();

		tabs.setup();

		TabHost.TabSpec spec;

		spec = tabs.newTabSpec("register");
		spec.setContent(R.id.register);
		spec.setIndicator("REGISTRO",
				res.getDrawable(android.R.drawable.ic_btn_speak_now));
		tabs.addTab(spec);

		spec = tabs.newTabSpec("registers");
		spec.setContent(R.id.registers);
		spec.setIndicator("LISTA",
				res.getDrawable(android.R.drawable.ic_dialog_map));
		tabs.addTab(spec);

		spec = tabs.newTabSpec("graph");
		spec.setContent(R.id.graph);
		spec.setIndicator("GRAFICA",
				res.getDrawable(android.R.drawable.ic_dialog_map));
		tabs.addTab(spec);

		spec = tabs.newTabSpec("mitab4");
		spec.setContent(R.id.tab4);
		spec.setIndicator("BALANCE",
				res.getDrawable(android.R.drawable.ic_dialog_map));
		tabs.addTab(spec);

		tabs.setCurrentTab(0);
	}

	// bulding the graph of the registers
	private void createGraph(Number[] data) {
		XYPlot mySimpleXYPlot;
		
		mySimpleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);

		// add a line
		XYSeries series1 = new SimpleXYSeries(Arrays.asList(data), // Array de
																	// datos
				SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Sólo valores
														// verticales
				"Series1"); // Nombre de la primera serie

		// custom color a the line
		LineAndPointFormatter series1Format = new LineAndPointFormatter(
				Color.rgb(0, 200, 0), // Color de la línea
				Color.rgb(0, 100, 0), // Color del punto
				Color.rgb(150, 190, 150), null); // Relleno

		// add to panel
		mySimpleXYPlot.addSeries(series1, series1Format);

	}

	/*
	 * *******************Menu configuration
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * Defines callbacks for service binding, passed to bindService()
	 */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

	/*
	 * *******************Operation in BD Moneycontrol
	 */
	private void insert(String date_register, float money, String description,
			String type) {

		// create or update the database
		MoneySQLiteHelper usdbh = new MoneySQLiteHelper(this, "Moneycontrol",
				null, 1);

		SQLiteDatabase db = usdbh.getWritableDatabase();

		// bd is open
		if (db != null) {
			// insert new register
			db.execSQL("INSERT INTO Money (date_register, money, description, type) "
					+ "VALUES ('"
					+ date_register
					+ "', "
					+ money
					+ ",'"
					+ description + "','" + type + "' )");

			// close bd
			db.close();
		}
	}

	private void delete(int id) {
		// create or update the database
		MoneySQLiteHelper usdbh = new MoneySQLiteHelper(this, "Moneycontrol",
				null, 1);

		SQLiteDatabase db = usdbh.getWritableDatabase();

		// bd is open
		if (db != null) {
			db.execSQL("DELETE FROM Money WHERE id=" + id);
			// close bd
			db.close();
		}
	}

	private void update(int id, String date_register, float money,
			String description, String type) {
		// create or update the database
		MoneySQLiteHelper usdbh = new MoneySQLiteHelper(this, "Moneycontrol",
				null, 1);

		SQLiteDatabase db = usdbh.getWritableDatabase();

		// bd is open
		if (db != null) {
			// Actualizar un registro
			db.execSQL("UPDATE Money SET date_register = '" + date_register
					+ "', money=" + money + ", description= '" + description
					+ "', type='" + type + "'" + "WHERE id=" + id);
			// close bd
			db.close();
		}

	}

}
