package timmons.android.map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainMenu extends Activity implements OnClickListener{
	
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.mainmenu);
        
	        //register the button events
	        View googleMapButton=findViewById(R.id.map_google_button);
	        googleMapButton.setOnClickListener(this);
	        
	        Intent i=getIntent();
	     String name=i.getStringExtra("Name");
	     TextView nameView=(TextView)findViewById(R.id.user_name_label);
	        nameView.setText("Welcome "+ name);
	        
	        

	    }
	 
	 public void exitButtonClick(View v)
	 {
		 finish();
		 
	 }
	@Override
	public void onClick(View v) {
		
		Intent i;
		
		switch(v.getId())
		{
		case (R.id.map_google_button):
			i=new Intent(this,GPSScreen.class);
		
		startActivity(i);
		break;

		case (R.id.exit_button):
			finish();
		break;
		}
	
	}
}
