package app.example.activity;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SchoolOrCity extends Activity {

	private Button button, button2;
	private Animation animation_hide, animation_show, animation_enlarge;

	private ImageView imageView;
	private TextView textView1, textView2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.register_head);

		button = (Button) findViewById(R.id.register_head_for_individual);
		button2 = (Button) findViewById(R.id.register_head_for_institution);
		imageView = (ImageView) findViewById(R.id.iv);
		textView1 = (TextView) findViewById(R.id.tv1);
		textView2 = (TextView) findViewById(R.id.tv2);

		animation_hide = AnimationUtils.loadAnimation(this,
				R.anim.register_fly_anim_one);
		animation_show = AnimationUtils.loadAnimation(this,
				R.anim.register_fly_anim_two);
		animation_enlarge = AnimationUtils.loadAnimation(this,
				R.anim.register_fly_anim_three);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Intent intent = new Intent(SchoolOrCity.this,
								Province.class);
						intent.putExtra("kind", "school");
						intent.putExtra("from", "register");
						intent.putExtra("register", "register");
						startActivity(intent);
						SchoolOrCity.this.finish();
					}
				}, 1500);

				button.startAnimation(animation_hide);

				button2.startAnimation(animation_show);

				button.bringToFront();

				imageView.startAnimation(animation_enlarge);
				textView1.startAnimation(animation_enlarge);
				textView2.startAnimation(animation_enlarge);

			}
		});

		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Handler handler2 = new Handler();
				handler2.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Intent intent = new Intent(SchoolOrCity.this,
								Province.class);
						intent.putExtra("kind", "society");
						intent.putExtra("from", "register");
						intent.putExtra("register", "register");
						startActivity(intent);
						SchoolOrCity.this.finish();
					}
				}, 1500);

				button.startAnimation(animation_hide);

				button2.startAnimation(animation_show);

				button2.bringToFront();

				imageView.startAnimation(animation_enlarge);
				textView1.startAnimation(animation_enlarge);
				textView2.startAnimation(animation_enlarge);

			}
		});

	}

}
