package com.example.cakeshop.ui.shop;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
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

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cakeshop.MainActivity;
import com.example.cakeshop.R;
import com.example.cakeshop.SqlDataShopBaseHelper;
import com.example.cakeshop.ui.history.HistoryFragment;

import java.io.ByteArrayOutputStream;

public class Buy_DialogFragment extends DialogFragment {

    private static final String DataBaseName = "DataShop";
    private static final int DataBaseVersion = 1;
    private static String DataBaseTable = "DataShopOrder";
    private static SQLiteDatabase db;
    private com.example.cakeshop.SqlDataShopBaseHelper SqlDataShopBaseHelper;
    private String CHANNEL_Shop = "Order";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SqlDataShopBaseHelper = new SqlDataShopBaseHelper(this.getContext(),DataBaseName,null,DataBaseVersion);
        createNotificationChannel();
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
                String product_names = product_name.getText().toString();

                if (product_type.equals(chocolate)){

                    int total_price = 450 * product_cost;
                    insertData(total_price,product_names);
                    sendNotification(product_names,total_price);

                }else if (product_type.equals(strawberry)){
                    int total_price = 400 * product_cost;
                    insertData(total_price,product_names);
                    sendNotification(product_names,total_price);

                }
                dismiss();
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

public void insertData(int product_cost,String product_name){
    long id;
    ContentValues contentValues = new ContentValues();
    contentValues.put("product_name",product_name);
    contentValues.put("product_price",product_cost);

    if (product_name.equals("巧克力蛋糕")){
        String imagepath  = "android.resource://" + getActivity().getPackageName() + "/" + R.drawable.chocolate;
        contentValues.put("product_image",imagepath);
    }
    else {
        String imagepath  = "android.resource://" + getActivity().getPackageName() + "/" + R.drawable.strawberry;
        contentValues.put("product_image",imagepath);
    }
    id = db.insert(DataBaseTable,null,contentValues);
}
    public void createNotificationChannel()
    {
        /**檢查手機版本是否支援通知；若支援則新增"頻道"*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_Shop, "ShopOrder", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel);

        }
    }

    public void sendNotification(String product_name,int price) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
//        intent.putExtra("toFragment","HistoryFragment");
        int flag = PendingIntent.FLAG_MUTABLE;
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, flag);


        NotificationCompat.Builder builder
                = new NotificationCompat.Builder(getActivity(),CHANNEL_Shop)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("成功訂購 "+ product_name)
                .setContentText("總金額是: " + price)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat
                = NotificationManagerCompat.from(getActivity());
        notificationManagerCompat.notify(1,builder.build());
    }
    }