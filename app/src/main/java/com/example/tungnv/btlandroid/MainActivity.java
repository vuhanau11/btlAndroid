package com.example.tungnv.btlandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tungnv.btlandroid.common.Common;
import com.example.tungnv.btlandroid.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button btnDangky, btnDangnhap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDangky = (Button)findViewById(R.id.Dangky);
        btnDangnhap = (Button)findViewById(R.id.Dangnhap);

        //init paper
        Paper.init(this);

        btnDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dangky = new Intent(MainActivity.this,dangky.class);
                startActivity(dangky);
            }
        });

        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dangnhap = new Intent(MainActivity.this,Dangnhap.class);
                startActivity(dangnhap);
            }
        });

        //Kiểm tra lưu mật khẩu
        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);
        if (user!=null &&pwd!=null){
            if (!user.isEmpty() && !pwd.isEmpty())
                login(user, pwd);
        }
    }

    private void login(final String phone, final String pwd) {
        //Trong firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        if (Common.isConnectedToInternet(getBaseContext())) {


            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Xin chờ chút...");
            mDialog.show();

            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (phone.length() == 0 || pwd.length() == 0) {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
                    } else

                        //Kiểm tra tài khoản có trong database không
                        if (dataSnapshot.child(phone).exists()) {
                            //Lấy thông tin ng dùng
                            mDialog.dismiss();
                            User user = dataSnapshot.child(phone).getValue(User.class);

                            user.setPhone(phone); //set phone

                            if (user.getPassword().equals(pwd)) {
                                Intent homeIntent = new Intent(MainActivity.this, Home.class);
                                Common.curentUser = user;
                                startActivity(homeIntent);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "Sai mật khẩu !!!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                        }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            Toast.makeText(MainActivity.this, "Vui lòng kiểm tra kết nối !!", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
