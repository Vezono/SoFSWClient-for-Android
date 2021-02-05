package ru.jabbergames.sofswclient;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class CmdFragment extends Fragment {
    onSomeEventListenerCmd someEventListener;
    int countLogMes = 0;
    View vv;
    Button btnCmdSend;
    Button btnCmdClear;

    public static CmdFragment newInstance(String message) {
        return new CmdFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            someEventListener = (onSomeEventListenerCmd) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_cmd, container, false);
        vv = v;
        btnCmdSend = v.findViewById(R.id.cmdSendButton);
        btnCmdClear = v.findViewById(R.id.cmdClearButton);
        // создаем обработчик нажатия
        View.OnClickListener oclBtnCmd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mCmdText = vv.findViewById(R.id.cmdText);
                String scom = mCmdText.getText().toString();
                if (!Objects.equals(scom, "")) {
                    someEventListener.SendCom(mCmdText.getText().toString());
                    addLog(mCmdText.getText().toString());
                }
                mCmdText.setText("");
            }
        };
        // создаем обработчик нажатия для кнопки очистить
        View.OnClickListener oclBtnCmdClr = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView mCmdText = vv.findViewById(R.id.logTextView);
                if (mCmdText != null) {
                    mCmdText.setText("");
                }
            }
        };
        // присвоим обработчик кнопке
        btnCmdClear.setOnClickListener(oclBtnCmdClr);
        btnCmdSend.setOnClickListener(oclBtnCmd);

        //авторпрокрутка
        TextView textView1 = v.findViewById(R.id.logTextView);
        textView1.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

                ScrollView scrollView1 = vv.findViewById(R.id.scrollViewCons);
                scrollView1.fullScroll(ScrollView.FOCUS_DOWN);
                // you can add a toast or whatever you want here
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                //override stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                //override stub
            }

        });


        return v;
    }

    public void addLog(String addstr) {
        if (getView() != null) {
            TextView mCmdText = getView().findViewById(R.id.logTextView);
            if (mCmdText != null) {
                mCmdText.append("\n\r" + addstr);
                countLogMes++;
                if (countLogMes > 300) {
                    mCmdText.setText("");
                    countLogMes = 0;
                }
            }
        }
    }

    public interface onSomeEventListenerCmd {
        void SendCom(String comstr);
    }

}