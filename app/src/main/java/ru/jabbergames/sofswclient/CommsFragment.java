package ru.jabbergames.sofswclient;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class CommsFragment extends Fragment {
    onSomeEventListenerCom someEventListener;

    public static CommsFragment newInstance(String message) {
        return new CommsFragment();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            someEventListener = (onSomeEventListenerCom) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_com_buts, container, false);
    }

    protected void ClearButtc() {
        if (getView() == null) { return; }
        LinearLayout ll = getView().findViewById(R.id.ComButtLay);
        ll.removeAllViewsInLayout();
    }

    protected void AddButC(String kay, String txt, View v) {
        if (!Utils.flag) { return; }
        if (Objects.equals(txt, "")) { return; }

        LinearLayout ll = v.findViewById(R.id.ComButtLay);
        Button btn = new Button(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int marginInDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
                        .getDisplayMetrics());
        lp.setMargins(marginInDp, marginInDp, marginInDp, marginInDp);
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        btn.setBackgroundColor(0x98838383);
        btn.setLayoutParams(lp);
        btn.setGravity(Gravity.START);
        btn.setTransformationMethod(null);
        btn.setText(txt);
        btn.setTag(kay);
        // создаем обработчик нажатия
        View.OnClickListener oclBtnCmd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String com = (String) v.getTag();
                someEventListener.SendCom(com);
                someEventListener.addLog(com);
                someEventListener.setCurrentIt(0);
            }
        };

        // присвоим обработчик кнопке
        btn.setOnClickListener(oclBtnCmd);
        ll.addView(btn);
    }

    public interface onSomeEventListenerCom {
        void addLog(String s);

        void SendCom(String comstr);

        void setCurrentIt(int i);
    }


}