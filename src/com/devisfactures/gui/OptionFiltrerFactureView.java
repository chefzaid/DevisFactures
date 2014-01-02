package com.devisfactures.gui;

import java.util.ArrayList;
import java.util.Date;

import com.devisfactures.dao.FactureDAO;
import com.devisfactures.model.Facture;
import com.devisfactures.service.DBService;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class OptionFiltrerFactureView extends Activity{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.optionfiltrerfacture);
		
		Button btn = (Button)findViewById(R.id.btnOptionFiltrerFacture);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FactureDAO dDAO = new FactureDAO();
				ArrayList<Facture> list = new ArrayList<Facture>();

				if(((RadioButton)findViewById(
						R.id.radioOptionFiltrerFactureEtat)).isChecked()){
					list = dDAO.filterEtatFacture(((CheckBox)findViewById(
							R.id.ckboxFacturePaye)).isChecked());
				}else if(((RadioButton)findViewById(
						R.id.radioOptionFiltrerFactureId)).isChecked()){
					list = dDAO.filterIdClient(((EditText)findViewById(
							R.id.txtOptionFiltrerFactureId)).getText()
							.toString());
				}else if(((RadioButton)findViewById(
						R.id.radioOptionFiltrerFactureDate)).isChecked()){
					Date dateStart = new Date();
					Date dateEnd = new Date();
					try{
						dateStart = DBService.parseDate(((EditText)findViewById(
								R.id.txtOptionFiltrerFactureDate1)).getText()
								.toString());
						dateEnd = DBService.parseDate(((EditText)findViewById(
								R.id.txtOptionFiltrerFactureDate2)).getText()
								.toString());
						list = dDAO.filterDate(dateStart, dateEnd);
					}catch(Exception e){
						Toast.makeText(OptionFiltrerFactureView.this, 
								"Date invalide, filtrage annulé !", 
				    			Toast.LENGTH_SHORT).show();
					}
				}
				
				if(list != null){
					setResult(RESULT_OK, getIntent().putExtra(
							"filteredFactures", list));
					finish();
				}else
					Toast.makeText(OptionFiltrerFactureView.this, 
							"Aucun résultat !", Toast.LENGTH_SHORT).show();
		}});
	}
}
