package edu.uph.m23si2.uphmobile23si1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView txvRegister;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtEmail= findViewById(R.id.edtEmail);
        edtPassword= findViewById(R.id.edtPassword);
        btnLogin= findViewById(R.id.btnLogin);
        txvRegister= findViewById(R.id.txvRegister);
        txvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to register
                toRegister();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString();
                login(email,password);
            }
        });
    }
    private void login(String email,String password){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //to main menu
                            toMain();
                        }else{
                            Toast.makeText(LoginActivity.this,
                                    "Login Gagal, silahkan cek kembali email dan password",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void toRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    private void toMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}