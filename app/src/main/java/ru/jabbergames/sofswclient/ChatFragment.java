package ru.jabbergames.sofswclient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.TimeZone;

public class ChatFragment extends Fragment {
    onSomeEventListenerCh someEventListener;
    private int chatMinId = 2147483647;
    private String nk = "";

    public static ChatFragment newInstance(String message) {
        return new ChatFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            someEventListener = (onSomeEventListenerCh) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_chat, container, false);
        someEventListener.SendCom("chatmess !chroom? descr");
        someEventListener.addLog("chatmess !chroom? descr");
        LinearLayout ll = v.findViewById(R.id.chatContent);
        if (ll.getChildCount() == 0) someEventListener.SendCom("chatmess !history");
        Button btn = v.findViewById(R.id.chatRoomSelButt);
        // создаем обработчик нажатия
        View.OnClickListener oclBtnCmd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll = v.findViewById(R.id.chatContent);
                ll.removeAllViewsInLayout();
                someEventListener.SendCom("chatmess !chroom? list");
                someEventListener.addLog("chatmess !chroom? list");
            }
        };
        // присвоим обработчик кнопке
        btn.setOnClickListener(oclBtnCmd);
        Button btncs = v.findViewById(R.id.chatSendButton);
        // создаем обработчик нажатия
        View.OnClickListener oclCBtnCmd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = v.findViewById(R.id.chatText);
                TextView tv = v.findViewById(R.id.chatToTextView);
                String disp = tv.getText().toString();
                if (disp.length() == 0) {
                    nk = "";
                }
                if (et.getText().length() > 0) {
                    someEventListener.SendCom("chatmess " + nk + et.getText().toString());
                    someEventListener.addLog("chatmess " + nk + et.getText().toString());
                    et.setText("");
                }
                Utils.seeHist = false;
            }
        };
        // присвоим обработчик кнопке
        btncs.setOnClickListener(oclCBtnCmd);
        return v;
    }

    protected void AddChatRoomD(String chatRoomName, String chatRoomDesc, String chatPeopleCount, View v) {
        TextView tv = v.findViewById(R.id.chatRoomName);
        tv.setText(chatRoomName);
        tv = v.findViewById(R.id.chatRoomCount);
        tv.setText(String.format(
                "%s%s%s",
                getString(R.string.room_cnt),
                chatPeopleCount,
                getString(R.string.room_cnt_p)
                )
        );
        tv = v.findViewById(R.id.chatRoomDescr);
        tv.setText(chatRoomDesc);
    }

    protected void AddChatRoomB(String chnum, String chname, String des, String incount, View v) {
        LinearLayout ll = v.findViewById(R.id.chatContent);
        Button btn = new Button(getActivity());
        String prom = "Комната: " + chname + "  {" + incount + "чел.}\n" + des;
        btn.setText(prom);
        btn.setTag("chatmess !chroom! " + chnum);
        btn.setTransformationMethod(null);
        // создаем обработчик нажатия
        View.OnClickListener oclBtnCmd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String com = (String) v.getTag();
                someEventListener.SendCom(com);
                someEventListener.addLog(com);
                LinearLayout ll = v.findViewById(R.id.chatContent);
                ll.removeAllViewsInLayout();
            }
        };

        // присвоим обработчик кнопке
        btn.setOnClickListener(oclBtnCmd);

        ll.addView(btn);
    }

    protected void AddToChat(String from, String message, String dtime, boolean priv, boolean totop,
                             int tid, View v) {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        LinearLayout ll = v.findViewById(R.id.chatContent);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int marginInDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
                        .getDisplayMetrics());
        lp.setMargins(marginInDp, marginInDp, marginInDp, marginInDp);
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;

        if (chatMinId > tid) chatMinId = tid;

        //кнопка загрузки истории
        if (ll.getChildCount() > 0) {
            Button ghbtn = (Button) ll.getChildAt(0);
            if (!ghbtn.getTag().toString().equals("ghbtn")) {
                ghbtn = new Button(activity);
                ghbtn.setBackgroundColor(0x98838383);
                ghbtn.setLayoutParams(lp);
                ghbtn.setGravity(Gravity.START);
                ghbtn.setTransformationMethod(null);
                ghbtn.setText("старые сообщения");
                ghbtn.setTag("ghbtn");
                // создаем обработчик нажатия
                View.OnClickListener oclBtnGhbtn = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout ll = v.findViewById(R.id.chatContent);
                        TextView tv = new TextView(activity);
                        tv.setText("=================");
                        tv.setGravity(Gravity.CENTER_HORIZONTAL);
                        ll.addView(tv, 1);
                        Utils.seeHist = true;
                        someEventListener.SendCom("chatmess !history " + chatMinId);
                        someEventListener.addLog("chatmess !history " + chatMinId);
                    }
                };
                // присвоим обработчик кнопке
                ghbtn.setOnClickListener(oclBtnGhbtn);
                ll.addView(ghbtn, 0);
            }

        }


        String shou = "";
        String smin;
        if (!dtime.equals("none")) {
            Calendar currentCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5"));
            int gmtOffset = TimeZone.getDefault().getRawOffset() - TimeZone.getTimeZone("GMT+5").getRawOffset();

            int index = dtime.indexOf(":");
            smin = dtime.substring(index + 1, 5);
            shou = dtime.substring(0, index);
            int min = Integer.parseInt(smin);
            int hou = Integer.parseInt(shou);

            currentCalendar.set(Calendar.HOUR_OF_DAY, hou);
            currentCalendar.set(Calendar.MINUTE, min);

            currentCalendar.setTimeInMillis(currentCalendar.getTimeInMillis() + gmtOffset);

            hou = currentCalendar.get(Calendar.HOUR_OF_DAY);
            min = currentCalendar.get(Calendar.MINUTE);

            if (min < 10) {
                shou = hou + ":0" + min;
            } else {
                shou = hou + ":" + min;
            }
        }

        Button btn = new Button(activity);
        btn.setBackgroundColor(0x98838383);
        btn.setLayoutParams(lp);
        btn.setGravity(Gravity.START);
        btn.setTransformationMethod(null);
        someEventListener.isChatFr();
        if (priv) {
            if (Utils.toastPrMesIsAcc) {
                String ffrom;
                ffrom = from;
                Toast toastPriv = Toast.makeText(activity.getApplicationContext(),
                        "Приватное сообщение от " + ffrom, Toast.LENGTH_SHORT);
                toastPriv.setGravity(Gravity.BOTTOM, 0, 0);
                toastPriv.show();
            }
            btn.setText(String.format("%s приватно от %s:\n\r%s", shou, from, message));
            btn.setTextColor(Color.parseColor("#ccff0e15"));
        } else {
            String prom = shou + " " + from + ":\n" + message;
            btn.setText(prom);
        }
        btn.setGravity(Gravity.START);
        btn.setTransformationMethod(null);
        btn.setTag(from);
        // создаем обработчик нажатия
        View.OnClickListener oclBtnCmdddd = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                nk = v.getTag().toString();
                showChPopupMenu(v);
            }
        };

        // присвоим обработчик кнопке
        btn.setOnClickListener(oclBtnCmdddd);

        if (totop) {
            ll.addView(btn, 1);
        } else {
            ll.addView(btn);
            if (!Utils.seeHist) {
                ScrollView scrollView1 = v.findViewById(R.id.scrollViewChat);
                scrollView1.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }
    }

    protected void showChPopupMenu(final View v) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        PopupMenu popupMenu = new PopupMenu(activity, v);

        //popupMenu.inflate(R.menu.popupmenu); // Для Android 4.0
        // для версии Android 3.0 нужно использовать длинный вариант
        popupMenu.getMenuInflater().inflate(R.menu.chat_pmenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                TextView tv = v.findViewById(R.id.chatToTextView);
                switch (item.getItemId()) {
                    case R.id.menu1:
                        String t = tv.getText().toString();
                        String prom;
                        if (t.indexOf("Приватно ") == 0) {
                            prom = nk + ", ";
                        } else {
                            prom = t + nk + ", ";
                        }
                        tv.setText(prom);
                        nk = tv.getText().toString();
                        return true;
                    case R.id.menu2:
                        tv.setText(String.format("Приватно %s", nk));
                        nk = "!private " + nk + " ";
                        return true;
                    case R.id.menu3:
                        someEventListener.SendCom("05 " + nk);
                        someEventListener.addLog("05 " + nk);
                        nk = "";
                        int i = 0;
                        someEventListener.setCurrentIt(i);
                        return true;
                    case R.id.menu4:
                        someEventListener.SendCom("chatmess !chroom? ulist");
                        someEventListener.addLog("chatmess !chroom? ulist");
                        return true;
                    case R.id.menu5:
                        tv.setText("");
                        EditText et = v.findViewById(R.id.chatText);
                        et.setText("");
                        nk = "";
                        return true;
                    default:
                        nk = "";
                        return false;
                }
            }

        });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {

            @Override
            public void onDismiss(PopupMenu menu) {

            }
        });
        popupMenu.show();
    }

    protected void ChatClearAll(View v) {
        LinearLayout ll = v.findViewById(R.id.chatContent);
        ll.removeAllViewsInLayout();
    }

    public interface onSomeEventListenerCh {
        void addLog(String s);

        void SendCom(String comstr);

        void setCurrentIt(int i);

        void isChatFr();
    }
}