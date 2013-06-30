package com.example.moneycontrol;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button saveButton = (Button) findViewById(R.id.save);
		final EditText moneyText = (EditText) findViewById(R.id.money);
		final Spinner typeSpinner = (Spinner) findViewById(R.id.type);

		// Implementamos el evento “click” del botón
		saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				// get the amount
				double money = new Double(moneyText.getText().toString());
				
				// gt the type spend
				String typeSpend = typeSpinner.getOnItemSelectedListener().toString();
				
				// Creamos el Intent
				//Intent intent = new Intent(MainActivity.this, FrmMensaje.class);

				// Creamos la información a pasar entre actividades
				//Bundle b = new Bundle();
				//b.putString("NOMBRE", txtNombre.getText().toString());

				// Añadimos la información al intent
				//intent.putExtras(b);

				// Iniciamos la nueva actividad
				//startActivity(intent);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
