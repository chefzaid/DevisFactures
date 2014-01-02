package com.devisfactures.model;

import java.io.Serializable;

public class FactureArticle implements IModel, Serializable{
	
	private static final long serialVersionUID = 1L;
	private CharSequence idFacture;
	private CharSequence idArticle;
	private int qtte;
	
	public FactureArticle() { }
	
	public FactureArticle(CharSequence idFacture, CharSequence idArticle, 
			int qtte) {
		this.idFacture = idFacture;
		this.idArticle = idArticle;
		this.qtte = qtte;
	}

	public CharSequence getIdFacture() {
		return idFacture;
	}

	public void setIdFacture(CharSequence idFacture) {
		this.idFacture = idFacture;
	}

	public CharSequence getIdArticle() {
		return idArticle;
	}

	public void setIdArticle(CharSequence idArticle) {
		this.idArticle = idArticle;
	}

	public int getQtte() {
		return qtte;
	}

	public void setQtte(int qtte) {
		this.qtte = qtte;
	}
	
	public Object[] toArray(){
		return new Object[]{ idFacture, idArticle, qtte };
	}
}
