package com.wuzuqing.module_common.activity;

import android.view.View;

import com.wuzuqing.component_base.base.mvc.BaseVcActivity;
import com.wuzuqing.module_common.R;


public class MyAccountActivity extends BaseVcActivity implements   View.OnClickListener {
    private int dissHudflag = 0;
    private int dissHud = 2;

    @Override
    protected int getLayout() {
        return R.layout.activity_my_account;
    }


    @Override
    protected void initView() {
//        mLlytMemberIcon.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
       /* if (!getPermission(Manifest.permission.CAMERA, PERMISSION_CAMERA)) {
            return;
        }
        if (!getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_STORAGE)) {
            return;
        }
        if (!getPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSION_STORAGE)) {
            return;
        }*/
        if (v.getId() == R.id.llyt_member_icon){
        }
    }

}
