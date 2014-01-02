package com.devisfactures.model;

import java.io.Serializable;

public class DevisArticle implements IModel, Serializable {
	
	private static final long serialVersionUID = 1L;
	private CharSequence idDevis;
	private CharSequence idArticle;
	private int qtte;
	
	public DevisArticle() { }
	
	public DevisArticle(CharSequence idDevis, CharSequence idArticle, 
			int qtte) {
		this.idDevis = idDevis;
		this.idArticle = idArticle;
		this.qtte = qtte;
	}

	public CharSequence getIdDevis() {
		return idDevis;
	}

	public void setIdDevis(CharSequence idDevis) {
		this.idDevis = idDevis;
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
		return new Object[]{ idDevis, idArticle, qtte };
	}

}
