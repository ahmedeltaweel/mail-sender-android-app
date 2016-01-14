package com.midnait.midnait;

import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

public class MainActivity extends AppCompatActivity {

	//todo add the email and password of sender mail
	private static final String USERNAME = "";
	private static final String PASSWORD = "";

	EditText email;
	Button submit_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		email = (EditText) findViewById(R.id.email_txt);
		submit_btn = (Button) findViewById(R.id.submit_btn);

		submit_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isNetworkConnected()) {
					if (!isValidEmail(email.getText().toString())) {
						email.setTextColor(Color.RED);
						showToast("Please, Enter a valid email");
					} else {
						String mail = email.getText().toString();
						sendEmail(mail);
					}
				} else {
					showToast("Please, Connect to internet");
				}
			}
		});
	}

	/**
	 * validate email address
	 *
	 * @param target
	 * @return
	 */
	private final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	/**
	 * show toast message
	 *
	 * @param message
	 */
	private void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}

	/**
	 * sending email
	 *
	 * @param email
	 */
	private void sendEmail(String email) {
		new SendEmailAsyncTask(email).execute();
	}

	/**
	 * sending email
	 */
	class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
		Mail m = new Mail(USERNAME, PASSWORD);

		public SendEmailAsyncTask(String email) {
			if (BuildConfig.DEBUG)
				Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");
			m.setFrom(USERNAME);
			m.addTo(email);
			m.setSubject("Android Mail");
			m.setBody("Hello this is mail from android");
			showToast("Email Sent Successfully");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");
			try {
				m.send();
				return true;
			} catch (AuthenticationFailedException e) {
				Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
				e.printStackTrace();
				return false;
			} catch (MessagingException e) {
				Log.e(SendEmailAsyncTask.class.getName(), "failed");
				e.printStackTrace();
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * check for internet
	 *
	 * @return bool
	 */
	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo() != null;
	}
}
