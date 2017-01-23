package com.myth.shishi.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myth.shishi.BaseActivity;
import com.myth.shishi.MyApplication;
import com.myth.shishi.R;
import com.myth.shishi.activity.WebviewActivity;
import com.myth.shishi.activity.YunSearchActivity;
import com.myth.shishi.entity.ColorEntity;
import com.myth.shishi.entity.Former;
import com.myth.shishi.entity.Writing;
import com.myth.shishi.util.CheckUtils;
import com.myth.shishi.util.StringUtils;
import com.myth.shishi.wiget.GCDialog;
import com.myth.shishi.wiget.MirrorLoaderView;
import com.myth.shishi.wiget.PasteEditText;
import com.myth.shishi.wiget.PingzeLinearlayout;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditFragment extends Fragment {

    private LinearLayout editContent;

    private String[] sList;

    private Context mContext;

    private ArrayList<EditText> editTexts = new ArrayList<EditText>();

    private View root;

    private Former former;

    private Writing writing;

    private TextView title;
    private MirrorLoaderView editContentBackground;

    private ImageView editTopBackground;

    public EditFragment() {
    }

    public static EditFragment getInstance(Former former, Writing writing) {
        EditFragment fileViewFragment = new EditFragment();
        fileViewFragment.former = former;
        fileViewFragment.writing = writing;
        return fileViewFragment;
    }

    @Override
    public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        mContext = inflater.getContext();
        root = inflater.inflate(R.layout.fragment_edit, container, false);
        initViews(root);
        return root;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (StringUtils.isNumeric(writing.getBgimg())) {
            int id = MyApplication.bgimgList[Integer.parseInt(writing.getBgimg())];
            editTopBackground.setImageResource(id);
            editContentBackground.setDrawableId(id);
        } else if (writing.getBitmap() != null) {
            root.setBackgroundDrawable(new BitmapDrawable(getResources(), writing.getBitmap()));
        } else {
            root.setBackgroundDrawable(new BitmapDrawable(getResources(), writing.getBgimg()));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        save();
    }

    public void save() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < editTexts.size(); i++) {
            if (i == editTexts.size() - 1) {
                sb.append(editTexts.get(i).getEditableText().toString());
            } else {
                sb.append(editTexts.get(i).getEditableText().toString() + "\n");
            }

        }
        writing.setText(sb.toString());
    }

    private void initViews(View view) {
        editTexts.clear();
        final View keyboard = view.findViewById(R.id.edit_keyboard);
        editContent = (LinearLayout) view.findViewById(R.id.edit_content);
        String s = former.getYun();

        MyApplication myApplication = (MyApplication) ((Activity) mContext).getApplication();
        if (s == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final EditText edittext = (EditText) inflater.inflate(R.layout.edittext, null);
            edittext.setPadding(0, 30, 0, 0);
            edittext.setTypeface(myApplication.getTypeface());
            edittext.setTextColor(getColor());
            if (writing.getText() != null) {
                edittext.setText(writing.getText());
            }
            edittext.setBackgroundDrawable(null);
            editTexts.add(edittext);
            edittext.requestFocus();
            editContent.addView(edittext);
        } else {
            sList = s.split("。");
            if (sList != null) {
                String[] tList = null;
                if (writing.getText() != null) {
                    tList = writing.getText().split("\n");
                }
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                for (int i = 0; i < sList.length; i++) {
                    HorizontalScrollView scrollView = new HorizontalScrollView(mContext);
                    scrollView.setHorizontalScrollBarEnabled(false);
                    View view1 = new PingzeLinearlayout(mContext, sList[i]);
                    scrollView.addView(view1);
                    view1.setPadding(0, 30, 0, 30);

                    final PasteEditText edittext = (PasteEditText) inflater
                            .inflate(R.layout.edittext, null);
                    if (i != sList.length - 1) {
                        edittext.line = i;
                        edittext.setOnPasteListener(onPasteListener);
                    }

                    edittext.setTypeface(myApplication.getTypeface());
                    edittext.setTextColor(getColor());

                    edittext.setLines(1);
                    final int index = i;
                    edittext.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                if (MyApplication.getCheckAble(mContext)) {
                                    CheckUtils.checkEditText(edittext, sList[index]);
                                }
                            } else {
                                keyboard.setVisibility(View.VISIBLE);
                                ((BaseActivity) mContext).setBottomGone();
                            }
                        }
                    });
                    editContent.addView(scrollView);
                    editContent.addView(edittext);
                    editTexts.add(edittext);

                    if (i == 0) {
                        edittext.requestFocus();
                    }

                    if (tList != null && tList.length > i) {
                        edittext.setText(tList[i]);
                    }
                }
            } else {
                Log.e("EditFragment", "sList is null");
            }
        }

        title = (TextView) view.findViewById(R.id.edit_title);

        title.setTypeface(myApplication.getTypeface());
        title.setTextColor(getColor());

        if (TextUtils.isEmpty(writing.getTitle())) {
            title.setText("点击输入标题");
        } else {
            title.setText(writing.getTitle());
        }

        title.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                final EditText et = new EditText(getActivity());
                new AlertDialog.Builder(getActivity()).setTitle("请输入标题").setIcon(android.R.drawable.ic_dialog_info).setView(
                        et).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        title.setText(et.getText().toString().trim());
                        writing.setTitle(title.getText().toString());
                    }
                }).setNegativeButton("取消", null).show();

            }
        });

        view.findViewById(R.id.edit_dict).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, YunSearchActivity.class);
                mContext.startActivity(intent);
            }
        });
        view.findViewById(R.id.edit_info).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebviewActivity.class);
                startActivity(intent);
            }
        });
        final View getfocus = view.findViewById(R.id.getfocus);
        getfocus.setFocusable(true);
        getfocus.setFocusableInTouchMode(true);

        keyboard.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                keyboard.setVisibility(View.GONE);
                ((BaseActivity) mContext).setBottomVisible();
                hideSoftInputFromWindow();
                getfocus.requestFocus();
                getfocus.requestFocusFromTouch();
            }
        });

        editContentBackground = (MirrorLoaderView) view.findViewById(R.id.background_image);
        editTopBackground = (ImageView) view.findViewById(R.id.edit_top_background);
    }

    private void hideSoftInputFromWindow() {
        View view = ((Activity) mContext).getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private int getColor() {
        ColorEntity colorEntity = MyApplication.getColorByPos(MyApplication.getDefaultShareColor(mContext));
        int color = 0x000000;
        if (colorEntity != null) {
            color = Color.rgb(colorEntity.getRed(), colorEntity.getGreen(), colorEntity.getBlue());
        }
        return color;
    }

    private PasteEditText.OnPasteListener onPasteListener = new PasteEditText.OnPasteListener() {

        @Override
        public void onPasteClick(final int line) {
            String string = editTexts.get(line).getEditableText().toString()
                    .trim();
            final String texts[] = split(string);
            if (texts == null || texts.length < 2) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString(GCDialog.DATA_CONTENT,
                    getString(R.string.tip_paste_auto));
            bundle.putString(GCDialog.DATA_TITLE, getString(R.string.auto_save));
            bundle.putString(GCDialog.CONFIRM_TEXT, getString(R.string.save));
            bundle.putString(GCDialog.CANCEL_TEXT, getString(R.string.cancel));
            new GCDialog(getActivity(), new GCDialog.OnCustomDialogListener() {

                @Override
                public void onConfirm() {
                    int j = 0;
                    for (int i = line; j < texts.length && i < editTexts.size(); i++, j++) {
                        editTexts.get(i).setText(texts[j]);
                    }
                }

                @Override
                public void onCancel() {
                }
            }, bundle).show();
        }
    };

    private String[] split(String str) {

        /* 正则表达式：句子结束符 */
        String regEx = "：|。|！|；";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);

        /* 按照句子结束符分割句子 */
        String[] words = p.split(str);

        /* 将句子结束符连接到相应的句子后 */
        if (words.length > 0) {
            int count = 0;
            while (count < words.length) {
                if (m.find()) {
                    words[count] += m.group();
                }
                count++;
            }
        }
        return words;

    }


}
