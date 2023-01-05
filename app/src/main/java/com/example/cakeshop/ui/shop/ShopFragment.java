package com.example.cakeshop.ui.shop;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.cakeshop.R;
import com.example.cakeshop.databinding.FragmentShopBinding;

public class ShopFragment extends Fragment  {
private FragmentShopBinding binding;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ShopViewModel shopViewModel =
                new ViewModelProvider(this).get(ShopViewModel.class);
        binding = FragmentShopBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.strawberryBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String product="strawberry";
                confirm(product);
            }
        });
        binding.chocolateBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String product="chocolate";
                confirm(product);
            }
        });



    }
    public void confirm(String product) {
        String product_types = product;
        Bundle bundle_types = new Bundle();
        bundle_types.putString("product_types", product_types);

        DialogFragment newFragment = new Buy_DialogFragment();
        newFragment.setArguments(bundle_types);
        newFragment.show(getParentFragmentManager(),"confirm");

    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}