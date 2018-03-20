package com.example.z1310_000.sharedppx.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.adapter.MsgAdapter;
import com.example.z1310_000.sharedppx.entity.Msg;

import java.util.ArrayList;
import java.util.List;

public class MyServiceActivity extends BaseActivity {
    private List<Msg> msgs=new ArrayList<>();
    private EditText input_edit;
    private Button sendBtn;
    private RecyclerView MsgrecyclerView;
    private MsgAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service);

        initToolbar("联系客服");

        initMsgs();

        input_edit= (EditText) findViewById(R.id.input_edit);
        sendBtn= (Button) findViewById(R.id.sendBtn);
        MsgrecyclerView= (RecyclerView) findViewById(R.id.MsgrecyclerView);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        MsgrecyclerView.setLayoutManager(layoutManager);

        adapter=new MsgAdapter(msgs);
        MsgrecyclerView.setAdapter(adapter);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content=input_edit.getText().toString();
                if(!"".equals(content)){
                    Msg msg=new Msg(content,Msg.TYPE_SENT);
                    msgs.add(msg);
                    adapter.notifyItemInserted(msgs.size()-1);
                    MsgrecyclerView.scrollToPosition(msgs.size()-1);

                    input_edit.setText("");
                }
            }
        });
    }

    private  void  initMsgs(){
        Msg msg1=new Msg("Hello!",Msg.TYPE_RECEIVED);
        msgs.add(msg1);
        Msg msg2=new Msg("Hello？",Msg.TYPE_SENT);
        msgs.add(msg2);
        Msg msg3=new Msg("我是罗小黑",Msg.TYPE_RECEIVED);
        msgs.add(msg3);
    }

    public static void startAction(Context context){
        Intent intent=new Intent(context,MyServiceActivity.class);
        context.startActivity(intent);
    }
}
