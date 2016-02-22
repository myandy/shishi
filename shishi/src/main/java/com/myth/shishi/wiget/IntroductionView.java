package com.myth.shishi.wiget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.StackView;
import android.widget.TextView;

import com.myth.shishi.R;
import com.myth.shishi.MyApplication;
import com.myth.shishi.adapter.IntroAdapter;
import com.myth.shishi.util.ResizeUtil;

public class IntroductionView extends RelativeLayout {

    private Context mContext;

    final int[] mColors = {R.drawable.intro1, R.drawable.intro2, R.drawable.intro3, R.drawable.intro4,
            R.drawable.intro5};
    private MyApplication myApplication;

    public IntroductionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public IntroductionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IntroductionView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void layoutItemContainer(View itemContainer) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) itemContainer.getLayoutParams();
        params.width = LayoutParams.MATCH_PARENT;
        params.height = ResizeUtil.resize(mContext, 864);
        itemContainer.setLayoutParams(params);
    }

    private void initView() {
        myApplication = (MyApplication) ((Activity) mContext).getApplication();

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.layout_intro, null);

        TextView title = (TextView) root.findViewById(R.id.title);
        title.setTypeface(myApplication.getTypeface());
        final StackView stackView = (StackView) root.findViewById(R.id.stack_view);
        layoutItemContainer(stackView);

        IntroAdapter colorAdapter = new IntroAdapter(mContext, mColors);
        stackView.setAdapter(colorAdapter);
        stackView.getLayoutParams().height = ResizeUtil.resize(mContext, 600);
        // stackView.setLayoutParams(new LayoutParams(-1, ));

        addView(root, new LayoutParams(-1, -1));
    }

}
