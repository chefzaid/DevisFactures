package com.devisfactures.model;

import java.io.Serializable;

public class Article implements IModel, Serializable{

	private static final long serialVersionUID = 1L;
	private CharSequence idArticle;
	private CharSequence libelle;
	private float prix;
	private int qtteEnStock;
	private CharSequence notes;
	
	public Article(){ }
	
	public Article(CharSequence idArticle, CharSequence libelle, 
			float prix, int qtteEnStock, CharSequence notes) {
		this.idArticle = idArticle;
		this.libelle = libelle;
		this.prix = prix;
		this.qtteEnStock = qtteEnStock;
		this.notes = notes;
	}	

	public CharSequence getIdArticle() {
		return idArticle;
	}

	public void setIdArticle(CharSequence idArticle) {
		this.idArticle = idArticle;
	}

	public CharSequence getLibelle() {
		return libelle;
	}

	public void setLibelle(CharSequence libelle) {
		this.libelle = libelle;
	}

	public float getPrix() {
		return prix;
	}

	public void setPrix(float prix) {
		this.prix = prix;
	}

	public int getQtteEnStock() {
		return qtteEnStock;
	}

	public void setQtteEnStock(int qtteEnStock) {
		this.qtteEnStock = qtteEnStock;
	}

	public CharSequence getNotes() {
		return notes;
	}

	public void setNotes(CharSequence notes) {
		this.notes = notes;
	}
	
	public Object[] toArray(){
		return new Object[]{ idArticle, libelle, prix, qtteEnStock, notes };
	}
}
