package com.nrk.mobiso;

import org.xml.sax.XMLReader;

import android.text.Editable;
import android.text.Html.TagHandler;

public class TagParser implements TagHandler{

	@Override
	public void handleTag(boolean opening, String tag, Editable output,
			XMLReader xmlReader) {
		//if(tag.equalsIgnoreCase("pre"))
	}

}
