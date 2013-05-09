package com.electricflurry;

import android.database.Cursor;

interface ConsumeCursor {
	/*
	 * This interface is here to just be implemented by anyone
	 * who needs to use my Database. This way you guys can send me
	 * any type of class that uses the below method which you write.
	 * A good example would be implementing it in a ListProfiles class or
	 * a single profiles class so it can set itself uniformly.
	 * It could have been easily done by you guys, but this standardizes that 
	 * specific part
	 * */
	
	public void consumeCursor(Cursor cursor);

}
