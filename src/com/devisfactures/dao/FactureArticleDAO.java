package com.devisfactures.dao;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.devisfactures.gui.FactureView;
import com.devisfactures.model.FactureArticle;

public class FactureArticleDAO implements IDAO<FactureArticle>{
	
	public static SQLiteDatabase db = Connection.getDB(null);

	public void create(FactureArticle devisArticle) {
		db.execSQL("INSERT INTO FactureArticle VALUES(?,?,?);", 
				devisArticle.toArray());
	}

	public void update(FactureArticle devisArticle) {
		db.execSQL("UPDATE FactureArticle SET IdFacture=?, IdArticle=?, " +
				"Qtte=? WHERE IdFacture='"
				+ devisArticle.getIdFacture() + "' AND IdArticle='" + 
				devisArticle.getIdArticle() + "'", devisArticle.toArray());
	}

	public void delete(String... params) {
		db.execSQL("DELETE FROM FactureArticle WHERE IdFacture = '" 
				+ params[0] + "' AND IdArticle = '" + params[1] + "'");
	}

	public ArrayList<FactureArticle> retrieve() {
		Cursor cursor = null;
		ArrayList<FactureArticle> list = 
			new ArrayList<FactureArticle>();
		cursor = db.rawQuery("SELECT * FROM FactureArticle WHERE IdFacture = '"
				+ FactureView.currentIdFacture + "'", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				list.add(new FactureArticle(cursor.getString(0), 
						cursor.getString(1), cursor.getInt(2)));
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
		}
		cursor.close();
		return list;
	}
	
	public ArrayList<String> findArticles(String idFacture){
		Cursor cursor = null;
		ArrayList<String> list = new ArrayList<String>();
		cursor = db.rawQuery("SELECT IdArticle FROM FactureArticle " +
				"WHERE IdFacture = '" + idFacture + "'", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				list.add(new String(cursor.getString(0)));
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
		}
		cursor.close();
		return list;
	}
	
	public int findQtte(String idFacture, String idArticle){
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT Qtte FROM FactureArticle " +
				"WHERE IdFacture = '" + idFacture + "' AND IdArticle = '" 
				+ idArticle + "'" , null);
		if(cursor.getCount() > 0)
			cursor.moveToFirst();
		int n = cursor.getInt(0);
		cursor.close();
		return n;
	}
	
	public float findPrixTotal(String idFacture){
		Cursor cursor = null;
		ArrayList<String> list = new ArrayList<String>();
		list = findArticles(idFacture);
		if(list.size() == 0)
			return 0;
		float prixTotal = 0;
		for(int i=0; i<list.size(); i++){
			cursor = db.rawQuery("SELECT Prix FROM Article WHERE IdArticle = '"
				+ list.get(i) + "'", null);
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				prixTotal += (cursor.getFloat(0) * findQtte(idFacture, 
						list.get(i)));
			}
		}
		cursor.close();
		return prixTotal;
	}
	

	
}
