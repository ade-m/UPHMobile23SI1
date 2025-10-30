package edu.uph.m23si2.uphmobile23si1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    Button btnLogin,btnTambah,btnMerah,btnKuning,btnHijau;
    TextView txvRegister;
    private FirebaseAuth mAuth;
    TextView txvSuhuKelembapan;
    Boolean merah,kuning,hijau;
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
        btnTambah= findViewById(R.id.btnTambah);

        btnMerah= findViewById(R.id.btnMerah);
        btnKuning= findViewById(R.id.btnKuning);
        btnHijau= findViewById(R.id.btnHijau);

        txvSuhuKelembapan = findViewById(R.id.txvSuhuKelembapan);

        bacaRD();

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

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("message");

                myRef.setValue("Hello, World!");
            }
        });

        btnMerah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("lalulintas/merah");
                merah = !merah;
                myRef.setValue(merah);
            }
        });
        btnHijau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("lalulintas/hijau");
                hijau = !hijau;
                myRef.setValue(hijau);
            }
        });
        btnKuning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("lalulintas/kuning");
                kuning = !kuning;
                myRef.setValue(kuning);
            }
        });
    }
    private void bacaRD(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("lalulintas");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                merah = dataSnapshot.child("merah").getValue(Boolean.class);
                kuning = dataSnapshot.child("kuning").getValue(Boolean.class);
                hijau = dataSnapshot.child("hijau").getValue(Boolean.class);

                float suhu = dataSnapshot.child("suhu").getValue(float.class);
                float kelembapan = dataSnapshot.child("kelembapan").getValue(float.class);

                txvSuhuKelembapan.setText("Suhu : "+suhu +"C Kelembapan : "+ kelembapan);

                if(merah) btnMerah.setText("Matikan Lampu Merah");
                else  btnMerah.setText("Hidupkan Lampu Merah");

                if(kuning) btnKuning.setText("Matikan Lampu Kuning");
                else  btnKuning.setText("Hidupkan Lampu Kuning");

                if(hijau) btnHijau.setText("Matikan Lampu Hijau");
                else  btnHijau.setText("Hidupkan Lampu Hijau");

                Log.d("LampuLaluLintas", "Value is: " + merah);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("LampuLaluLintas", "Failed to read value.", error.toException());
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