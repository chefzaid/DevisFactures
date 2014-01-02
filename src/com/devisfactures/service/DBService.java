package com.devisfactures.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.devisfactures.dao.*;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBService{
	
	public static SQLiteDatabase db = Connection.getDB(null);
	
	public static boolean isTableEmpty(String table){
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT COUNT(*) FROM " + table, null);
		cursor.moveToFirst();
		int n = cursor.getInt(0);
		cursor.close();
		if(n > 0)
			return false;
		return true;
	}
	
	public static float prixTTC(float prixHT){
		InfosDAO iDAO = new InfosDAO();
		float prixTTC = prixHT * (1 + Float.parseFloat(
				(String)iDAO.retrieveColumn("TVA")));
		prixTTC *= 100;
		prixTTC = Math.round(prixTTC);
		prixTTC /= 100;
		return prixTTC;
	}
	
	public static String formatDate(Date date){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return new String(df.format(date));
	}
	
	public static Date parseDate(String date){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date d = new Date();
		try {
			d = df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

}

