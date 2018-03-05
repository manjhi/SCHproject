package manjinder.sch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mDriver,mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDriver=(Button)findViewById(R.id.driver);
        mUser=(Button)findViewById(R.id.user);

        mDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DriverLogin.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,StudentLogin.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}
