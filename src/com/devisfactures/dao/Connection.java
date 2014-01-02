package com.devisfactures.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Connection {
	
	private static SQLiteDatabase db;
	
	private Connection(Context context){
		db = context.openOrCreateDatabase("/sdcard/devisfactures.db", 
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		db.beginTransaction();
		String query[] = new String[]{
			"CREATE TABLE IF NOT EXISTS Client( IdClient TEXT NOT NULL PRIMARY KEY, RaisonSociale TEXT, Adresse TEXT, Email TEXT, Tel TEXT, Fax TEXT, Notes TEXT);",
			"CREATE TABLE IF NOT EXISTS Article( IdArticle TEXT NOT NULL PRIMARY KEY, Libelle TEXT, Prix REAL, QtteEnStock INTEGER, Notes TEXT);",
			"CREATE TABLE IF NOT EXISTS Devis( IdDevis TEXT NOT NULL PRIMARY KEY, Date INTEGER, PrixHT REAL, Facture INTEGER, IdClient TEXT NOT NULL, Notes TEXT, FOREIGN KEY(IdClient) REFERENCES Client(IdClient));",
			"CREATE TABLE IF NOT EXISTS Facture( IdFacture TEXT NOT NULL PRIMARY KEY, Date INTGEGER, PrixHT REAL, Paye INTEGER, IdClient TEXT NOT NULL, Notes TEXT, FOREIGN KEY(IdClient) REFERENCES Client(IdClient));",
			"CREATE TABLE IF NOT EXISTS DevisArticle(IdDevis TEXT NOT NULL, IdArticle TEXT NOT NULL, Qtte INTEGER, PRIMARY KEY(IdDevis, IdArticle), FOREIGN KEY(IdDevis) REFERENCES Devis(IdDevis) ON DELETE CASCADE, FOREIGN KEY(IdArticle) REFERENCES Article(IdArticle));",
			"CREATE TABLE IF NOT EXISTS FactureArticle(IdFacture TEXT NOT NULL, IdArticle TEXT NOT NULL, Qtte INTEGER, PRIMARY KEY(IdFacture, IdArticle), FOREIGN KEY(IdFacture) REFERENCES Facture(IdFacture) ON DELETE CASCADE, FOREIGN KEY(IdArticle) REFERENCES Article(IdArticle));",
			"CREATE TABLE IF NOT EXISTS Infos( RaisonSociale TEXT, Adresse TEXT, Email TEXT, Tel TEXT, Fax TEXT, Notes TEXT, TVA REAL, Devise TEXT, Logo TEXT);"
		};
		for(int i=0; i<7; i++)
			db.execSQL(query[i]);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public static SQLiteDatabase getDB(Context context){
		if(db == null && context != null)
			new Connection(context);
		return db;
	}
}
