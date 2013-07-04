package com.example.moneycontrol;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MoneySQLiteHelper extends SQLiteOpenHelper {

	// Sentencia SQL para crear la tabla de Usuarios
	String sqlCreate = "CREATE TABLE Money (id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "date_register date ," + "money REAL, " + "description TEXT,"
			+ "type TEXT)";

	// public MoneySQLiteHelper(Context contexto, Calendar date_register,
	// float money, String description, String type,
	// CursorFactory factory, int version) {
	public MoneySQLiteHelper(Context contexto, String nombre,
			CursorFactory factory, int version) {
		super(contexto, nombre, factory, version);
		//contexto.deleteDatabase("Moneycontrol");
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Se ejecuta la sentencia SQL de creación de la tabla
		db.execSQL(sqlCreate);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior,
			int versionNueva) {
		// NOTA: Por simplicidad del ejemplo aquí utilizamos directamente
		// la opción de eliminar la tabla anterior y crearla de nuevo
		// vacía con el nuevo formato.
		// Sin embargo lo normal será que haya que migrar datos de la
		// tabla antigua a la nueva, por lo que este método debería
		// ser más elaborado.
		// Se elimina la versión anterior de la tabla
		db.execSQL("DROP TABLE IF EXISTS Money");
		// Se crea la nueva versión de la tabla
		db.execSQL(sqlCreate);
	}
}