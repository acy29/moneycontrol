package com.example.moneycontrol;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.widget.ArrayAdapter;

public class RegistersTableService extends IntentService {

	// Binder given to clients
	private final IBinder mBinder = new LocalBinder();

	public class LocalBinder extends Binder {
		RegistersTableService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return RegistersTableService.this;
		}
	}
	
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

	/**
	 * A constructor is required, and must call the super IntentService(String)
	 * constructor with a name for the worker thread.
	 */
	public RegistersTableService() {
		super("HelloIntentService");
	}

	/**
	 * The IntentService calls this method from the default worker thread with
	 * the intent that started the service. When this method returns,
	 * IntentService stops the service, as appropriate.
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		// Normally we would do some work here, like download a file.
		// For our sample, we just sleep for 5 seconds.
		long endTime = System.currentTimeMillis() + 5 * 1000;
		while (System.currentTimeMillis() < endTime) {
			synchronized (this) {
				try {
					wait(endTime - System.currentTimeMillis());
				} catch (Exception e) {
				}
			}
		}
	}

	public ArrayAdapter<String> buldingTable() {
		// get the register
		String[] register = getAll();

		// bulding the info table with register
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, register);

		// put the data in the table
		return adaptador;
	}

	public String[] getAll() {

		String[] register;

		// create or update the database
		MoneySQLiteHelper usdbh = new MoneySQLiteHelper(this, "Moneycontrol",
				null, 1);

		SQLiteDatabase db = usdbh.getWritableDatabase();

		// bd is open
		if (db != null) {

			// get all register
			Cursor cursor = db
					.rawQuery(
							"SELECT date_register, money, description, type FROM Money",
							null);

			register = new String[cursor.getCount() * 4];
			int i = 0;

			while (cursor.moveToNext()) {
				// quit the HH:MM:SS
				String[] castDate = cursor.getString(
						cursor.getColumnIndex("date_register")).split(" ");

				register[i++] = castDate[0];
				register[i++] = cursor
						.getString(cursor.getColumnIndex("money"));
				register[i++] = cursor.getString(cursor
						.getColumnIndex("description"));
				register[i++] = cursor.getString(cursor.getColumnIndex("type"));
			}

			// close bd
			db.close();

			return register;

		}
		return new String[0];
	}
}
