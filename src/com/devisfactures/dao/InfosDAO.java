package com.devisfactures.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.devisfactures.model.Infos;

public class InfosDAO{

	public static SQLiteDatabase db = Connection.getDB(null);
	
	public void create(Infos infos) {
		db.execSQL("INSERT INTO Infos VALUES(?,?,?,?,?,?,?,?,?);", 
				infos.toArray());
	}

	public void update(Infos infos) {
		db.execSQL("UPDATE Infos SET RaisonSociale=?, Adresse=?, Email=?, " +
				"Tel=?, Fax=?, Notes=?, TVA=?, Devise=?, Logo=?", 
				infos.toArray());
	}

	public void delete(String... params) {
		//Do nothing
	}
	
	public Infos retrieve() {
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT * FROM Infos", null);
		if(cursor.getCount() > 0)
			cursor.moveToFirst();
		Infos i = new Infos(cursor.getString(0), cursor.getString(1), 
				cursor.getString(2), cursor.getString(3), cursor.getString(4), 
				cursor.getString(5), cursor.getFloat(6), cursor.getString(7), 
				cursor.getString(8));
		cursor.close();
		return i;
	}
	
	public String retrieveColumn(String colName){
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT " + colName + " FROM Infos", null);
		if(cursor.getCount() > 0)
			cursor.moveToFirst();
		String str = cursor.getString(0);
		cursor.close();
		return str;
	}

}
