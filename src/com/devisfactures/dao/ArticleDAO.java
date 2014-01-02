package com.devisfactures.dao;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.devisfactures.model.Article;

public class ArticleDAO implements IDAO<Article>{
	
	public static SQLiteDatabase db = Connection.getDB(null);
	
	public void create(Article article) {
		db.execSQL("INSERT INTO Article VALUES(?,?,?,?,?);", article.toArray());
	}

	public void update(Article article) {
		db.execSQL("UPDATE Article SET IdArticle=?, Libelle=?, Prix=?, " +
				"QtteEnStock=?, Notes=? WHERE IdArticle='"
				+ article.getIdArticle() + "'", article.toArray());
	}

	public void delete(String... params) {
		db.execSQL("DELETE FROM Article WHERE IdArticle = '" + params[0] + "'");
	}

	public ArrayList<Article> retrieve() {
		Cursor cursor = null;
		ArrayList<Article> list = new ArrayList<Article>();
		cursor = db.rawQuery("SELECT * FROM Article", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				list.add(new Article(cursor.getString(0), 
						cursor.getString(1), cursor.getFloat(2), 
						cursor.getInt(3), cursor.getString(4)));
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
		}
		cursor.close();
		return list;
	}

	public Article find(String idArticle){
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT * FROM Article WHERE IdArticle = '" + 
				idArticle + "'", null);
		if(cursor.getCount() > 0)
			cursor.moveToFirst();
		Article a = new Article(cursor.getString(0), 
				cursor.getString(1), cursor.getFloat(2), 
				cursor.getInt(3), cursor.getString(4));
		cursor.close();
		return a;
	}
	
	public ArrayList<Article> filterQtte(int qtte){
		Cursor cursor = null;
		ArrayList<Article> list = new ArrayList<Article>();
		cursor = db.rawQuery("SELECT * FROM Article WHERE QtteEnStock = '" + 
				qtte + "'", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				list.add(new Article(cursor.getString(0), 
						cursor.getString(1), cursor.getFloat(2), 
						cursor.getInt(3), cursor.getString(4)));
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
			cursor.close();
			return list;
		}
		cursor.close();
		return null;
	}
	
	public ArrayList<Article> filterPrix(float prix){
		Cursor cursor = null;
		ArrayList<Article> list = new ArrayList<Article>();
		cursor = db.rawQuery("SELECT * FROM Article WHERE Prix = '" + 
				prix + "'", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				list.add(new Article(cursor.getString(0), 
						cursor.getString(1), cursor.getFloat(2), 
						cursor.getInt(3), cursor.getString(4)));
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
			cursor.close();
			return list;
		}
		cursor.close();
		return null;
	}
	
	public ArrayList<Article> sort(String col, String order){
		Cursor cursor = null;
		ArrayList<Article> list = new ArrayList<Article>();
		cursor = db.rawQuery("SELECT * FROM Article ORDER BY " + col + 
				" " + order, null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				list.add(new Article(cursor.getString(0), 
						cursor.getString(1), cursor.getFloat(2), 
						cursor.getInt(3), cursor.getString(4)));
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
			cursor.close();
			return list;
		}
		cursor.close();
		return null;
	}
	
	public String[] retrieveArray(){
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT IdArticle || ' - ' || " +
				"Libelle FROM Article", null);
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
	
	public String[] findLibellePrix(String idArticle){
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT Libelle, Prix FROM Article " +
				"WHERE IdArticle = '" + idArticle + "'", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			String str[] = new String[]{ cursor.getString(0), 
					String.valueOf(cursor.getFloat(1)) };
			cursor.close();
			return str;
		}
		cursor.close();
		return null;
	}
	
	public int findQtte(String idArticle){
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT QtteEnStock FROM Article " +
				"WHERE IdArticle = '" + idArticle + "'", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			int n = cursor.getInt(0);
			cursor.close();
			return n;
		}
		cursor.close();
		return 0;
	}
	
	public void setQtte(String idArticle, int qtte){
		int newQtte = findQtte(idArticle);
		newQtte -= qtte;
		db.execSQL("UPDATE Article SET QtteEnStock=? WHERE IdArticle=?", 
				new Object[]{newQtte, idArticle});
	}
	
	public String getLastIdArticle(){
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT IdArticle FROM Article " +
				"ORDER BY IdArticle DESC LIMIT 1", null);
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
