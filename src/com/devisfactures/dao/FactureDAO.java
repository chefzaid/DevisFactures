package com.devisfactures.dao;

import java.util.ArrayList;
import java.util.Date;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.devisfactures.model.Facture;

public class FactureDAO implements IDAO<Facture>{
	
	public static SQLiteDatabase db = Connection.getDB(null);
	
	public void create(Facture devis) {
		db.execSQL("INSERT INTO Facture VALUES(?,?,?,?,?,?);", devis.toArray());	
	}

	public void update(Facture devis) {
		db.execSQL("UPDATE Facture SET IdFacture=?, Date=?, PrixHT=?, " +
				"Paye=?, IdClient=?, Notes=? WHERE IdFacture='"
				+ devis.getIdFacture() +"'", devis.toArray());
	}

	public void delete(String... params) {
		db.execSQL("DELETE FROM Facture WHERE IdFacture = '" + params[0] + "'");
	}

	public ArrayList<Facture> retrieve() {
		Cursor cursor = null;
		ArrayList<Facture> list = new ArrayList<Facture>();
		cursor = db.rawQuery("SELECT * FROM Facture", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				list.add(new Facture(cursor.getString(0), new Date(
						cursor.getLong(1)*1000), cursor.getFloat(2),
						(boolean)(cursor.getInt(3)!=0), cursor.getString(4), 
						cursor.getString(5)));
					cursor.moveToNext();
			}while(!cursor.isAfterLast());
		}
		cursor.close();
		return list;
	}
	
	public Facture find(String idFacture){
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT * FROM Facture WHERE IdFacture = '" + 
				idFacture + "'", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			Facture f =  new Facture(cursor.getString(0), new Date(
					cursor.getLong(1)*1000), cursor.getFloat(2),
				(boolean)(cursor.getInt(3)!=0), cursor.getString(4),
				cursor.getString(5));
			cursor.close();
			return f;
		}
		cursor.close();
		return null;
	}
	
	public ArrayList<Facture> filterEtatFacture(boolean etat){
		Cursor cursor = null;
		ArrayList<Facture> list = new ArrayList<Facture>();
		cursor = db.rawQuery("SELECT * FROM Facture WHERE Paye = '" + 
				(etat?1:0) + "'", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				list.add(new Facture(cursor.getString(0), new Date(
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
	
	public ArrayList<Facture> filterIdClient(String idClient){
		Cursor cursor = null;
		ArrayList<Facture> list = new ArrayList<Facture>();
		cursor = db.rawQuery("SELECT * FROM Facture WHERE IdClient = '" + 
				idClient + "'", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				list.add(new Facture(cursor.getString(0), new Date(
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
	
	public ArrayList<Facture> filterDate(Date dateStart, Date dateEnd){
		Cursor cursor = null;
		ArrayList<Facture> list = new ArrayList<Facture>();
		cursor = db.rawQuery("SELECT * FROM Facture WHERE Date BETWEEN " + 
				dateStart.getTime()/1000 + 
				" AND " + dateEnd.getTime()/1000, null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				list.add(new Facture(cursor.getString(0), new Date(
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
	
	public ArrayList<Facture> sort(String col, String order){
		Cursor cursor = null;
		ArrayList<Facture> list = new ArrayList<Facture>();
		cursor = db.rawQuery("SELECT * FROM Facture ORDER BY " + col + " " + 
				order, null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				list.add(new Facture(cursor.getString(0), new Date(
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
		db.execSQL("UPDATE Facture SET PrixHT=? WHERE IdFacture=?", 
				new Object[]{ total, idDevis });
	}
	
	public String getLastIdFacture(){
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT IdFacture FROM Facture " +
				"ORDER BY IdFacture DESC LIMIT 1", null);
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
