package net.basicmodel

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weeboos.permissionlib.PermissionRequest
import com.tencent.mmkv.MMKV
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MMKV.initialize(this)
        requestPermission()
    }
    private fun requestPermission(){
        PermissionRequest.getInstance().build(this).requestPermission(object :PermissionRequest.PermissionListener{
            override fun permissionGranted() {
                startActivity(Intent(this@MainActivity,HomeActivity::class.java))
            }

            override fun permissionDenied(permissions: ArrayList<String>?) {
                finish()
            }

            override fun permissionNeverAsk(permissions: ArrayList<String>?) {
                finish()
            }
        },permissions)
    }
}