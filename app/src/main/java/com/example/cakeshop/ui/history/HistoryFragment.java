package com.example.cakeshop.ui.history;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cakeshop.R;
import com.example.cakeshop.SqlDataShopBaseHelper;
import com.example.cakeshop.databinding.FragmentHistoryBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryFragment extends Fragment {
    public static String[] HistoryID,History_name,History_price,History_image,imagePath;

    private FragmentHistoryBinding binding;
    private static final String DataBaseName = "DataShop";
    private static final int DataBaseVersion = 1;
    private static String DataBaseTable = "DataShopOrder";
    private static SQLiteDatabase db;
    private com.example.cakeshop.SqlDataShopBaseHelper SqlDataShopBaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        HistoryViewModel historyViewModel =
                new ViewModelProvider(this).get(HistoryViewModel.class);
        SqlDataShopBaseHelper = new SqlDataShopBaseHelper(this.getContext(),DataBaseName,null,DataBaseVersion);
        db = SqlDataShopBaseHelper.getWritableDatabase();
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SqlGetData();
        ListView_Customer(this.getContext());
        return root;
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    public void SqlGetData(){

        Cursor c = db.rawQuery("SELECT _id,product_name,product_price,product_image FROM " + DataBaseTable,null);
        HistoryID = new String[c.getCount()];
        History_name = new String[c.getCount()];
        History_price = new String[c.getCount()];
        History_image = new String[c.getCount()];
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
            HistoryID[i] = c.getString(0);
            History_name[i] = c.getString(1);
            History_price[i] = c.getString(2);
            History_image[i] = c.getString(3);
            c.moveToNext();
        }
    }
    public void ListView_Customer(Context context){
        binding.HistoryShowList.setAdapter(null);
        if(HistoryID.length == 0){
            Toast.makeText(context,"查無資料!!",Toast.LENGTH_SHORT).show();
        }
        else {
            SimpleAdapter adapter = new SimpleAdapter(context, getData(), R.layout.history_list, new String[]{"name", "price","image"}, new int[]{R.id.history_name, R.id.history_price,R.id.history_image});
            binding.HistoryShowList.setAdapter(adapter);
        }
    }
    public List getData() {
        List list = new ArrayList();
        Map map = new HashMap();
        for(int i = 0;i<HistoryID.length;i++){
            map = new HashMap();
            map.put("name", History_name[i].toString());
            map.put("price", History_price[i].toString());
            map.put("image", History_image[i].toString());
            list.add(map);
        }
        return list;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}