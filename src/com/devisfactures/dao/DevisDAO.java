package com.devisfactures.dao;

import java.util.ArrayList;
import java.util.Date;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.devisfactures.model.Devis;

public class DevisDAO implements IDAO<Devis>{
	
	public static SQLiteDatabase db = Connection.getDB(null);
	
	public void create(Devis devis) {
		db.execSQL("INSERT INTO Devis VALUES(?,?,?,?,?,?);", devis.toArray());	
	}

	public void update(Devis devis) {
		db.execSQL("UPDATE Devis SET IdDevis=?, Date=?, PrixHT=?, " +
				"Facture=?, IdClient=?, Notes=? WHERE IdDevis='"
				+ devis.getIdDevis() +"'", devis.toArray());
	}

	public void delete(String... params) {
		db.execSQL("DELETE FROM Devis WHERE IdDevis = '" + params[0] + "'");
	}

	public ArrayList<Devis> retrieve() {
		Cursor cursor = null;
		ArrayList<Devis> list = new ArrayList<Devis>();
		cursor = db.rawQuery("SELECT * FROM Devis", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				list.add(new Devis(cursor.getString(0), new Date(
						cursor.getLong(1)*1000), cursor.getFloat(2),
						(boolean)(cursor.getInt(3)!=0), cursor.getString(4), 
						cursor.getString(5)));
					cursor.moveToNext();
			}while(!cursor.isAfterLast());
		}
		cursor.close();
		return list;
	}
	
	public Devis find(String idDevis){
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT * FROM Devis WHERE IdDevis = '" + 
				idDevis + "'", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			Devis d =  new Devis(cursor.getString(0), new Date(
					cursor.getLong(1)*1000), cursor.getFloat(2),
				(boolean)(cursor.getInt(3)!=0), cursor.getString(4),
				cursor.getString(5));
			cursor.close();
			return d;
		}
		cursor.close();
		return null;
	}
	
	public ArrayList<Devis> filterEtatDevis(boolean etat){
		Cursor cursor = null;
		ArrayList<Devis> list = new ArrayList<Devis>();
		cursor = db.rawQuery("SELECT * FROM Devis WHERE Facture = '" + 
				(etat?1:0) + "'", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				list.add(new Devis(cursor.getString(0), new Date(
						cursor.getLong(1)*1000), cursor.getFloat(2),
						(boolean)(cursor.getInt(3)!=0), cursor.getString(4), 
						cursor.getString(5)));
					cursor.moveToNext();
			}while(!cursor.isAfterLast());
			cursor.close();
			return list;
		}
		cursor.close();
		return null;
	}
	
	public ArrayList<Devis> filterIdClient(String idClient){
		Cursor cursor = null;
		ArrayList<Devis> list = new ArrayList<Devis>();
		cursor = db.rawQuery("SELECT * FROM Devis WHERE IdClient = '" + 
				idClient + "'", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				list.add(new Devis(cursor.getString(0), new Date(
						cursor.getLong(1)*1000), cursor.getFloat(2),
						(boolean)(cursor.getInt(3)!=0), cursor.getString(4), 
						cursor.getString(5)));
					cursor.moveToNext();
			}while(!cursor.isAfterLast());
			cursor.close();
			return list;
		}
		cursor.close();
		return null;
	}
	
	public ArrayList<Devis> filterDate(Date dateStart, Date dateEnd){
		Cursor cursor = null;
		ArrayList<Devis> list = new ArrayList<Devis>();
		cursor = db.rawQuery("SELECT * FROM Devis WHERE Date BETWEEN " + 
				dateStart.getTime()/1000 + 
				" AND " + dateEnd.getTime()/1000, null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				list.add(new Devis(cursor.getString(0), new Date(
						cursor.getLong(1)*1000), cursor.getFloat(2),
						(boolean)(cursor.getInt(3)!=0), cursor.getString(4), 
						cursor.getString(5)));
					cursor.moveToNext();
			}while(!cursor.isAfterLast());
			cursor.close();
			return list;
		}
		cursor.close();
		return null;
	}
	
	public ArrayList<Devis> sort(String col, String order){
		Cursor cursor = null;
		ArrayList<Devis> list = new ArrayList<Devis>();
		cursor = db.rawQuery("SELECT * FROM Devis ORDER BY " + col + " " + 
				order, null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				list.add(new Devis(cursor.getString(0), new Date(
						cursor.getLong(1)*1000), cursor.getFloat(2),
						(boolean)(cursor.getInt(3)!=0), cursor.getString(4), 
						cursor.getString(5)));
					cursor.moveToNext();
			}while(!cursor.isAfterLast());
			cursor.close();
			return list;
		}
		cursor.close();
		return null;
	}
	
	public void updatePrix(String idDevis, float total){
		db.execSQL("UPDATE Devis SET PrixHT=? WHERE IdDevis=?", 
				new Object[]{ total, idDevis });
	}
	
	public String getLastIdDevis(){
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT IdDevis FROM Devis " +
				"ORDER BY IdDevis DESC LIMIT 1", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			String str = cursor.getString(0);
			cursor.close();
			return str;
		}
		cursor.close();
		return null;
	}

}
