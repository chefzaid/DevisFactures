package com.devisfactures.gui;

import com.devisfactures.dao.InfosDAO;
import com.devisfactures.model.Infos;
import com.devisfactures.service.DBService;
import com.devisfactures.service.FileChooser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OptionInfosView extends Activity {

	Infos infos = new Infos();
	InfosDAO iDAO = new InfosDAO();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.optioninfos);
		
		if(!DBService.isTableEmpty("Infos"))
			fillComponents();
		
		Button btnValider = (Button)findViewById(R.id.btnOptionInfosValider);
		btnValider.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				infos.setRaisonSociale(((EditText)findViewById(
						R.id.txtOptionInfosRaisonSociale)).getText());
				infos.setAdresse(((EditText)findViewById(
						R.id.txtOptionInfosAdr)).getText());
				infos.setEmail(((EditText)findViewById(
						R.id.txtOptionInfosEmail)).getText());
				infos.setTel(((EditText)findViewById(
						R.id.txtOptionInfosTel)).getText());
				infos.setFax(((EditText)findViewById(
						R.id.txtOptionInfosFax)).getText());
				infos.setNotes(((EditText)findViewById(
						R.id.txtOptionInfosNotes)).getText());
				try{
					infos.setTva(Float.parseFloat(((EditText)findViewById(
							R.id.txtOptionInfosTVA)).getText().toString()));
				}catch(Exception e){
					Toast.makeText(OptionInfosView.this, "TVA invalide !", 
			    			Toast.LENGTH_SHORT).show();
				}
				infos.setDevise(((EditText)findViewById(
						R.id.txtOptionInfosDevise)).getText());
				infos.setLogo(((EditText)findViewById(
						R.id.txtOptionInfosLogo)).getText());
				if(DBService.isTableEmpty("Infos"))
					iDAO.create(infos);
				else
					iDAO.update(infos);
				Toast.makeText(OptionInfosView.this, "Mise à jour effectuée", 
		    			Toast.LENGTH_SHORT).show();
				fillComponents();
		}});
		
		Button btnLogo = (Button)findViewById(R.id.btnOptionInfosLogo);
		btnLogo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent fileChooserIntent = new Intent(OptionInfosView.this, 
						FileChooser.class);
				startActivityForResult(fileChooserIntent, 1);
		}});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, 
			Intent data){
		if(resultCode == RESULT_OK){
			String logoFilename = data.getExtras().getString("filename");
			((EditText)findViewById(R.id.txtOptionInfosLogo))
					.setText(logoFilename);
		}
	}
	
	public void fillComponents(){
		infos = iDAO.retrieve();
		((EditText)findViewById(R.id.txtOptionInfosRaisonSociale)).setText(
				infos.getRaisonSociale());
		((EditText)findViewById(R.id.txtOptionInfosAdr)).setText(
				infos.getAdresse());
		((EditText)findViewById(R.id.txtOptionInfosEmail)).setText(
				infos.getEmail());
		((EditText)findViewById(R.id.txtOptionInfosTel)).setText(
				infos.getTel());
		((EditText)findViewById(R.id.txtOptionInfosFax)).setText(
				infos.getFax());
		((EditText)findViewById(R.id.txtOptionInfosNotes)).setText(
				infos.getNotes());
		((EditText)findViewById(R.id.txtOptionInfosTVA)).setText(
				String.valueOf(infos.getTva()));
		((EditText)findViewById(R.id.txtOptionInfosDevise)).setText(
				infos.getDevise());
		((EditText)findViewById(R.id.txtOptionInfosLogo)).setText(
				infos.getLogo());
	}
	
}
