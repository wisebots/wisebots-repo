package com.wisebots.core.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.wisebots.dataset.Neuron;

public class FilePersistence{
	
	private static final String path = "/Users/adrianobrito/Documentos/Brain/";

	public Neuron query(int[] state) {
		// TODO Auto-generated method stub
		return null;
	}

	public void learn(Neuron neuron) {
		try {
			JSONObject neurofile = new JSONObject();
			neurofile.put(neuron.getInstance(), neuron);
			
			FileOutputStream fos = new FileOutputStream(path + neuron.getInstance());
			fos.write(neurofile.toString().getBytes());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
