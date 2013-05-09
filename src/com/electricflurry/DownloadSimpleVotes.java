package com.electricflurry;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

public class DownloadSimpleVotes extends AsyncTask<Object, Void, BaseAdapter> {

	@Override
	protected BaseAdapter doInBackground(Object... params) {
		String url = (String)params[0];
		BaseAdapter adapter = (BaseAdapter)params[1];
		try {
			DocumentBuilderFactory dbf  = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(url);
			
			if(adapter instanceof SimpleVoteBaseAdapter)
				((SimpleVoteBaseAdapter)adapter).consumeXml(doc);
			else
				((ComplicatedVoteBaseAdapter)adapter).consumeXml(doc);
			
		}
		catch (ParserConfigurationException e) {
			Log.e("VoteFragment", "A ParserConfigurationException occurred!!!!!!!!!");
			return null;
		}
		catch (IOException e) {
			Log.e("VoteFragment", "An IOException occurred!!!!!!!!!");
			return null;
		}
		catch (SAXException e) {
			Log.e("VoteFragment", "A SAXException occurred!!!!!!!!!");
			return null;
		}
		
		
		return adapter;
	}
	
	protected void onPostExecute(BaseAdapter adapter) {
		if(adapter == null) {
			Log.e("DownloadSimpleVotes", "The adapter came back null meaning there was an error!!");
		} else
			adapter.notifyDataSetChanged();
     }
	
}//end of DownloadSimpleVotes
