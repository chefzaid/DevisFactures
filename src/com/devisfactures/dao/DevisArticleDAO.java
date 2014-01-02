package com.devisfactures.dao;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.devisfactures.gui.DevisView;
import com.devisfactures.model.DevisArticle;

public class DevisArticleDAO implements IDAO<DevisArticle>{
	
	public static SQLiteDatabase db = Connection.getDB(null);

	public void create(DevisArticle devisArticle) {
		db.execSQL("INSERT INTO DevisArticle VALUES(?,?,?);", 
				devisArticle.toArray());
	}

	public void update(DevisArticle devisArticle) {
		db.execSQL("UPDATE DevisArticle SET IdDevis=?, IdArticle=?, " +
				"Qtte=? WHERE IdDevis='"
				+ devisArticle.getIdDevis() + "' AND IdArticle='" + 
				devisArticle.getIdArticle() + "'", devisArticle.toArray());
	}

	public void delete(String... params) {
		db.execSQL("DELETE FROM DevisArticle WHERE IdDevis = '" + params[0] + 
				"' AND IdArticle = '" + params[1] + "'");
	}

	public ArrayList<DevisArticle> retrieve() {
		Cursor cursor = null;
		ArrayList<DevisArticle> list = new ArrayList<DevisArticle>();
		cursor = db.rawQuery("SELECT * FROM DevisArticle WHERE IdDevis = '"
				+ DevisView.currentIdDevis + "'", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				list.add(new DevisArticle(cursor.getString(0), 
						cursor.getString(1), cursor.getInt(2)));
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
		}
		cursor.close();
		return list;
	}
	
	public ArrayList<String> findArticles(CharSequence idDevis){
		Cursor cursor = null;
		ArrayList<String> list = new ArrayList<String>();
		cursor = db.rawQuery("SELECT IdArticle FROM DevisArticle " +
				"WHERE IdDevis = '" + idDevis + "'", null);
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
	
	public int findQtte(String idDevis, String idArticle){
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT Qtte FROM DevisArticle WHERE IdDevis = '"
				+ idDevis + "' AND IdArticle = '" + idArticle + "'" , null);
		if(cursor.getCount() > 0)
			cursor.moveToFirst();
		int n = cursor.getInt(0);
		cursor.close();
		return n;
	}
	
	public float findPrixTotal(String idDevis){
		Cursor cursor = null;
		ArrayList<String> list = new ArrayList<String>();
		list = findArticles(idDevis);
		if(list.size() == 0)
			return 0;
		float prixTotal = 0;
		for(int i=0; i<list.size(); i++){
			cursor = db.rawQuery("SELECT Prix FROM Article WHERE IdArticle = '"
				+ list.get(i) + "'", null);
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				prixTotal += (cursor.getFloat(0) * findQtte(idDevis, 
						list.get(i)));
			}
		}
		cursor.close();
		return prixTotal;
	}
	
	public boolean convFacture(String idFacture, String idDevis){
		ArrayList<String> articles = findArticles(idDevis);
		ArticleDAO aDAO = new ArticleDAO();
		boolean flag = false;
		for(int i=0; i<articles.size(); i++){
			if(findQtte(idDevis, articles.get(i)) <= aDAO.findQtte(
					articles.get(i)))
				flag = true;
			else
				flag = false;
		}
		if(flag == true){
			db.execSQL("INSERT INTO FactureArticle SELECT '" + idFacture +
					"' , IdArticle, Qtte FROM DevisArticle WHERE IdDevis = '" + 
					idDevis + "';");
			FactureArticleDAO faDAO = new FactureArticleDAO();
			FactureDAO fDAO = new FactureDAO();
			fDAO.updatePrix(idFacture, faDAO.findPrixTotal(idFacture));
			return true;
		}
		return false;
	}
	
}
