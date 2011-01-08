package timmons.android.map;



import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

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
	//protected AccountManager accountManager;
	//protected Account account;
	protected Facebook facebook;
	private String name;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //accountManager = AccountManager.get(this);
        //final Intent intent = getIntent();
        //Account[] accounts = accountManager.getAccountsByType("com.google");
        //account= (Account)accounts[0];
        
        facebook=new Facebook("158864434161538");
       
        
        View loginButton=findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
    }

    public void exitButtonClick(View v)
	 {
    	try {
			facebook.logout(this);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 finish();
		 
	 }
	@Override
	public void onClick(View view) {
		
		//DefaultHttpClient http_client = new DefaultHttpClient();
		//accountManager.getAuthToken(account, "ah", false, new GetAuthTokenCallback(), null);
			
		facebook.authorize(this, new String[] {}, new AuthorizeListener());
	
	}
	
	public void onLoginSuccess() 
	{
		try {
			String s=facebook.request("me");
			JSONObject jObject;
			//String name;
			try {
				jObject = new JSONObject(s);
				 name= jObject.getString("name");
				
				int k=0;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Intent i=new Intent(this,MainMenu.class);
		
		i.putExtra("Name", name);
		startActivity(i);
	}
	public void onLoginError()
	{
		AlertDialog alertDialog=new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Invalid Login");
		alertDialog.setMessage("Invalid username or password");
		alertDialog.show();
	}
	
	private class AuthorizeListener implements DialogListener {
		  public void onComplete(Bundle values) {
		   //  Handle a successful login
			  
			  onLoginSuccess();
		  }

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onError(DialogError e) {
			// TODO Auto-generated method stub
			onLoginError();
		}

		@Override
		public void onFacebookError(FacebookError e) {
			// TODO Auto-generated method stub
			onLoginError();
		}
		}
}