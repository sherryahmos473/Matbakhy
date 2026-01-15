package com.example.matbakhy;

import android.content.Context;
import android.widget.Toast;

public class MyToast {
    MyToast(Context context, String messsage){
        Toast.makeText(context,messsage,Toast.LENGTH_SHORT).show();
    }
}
