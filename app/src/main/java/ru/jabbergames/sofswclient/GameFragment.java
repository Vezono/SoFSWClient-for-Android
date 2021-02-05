package ru.jabbergames.sofswclient;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.lang.reflect.Field;
import java.util.Objects;

public class GameFragment extends Fragment {
    onSomeEventListenerGm someEventListenerGm;
    ImageButton btnGoEast;
    ImageButton btnGoSouth;
    ImageButton btnGoNorth;
    ImageButton btnGoWest;
    ImageButton btnCont;
    TextView chatMes;
    View vc;
    int countNewMessage = 0;
    private int STextEditID;
    private boolean frstTstShw = true;
    final View.OnClickListener oclBtnCont = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            refreshButtonPressed();
        }
    };
    private boolean uot;

    public static GameFragment newInstance(String message) {
        return new GameFragment();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            someEventListenerGm = (onSomeEventListenerGm) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_game, container, false);
        btnCont = v.findViewById(R.id.comBarButton0);
        // создаем обработчик нажатия
        btnGoWest = v.findViewById(R.id.comBarButton1);
        btnGoNorth = v.findViewById(R.id.comBarButton2);
        btnGoSouth = v.findViewById(R.id.comBarButton3);
        btnGoEast = v.findViewById(R.id.comBarButton4);
        chatMes = v.findViewById(R.id.chatMes);

        View.OnClickListener oclBtnGoWest = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                someEventListenerGm.SendCom("w");
                someEventListenerGm.addLog("w");
            }
        };

        // создаем обработчик нажатия
        View.OnClickListener oclBtnGoNorth = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                someEventListenerGm.SendCom("n");
                someEventListenerGm.addLog("n");
            }
        };

        // создаем обработчик нажатия
        View.OnClickListener oclBtnGoSouth = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                someEventListenerGm.SendCom("s");
                someEventListenerGm.addLog("s");
            }
        };

        // создаем обработчик нажатия
        View.OnClickListener oclBtnGoEast = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                someEventListenerGm.SendCom("e");
                someEventListenerGm.addLog("e");
            }
        };


        btnCont.setOnClickListener(oclBtnCont);
        btnGoWest.setOnClickListener(oclBtnGoWest);
        btnGoNorth.setOnClickListener(oclBtnGoNorth);
        btnGoSouth.setOnClickListener(oclBtnGoSouth);
        btnGoEast.setOnClickListener(oclBtnGoEast);
        return v;
    }

    protected void AddGText(String txt, View view) {
        if (txt.equals("")) {
            return;
        }

        Utils.inFight = txt.contains("бой. (");
        LinearLayout ll = view.findViewById(R.id.GameLinearLayout);
        TextView tv = new TextView(view.getContext());
        tv.setText(txt);
        ll.addView(tv);
    }

    protected void AddButG(String kay, String txt, View view) {
        if (txt.equals("")) {
            return;
        }
        LinearLayout ll = view.findViewById(R.id.GameLinearLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int marginInDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
                        .getDisplayMetrics());
        lp.setMargins(marginInDp, marginInDp, marginInDp, marginInDp);
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        switch (kay) {
            case "name":
            case "X":
            case "N":
            case "Х":
                //добавить поле ввода
                EditText et = new EditText(getActivity());
                et.setHint(txt);
                STextEditID = View.generateViewId();
                et.setId(STextEditID);
                ll.addView(et);

                // создаем обработчик нажатия
                View.OnClickListener oclBtnCmdS = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText mCmdText = vc.findViewById(STextEditID);
                        String scom = mCmdText.getText().toString();
                        if (scom.equals("")) {
                            scom = "0";
                        }
                        someEventListenerGm.SendCom(scom);
                        someEventListenerGm.addLog(scom);
                        mCmdText.setText("");
                        btnCont.setOnClickListener(oclBtnCont);
                    }
                };
                // присвоим обработчик кнопке
                btnCont.setOnClickListener(oclBtnCmdS);

                Button btnok = new Button(getActivity());
                btnok.setBackgroundColor(0x98838383);
                btnok.setLayoutParams(lp);
                btnok.setGravity(Gravity.START);
                btnok.setTransformationMethod(null);
                btnok.setText(getString(R.string.OK));
                btnok.setOnClickListener(oclBtnCmdS);
                ll.addView(btnok);
                break;
            default:
                Button btn = new Button(getActivity());
                btn.setBackgroundColor(0x98838383);
                btn.setLayoutParams(lp);
                btn.setGravity(Gravity.START);
                btn.setTransformationMethod(null);
                btn.setText(txt);
                btn.setTag(kay);
                switch (kay) {

                    case "SETTINGS swtheme":
                        final boolean light = txt.contains("Выбрана: Светлая тема");
                        View.OnClickListener oclBtnCmdd = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.changeToTheme(Objects.requireNonNull(getActivity()), Utils.THEME_DARK);
                                someEventListenerGm.ChangeTitle(light);
                                String com = (String) v.getTag();
                                someEventListenerGm.SendCom(com);
                                someEventListenerGm.addLog(com);
                                //tabHost.setCurrentTabByTag(tabTags[0]);
                            }
                        };
                        btn.setOnClickListener(oclBtnCmdd);
                        break;

                    case "SETTINGS swpush_rdy":
                        final boolean toastHpIsAcc = txt.contains("выключены");
                        oclBtnCmdd = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.toastHpIsAcc = toastHpIsAcc;
                                String com = (String) v.getTag();
                                someEventListenerGm.SendCom(com);
                                someEventListenerGm.addLog(com);
                                //tabHost.setCurrentTabByTag(tabTags[0]);
                            }
                        };
                        btn.setOnClickListener(oclBtnCmdd);
                        break;

                    case "SETTINGS swpush_prmes":
                        final boolean toastPrMesIsAcc = txt.contains("выключены");
                        oclBtnCmdd = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.toastPrMesIsAcc = toastPrMesIsAcc;
                                String com = (String) v.getTag();
                                someEventListenerGm.SendCom(com);
                                someEventListenerGm.addLog(com);
                                //tabHost.setCurrentTabByTag(tabTags[0]);
                            }
                        };
                        btn.setOnClickListener(oclBtnCmdd);

                    default:
                        // создаем обработчик нажатия
                        View.OnClickListener oclBtnCmd = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String com = (String) v.getTag();
                                someEventListenerGm.SendCom(com);
                                someEventListenerGm.addLog(com);
                            }
                        };
                        // присвоим обработчик кнопке
                        btn.setOnClickListener(oclBtnCmd);
                        if (Utils.inFight) {
                            btnCont.setTag(kay);
                            // создаем обработчик нажатия
                            View.OnClickListener oclBtnCmddd = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Utils.inFight = false;
                                    String com = (String) v.getTag();
                                    someEventListenerGm.SendCom(com);
                                    someEventListenerGm.addLog(com);
                                }
                            };
                            btnCont = view.findViewById(R.id.comBarButton0);
                            btnCont.setOnClickListener(oclBtnCmddd);
                        } else {
                            btnCont = view.findViewById(R.id.comBarButton0);
                            btnCont.setOnClickListener(oclBtnCont);
                        }
                        break;
                }
                ll.addView(btn);
                break;
        }
    }

    protected void SetPname(String nm) {
        if (getView() == null) { return; }
        TextView tv = getView().findViewById(R.id.player_name_text);
        tv.setText(nm);
    }

    protected void SetPlev(String de, String lv) {
        if (getView() == null) { return; }
        TextView tv = getView().findViewById(R.id.player_lev_text);
        tv.setText(String.format("  %s%s", de, lv));
    }

    protected void SetPHP(String hpdes, String hp, String hpmax) {
        View view = getView();
        if (view == null) { return; }
        TextView tv = view.findViewById(R.id.progress_hp_text);
        tv.setText(String.format("%s%s/%s", hpdes, hp, hpmax));
        if (Utils.toastHpIsAcc) {
            if (hp.equals(hpmax) & uot) {
                uot = false;
                Toast toastPriv = Toast.makeText(getActivity().getApplicationContext(),
                        "Жизни героя восстановлены!", Toast.LENGTH_SHORT);
                toastPriv.setGravity(Gravity.BOTTOM, 0, 0);
                toastPriv.show();
            }
            else if (!hp.equals(hpmax)) {
                uot = true;
            }
        }
        ProgressBar pb = view.findViewById(R.id.progressBarHP);
        pb.setMax(Integer.parseInt(hpmax));
        pb.setProgress(Integer.parseInt(hp));
    }

    protected void SetPSP(String spdes, String sp, String spmax) {
        View view = getView();
        if (view == null) {
            return;
        }
        TextView tv = getView().findViewById(R.id.progress_sp_text);
        tv.setText(String.format("%s%s/%s", spdes, sp, spmax));
        ProgressBar pb = getView().findViewById(R.id.progressBarSP);
        pb.setMax(Integer.parseInt(spmax));
        pb.setProgress(Integer.parseInt(sp));
    }

    protected void SetPPT(String ptdes, String pt, String ptmax) {
        View view = getView();
        if (view == null) {
            return;
        }
        String shpt = pt;
        if (pt.length() > 5) {
            shpt = pt.substring(0, pt.length() - 3) + "k";
        }
        TextView tv = view.findViewById(R.id.progress_pt_text);
        int i = Integer.parseInt(ptmax) - Integer.parseInt(pt);
        tv.setText(String.format("%s%s/%s", ptdes, shpt, i));
        ProgressBar pb = view.findViewById(R.id.progressBarPT);
        pb.setMax(Integer.parseInt(ptmax));
        pb.setProgress(Integer.parseInt(pt));
    }

    protected void SetAtten(String p) {
        View view = getView();
        if (view == null) {
            return;
        }
        TextView tv = view.findViewById(R.id.player_atten_text);
        String text = p.contains("1") ? getString(R.string.level_up_atten) : "";
        tv.setText(text);
    }

    protected void UpdateMap(String x, String y, String code, View v) {
        ImageView iv = v.findViewById(R.id.map);
        Bitmap prom;
        Bitmap tempBitmap = Bitmap.createBitmap(iv.getWidth(), iv.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(tempBitmap);
        Bitmap image;

        int background = Utils.isLight ? R.drawable.background_l : R.drawable.background_d;
        image = BitmapFactory.decodeResource(getResources(), background);

        prom = Bitmap.createScaledBitmap(image, iv.getWidth(), iv.getHeight(), false);
        tempCanvas.drawBitmap(prom, 0, 0, null);
        Paint transparentpaint = new Paint();
        transparentpaint.setAlpha(200); // 0 - 255

        if (!Utils.mapC.contains(x + ":" + y)) {
            Utils.mapC.add(x + ":" + y);
            Utils.mapP.add(code);
        }

        //16x16
        int cx = Integer.parseInt(x);
        int cy = Integer.parseInt(y);
        String tx;
        String ty;
        image = BitmapFactory.decodeResource(getResources(), R.drawable.s01_l);
        int rz = image.getHeight();
        int hmc = (iv.getWidth() / 2) - 1;
        int vmc = iv.getHeight() / 2;
        int hdc = hmc / rz;
        int vdc = vmc / rz;

        for (int i = (-1 * hdc - 1); i <= hdc; i++) {
            for (int j = (-1 * vdc); j <= vdc + 1; j++) {
                tx = Integer.toString(cx + i);
                ty = Integer.toString(cy + j);
                int pt = Utils.mapC.indexOf(tx + ":" + ty);
                if (pt > -1) {
                    Resources r = getResources();
                    String gogo = Utils.isLight ? "_1" : "_d";
                    try {
                        Class<R.drawable> res = R.drawable.class;
                        Field field = res.getField(Utils.mapP.get(pt) + gogo);
                        int drawableId = field.getInt(null);
                        image = BitmapFactory.decodeResource(r, drawableId);
                        //image.setPremultiplied(true);
                        //image.setHasAlpha(true);
                        tempCanvas.drawBitmap(image, hmc + i * rz, vmc - j * rz, transparentpaint);
                    }
                    catch (Exception ignored) { }
                }
            }
        }
        tempCanvas.drawBitmap(image, hmc, vmc, transparentpaint);
        iv.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));

    }

    protected void refreshButtonPressed() {
        if (frstTstShw) {
            frstTstShw = false;
            Toast toastPriv = Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                    "Вы нажали кнопку ОК (Enter)", Toast.LENGTH_SHORT);
            toastPriv.setGravity(Gravity.BOTTOM, -30, 0);
            toastPriv.show();
        }
        someEventListenerGm.SendCom("0");
        someEventListenerGm.addLog("0");
    }

    protected void setCountNewMessage(int position) {
        if (position == 2) {
            chatMes.setText("");
            countNewMessage = 0;
        } else countNewMessage += 1;
        if (chatMes != null) {
            if (countNewMessage != 0) {
                String prom = getString(R.string.ChatMessCounter) + countNewMessage;
                chatMes.setText(prom);
            } else chatMes.setText("");
        }
    }

    protected void ClearButtc() {
        View view = getView();
        if (view != null) {
            LinearLayout ll = view.findViewById(R.id.comsButtsLinearLayout);
            ll.removeAllViewsInLayout();
        }
    }

    protected void AddButC(String kay, String txt, View v) {
        if (Utils.flag) {
            return;
        }
        if (txt.equals("")) {
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }

        Button btn = new Button(activity);
        int sizeInDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 43, getResources().getDisplayMetrics());
        btn.setBackground(ContextCompat.getDrawable(activity, R.drawable.buttonroundbg));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(sizeInDp, sizeInDp);
        lp.setMargins(0, 6, 0, 6);
        btn.setLayoutParams(lp);
        btn.setGravity(Gravity.START);
        btn.setText(txt.substring(0, 2));
        btn.setTag(kay);
        // создаем обработчик нажатия
        View.OnClickListener oclBtnCmd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String com = (String) v.getTag();
                someEventListenerGm.SendCom(com);
                someEventListenerGm.addLog(com);
                someEventListenerGm.setCurrentIt(0);
            }
        };

        // присвоим обработчик кнопке
        btn.setOnClickListener(oclBtnCmd);
        LinearLayout ll = v.findViewById(R.id.comsButtsLinearLayout);
        ll.addView(btn);
    }

    public interface onSomeEventListenerGm {
        void addLog(String s);

        void SendCom(String comstr);

        int generateViewId();

        void ChangeTitle(boolean light);

        void setCurrentIt(int i);
    }
}
