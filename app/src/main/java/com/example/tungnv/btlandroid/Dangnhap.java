package com.example.tungnv.btlandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tungnv.btlandroid.common.Common;
import com.example.tungnv.btlandroid.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class Dangnhap extends AppCompatActivity {
    EditText edtphone, edtpassword;
    Button btnDangnhap;
    CheckBox ckbRemember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);

        edtphone = (MaterialEditText)findViewById(R.id.phone);
        edtpassword = (MaterialEditText)findViewById(R.id.password);
        btnDangnhap = (Button)findViewById(R.id.Dangnhap);
        ckbRemember = (CheckBox)findViewById(R.id.ckbnho);
        //Init paper
        Paper.init(this);

        //Trong firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isConnectedToInternet(getBaseContext())) {

                    //Lưu tài khoản và mật khẩu
                    if (ckbRemember.isChecked()){
                        Paper.book().write(Common.USER_KEY, edtphone.getText().toString());
                        Paper.book().write(Common.PWD_KEY, edtpassword.getText().toString());
                    }

                    final ProgressDialog mDialog = new ProgressDialog(Dangnhap.this);
                    mDialog.setMessage("Xin chờ chút...");
                    mDialog.show();

                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (edtphone.length() == 0 || edtpassword.length() == 0) {
                                mDialog.dismiss();
                                Toast.makeText(Dangnhap.this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
                            } else

                                //Kiểm tra tài khoản có trong database không
                                if (dataSnapshot.child(edtphone.getText().toString()).exists()) {
                                    //Lấy thông tin ng dùng
                                    mDialog.dismiss();
                                    User user = dataSnapshot.child(edtphone.getText().toString()).getValue(User.class);

                                    user.setPhone(edtphone.getText().toString()); //set phone

                                    if (user.getPassword().equals(edtpassword.getText().toString())) {
                                        Intent homeIntent = new Intent(Dangnhap.this, Home.class);
                                        Common.curentUser = user;
                                        startActivity(homeIntent);
                                        finish();

                                        table_user.removeEventListener(this);
                                    } else {
                                        Toast.makeText(Dangnhap.this, "Sai mật khẩu !!!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(Dangnhap.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                                }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    Toast.makeText(Dangnhap.this, "Vui lòng kiểm tra kết nối !!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
