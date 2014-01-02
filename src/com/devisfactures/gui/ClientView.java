package com.devisfactures.gui;

import java.util.ArrayList;

import com.devisfactures.dao.ClientDAO;
import com.devisfactures.model.Client;
import com.devisfactures.service.DBService;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ClientView extends Activity{

	private Client client = new Client();
	private ClientDAO cDAO = new ClientDAO();
	private ArrayList<Client> list = new ArrayList<Client>();
	private int index = 0;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client);
		
		if(!DBService.isTableEmpty("Client")){
			list = cDAO.retrieve();
			index = list.size() - 1;
			updateScreen();
		}else
			clearComponents();
		
		if(!DevisView.currentIdClient.equals("")){
			Client c = cDAO.find(DevisView.currentIdClient.toString());
			index = getIndexOf(c);
			updateScreen();
			DevisView.currentIdClient = "";
		}

	   
		Button btnVider = (Button)findViewById(R.id.btnClientVider);
		btnVider.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				clearComponents();
		}});
		
		Button btnAjouter = (Button)findViewById(R.id.btnClientAjouter);
		btnAjouter.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				initClient();
				try{
					cDAO.create(client);
				}catch(Exception e){
					Toast.makeText(ClientView.this, "Erreur de contrainte, " +
							"ajout annulée !", Toast.LENGTH_SHORT).show();
					return;
				}
				list = cDAO.retrieve();
				index = list.size() - 1;
				updateScreen();
				Toast.makeText(ClientView.this, "Ajout effectué", 
		    			Toast.LENGTH_SHORT).show();
		}});
		
		Button btnModifier = (Button)findViewById(R.id.btnClientModifier);
		btnModifier.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				initClient();
				try{
					cDAO.update(client);
				}catch(Exception e){
					Toast.makeText(ClientView.this, "Erreur de contrainte, " +
							"modification annulée !", 
							Toast.LENGTH_SHORT).show();
					return;
				}
				list = cDAO.retrieve();
				updateScreen();
				Toast.makeText(ClientView.this, "Modification effectuée", 
		    			Toast.LENGTH_SHORT).show();
		}});
		
		Button btnSuppr = (Button)findViewById(R.id.btnClientSupprimer);
		btnSuppr.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				initClient();
				try{
					cDAO.delete(client.getIdClient().toString());
				}catch(Exception e){
					Toast.makeText(ClientView.this, "Erreur de contrainte, " +
							"suppression annulée !", Toast.LENGTH_SHORT).show();
					return;
				}
				list = cDAO.retrieve();
				if(index > 0)
					index--;
				else if(index == 0 && list.size() == 0)
					clearComponents();
				updateScreen();
				Toast.makeText(ClientView.this, "Suppression effectuée", 
		    			Toast.LENGTH_SHORT).show();
		}});
		
		Button btnPrem = (Button)findViewById(R.id.btnClientPremier);
		btnPrem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				index = 0;
				updateScreen();
		}});
		
		Button btnDern = (Button)findViewById(R.id.btnClientDernier);
		btnDern.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				index = list.size() - 1;
				updateScreen();
		}});
		
		Button btnPrec = (Button)findViewById(R.id.btnClientPrecedent);
		btnPrec.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(index != 0)
					index--;
				updateScreen();
		}});
		
		Button btnSuiv = (Button)findViewById(R.id.btnClientSuivant);
		btnSuiv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(index != list.size() - 1)
					index++;
				updateScreen();
		}});
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.clientmenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.optionClientRecherchercher :
			Intent optionRechercherIntent = new Intent(ClientView.this, 
					OptionRechercherView.class);
			startActivityForResult(optionRechercherIntent, 1);
			return true;
		case R.id.optionClientAppel :
			initClient();
			Intent callIntent = new Intent(Intent.ACTION_CALL, 
					Uri.parse("tel:" + cDAO.find(client.getIdClient()
					.toString()).getTel().toString().trim())); 
			startActivity(callIntent);
			return true;
		default :
			 return super.onOptionsItemSelected(item);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, 
			Intent data){
		if(resultCode == RESULT_OK){
			Client c = (Client)data.getExtras().get("client");
			index = getIndexOf(c);
			updateScreen();
		}
	}

	private void initClient(){
		client.setIdClient(((EditText)findViewById(
				R.id.txtClientId)).getText());
		client.setRaisonSociale(((EditText)findViewById(
				R.id.txtClientRaisonSociale)).getText());
		client.setAdresse(((EditText)findViewById(
				R.id.txtClientAdr)).getText().toString());
		client.setEmail(((EditText)findViewById(
				R.id.txtClientEmail)).getText().toString());
		client.setTel(((EditText)findViewById(R.id.txtClientTel)).getText());
		client.setFax(((EditText)findViewById(R.id.txtClientFax)).getText());
		client.setNotes(((EditText)findViewById(
				R.id.txtClientNotes)).getText());
	}
	
	private void updateScreen(){
		if(!list.isEmpty()){
			Client c = list.get(index);
			fillComponents(c);
			changeButtonsState();
		}else
			changeButtonsState();
	}
	
	private void changeButtonsState(){
		if(index == 0){
			((Button)findViewById(R.id.btnClientSuivant)).setEnabled(true);
			((Button)findViewById(R.id.btnClientDernier)).setEnabled(true);
			((Button)findViewById(R.id.btnClientPrecedent)).setEnabled(false);
			((Button)findViewById(R.id.btnClientPremier)).setEnabled(false);
		}else if(index == list.size() - 1 || index == list.size()){
			((Button)findViewById(R.id.btnClientSuivant)).setEnabled(false);
			((Button)findViewById(R.id.btnClientDernier)).setEnabled(false);
			((Button)findViewById(R.id.btnClientPrecedent)).setEnabled(true);
			((Button)findViewById(R.id.btnClientPremier)).setEnabled(true);
		}else{
			((Button)findViewById(R.id.btnClientPremier)).setEnabled(true);
			((Button)findViewById(R.id.btnClientDernier)).setEnabled(true);
			((Button)findViewById(R.id.btnClientPrecedent)).setEnabled(true);
			((Button)findViewById(R.id.btnClientSuivant)).setEnabled(true);
		}
		
		if(list.size() == 1 || list.size() == 0){ 
			((Button)findViewById(R.id.btnClientSuivant)).setEnabled(false);
			((Button)findViewById(R.id.btnClientDernier)).setEnabled(false);
			((Button)findViewById(R.id.btnClientPrecedent)).setEnabled(false);
			((Button)findViewById(R.id.btnClientPremier)).setEnabled(false);
		}
		if(index == 1){
			((Button)findViewById(R.id.btnClientPrecedent)).setEnabled(true);
			((Button)findViewById(R.id.btnClientPremier)).setEnabled(true);
		}
	}
	
	private void fillComponents(Client c){
		((EditText)findViewById(R.id.txtClientId)).setText(c.getIdClient());
		((EditText)findViewById(R.id.txtClientRaisonSociale))
				.setText(c.getRaisonSociale());
		((EditText)findViewById(R.id.txtClientAdr)).setText(c.getAdresse());
		((EditText)findViewById(R.id.txtClientEmail)).setText(c.getEmail());
		((EditText)findViewById(R.id.txtClientTel)).setText(c.getTel());
		((EditText)findViewById(R.id.txtClientFax)).setText(c.getFax());
		((EditText)findViewById(R.id.txtClientNotes)).setText(c.getNotes());
		index = getIndexOf(c);
	}
	
	private void clearComponents(){
		String newId = new String();
		if(cDAO.getLastIdClient() != null){
			String str = cDAO.getLastIdClient().substring(1);
			int id = Integer.parseInt(str) + 1;
			newId = "C" + String.format("%03d", id);
		}else
			newId = "C001";
		
		((EditText)findViewById(R.id.txtClientId)).setText(newId);
		((EditText)findViewById(R.id.txtClientRaisonSociale)).setText("");
		((EditText)findViewById(R.id.txtClientAdr)).setText("");
		((EditText)findViewById(R.id.txtClientEmail)).setText("");
		((EditText)findViewById(R.id.txtClientTel)).setText("");
		((EditText)findViewById(R.id.txtClientFax)).setText("");
		((EditText)findViewById(R.id.txtClientNotes)).setText("");
		((EditText)findViewById(R.id.txtClientRaisonSociale)).requestFocus();
		index = list.size();
		changeButtonsState();
	}
	
	private int getIndexOf(Client c){
		for(int i=0; i<list.size(); i++){
			if(list.get(i).getIdClient().equals(c.getIdClient())){
				return i;
			}
		}
		return 0;
	}
	
}

