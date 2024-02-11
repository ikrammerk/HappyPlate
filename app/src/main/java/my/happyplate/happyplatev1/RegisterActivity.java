package my.happyplate.happyplatev1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText editEmail, editPassword, editRePassword;
    Button btDangky;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editEmail = findViewById(R.id.modifiermail);
        editPassword = findViewById(R.id.editPassword);
        editRePassword = findViewById(R.id.modifierpsswd);
        btDangky = findViewById(R.id.buttonikram);

        auth = FirebaseAuth.getInstance();


        btDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String StrEmail = editEmail.getText().toString();
                String StrPassword = editPassword.getText().toString();
                String StrRePassword = editRePassword.getText().toString();

                if(StrPassword.equals(StrRePassword)){
                    if(TextUtils.isEmpty(StrEmail)||TextUtils.isEmpty(StrPassword)){
                        Toast.makeText(RegisterActivity.this,"Entrez vos données", Toast.LENGTH_SHORT).show();
                    }else if(StrPassword.length()<6){
                        Toast.makeText(RegisterActivity.this,"Mot de passe trop court", Toast.LENGTH_SHORT).show();
                    }else {
                        Register(StrEmail,StrPassword);
                    }
                }else {
                    Toast.makeText(RegisterActivity.this,"Confirmez votre mot de passe", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private  void  Register(String txtEmail, String txtPassword){
        auth.createUserWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Inscription réussie",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    finish();
                }else {
                    Toast.makeText(RegisterActivity.this,"Inscription échouée", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void login(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}