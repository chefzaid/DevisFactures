package com.devisfactures.model;

import java.io.Serializable;
import java.util.Date;

public class Devis implements IModel, Serializable{

	private static final long serialVersionUID = 1L;
	private CharSequence idDevis;
	private Date date;
	private float prixHT;
	private boolean facture;
	private CharSequence idClient;
	private CharSequence notes;
	
	public Devis(){
		idDevis = "";
		date = new Date();
		prixHT = 0;
		facture = false;
		idClient = "";
		notes = "";
	}
	
	public Devis(CharSequence idDevis,Date date, float prixHT,
			boolean facture, CharSequence idClient, CharSequence notes) {
		this.idDevis = idDevis;
		this.date = date;
		this.prixHT = prixHT;
		this.facture = facture;
		this.idClient = idClient;
		this.notes = notes;
	}

	public CharSequence getIdDevis() {
		return idDevis;
	}

	public void setIdDevis(CharSequence idDevis) {
		this.idDevis = idDevis;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getPrixHT() {
		return prixHT;
	}

	public void setPrixHT(float prixHT) {
		this.prixHT = prixHT;
	}

	public boolean isFacture() {
		return facture;
	}

	public void setFacture(boolean facture) {
		this.facture = facture;
	}

	public CharSequence getIdClient() {
		return idClient;
	}

	public void setIdClient(CharSequence idClient) {
		this.idClient = idClient;
	}

	public CharSequence getNotes() {
		return notes;
	}

	public void setNotes(CharSequence notes) {
		this.notes = notes;
	}
	
	public Object[] toArray(){
		return new Object[]{ idDevis, date.getTime()/1000, prixHT, facture, 
				idClient, notes };
	}

}
