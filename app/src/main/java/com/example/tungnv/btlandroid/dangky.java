package com.example.tungnv.btlandroid;

import android.app.ProgressDialog;
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
import com.rengwuxian.materialedittext.MaterialEditText;

public class dangky extends AppCompatActivity {

    MaterialEditText edtphone, edtname, edtpassword;
    Button btnDangky;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);

        edtname = (MaterialEditText)findViewById(R.id.name);
        edtphone = (MaterialEditText)findViewById(R.id.phone);
        edtpassword = (MaterialEditText)findViewById(R.id.password);
        btnDangky = (Button)findViewById(R.id.Dangky);

        //Trong firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    final ProgressDialog mDialog = new ProgressDialog(dangky.this);
                    mDialog.setMessage("Xin chờ chút...");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (edtname.length() == 0 || edtpassword.length() == 0 || edtphone.length() == 0) {
                                mDialog.dismiss();
                                Toast.makeText(dangky.this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
                            } else if (dataSnapshot.child(edtphone.getText().toString()).exists()) {
                                //Kiểm tra SDt đã tồn tại
                                mDialog.dismiss();
                                Toast.makeText(dangky.this, "Số điện thoại đã tồn tại", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();
                                User user = new User(edtname.getText().toString(), edtpassword.getText().toString());
                                table_user.child(edtphone.getText().toString()).setValue(user);
                                Toast.makeText(dangky.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else{
                    Toast.makeText(dangky.this, "Vui lòng kiểm tra kết nối !!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
