package com.example.matbakhy.helper;

import android.content.Context;
import android.widget.Toast;

public class MyToast {
    public MyToast(Context context, String messsage){
        Toast.makeText(context,messsage,Toast.LENGTH_SHORT).show();
    }
}
