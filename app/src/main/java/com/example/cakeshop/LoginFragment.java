package com.example.cakeshop;

import android.content.SyncAdapterType;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cakeshop.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

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
        db = sqlDataBaseHelper.getWritableDatabase(); // 開啟資料庫


    }
    public static String[] AccountArray,AccountID,PasswordArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
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

                if (cursor.moveToNext()){
                    System.out.println("login success");
                }
                else{
                    System.out.println("login failed");

                }



//
//                AccountArray = new String[c.getCount()];
//                AccountID = new String[c.getCount()];
//                PasswordArray = new String[c.getCount()];
//                c.moveToFirst();
//                for(int i=0;i<c.getCount();i++){
//                    AccountID[i] = c.getString(0);
//                    AccountArray[i] = c.getString(1);
//                    PasswordArray[i] = c.getString(2);
//                    c.moveToNext();
//                }



            }
        });

    }

}