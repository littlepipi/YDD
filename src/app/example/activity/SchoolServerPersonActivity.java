package app.example.activity;

import tx.ydd.app.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class SchoolServerPersonActivity extends Activity {

	private ImageButton back;
	private TextView personname, personschool, persongrade, personslogan,
			personintroduction, personactivities;
	private String name;
	private String school;
	private String grade;
	private String slogan;
	private String introduction;
	private String activities;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_school_server_person);
		getdata();// ���ݻ�ȡ
		init();// �ؼ���ʼ��
		OnClickListener();// ����¼�

	}

	private void init() {
		// TODO Auto-generated method stub
		back = (ImageButton) findViewById(R.id.introduction_back);// ���ؼ�
		personname = (TextView) findViewById(R.id.name);// ����
		personschool = (TextView) findViewById(R.id.school);// ѧУ
		persongrade = (TextView) findViewById(R.id.grade);// �꼶
		personslogan = (TextView) findViewById(R.id.slogan);// �ں�
		personintroduction = (TextView) findViewById(R.id.introduction);// ��������
		personactivities = (TextView) findViewById(R.id.activities);// �

		personname.setText(name);
		personschool.setText(school);
		persongrade.setText(grade);
		personslogan.setText(slogan);
		personintroduction.setText(introduction);
		personactivities.setText(activities);

	}

	private void OnClickListener() {
		// TODO Auto-generated method stub
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void getdata() {
		// TODO Auto-generated method stub

		Bundle bundle = getIntent().getExtras();

		name = bundle.getString("name");
		school = bundle.getString("school");
		grade = bundle.getString("grade");
		slogan = bundle.getString("slogan");
		introduction = bundle.getString("introduction");
		activities = bundle.getString("activities");

	}
}
