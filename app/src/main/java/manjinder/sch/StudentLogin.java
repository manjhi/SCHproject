package manjinder.sch;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class StudentLogin extends AppCompatActivity {

    private EditText mEmail,mPassword;
    private Button mLogin;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        mAuth=FirebaseAuth.getInstance();
        firebaseAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
//                    String user_id=mAuth.getCurrentUser().getUid();
//                    DatabaseReference current_user_db= FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(user_id);
//                    Map mPushdata=new HashMap();
//                    mPushdata.put("Email",mEmail);

//                    current_user_db.setValue(true);
                    Intent intent=new Intent(StudentLogin.this,DriverMap.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
        mEmail=(EditText)findViewById(R.id.email);
        mPassword=(EditText)findViewById(R.id.password);

        mLogin=(Button)findViewById(R.id.login);

        mLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String email=mEmail.getText().toString();
                final String password=mPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(StudentLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(StudentLogin.this,"Signup Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
