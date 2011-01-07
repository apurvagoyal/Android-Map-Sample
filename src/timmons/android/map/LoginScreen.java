package timmons.android.map;



import org.apache.http.impl.client.DefaultHttpClient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class LoginScreen extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	protected AccountManager accountManager;
	protected Account account;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        accountManager = AccountManager.get(this);
        final Intent intent = getIntent();
        Account[] accounts = accountManager.getAccountsByType("com.google");
        account= (Account)accounts[0];
        //create the database
        
        View loginButton=findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
    }

    public void exitButtonClick(View v)
	 {
		 finish();
		 
	 }
	@Override
	public void onClick(View view) {
		
		DefaultHttpClient http_client = new DefaultHttpClient();
		//accountManager.getAuthToken(account, "ah", false, new GetAuthTokenCallback(), null);
		
		
		EditText userNameTextView=(EditText)findViewById(R.id.userNameText);
		EditText passwordTextView=(EditText)findViewById(R.id.passwordText);
		
		if(userNameTextView.getText().toString().equals("testuser") && passwordTextView.getText().toString().equals("test"))
		{
			Intent i=new Intent(this,MainMenu.class);
			startActivity(i);
		}
		else
		{
			AlertDialog alertDialog=new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Invalid Login");
			alertDialog.setMessage("Invalid username or password");
			alertDialog.show();
			
		}
		
	}
}