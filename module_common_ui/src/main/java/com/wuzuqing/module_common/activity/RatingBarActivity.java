package com.wuzuqing.module_common.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wuzuqing.component_base.util.LogUtils;
import com.wuzuqing.module_common.R;
import com.wuzuqing.module_common.widget.RatingBar;
import com.wuzuqing.module_common.widget.RatingBarV2;

public class RatingBarActivity extends AppCompatActivity implements View.OnClickListener {

    private RatingBar mRatingBar;
    private RatingBarV2 mRatingBarV2;
    private EditText mEtNumber;
    private TextView mBtSubmit;
    private float mNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_bar);
        initView();
    }

    private void initView() {
        mRatingBar = (RatingBar) findViewById(R.id.ratingbar);
        mRatingBarV2 = (RatingBarV2) findViewById(R.id.ratingbar_v2);
        mEtNumber = (EditText) findViewById(R.id.et_number);
        mBtSubmit = (TextView) findViewById(R.id.bt_submit);

        mBtSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_submit){
            String numberStr = mEtNumber.getText().toString();
            try {
                mNumber = Float.valueOf(numberStr);
            }catch (Exception e){
                mNumber = 0 ;
                LogUtils.d(e);
            }
            mRatingBar.setStar(mNumber);
            mRatingBarV2.setStarMark(mNumber);
        }
    }
}
