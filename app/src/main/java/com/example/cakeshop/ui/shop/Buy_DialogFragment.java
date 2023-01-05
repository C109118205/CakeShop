package com.example.cakeshop.ui.shop;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import com.example.cakeshop.R;
import com.example.cakeshop.SqlDataShopBaseHelper;

import java.io.ByteArrayOutputStream;

public class Buy_DialogFragment extends DialogFragment {

    private static final String DataBaseName = "DataShop";
    private static final int DataBaseVersion = 1;
    private static String DataBaseTable = "Shop_Order";
    private static SQLiteDatabase db;
    private com.example.cakeshop.SqlDataShopBaseHelper SqlDataShopBaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SqlDataShopBaseHelper = new SqlDataShopBaseHelper(this.getContext(),DataBaseName,null,DataBaseVersion);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String product_type = getArguments().getString("product_types");
        View view = inflater.inflate(R.layout.fragment_buy__dialog, container, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.product_cost);
        TextView product_name = (TextView) view.findViewById(R.id.product_name);
        TextView product_price = (TextView) view.findViewById(R.id.product_price);
        Button product_confirm = (Button)view.findViewById(R.id.product_confirm);
        Button product_cancel = (Button)view.findViewById(R.id.product_cancel);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.product_cost, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        String chocolate="chocolate";
        String strawberry="strawberry";

        if (product_type.equals(chocolate)){
            product_name.setText("巧克力蛋糕");
            product_price.setText("450元");
        }else if (product_type.equals(strawberry)){
            product_name.setText("草莓蛋糕");
            product_price.setText("400元");
        }

        int imagesid = getResources().getIdentifier(product_type,"drawable", getActivity().getPackageName());
        ImageView imageView = (ImageView) view.findViewById(R.id.product);

        imageView.setImageResource(imagesid);

        product_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = SqlDataShopBaseHelper.getWritableDatabase(); // 開啟資料庫

                int product_cost = Integer.parseInt(spinner.getSelectedItem().toString());
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imagesid);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageInByte = stream.toByteArray();

                if (product_type.equals(chocolate)){

                    int total_price = 450 * product_cost;
                    long id;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("product_name",product_name.getText().toString());
                    contentValues.put("product_price",total_price);
                    contentValues.put("product_image",imageInByte);
                    id = db.insert(DataBaseTable,null,contentValues);

                }else if (product_type.equals(strawberry)){
                    int total_price = 400 * product_cost;
                    long id;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("product_name",product_name.getText().toString());
                    contentValues.put("product_price",total_price);
                    contentValues.put("product_image",imageInByte);
                    id = db.insert(DataBaseTable,null,contentValues);


                }

                Toast.makeText(getActivity(), "購買成功", Toast.LENGTH_SHORT).show();
            }
        });
        product_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "取消購買", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        return view;
    }



}