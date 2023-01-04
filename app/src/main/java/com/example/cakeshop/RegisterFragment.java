package com.example.cakeshop;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cakeshop.databinding.FragmentRegisterBinding;


public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;

    private static final String DataBaseName = "DataBaseIt";
    private static final int DataBaseVersion = 1;
    private static String DataBaseTable = "Users";
    private static SQLiteDatabase db;
    private SqlDataBaseHelper sqlDataBaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 建立SQLiteOpenHelper物件
        sqlDataBaseHelper = new SqlDataBaseHelper(this.getContext(),DataBaseName,null,DataBaseVersion,DataBaseTable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
    public int register;
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RegisterFragment.this)
                        .navigate(R.id.action_RegisterFragment_to_LoginFragment);
            }
        });
        binding.RegisterUserbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = sqlDataBaseHelper.getWritableDatabase(); // 開啟資料庫
                String account = binding.EditUserName.getText().toString();
                String password = binding.registerPassword.getText().toString();
                String Check_password = binding.RegisterCheckPassword.getText().toString();

                String [] projection = {
                        "account"
                };
                String selection = "account = ?";
                String [] selectionArgs = {account};
                Cursor cursor = db.query(
                  DataBaseTable,
                  projection,
                  selection,
                  selectionArgs,
                  null,
                  null,
                  null
                );
                int cursorCount = cursor.getCount();

                if (password.equals(Check_password)){
                    if (cursorCount > 0 ){
                        Toast.makeText(getActivity(), "帳號已被註冊", Toast.LENGTH_SHORT).show();
                        getDialog(register=3);
                    }
                    else{
                        long id;
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("account",account);
                        contentValues.put("password",password);
                        id = db.insert(DataBaseTable,null,contentValues);
                        Toast.makeText(getActivity(), "註冊成功", Toast.LENGTH_SHORT).show();
                        getDialog(register=1);
                    }

                }
                else {
                    getDialog(register=0);
                    Toast.makeText(getActivity(), "密碼不一致", Toast.LENGTH_SHORT).show();

                }
                cursor.close();
                db.close();

            }
        });
        binding.Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.EditUserName.setText("");
                binding.registerPassword.setText("");
                binding.RegisterCheckPassword.setText("");
            }
        });
    }
    private void getDialog(int register) {
        AlertDialog.Builder dialogregister = new AlertDialog.Builder(getActivity());
        dialogregister.setCancelable(false);
        if (register==1){
            dialogregister.setTitle("註冊成功");
            dialogregister.setMessage("註冊成功");
            dialogregister.setNegativeButton("確認", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    NavHostFragment.findNavController(RegisterFragment.this)
                            .navigate(R.id.action_RegisterFragment_to_LoginFragment);
                }
            });
        }
        else if (register==0){
            dialogregister.setTitle("註冊失敗");
            dialogregister.setMessage("密碼不一致");
            dialogregister.setNegativeButton("確認", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    binding.registerPassword.setText("");
                    binding.RegisterCheckPassword.setText("");
                    dialogInterface.dismiss();
                }
            });
        }
        else{
            dialogregister.setTitle("註冊失敗");
            dialogregister.setMessage("帳號已被註冊");
            dialogregister.setNegativeButton("確認", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    binding.EditUserName.setText("");
                    binding.registerPassword.setText("");
                    binding.RegisterCheckPassword.setText("");
                    dialogInterface.dismiss();
                }
            });
        }


        dialogregister.create().show();
    }
}