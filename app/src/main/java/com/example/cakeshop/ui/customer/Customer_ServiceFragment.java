package com.example.cakeshop.ui.customer;

import androidx.lifecycle.ViewModelProvider;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cakeshop.R;
import com.example.cakeshop.SqlDataBaseHelper;
import com.example.cakeshop.databinding.FragmentCustomerServiceBinding;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Customer_ServiceFragment extends Fragment {
private FragmentCustomerServiceBinding binding;
    private static final String DataBaseName = "DataBaseIt";
    private static final int DataBaseVersion = 1;
    private static String DataBaseTable = "Users";

    private static SQLiteDatabase db;
    private SqlDataBaseHelper sqlDataBaseHelper;
    public  String account = "Guest";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference Mymessage = database.getReference("messages");
    ValueEventListener valueEventListener ;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqlDataBaseHelper = new SqlDataBaseHelper(this.getContext(),DataBaseName,null,DataBaseVersion,DataBaseTable);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        CustomerViewModel customerViewModel =
                new ViewModelProvider(this).get(CustomerViewModel.class);
        binding = FragmentCustomerServiceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        getFirebase();
        Mymessage.addValueEventListener(valueEventListener);




        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                String message = binding.messageEditText.getText().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference mRefs = database.getReference("messages");
                Map<String, Object> data = new HashMap<>();
                data.put("message", message);
                data.put("sender", account);
                mRefs.push().setValue(data);
                data.clear();

            }
        });

        return root;

    }
public void getData(){
    db = sqlDataBaseHelper.getWritableDatabase(); // 開啟資料庫
    String [] projection = {"*"};
    String selection = "isOnline = ?";
    String[] selectionArgs = {"1"};
    Cursor cursor  = db.query(DataBaseTable,projection,selection,selectionArgs,null,null,null);
    if (cursor.moveToFirst()) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
        account = cursor.getString(cursor.getColumnIndexOrThrow("account"));
        String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
        int isonline = cursor.getInt(cursor.getColumnIndexOrThrow("isOnline"));
    }else
        account = "Guest";
    cursor.close();
    db.close();
}
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Mymessage.removeEventListener(valueEventListener);
        binding = null;
    }

    @Override
    public  void onStop(){
        super.onStop();
        Mymessage.removeEventListener(valueEventListener);

    }

    public  void  getFirebase(){
        List<Map<String, String>> data = new ArrayList<>();

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                getData();
                System.out.println(account);
                for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                    ChatMessage message = childSnapshot.getValue(ChatMessage.class);

//                        System.out.println(message.getSender() + ": " + message.getMessage());
                    Map<String, String> item = new HashMap<>();
                    item.put("sender", message.getSender());
                    item.put("message", message.getMessage());
                    data.add(item);

                }
                SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, R.layout.message,new String[]{"message","sender"}, new int[]{R.id.messageTextView,R.id.messengerTextView});
                binding.listOfMessages.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }
}