package com.wuzuqing.component_base.base.mvc

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import com.wuzuqing.component_base.R
import com.wuzuqing.component_base.dialog.MyAlertDialog
import com.wuzuqing.component_base.util.LogUtils

/**
 * @Created by TOME .
 * @时间 2018/5/17 17:11
 * @描述 ${permissionsdispatcher 处理权限管理}
 */

abstract class BaseVcPermissionActivity : BaseVcActivity() {


    private var isShouldShow = false
    private lateinit var dialog: MyAlertDialog

    /**判断当前版本为6.0以上 */
    protected val isMarshmallow: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    /**判断是否含有当前权限 */
    fun getPermission(permission: String, requestCode: Int): Boolean {
        LogUtils.d("申请权限!isGranted(permission)=" + !isGranted(permission))
        LogUtils.d("申请权限isMarshmallow=$isMarshmallow")

        if (!isGranted(permission) && isMarshmallow) {
            //当前权限未授权，并且系统版本为6.0以上，需要申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                isShouldShow = true
                LogUtils.d("申请权限=$permission")
                ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            } else {
                LogUtils.d("没有申请权限=$permission")
                isShouldShow = false
                showPresmissionDialog(requestCode)
            }
            return false
        }
        return true
    }

    /**判断当前是否已经授权 */
    protected fun isGranted(permission: String): Boolean {
        val granted = ActivityCompat.checkSelfPermission(this, permission)
        return granted == PackageManager.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions != null && permissions.size > 0 && grantResults != null && grantResults.size > 0) {
            val grantResult = grantResults[0]
            val permission = permissions[0]
            if (grantResult == PackageManager.PERMISSION_DENIED && !ActivityCompat.shouldShowRequestPermissionRationale(this, permission) && !isShouldShow) {
                //没有获取到权限,并且用户选择了不在提醒
                showPresmissionDialog(requestCode)
            }
        }
    }


    private fun showPresmissionDialog(requestCode: Int) {


        // dialog = new MyAlertDialog("权限设置",initView(requestCode),"取消",new String[]{"去设置"},null,this, AlertView.Style.Alert, IFlag.FLAG_SET_PERMISSION,this);
        dialog = MyAlertDialog(this).builder().setTitle("权限设置")
                .setMsg(initView(requestCode)).setNegativeButton("取消") { dialog.dismiss() }.setPositiveButton("去设置") { _, dialog ->
                    //去设置
                    dialog.dismiss()
                    val packageURI = Uri.parse("package:$packageName")
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                    startActivity(intent)
                }

        dialog.show()

    }


    fun initView(requestCode: Int): String {
        var message = ""
        if (requestCode == BaseVcPermissionActivity.PERMISSION_CAMERA) {
            //照相机权限
            message = getString(R.string.home_permission_camera)
        } else if (requestCode == BaseVcPermissionActivity.PERMISSION_PHONE) {
            //电话权限
            message = getString(R.string.home_permission_phone)
        } else if (requestCode == BaseVcPermissionActivity.PERMISSION_STORAGE) {
            //文件操作权限
            message = getString(R.string.home_permission_storage)
        } else {
            message = getString(R.string.home_permission_default)
        }
        return message
        //        return requestCode+"";
    }

    companion object {

        /***照相机权限 */
        val PERMISSION_CAMERA = 10001

        /**文件管理权限 */
        val PERMISSION_STORAGE = 10002

        /***电话权限 */
        val PERMISSION_PHONE = 10003
    }
}
