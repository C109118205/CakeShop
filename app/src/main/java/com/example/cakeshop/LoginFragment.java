package com.example.cakeshop;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SyncAdapterType;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.SharedPreferences;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cakeshop.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    private SharedPreferences preferences;
    private  SharedPreferences.Editor editor;
    private String sharedPrefFile =
            "com.example.CakeShop.sharedprefs";

    private FragmentLoginBinding binding;
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
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        EditText username = (EditText) binding.EditUserName;
        EditText password = (EditText) binding.editTextTextPassword;
        CheckBox remember = (CheckBox) binding.remember;

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean isRemember = preferences.getBoolean("remember_password",false);
        if (isRemember){
            String edit_account = preferences.getString("account","");
            String edit_password = preferences.getString("password","");
            username.setText(edit_account);
            password.setText(edit_password);
            remember.setChecked(true);
        }
        return root;
    }


    public boolean Login;
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.RegisterPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_LoginFragment_to_RegisterFragment);
            }
        });
        binding.LoginUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db = sqlDataBaseHelper.getWritableDatabase(); // 開啟資料庫
                String username = binding.EditUserName.getText().toString();
                String password = binding.editTextTextPassword.getText().toString();

                String [] projection = {
                        "account",
                        "password"
                };

                String selection = "account = ? and password = ?";
                String[] selectionArgs = { username ,password};

//                String query = "select * from "+ DataBaseTable + " where account = ? and password = ?";

                Cursor cursor  = db.query(
                        DataBaseTable,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                int cursorCount = cursor.getCount();
                cursor.close();
                db.close();

                if (cursorCount>0){
                    getDialog(Login=true,username);
                    editor = preferences.edit();

                    if (binding.remember.isChecked()){
                        editor.putBoolean("remember_password",true);
                        editor.putString("account",username);
                        editor.putString("password",password);
                        Toast.makeText(getActivity(), "成功記住", Toast.LENGTH_SHORT).show();

                    }else {
                        editor.clear();
                        Toast.makeText(getActivity(), "不記住", Toast.LENGTH_SHORT).show();
                    }
                    editor.apply();

                }
                else{
                    getDialog(Login=false,null);

                }
           }
        });

    }
    private void getDialog(boolean Login,String username) {
        AlertDialog.Builder dialogregister = new AlertDialog.Builder(getActivity());
        dialogregister.setCancelable(false);
        if (Login){
            dialogregister.setTitle("登入成功");
            dialogregister.setMessage("登入成功，歡迎: " + username);
            dialogregister.setNegativeButton("確認", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    NavHostFragment.findNavController(LoginFragment.this)
                            .navigate(R.id.action_LoginFragment_to_HomeFragment);
                }
            });
        }
        else
        {
            dialogregister.setTitle("登入失敗");
            dialogregister.setMessage("密碼不正確或帳號不正確");
            dialogregister.setNegativeButton("確認", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }


        dialogregister.create().show();
    }
}