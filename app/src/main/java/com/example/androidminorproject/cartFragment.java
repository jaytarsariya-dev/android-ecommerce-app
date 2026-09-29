package com.example.androidminorproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link CartFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class cartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public cartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
//     TODO: Rename and change types and number of parameters
    public static cartFragment newInstance(String param1, String param2) {
        cartFragment fragment = new cartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }
}

//    public class CartFragment extends Fragment {
//        private ListView cartListView;
//
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            View view = inflater.inflate(R.layout.fragment_cart, container, false);
//            cartListView = view.findViewById(R.id.cart_list_view);
//            updateCart();
//            return view;
//        }
//
//
//        public void updateCart() {
//            List<Product> products = cart.getInstance().getProducts();
//            ArrayAdapter<Product> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, products);
//            cartListView.setAdapter(adapter);
//        }
//    }
//    public class CartFragment extends Fragment {
//        private ListView cartListView;
//        private List<Product> products;
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            View view = inflater.inflate(R.layout.fragment_cart, container, false);
//            cartListView = view.findViewById(R.id.cart_list_view);
//            products = new ArrayList<>();
//            return view;
//        }
//
//        public void addProductToCart(Product product) {
//            products.add(product);
//            updateCartUI();
//        }
//
//        private void updateCartUI() {
//            ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(getActivity(), android.R.layout.simple_list_item_1, products);
//            cartListView.setAdapter(adapter);

//    }}
//
