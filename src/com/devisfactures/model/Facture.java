package com.devisfactures.model;

import java.io.Serializable;
import java.util.Date;

public class Facture implements IModel, Serializable{

	private static final long serialVersionUID = 1L;
	private CharSequence idFacture;
	private Date date;
	private float prixHT;
	private boolean paye;
	private CharSequence idClient;
	private CharSequence notes;
	
	public Facture(){ }
	
	public Facture(CharSequence idFacture, Date date, float prixHT,
			boolean facture, CharSequence idClient, CharSequence notes) {
		this.idFacture = idFacture;
		this.date = date;
		this.prixHT = prixHT;
		this.paye = facture;
		this.idClient = idClient;
		this.notes = notes;
	}

	public CharSequence getIdFacture() {
		return idFacture;
	}

	public void setIdFacture(CharSequence idFacture) {
		this.idFacture = idFacture;
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

	public boolean isPaye() {
		return paye;
	}

	public void setPaye(boolean facture) {
		this.paye = facture;
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
		return new Object[]{ idFacture, date.getTime()/1000, prixHT, paye, 
				idClient, notes };
	}
	
}
