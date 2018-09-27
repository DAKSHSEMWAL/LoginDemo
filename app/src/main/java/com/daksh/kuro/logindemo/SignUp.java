package com.daksh.kuro.logindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.daksh.kuro.logindemo.User.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.UUID;

import info.hoang8f.widget.FButton;

public class SignUp extends AppCompatActivity {

    MaterialEditText edtPhone,edtName,edtPassword,edtDOB,edtGender,edtEmail;
    FButton btnSignUp,btnSelect,btnUpload;
    FirebaseDatabase database;
    DatabaseReference table_user;
    FirebaseStorage storage;
    User newUser;
    StorageReference storageReference;
    Uri saveuri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtName     = (MaterialEditText)findViewById(R.id.enterName);
        edtPhone    = (MaterialEditText)findViewById(R.id.enterPhone);
        edtPassword = (MaterialEditText)findViewById(R.id.enterPassword);
        edtDOB     = (MaterialEditText)findViewById(R.id.enterDate);
        edtEmail    = (MaterialEditText)findViewById(R.id.enterEmail);
        edtGender = (MaterialEditText)findViewById(R.id.enterGender);

        btnSignUp   = (FButton)findViewById(R.id.btnSignUp1);
        btnSelect =(FButton)findViewById(R.id.btnSelect);
        btnUpload =(FButton)findViewById(R.id.btnUpload);

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("data");
        storage = FirebaseStorage.getInstance();
        storageReference =storage.getReference();

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveuri != null)
                {
                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Uploading....");
                    mDialog.show();

                    String imageName = UUID.randomUUID().toString();
                    final StorageReference imageFolder = storageReference.child("images/"+imageName);
                    imageFolder.putFile(saveuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newUser = new User(edtName.getText().toString(),edtPassword.getText().toString(),edtPhone.getText().toString(), edtEmail.getText().toString(), saveuri.toString(), edtGender.getText().toString(),edtDOB.getText().toString());
                                }
                            });
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    mDialog.dismiss();
                                    Toast.makeText(SignUp.this,""+ exception.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    mDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                }
                            });
                }
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),Common.PICK_IMAGE_REQUEST);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please Wait ....");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "User Already Registered", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if(newUser != null)
                            {
                                table_user.child(edtPhone.getText().toString()).setValue(newUser);
                            }

                            Toast.makeText(SignUp.this, "Sign Up Sucessful!", Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(SignUp.this,Login.class);
                            startActivity(homeIntent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!= null)
        {
            saveuri = data.getData();
            btnSelect.setText("Image Selected");
        }
    }
}
