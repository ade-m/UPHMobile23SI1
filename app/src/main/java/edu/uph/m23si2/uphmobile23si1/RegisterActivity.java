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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    EditText edtNama,edtStudentID,edtTempatLahir,edtTanggalLahir,edtProdi,edtTelepon;
    Button btnRegister;
    TextView txvLogin;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        edtNama = findViewById(R.id.edtNama);
        edtStudentID = findViewById(R.id.edtStudentID);
        edtTempatLahir = findViewById(R.id.edtTempatLahir);
        edtTanggalLahir = findViewById(R.id.edtTanggalLahir);
        edtProdi = findViewById(R.id.edtProdi);
        edtTelepon = findViewById(R.id.edtTelepon);

        btnRegister = findViewById(R.id.btnRegister);
        txvLogin = findViewById(R.id.txvLogin);
        txvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to login
                toLogin();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // register>>>>
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString();
                register(email,password);

                //proses simpan data student
                String nama = edtNama.getText().toString().trim();
                String studentID = edtStudentID.getText().toString().trim();
                String tempatLahir = edtTempatLahir.getText().toString().trim();
                String tanggalLahir = edtTanggalLahir.getText().toString().trim();
                String prodi = edtProdi.getText().toString().trim();
                String telepon = edtTelepon.getText().toString().trim();
                simpanDataStudent(email,nama,studentID,tempatLahir,
                        tanggalLahir,prodi,telepon);
            }
        });
    }

    private void simpanDataStudent(String email, String nama,String studentID,
                                   String tempatLahir, String tanggalLahir,
                                   String prodi, String telepon){
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        Map<String, Object> student = new HashMap<>();
        student.put("email",email);
        student.put("nama",nama);
        student.put("studentID",studentID);
        student.put("tempatLahir",tempatLahir);
        student.put("tanggalLahir",tanggalLahir);
        student.put("programStudi",prodi);
        student.put("nomorTelepon",telepon);

        database.collection("student")
                .add(student)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("STUDENT", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("STUDENT", "Error adding document", e);
                    }
                });


    }
    private void register(String email, String password){
                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this,"Register Berhasil",
                                            Toast.LENGTH_LONG).show();
                                    toLogin();
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this,"Register Gagal",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
    }
    private void toLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}