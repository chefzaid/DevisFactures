package com.devisfactures.model;

import java.io.Serializable;

public class Client implements IModel, Serializable{

	private static final long serialVersionUID = 1L;
	protected CharSequence idClient;
	protected CharSequence raisonSociale;
	protected CharSequence adresse;
	protected CharSequence email;
	protected CharSequence tel;
	protected CharSequence fax;
	protected CharSequence notes;
	
	public Client(){ }
	
	public Client(CharSequence idClient, CharSequence raisonSociale,
			CharSequence adresse, CharSequence email, CharSequence tel,
			CharSequence fax, CharSequence notes) {
		this.idClient = idClient;
		this.raisonSociale = raisonSociale;
		this.adresse = adresse;
		this.email = email;
		this.tel = tel;
		this.fax = fax;
		this.notes = notes;
	}

	public CharSequence getIdClient() {
		return idClient;
	}

	public void setIdClient(CharSequence idClient) {
		this.idClient = idClient;
	}

	public CharSequence getRaisonSociale() {
		return raisonSociale;
	}

	public void setRaisonSociale(CharSequence raisonSociale) {
		this.raisonSociale = raisonSociale;
	}

	public CharSequence getAdresse() {
		return adresse;
	}

	public void setAdresse(CharSequence adresse) {
		this.adresse = adresse;
	}

	public CharSequence getEmail() {
		return email;
	}

	public void setEmail(CharSequence email) {
		this.email = email;
	}

	public CharSequence getTel() {
		return tel;
	}

	public void setTel(CharSequence tel) {
		this.tel = tel;
	}

	public CharSequence getFax() {
		return fax;
	}

	public void setFax(CharSequence fax) {
		this.fax = fax;
	}

	public CharSequence getNotes() {
		return notes;
	}

	public void setNotes(CharSequence notes) {
		this.notes = notes;
	}
	
	public Object[] toArray(){
		return new Object[]{ idClient, raisonSociale, adresse, email, tel, 
				fax, notes };
	}
	
}
