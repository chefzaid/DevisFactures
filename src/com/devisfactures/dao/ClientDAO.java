package com.devisfactures.dao;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.devisfactures.model.Client;

public class ClientDAO implements IDAO<Client>{
	
	public static SQLiteDatabase db = Connection.getDB(null);

	public void create(Client client) {
		db.execSQL("INSERT INTO Client VALUES(?,?,?,?,?,?,?);", 
				client.toArray());
	}

	public void update(Client client) {
		db.execSQL("UPDATE Client SET IdClient=?, RaisonSociale=?, Adresse=?," +
				" Email=?, Tel=?, Fax=?, Notes=? WHERE IdClient='"
				+ client.getIdClient() + "'", client.toArray());
	}

	public void delete(String... params) {
		db.execSQL("DELETE FROM Client WHERE IdClient = '" + params[0] + "'");
	}

	public ArrayList<Client> retrieve() {
		Cursor cursor = null;
		ArrayList<Client> list = new ArrayList<Client>();
		cursor = db.rawQuery("SELECT * FROM Client", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				list.add(new Client(cursor.getString(0), 
						cursor.getString(1), cursor.getString(2), 
						cursor.getString(3), cursor.getString(4), 
						cursor.getString(5), cursor.getString(6)));
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
		}
		cursor.close();
		return list;
	}
	
	public Client find(String idClient){
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT * FROM Client WHERE IdClient = '"
				+ idClient + "'", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			Client c = new Client(cursor.getString(0),
					cursor.getString(1), cursor.getString(2), 
					cursor.getString(3), cursor.getString(4), 
					cursor.getString(5), cursor.getString(6));
			cursor.close();
			return c;
		}
		cursor.close();
		return null;
	}
	
	public String[] retrieveArray(){
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT IdClient || ' - ' || RaisonSociale " +
				"FROM Client", null);
		String[] array = new String[cursor.getCount()];
		int i = 0;
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				array[i] = cursor.getString(0);
				i++;
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
		}
		cursor.close();
		return array;
	}
	
	public String getLastIdClient(){
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT IdClient FROM Client " +
				"ORDER BY IdClient DESC LIMIT 1", null);
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
