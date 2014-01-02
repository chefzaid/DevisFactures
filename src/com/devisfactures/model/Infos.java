package com.devisfactures.model;

import java.io.Serializable;

public class Infos extends Client implements IModel, Serializable {

	private static final long serialVersionUID = 1L;
	private float tva;
	private CharSequence devise;
	private CharSequence logo;
	
	public Infos(){ }
	
	public Infos(CharSequence raisonSociale, CharSequence adresse, 
			CharSequence email, CharSequence tel, CharSequence fax, 
			CharSequence notes, float tva, CharSequence devise, 
			CharSequence logo) {
		super("", raisonSociale, adresse, email, tel, fax, notes);
		this.tva = tva;
		this.devise = devise;
		this.logo = logo;
	}
	
	public float getTva() {
		return tva;
	}

	public void setTva(float tva) {
		this.tva = tva;
	}

	public CharSequence getDevise() {
		return devise;
	}

	public void setDevise(CharSequence devise) {
		this.devise = devise;
	}

	public CharSequence getLogo() {
		return logo;
	}
	
	public void setLogo(CharSequence logo) {
		this.logo = logo;
	}
	
	public Object[] toArray(){
		return new Object[]{raisonSociale, adresse, email, tel, fax, notes, 
				tva, devise, logo};
	}

}
