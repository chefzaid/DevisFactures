package com.devisfactures.gui;

import java.util.ArrayList;
import java.util.Date;

import com.devisfactures.dao.DevisDAO;
import com.devisfactures.model.Devis;
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

public class OptionFiltrerDevisView extends Activity{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.optionfiltrerdevis);

		Button btn = (Button)findViewById(R.id.btnOptionFiltrerDevis);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DevisDAO dDAO = new DevisDAO();
				ArrayList<Devis> list = new ArrayList<Devis>();

				if(((RadioButton)findViewById(
						R.id.radioOptionFiltrerDevisEtat)).isChecked()){
					list = dDAO.filterEtatDevis(((CheckBox)findViewById(
							R.id.ckboxOptionFiltrerDevisEtat)).isChecked());
				}else if(((RadioButton)findViewById(
						R.id.radioOptionFiltrerDevisId)).isChecked()){
					list = dDAO.filterIdClient(((EditText)findViewById(
							R.id.txtOptionFiltrerDevisId)).getText()
							.toString());
				}else if(((RadioButton)findViewById(
						R.id.radioOptionFiltrerDevisDate)).isChecked()){
					Date dateStart = new Date();
					Date dateEnd = new Date();
					try{
						dateStart = DBService.parseDate(((EditText)findViewById(
								R.id.txtOptionFiltrerDevisDate1)).getText()
								.toString());
						dateEnd = DBService.parseDate(((EditText)findViewById(
								R.id.txtOptionFiltrerDevisDate2)).getText()
								.toString());
						list = dDAO.filterDate(dateStart, dateEnd);
					}catch(Exception e){
						Toast.makeText(OptionFiltrerDevisView.this, 
								"Date invalide, filtrage annulé !", 
				    			Toast.LENGTH_SHORT).show();
					}
				}
				
				if(list != null){
					setResult(RESULT_OK, getIntent().putExtra("filteredDevis", 
							list));
					finish();
				}else
					Toast.makeText(OptionFiltrerDevisView.this, "Aucun résultat !", 
			    			Toast.LENGTH_SHORT).show();
		}});
	}

}
