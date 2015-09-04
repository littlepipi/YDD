package app.example.aboutus;


import tx.ydd.app.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import app.example.activity.SysApplication;

public class PlatformActivity extends Activity {
	private ImageButton back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myplatform);
		SysApplication.getInstance().addActivity(this);
		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PlatformActivity.this.finish();
				PlatformActivity.this.overridePendingTransition(
						R.anim.in_from_left, R.anim.out_to_right);
			}
		});
		
	}

}
