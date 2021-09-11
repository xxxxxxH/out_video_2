package net.basicmodel

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.tencent.mmkv.MMKV
import com.zxy.zxydialog.AlertDialogUtils
import kotlinx.android.synthetic.main.layout_fragment_files.*
import net.adapter.VideoAdapter
import net.entity.VideoEntity
import net.entity.VideoEntityWithoutBitmap
import net.utils.Contanst
import net.utils.Utils

@Suppress("UNCHECKED_CAST")
class FolderActivity : AppCompatActivity() {
    var data: ArrayList<VideoEntity>? = null
    var videoAdapter: VideoAdapter? = null
    var current:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_fragment_files)
        initData()
    }

    private fun initData() {
        data = Contanst.data
        initView()
    }

    private fun initView() {
        videoAdapter = VideoAdapter(this, R.layout.layout_item, data)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = videoAdapter
        videoAdapter!!.addChildClickViewIds(R.id.menu)
        videoAdapter!!.setOnItemChildClickListener(object : OnItemChildClickListener {
            override fun onItemChildClick(
                adapter: BaseQuickAdapter<*, *>,
                view: View,
                position: Int
            ) {
                if (view.id == R.id.menu) {
                    showMenu(adapter.data[position] as VideoEntity)
                }
                current = position
            }

        })
        videoAdapter!!.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val entity = adapter.data[position] as VideoEntity
                val url = entity.url
                val name = entity.name
                val intent = Intent(this@FolderActivity, VideoActivity::class.java)
                intent.putExtra("url", url)
                intent.putExtra("name", name)
                Utils.saveKey(name)
                MMKV.defaultMMKV()?.encode(name, Utils.handleSingleEntityBitmap(entity))
                startActivity(intent)
            }
        })
    }

    private fun showMenu(data: VideoEntity) {
        AlertDialogUtils.build(this)
            .setView(R.layout.layout_dialog_pop)
            .setTransparency(0.5f)
            .setOnClick(R.id.play, R.id.information, R.id.delete)
            .create { view, alertDialogUtils ->
                when (view.id) {
                    R.id.play -> {
                        alertDialogUtils.dismiss()
                    }
                    R.id.information -> {
                        showInfoDlg(data).show()
                        alertDialogUtils.dismiss()
                    }
                    R.id.delete -> {
                        alertDialogUtils.dismiss()
                        videoAdapter!!.removeAt(current)
                    }
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun showInfoDlg(data: VideoEntity): AlertDialog {
        val d = AlertDialog.Builder(this).create()
        val view = LayoutInflater.from(this).inflate(R.layout.layout_dialog_info, null)
        d.setView(view)
        view.findViewById<TextView>(R.id.filename).text = "FileName:${data.name}"
        view.findViewById<TextView>(R.id.filepath).text = "FilePath:${data.url}"
        view.findViewById<TextView>(R.id.duration).text = "Duration:${data.duration}"
        view.findViewById<TextView>(R.id.type).text =
            "Type:${data.url.substring(data.url.indexOfLast { it == '.' } + 1)}"
        view.findViewById<TextView>(R.id.confirm).setOnClickListener {
            d.dismiss()
        }
        return d
    }

}